package utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Fills the unified "Microplan Template" that the campaign's upload screen
 * generates, so the filled sheet can be uploaded back.
 *
 * A static committed template cannot be used: the workbook the app generates is
 * campaign-specific. Its hidden _h_Meta_h_ sheet carries a campaign uuid, its
 * hidden _h_SimpleLookup_h_ sheet drives the cascading boundary dropdowns, and
 * its "Boundary List" arrives pre-populated with exactly the boundaries that
 * campaign selected. So this always edits the downloaded workbook in place and
 * leaves every hidden sheet untouched.
 *
 * Layout (verified against hcm-demo on 2026-09-09, BEDNET / CHAD - ITN):
 *   row 1 = localization keys, row 2 = human-readable headers, data from row 3.
 *
 * What the app requires the user to supply:
 *   Boundary List  - "Household Target at village level" is marked Mandatory;
 *                    the remaining target columns vary by campaign type.
 *   User List      - arrives empty; Phone Number and Employment Type are marked
 *                    Mandatory, and at least one role is needed.
 *   Facilities List- arrives pre-populated, but facilities default to a
 *                    "Facility Usage" of Inactive.
 */
public class MicroplanTemplateFiller {

    private static final String SHEET_FACILITIES = "Facilities List";
    private static final String SHEET_USERS = "User List";
    private static final String SHEET_BOUNDARY = "Boundary List";

    /** Row 1 holds localization keys; columns are resolved by key, never by index. */
    private static final int KEY_ROW = 0;
    /** Data begins on the third row (after key row and header row). */
    private static final int FIRST_DATA_ROW = 2;

    private static final String KEY_BOUNDARY_CODE = "HCM_ADMIN_CONSOLE_BOUNDARY_CODE";
    private static final String KEY_TARGET = "HCM_ADMIN_CONSOLE_TARGET";
    private static final String KEY_FACILITY_USAGE = "HCM_ADMIN_CONSOLE_FACILITY_USAGE";
    private static final String KEY_USER_NAME = "HCM_ADMIN_CONSOLE_USER_NAME";
    private static final String KEY_USER_PHONE = "HCM_ADMIN_CONSOLE_USER_PHONE_NUMBER";
    private static final String KEY_USER_ROLE_1 = "HCM_ADMIN_CONSOLE_USER_ROLE_MULTISELECT_1";
    private static final String KEY_USER_EMPLOYMENT = "HCM_ADMIN_CONSOLE_USER_EMPLOYMENT_TYPE";
    private static final String KEY_USER_USAGE = "HCM_ADMIN_CONSOLE_USER_USAGE";
    private static final String KEY_ROW_ID = "HCM_ADMIN_CONSOLE____ROW_ID";

    /** Boundary-level columns carry the MICROPLAN_ prefix and differ per hierarchy. */
    private static final String BOUNDARY_LEVEL_PREFIX = "MICROPLAN_";

    private static final int HOUSEHOLD_TARGET = 100;
    private static final String EMPLOYMENT_TYPE = "Permanent";
    private static final String ACTIVE = "Active";
    private static final String DEFAULT_ROLE = "DISTRIBUTOR";
    /** One campaign user is enough to satisfy the User List; it must not be empty. */
    private static final int USER_COUNT = 1;

    /**
     * Fills the workbook at {@code downloaded} and writes the result to
     * {@code filled}.
     *
     * @param downloaded template as produced by the campaign's upload screen
     * @param filled     destination for the filled workbook
     * @return {@code filled}, for chaining into the upload call
     */
    public static Path fill(Path downloaded, Path filled) {
        try (InputStream in = Files.newInputStream(downloaded);
             Workbook wb = new XSSFWorkbook(in)) {

            List<Map<String, String>> boundaries = fillBoundaryTargets(wb);
            if (boundaries.isEmpty()) {
                throw new IllegalStateException(
                        "'" + SHEET_BOUNDARY + "' had no data rows — the campaign's boundaries "
                                + "were not carried into the template, so there is nothing to target.");
            }
            activateFacilities(wb);
            addUsers(wb, boundaries, USER_COUNT);

            Files.createDirectories(filled.toAbsolutePath().getParent());
            try (FileOutputStream out = new FileOutputStream(filled.toFile())) {
                wb.write(out);
            }
            System.out.println("[Template] Filled " + boundaries.size() + " boundary row(s) and added "
                    + USER_COUNT + " user(s) -> " + filled);
            return filled;
        } catch (Exception e) {
            throw new RuntimeException("Could not fill microplan template " + downloaded, e);
        }
    }

    /**
     * Writes the mandatory household target, plus any campaign-type-specific
     * target columns, for every pre-populated boundary row.
     *
     * @return each boundary row's level values and service boundary code, so the
     *         users added later sit on a boundary the campaign actually covers.
     */
    private static List<Map<String, String>> fillBoundaryTargets(Workbook wb) {
        Sheet sheet = requireSheet(wb, SHEET_BOUNDARY);
        Map<String, Integer> cols = keyToColumn(sheet);
        List<Map<String, String>> rows = new ArrayList<>();

        for (int r = FIRST_DATA_ROW; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null || isBlank(row, cols.get(KEY_BOUNDARY_CODE))) continue;

            // Every target column is numeric; the mandatory one is KEY_TARGET and
            // the rest are campaign-type specific (e.g. TARGET_BEDNET_COLUMN_2/3).
            for (Map.Entry<String, Integer> e : cols.entrySet()) {
                if (e.getKey().startsWith(KEY_TARGET)) {
                    row.createCell(e.getValue()).setCellValue(HOUSEHOLD_TARGET);
                }
            }

            Map<String, String> captured = new LinkedHashMap<>();
            for (Map.Entry<String, Integer> e : cols.entrySet()) {
                if (e.getKey().startsWith(BOUNDARY_LEVEL_PREFIX)
                        || e.getKey().equals(KEY_BOUNDARY_CODE)) {
                    captured.put(e.getKey(), readString(row, e.getValue()));
                }
            }
            rows.add(captured);
        }
        return rows;
    }

    /** Facilities arrive pre-populated but Inactive; a campaign needs them usable. */
    private static void activateFacilities(Workbook wb) {
        Sheet sheet = requireSheet(wb, SHEET_FACILITIES);
        Map<String, Integer> cols = keyToColumn(sheet);
        Integer usageCol = cols.get(KEY_FACILITY_USAGE);
        if (usageCol == null) return;

        for (int r = FIRST_DATA_ROW; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null || isBlank(row, cols.get(KEY_BOUNDARY_CODE))) continue;
            row.createCell(usageCol).setCellValue(ACTIVE);
        }
    }

    /**
     * Appends campaign users onto the first boundary row, which is the deepest
     * boundary the campaign selected. Phone numbers are generated per run so
     * repeated runs do not collide on an already-registered number.
     */
    private static void addUsers(Workbook wb, List<Map<String, String>> boundaries, int userCount) {
        Sheet sheet = requireSheet(wb, SHEET_USERS);
        Map<String, Integer> cols = keyToColumn(sheet);
        Map<String, String> boundary = boundaries.get(0);

        long stamp = System.currentTimeMillis() % 100000L;
        for (int i = 0; i < userCount; i++) {
            Row row = sheet.createRow(FIRST_DATA_ROW + i);

            for (Map.Entry<String, String> e : boundary.entrySet()) {
                Integer col = cols.get(e.getKey());
                if (col != null && e.getValue() != null && !e.getValue().isEmpty()) {
                    row.createCell(col).setCellValue(e.getValue());
                }
            }

            // 10-digit number starting with 9, unique per run and per row.
            String phone = "9" + String.format("%05d", stamp) + String.format("%04d", i);
            setIfPresent(row, cols, KEY_USER_NAME, "AutoTestUser" + stamp + i);
            setIfPresent(row, cols, KEY_USER_PHONE, phone);
            setIfPresent(row, cols, KEY_USER_ROLE_1, DEFAULT_ROLE);
            setIfPresent(row, cols, KEY_USER_EMPLOYMENT, EMPLOYMENT_TYPE);
            setIfPresent(row, cols, KEY_USER_USAGE, ACTIVE);
            // __ROW_ID is assigned by the server for new rows; leave it empty.
            Integer rowId = cols.get(KEY_ROW_ID);
            if (rowId != null) row.createCell(rowId).setCellValue("");
        }
    }

    // --- helpers ---

    private static Sheet requireSheet(Workbook wb, String name) {
        Sheet sheet = wb.getSheet(name);
        if (sheet == null) {
            throw new IllegalStateException("Template has no '" + name + "' sheet. Sheets present: "
                    + sheetNames(wb) + " — the template format has changed.");
        }
        return sheet;
    }

    private static List<String> sheetNames(Workbook wb) {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < wb.getNumberOfSheets(); i++) names.add(wb.getSheetName(i));
        return names;
    }

    /** Maps each localization key in row 1 to its column index. */
    private static Map<String, Integer> keyToColumn(Sheet sheet) {
        Map<String, Integer> cols = new LinkedHashMap<>();
        Row keys = sheet.getRow(KEY_ROW);
        if (keys == null) return cols;
        for (int c = 0; c < keys.getLastCellNum(); c++) {
            String key = readString(keys, c);
            if (key != null && !key.isEmpty()) cols.put(key, c);
        }
        return cols;
    }

    private static void setIfPresent(Row row, Map<String, Integer> cols, String key, String value) {
        Integer col = cols.get(key);
        if (col != null) row.createCell(col).setCellValue(value);
    }

    private static boolean isBlank(Row row, Integer col) {
        if (col == null) return true;
        String v = readString(row, col);
        return v == null || v.isEmpty();
    }

    private static String readString(Row row, int col) {
        Cell cell = row.getCell(col);
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:  return cell.getStringCellValue().trim();
            case NUMERIC: return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default:      return "";
        }
    }
}
