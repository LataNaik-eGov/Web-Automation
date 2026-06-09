package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;
import utils.ConfigReader;
import utils.TestDataReader;

public class HRMSPage extends BasePage {

    // --- Employee inbox CSS locators ---
    private final String searchInput = "input[name='codes']";
    private final String campaignDateInput = "input.employee-card-input[type='date'][min]";

    // --- Aria-role locators (from recorded UI flow) ---
    private final Locator nextButton;
    private final Locator usernameInput;
    private final Locator passwordInput;
    private final Locator confirmPasswordInput;
    private final Locator nameInput;
    private final Locator mobileInput;
    private final Locator femaleRadio;
    private final Locator maleRadio;
    private final Locator dobInput;
    private final Locator emailInput;
    private final Locator addressInput;
    private final Locator doaInput;

    public HRMSPage(Page page) {
        super(page);
        this.nextButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next"));
        this.usernameInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Username"));
        this.passwordInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password").setExact(true));
        this.confirmPasswordInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Confirm Password"));
        this.nameInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Name").setExact(true));
        this.mobileInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Mobile Number"));
        this.femaleRadio = page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("Female"));
        this.maleRadio = page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("Male"));
        this.dobInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Date of Birth"));
        this.emailInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email"));
        this.addressInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Correspondence Address"));
        this.doaInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Date of Appointment"));
    }

    // ==================== STEP 1: HIERARCHY TYPE ====================

    public HRMSPage selectHierarchyType(String searchText, String optionText) {
        page.getByRole(AriaRole.TEXTBOX).first().click();
        page.getByRole(AriaRole.TEXTBOX).first().fill(searchText);
        page.getByText(optionText, new Page.GetByTextOptions().setExact(true)).click();
        return this;
    }

    // ==================== STEP 2: NEXT ====================

    public HRMSPage clickNext() {
        nextButton.click();
        page.waitForLoadState();
        return this;
    }

    // ==================== STEP 3: LOGIN DETAILS ====================

    public HRMSPage fillLoginDetails(String empId, String pwd) {
        page.evaluate("window.scrollTo(0, 0)");
        usernameInput.waitFor(new Locator.WaitForOptions().setTimeout(30000));
        usernameInput.fill(empId);
        passwordInput.fill(pwd);
        confirmPasswordInput.fill(pwd);
        return this;
    }

    // ==================== STEP 4: PERSONAL DETAILS ====================

    public HRMSPage fillPersonalDetails(String name, String mobile, String gender,
            String dob, String emailId, String addr) {
        nameInput.waitFor();
        nameInput.fill(name);
        mobileInput.fill(mobile);

        if ("Female".equalsIgnoreCase(gender)) {
            femaleRadio.check();
        } else {
            maleRadio.check();
        }

        dobInput.fill(dob);
        emailInput.fill(emailId);
        addressInput.fill(addr);
        return this;
    }

    // ==================== STEP 5: EMPLOYMENT DETAILS ====================

    public HRMSPage fillEmploymentDetails(String empType, String doa,
            String department, String designation, String roleName, String jurisdiction) {

        // Employment Type — textbox-based dropdown
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Employment Type")).click();
        page.getByText(empType).first().click();

        // Date of Appointment
        doaInput.waitFor();
        doaInput.fill(doa);

        // Department — textbox-based dropdown; options appear in #jk-dropdown-unique
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Department")).click();
        page.locator("#jk-dropdown-unique").getByText(department).click();

        // Designation — textbox-based dropdown
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Designation")).click();
        page.getByText(designation).first().click();

        // Role — open multi-select (last .digit-cursorPointer targets the Roles field)
        page.locator(".digit-cursorPointer").last().click();
        page.waitForTimeout(500);
        // :has(> child) matches only the immediate row div (not ancestor wrappers that also contain all checkboxes)
        page.locator("div:has(> .digit-multi-select-dropdown-menuitem)")
                .filter(new Locator.FilterOptions().setHasText(roleName))
                .first()
                .locator(".digit-multi-select-dropdown-menuitem")
                .check();
        page.getByText("Login DetailsUsername *").click();

        // Jurisdiction — expand the Country section and pick by name
        page.locator("div")
                .filter(new Locator.FilterOptions().setHasText(Pattern.compile("^Area 1\\*Country$")))
                .first().click();
        page.locator(".cp.profile-dropdown--item")
                .filter(new Locator.FilterOptions().setHasText(jurisdiction))
                .first().click();

        return this;
    }

    // ==================== FULL CREATE FLOW ====================

    public static String generateMobile() {
        return "8" + String.format("%09d", ThreadLocalRandom.current().nextInt(100000000, 999999999));
    }

    private void fillCreateForm(String username, String mobile) {
        String[] roles = TestDataReader.get("HRMS_ROLES").split(",");
        String role = roles[ThreadLocalRandom.current().nextInt(roles.length)].trim();
        String country = ConfigReader.get("COUNTRY");

        System.out.println("[HRMS] Username: " + username + " | Mobile: " + mobile + " | Role: " + role);

        selectHierarchyType(country, country)
            .clickNext()
            .fillLoginDetails(username, TestDataReader.get("HRMS_PASSWORD"))
            .fillPersonalDetails(
                TestDataReader.get("HRMS_NAME"),
                mobile,
                TestDataReader.get("HRMS_GENDER"),
                TestDataReader.get("HRMS_DOB"),
                TestDataReader.get("HRMS_EMAIL"),
                TestDataReader.get("HRMS_ADDRESS"))
            .fillEmploymentDetails(
                TestDataReader.get("HRMS_TYPE"),
                TestDataReader.get("HRMS_DOA"),
                TestDataReader.get("HRMS_DEPARTMENT"),
                TestDataReader.get("HRMS_DESIGNATION"),
                role,
                TestDataReader.get("HRMS_JURISDICTION"));
    }

    public String createEmployee() {
        String username = "Test-" + System.currentTimeMillis() % 100000;
        fillCreateForm(username, generateMobile());
        submitForm();
        return getCreatedUsername();
    }

    public String createEmployeeWithMobile(String mobile) {
        String username = "Test-" + System.currentTimeMillis() % 100000;
        fillCreateForm(username, mobile);
        submitForm();
        return getCreatedUsername();
    }

    public boolean createEmployeeWithDuplicateUsername(String username) {
        fillCreateForm(username, generateMobile());

        Locator submitBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit"));
        submitBtn.first().click();
        page.waitForTimeout(2000);
        submitBtn.last().click();

        try {
            page.getByText("Username exists")
                    .waitFor(new Locator.WaitForOptions().setTimeout(10000));
            System.out.println("[HRMS] Toast confirmed: 'Username exists' for duplicate username: " + username);
            return true;
        } catch (Exception e) {
            System.out.println("[HRMS] 'Username exists' toast not found for: " + username);
            return false;
        }
    }

    public boolean createEmployeeWithDuplicateMobile(String mobile) {
        String username = "Test-" + System.currentTimeMillis() % 100000;
        fillCreateForm(username, mobile);

        Locator submitBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit"));
        submitBtn.first().click();
        page.waitForTimeout(2000);
        submitBtn.last().click();

        try {
            page.getByText("Mobile number already exist!")
                    .waitFor(new Locator.WaitForOptions().setTimeout(10000));
            System.out.println("[HRMS] Toast confirmed: 'Mobile number already exist!' for: " + mobile);
            return true;
        } catch (Exception e) {
            System.out.println("[HRMS] 'Mobile number already exist!' toast not found for: " + mobile);
            return false;
        }
    }

    public boolean searchAndVerifyEmployee(String username) {
        goBackToHome();
        goToSearchFromHome();
        page.waitForTimeout(3000);
        searchEmployee(username);

        // Click the username link in results to open Employee Details
        Locator link = page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName(username));
        link.waitFor(new Locator.WaitForOptions().setTimeout(30000));
        link.click();

        // On Employee Details screen verify the Username field matches
        return verifyUsernameOnDetailsPage(username);
    }

    private boolean verifyUsernameOnDetailsPage(String username) {
        try {
            // Employee Details shows "Username" label with the value below it
            page.getByText("Username").waitFor(new Locator.WaitForOptions().setTimeout(10000));
            page.getByText(username).waitFor(new Locator.WaitForOptions().setTimeout(5000));
            System.out.println("[HRMS] Employee details verified — Username: " + username);
            return true;
        } catch (Exception e) {
            System.out.println("[HRMS] Username not found on Employee Details page: " + username);
            return false;
        }
    }

    // ==================== STEP 6: SUBMIT ====================

    public void submitForm() {
        Locator submitBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit"));
        submitBtn.first().click();

        // Popup appears with a second Submit — wait for it to render then click last()
        // .last() targets popup button even if the original Submit is still in the DOM
        page.waitForTimeout(1000);
        submitBtn.last().click();

        page.getByText("Employee Created Successfully")
                .waitFor(new Locator.WaitForOptions().setTimeout(60000));
    }

    // ==================== VERIFICATION ====================

    public boolean isEmployeeCreatedSuccessfully() {
        try {
            page.getByText("Employee Created Successfully")
                    .waitFor(new Locator.WaitForOptions().setTimeout(60000));
            return true;
        } catch (Exception e) {
            System.out.println("[HRMS] Success screen not found: " + e.getMessage().split("\n")[0]);
            return false;
        }
    }

    public String getCreatedUsername() {
        try {
            // Success screen: "Username" label / "Test-XXXXX" value on the next line
            // Find innermost element whose complete text is exactly "Test-<digits>"
            Locator usernameLocator = page.locator("*")
                    .filter(new Locator.FilterOptions().setHasText(Pattern.compile("^Test-\\d+$")))
                    .last();
            usernameLocator.waitFor(new Locator.WaitForOptions().setTimeout(10000));
            String username = usernameLocator.textContent().trim();
            System.out.println("[HRMS] Captured username from success screen: " + username);
            return username;
        } catch (Exception e) {
            System.out.println("[HRMS] Could not read username from success screen: " + e.getMessage().split("\n")[0]);
            return null;
        }
    }

    // ==================== EMPLOYEE INBOX: SEARCH ====================

    public HRMSPage openEmployeeByUsername(String username) {
        Locator link = page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName(username));
        link.waitFor(new Locator.WaitForOptions().setTimeout(30000));
        link.click();
        return this;
    }

    public HRMSPage searchEmployee(String empId) {
        page.locator(searchInput).waitFor(new Locator.WaitForOptions().setTimeout(10000));
        page.locator(searchInput).fill(empId);
        page.waitForTimeout(500);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
        page.waitForTimeout(2000);
        System.out.println("[HRMS] Searched for employee: " + empId);
        return this;
    }

    public boolean isEmployeeFound(String username) {
        try {
            // Search results show the username as a link
            Locator link = page.getByRole(AriaRole.LINK,
                    new Page.GetByRoleOptions().setName(username));
            link.waitFor(new Locator.WaitForOptions().setTimeout(10000));
            System.out.println("[HRMS] Employee found in results: " + username);
            return link.isVisible();
        } catch (Exception e) {
            System.out.println("[HRMS] Employee not found in results: " + username);
            return false;
        }
    }

    public HRMSPage openEmployeeResult() {
        page.waitForTimeout(2000);
        String[] selectors = {
                "table tbody tr td:first-child a",
                "tbody tr td a",
                "tbody a"
        };
        for (String sel : selectors) {
            int count = page.locator(sel).count();
            System.out.println("[HRMS] Selector '" + sel + "' count: " + count);
            if (count > 0) {
                page.locator(sel).first().dispatchEvent("click");
                page.waitForTimeout(2000);
                System.out.println("[HRMS] Opened employee result using: " + sel);
                return this;
            }
        }
        throw new RuntimeException("[HRMS] No employee result link found in results table");
    }

    public HRMSPage openTakeActionMenu() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Take Action")).click();
        page.waitForTimeout(500);
        return this;
    }

    private void clickMenuItemByText(String menuText) {
        page.getByText(menuText).click();
        page.waitForTimeout(500);
    }

    // ==================== EMPLOYEE INBOX: EDIT EMPLOYEE ====================

    public HRMSPage editAndSave() {
        String updatedName = TestDataReader.get("HRMS_NAME") + " Updated";

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Take Action")).click();
        page.getByText("Edit Employee").click();

        nameInput.waitFor(new Locator.WaitForOptions().setTimeout(10000));
        nameInput.fill(updatedName);
        System.out.println("[HRMS] Updated name to: " + updatedName);

        // Same two-Submit pattern as create
        Locator submitBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit"));
        submitBtn.first().click();
        page.waitForTimeout(2000);
        submitBtn.last().click();

        page.getByText("Employee Details Updated")
                .waitFor(new Locator.WaitForOptions().setTimeout(30000));
        return this;
    }

    // ==================== EMPLOYEE INBOX: DEACTIVATE ====================

    public boolean performDeactivate(String username) {
        openTakeActionMenu();
        page.getByText("Deactivate Employee").click();

        // Select deactivation reason — pick randomly from comma-separated list
        page.getByRole(AriaRole.TEXTBOX).first().click();
        String[] deactivationReasons = TestDataReader.get("HRMS_DEACTIVATION_REASON").split(",");
        String deactivationReason = deactivationReasons[ThreadLocalRandom.current().nextInt(deactivationReasons.length)].trim();
        System.out.println("[HRMS] Selected deactivation reason: " + deactivationReason);
        try {
            page.getByText(deactivationReason).waitFor(new Locator.WaitForOptions().setTimeout(5000));
            page.getByText(deactivationReason).click();
        } catch (Exception e) {
            System.out.println("[HRMS] Reason '" + deactivationReason + "' not found, selecting first available");
            page.keyboard().press("ArrowDown");
            page.waitForTimeout(300);
            page.keyboard().press("Enter");
        }

        // Enter remarks
        Locator remarks = page.getByRole(AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName("Enter Remarks"));
        remarks.click();
        remarks.fill(TestDataReader.get("HRMS_DEACTIVATION_REMARKS"));

        // Confirm deactivation
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Deactivate Employee")).click();

        try {
            page.getByText("Employee Deactivated")
                    .waitFor(new Locator.WaitForOptions().setTimeout(30000));
            System.out.println("[HRMS] Employee deactivated: " + username);
            return true;
        } catch (Exception e) {
            System.out.println("[HRMS] Deactivation failed: " + e.getMessage().split("\n")[0]);
            return false;
        }
    }

    // ==================== EMPLOYEE INBOX: REACTIVATE ====================

    public boolean performReactivate(String username) {
        openTakeActionMenu();
        page.getByText("Activate Employee").click();

        // Select reactivation reason — pick randomly from comma-separated list
        page.getByRole(AriaRole.TEXTBOX).first().click();
        String[] reactivationReasons = TestDataReader.get("HRMS_REACTIVATION_REASON").split(",");
        String reactivationReason = reactivationReasons[ThreadLocalRandom.current().nextInt(reactivationReasons.length)].trim();
        System.out.println("[HRMS] Selected reactivation reason: " + reactivationReason);
        try {
            page.getByText(reactivationReason).waitFor(new Locator.WaitForOptions().setTimeout(5000));
            page.getByText(reactivationReason).click();
        } catch (Exception e) {
            System.out.println("[HRMS] Reason '" + reactivationReason + "' not found, selecting first available");
            page.keyboard().press("ArrowDown");
            page.waitForTimeout(300);
            page.keyboard().press("Enter");
        }

        try {
            page.getByText("Employee Activated Successfully")
                    .waitFor(new Locator.WaitForOptions().setTimeout(30000));
            System.out.println("[HRMS] Employee reactivated: " + username);
            return true;
        } catch (Exception e) {
            System.out.println("[HRMS] Reactivation failed: " + e.getMessage().split("\n")[0]);
            return false;
        }
    }

    // ==================== EMPLOYEE INBOX: CAMPAIGN ASSIGNMENT ====================

    public HRMSPage clickEditCampaignAssignment() {
        clickMenuItemByText("Edit Campaign Assignment");
        return this;
    }

    public HRMSPage selectTodayForCampaign() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Locator dateInput = page.locator(campaignDateInput).last();
        dateInput.waitFor(new Locator.WaitForOptions().setTimeout(30000));
        dateInput.scrollIntoViewIfNeeded();
        dateInput.fill(today);
        dateInput.dispatchEvent("change");
        page.waitForTimeout(500);
        System.out.println("[HRMS] Campaign date set to: " + today);
        return this;
    }

    // ==================== EMPLOYEE INBOX: SUCCESS & NAVIGATION ====================

    public boolean isSuccessMessageVisible(String expectedText) {
        try {
            page.getByText(expectedText)
                    .waitFor(new Locator.WaitForOptions().setTimeout(30000));
            System.out.println("[HRMS] Success message visible: " + expectedText);
            return true;
        } catch (Exception e) {
            System.out.println("[HRMS] Primary success banner not found: " + e.getMessage().split("\n")[0]);
        }

        String[] toastSelectors = {
                ".toast-success", ".Toastify__toast", "div[role='alert']",
                ".digit-toast", "[class*='toast']", "[class*='success']"
        };
        for (String sel : toastSelectors) {
            try {
                if (page.locator(sel).count() > 0) {
                    String text = page.locator(sel).first().textContent();
                    System.out.println("[HRMS] Toast message (" + sel + "): " + text);
                    if (text.contains(expectedText)) return true;
                }
            } catch (Exception ex) {
                System.out.println("[HRMS] Toast check failed for " + sel + ": " + ex.getMessage().split("\n")[0]);
                break;
            }
        }

        System.out.println("[HRMS] No success indicator found for: " + expectedText);
        return false;
    }

    public void goBackToHome() {
        Locator btn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Go Back to Home"));
        try {
            btn.waitFor(new Locator.WaitForOptions().setTimeout(10000));
            btn.click();
        } catch (Exception e) {
            page.navigate(page.url().split("/hrms")[0] + "/digit-ui/employee");
        }
        // Wait for home page cards to render (React SPA — load event fires before components mount)
        page.locator("h2.digit-button-label").first()
                .waitFor(new Locator.WaitForOptions().setTimeout(15000));
        System.out.println("[HRMS] Navigated back to home");
    }

    public HRMSPage goToSearchFromHome() {
        // Use the same CSS locator as HomePage — the card may be a div, not a <button>
        Locator searchUserCard = page.locator("h2.digit-button-label")
                .filter(new Locator.FilterOptions().setHasText("Search User"));
        searchUserCard.waitFor(new Locator.WaitForOptions().setTimeout(10000));
        searchUserCard.click();

        // Hierarchy selection screen appears first — click Next to reach the search form
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next"))
                .waitFor(new Locator.WaitForOptions().setTimeout(10000));
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();

        page.locator(searchInput).waitFor(new Locator.WaitForOptions().setTimeout(15000));
        return this;
    }
}
