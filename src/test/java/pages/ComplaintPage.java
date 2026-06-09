package pages;

import com.microsoft.playwright.options.FilePayload;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.regex.Pattern;

import utils.ConfigReader;
import utils.TestDataReader;

/**
 * Page Object for Complaint Form.
 */
public class ComplaintPage extends BasePage {

    // Create complaint locators
    private final Locator complaintTypeDropdown;
    private final Locator complaintDateInput;
    private final Locator countryDropdown;
    private final Locator stateDropdown;
    private final Locator lgaDropdown;
    private final Locator wardDropdown;
    private final Locator villageDropdown;
    private final Locator areaDropdown;
    private final Locator complainantRadio;
    private final Locator descriptionField;
    private final Locator submitButton;

    // Confirmation locator
    private final Locator complaintNumberLabel;
    private final Locator backToComplaintsInbox;

    // Search, resolve, reject & assign complaint locators
    private final Locator complaintNumberInput;
    private final Locator searchButton;
    private final Locator takeActionButton;
    private final Locator resolveOption;
    private final Locator rejectOption;
    private final Locator assignOption;
    private final Locator rejectionReasonDropdown;
    private final Locator selectEmployeeDropdown;
    private final Locator employeeComments;

    public ComplaintPage(Page page) {
        super(page);
        this.complaintTypeDropdown = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Complaint Type"));
        this.complaintDateInput = page.locator("input[name=\"ComplaintDate\"]");
        this.countryDropdown = page.getByRole(AriaRole.TEXTBOX).nth(3);
        this.stateDropdown = page.getByRole(AriaRole.TEXTBOX).nth(4);
        this.lgaDropdown = page.getByRole(AriaRole.TEXTBOX).nth(5);
        this.wardDropdown = page.locator("input[type=\"text\"]").nth(5);
        this.villageDropdown = page.locator("div:nth-child(5) > .digit-text-input-field-without-card > .digit-dropdown-employee-select-wrap > .digit-dropdown-select > .digit-dropdown-employee-select-wrap--elipses");
        this.areaDropdown = page.locator("div:nth-child(6) > .digit-text-input-field-without-card > .digit-dropdown-employee-select-wrap > .digit-dropdown-select > .digit-dropdown-employee-select-wrap--elipses");
        this.complainantRadio = page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("Are you raising a complaint"));
        this.descriptionField = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Complaint description"));
        this.submitButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit"));
        this.complaintNumberLabel = page.locator(".digit-panel-response");
        this.backToComplaintsInbox = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search Complaint"));
        this.complaintNumberInput = page.locator("input[name=\"complaintNumber\"]");
        this.searchButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search").setExact(true));
        this.takeActionButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Take Action"));
        this.resolveOption = page.getByText("Resolve");
        this.rejectOption = page.getByText("Reject");
        this.assignOption = page.getByText("Assign", new Page.GetByTextOptions().setExact(true));
        this.rejectionReasonDropdown = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Rejection Reason"));
        this.selectEmployeeDropdown = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Select Employee"));
        this.employeeComments = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Employee Comments"));
    }

    // ==================== MAIN ACTIONS ====================

    public String fillForm(String description) {
        return fillFormWithFile(description, null);
    }

    public String fillFormWithFile(String description, String filePath) {
        waitForVisible(complaintTypeDropdown);
        wait(5000);
        page.waitForLoadState();
        waitForVisible(complaintTypeDropdown);
        selectComplaintType();
        selectDate(LocalDate.now().toString());
        selectCountry();
        selectState();
        selectLGA();
        selectWard();
        selectVillage();
        selectArea();
        selectComplainant();
        enterDescription(description);
        if (filePath != null) {
            uploadFile(filePath);
            wait(2000);
        }
        clickSubmit();
        return getComplaintNumber();
    }

    public String getComplaintNumber() {
        waitForVisible(complaintNumberLabel);
        return complaintNumberLabel.textContent().trim();
    }

    public void clickBackToComplaintsInbox() {
        backToComplaintsInbox.click();
    }

    public boolean isStatusVisible(String expectedStatus) {
        Locator statusDiv = page.locator("div")
            .filter(new Locator.FilterOptions().setHasText(Pattern.compile("^" + expectedStatus + "$")))
            .first();
        statusDiv.waitFor();
        return statusDiv.isVisible();
    }

    public boolean isComplaintFound(String complaintNumber) {
        searchComplaint(complaintNumber);
        Locator link = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(complaintNumber));
        link.waitFor();
        return link.isVisible();
    }

    // ==================== INDIVIDUAL ACTIONS ====================

    public void selectComplaintType() {
        complaintTypeDropdown.click();
        complaintTypeDropdown.click();
        wait(1000);
        String[] types = TestDataReader.get("COMPLAINT_TYPES").split(",");
        String type = types[new java.util.Random().nextInt(types.length)].trim();
        page.getByText(type, new Page.GetByTextOptions().setExact(true)).click();
    }

    public void selectDate(String date) {
        complaintDateInput.fill(date);
    }

    public void selectCountry() {
        countryDropdown.click();
        wait(1000);
        page.locator("div").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^" + ConfigReader.get("COUNTRY") + "$"))).nth(3).click();
    }

    public void selectState() {
        stateDropdown.click();
        wait(1000);
        page.getByText(ConfigReader.get("STATE")).click();
    }

    public void selectLGA() {
        lgaDropdown.click();
        wait(1000);
        page.getByText(ConfigReader.get("LGA")).click();
    }

    public void selectWard() {
        wardDropdown.click();
        wait(1000);
        page.getByText(ConfigReader.get("WARD")).click();
    }

    public void selectVillage() {
        villageDropdown.click();
        wait(1000);
        page.getByText(ConfigReader.get("VILLAGE")).click();
    }

    public void selectArea() {
        areaDropdown.click();
        wait(1000);
        page.getByText(ConfigReader.get("AREA")).click();
    }

    public void selectComplainant() {
        complainantRadio.check();
    }

    public void enterDescription(String description) {
        descriptionField.click();
        descriptionField.fill(description);
    }

    public void uploadFile(String filePath) {
        try {
            java.nio.file.Path path = Paths.get(filePath).toAbsolutePath();
            String fileName = path.getFileName().toString();
            String mimeType = fileName.endsWith(".pdf") ? "application/pdf" : "image/jpeg";
            byte[] content = Files.readAllBytes(path);
            page.locator("#upload-complaintFile").setInputFiles(new FilePayload(fileName, mimeType, content));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read upload file: " + filePath, e);
        }
    }

    public void clickSubmit() {
        submitButton.click();
    }

    // ==================== SEARCH & RESOLVE ====================

    public ComplaintPage searchAndResolve(String complaintNumber, String comments) {
        searchComplaint(complaintNumber);
        openComplaint(complaintNumber);
        resolve(comments, null);
        return this;
    }

    public ComplaintPage searchAndResolve(String complaintNumber, String comments, String filePath) {
        searchComplaint(complaintNumber);
        openComplaint(complaintNumber);
        resolve(comments, filePath);
        return this;
    }

    public void searchComplaint(String complaintNumber) {
        page.waitForTimeout(1000);
        complaintNumberInput.click();
        complaintNumberInput.fill(complaintNumber);
        searchButton.click();
    }

    public void openComplaint(String complaintNumber) {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(complaintNumber)).click();
    }

    public void takeAction() {
        takeActionButton.click();
    }

    public void clickResolve() {
        resolveOption.click();
    }

    public void enterComments(String comments) {
        employeeComments.click();
        employeeComments.fill(comments);
    }

    public void resolve(String comments, String filePath) {
        takeAction();
        clickResolve();
        enterComments(comments);
        if (filePath != null) {
            uploadFile(filePath);
            wait(3000);
        }
        clickSubmit();
    }

    // ==================== REJECT ====================

    public ComplaintPage searchAndReject(String complaintNumber, String comments) {
        searchComplaint(complaintNumber);
        openComplaint(complaintNumber);
        reject(comments, null);
        return this;
    }

    public ComplaintPage searchAndReject(String complaintNumber, String comments, String filePath) {
        searchComplaint(complaintNumber);
        openComplaint(complaintNumber);
        reject(comments, filePath);
        return this;
    }

    public void clickReject() {
        rejectOption.click();
    }

    public void selectRejectionReason() {
        rejectionReasonDropdown.click();
        wait(1000);
        String[] reasons = TestDataReader.get("REJECTION_REASON").split(",");
        String reason = reasons[new java.util.Random().nextInt(reasons.length)].trim();
        page.getByText(reason, new Page.GetByTextOptions().setExact(true)).click();
    }

    public void reject(String comments, String filePath) {
        takeAction();
        clickReject();
        selectRejectionReason();
        enterComments(comments);
        if (filePath != null) {
            uploadFile(filePath);
            wait(3000);
        }
        clickSubmit();
    }

    // ==================== ASSIGN ====================

    public ComplaintPage searchAndAssign(String complaintNumber, String comments) {
        searchComplaint(complaintNumber);
        openComplaint(complaintNumber);
        assign(comments, null);
        return this;
    }

    public ComplaintPage searchAndAssign(String complaintNumber, String comments, String filePath) {
        searchComplaint(complaintNumber);
        openComplaint(complaintNumber);
        assign(comments, filePath);
        return this;
    }

    public void clickAssign() {
        assignOption.click();
    }

    public void selectEmployee() {
        waitForVisible(selectEmployeeDropdown);
        selectEmployeeDropdown.click();
        wait(1000);
        String employeeName = TestDataReader.get("ASSIGN_EMPLOYEE");
        try {
            page.getByText(employeeName).first()
                    .waitFor(new Locator.WaitForOptions().setTimeout(5000));
            page.getByText(employeeName).first().click();
        } catch (Exception e) {
            // Configured employee not in dropdown — navigate to first item with keyboard
            System.out.println("[Complaint] Employee '" + employeeName + "' not found, selecting first available");
            page.keyboard().press("ArrowDown");
            wait(300);
            page.keyboard().press("Enter");
        }
    }

    public void assign(String comments, String filePath) {
        takeAction();
        clickAssign();
        selectEmployee();
        wait(1000);
        waitForVisible(employeeComments);
        enterComments(comments);
        if (filePath != null) {
            uploadFile(filePath);
            wait(3000);
        }
        clickSubmit();
        waitForHidden(".digit-popup-overlay");
    }
}
