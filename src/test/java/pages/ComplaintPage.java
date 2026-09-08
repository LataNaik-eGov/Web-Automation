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

    // Hierarchy selection locators
    private final Locator nextButton;

    // Create complaint locators
    private final Locator complaintTypeDropdown;
    private final Locator complaintDateInput;
    private final Locator complainantRadio;
    private final Locator complainantNameInput;
    private final Locator complainantContactInput;
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
        this.nextButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next"));
        this.complaintTypeDropdown = page.locator("#pgr-create-complaint-standalone-cs_complaint_details_complaint_type-field");
        this.complaintDateInput = page.locator("input[name=\"ComplaintDate\"]");
        this.complainantRadio = page.locator("#pgr-create-complaint-standalone-es_createcomplaint_for-field-0");
        this.complainantNameInput = page.locator("#pgr-create-complaint-standalone-complaints_complainant_name-field");
        this.complainantContactInput = page.locator("#pgr-create-complaint-standalone-complaints_complainant_contact_number-field");
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
        selectHierarchyType();
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
        link.waitFor(new Locator.WaitForOptions().setTimeout(60000));
        return link.isVisible();
    }

    // ==================== INDIVIDUAL ACTIONS ====================

    public void selectHierarchyType() {
        String hierarchy = ConfigReader.get("HIERARCHY_TYPE");
        selectHierarchyType(hierarchy != null ? hierarchy : "NIGERIA");
    }

    public void selectHierarchyType(String hierarchy) {
        // The hierarchy tile is a div[role="button"] in this environment (not a literal
        // <button>), and its inner content has pointer-events:none in headless Chromium,
        // so a plain click is reported as intercepted by its own wrapper.
        Locator card = page.locator(
                "button:has-text('" + hierarchy + "'), div.select-hierarchy-campaign-selection-card:has-text('" + hierarchy + "')")
                .first();
        card.scrollIntoViewIfNeeded();
        card.dispatchEvent("click");
        nextButton.click();
        page.waitForLoadState();
    }

    public void selectComplaintType() {
        complaintTypeDropdown.click(new Locator.ClickOptions().setForce(true));
        waitForOverlayToHide();
        String[] types = TestDataReader.get("COMPLAINT_TYPES").split(",");
        String type = types[new java.util.Random().nextInt(types.length)].trim();
        page.locator("div.digit-dropdown-item").filter(new Locator.FilterOptions().setHasText(type)).first().click();
    }

    public void selectDate(String date) {
        complaintDateInput.fill(date);
    }

    // Locates a boundary dropdown's input by the label of its level (e.g. "Country",
    // "Province"), scoped to its own boundary-dropdown-wrapper — avoids the fragile
    // nth-index locators that broke whenever the boundary section's layout shifted.
    private Locator boundaryDropdown(String levelLabel) {
        return page.locator(
                "xpath=//div[contains(@class,'boundary-dropdown-wrapper')]" +
                "[.//div[starts-with(normalize-space(),'" + levelLabel + "')]]//input");
    }

    public void selectBoundaryOption(String levelLabel, String optionText) {
        Locator dropdown = boundaryDropdown(levelLabel);
        // The boundary section renders a loading spinner until its data arrives, which
        // can take well past the default 30s timeout on this environment.
        dropdown.waitFor(new Locator.WaitForOptions().setTimeout(90000));
        dropdown.click(new Locator.ClickOptions().setForce(true));
        page.locator("div.digit-dropdown-item").filter(new Locator.FilterOptions().setHasText(optionText)).first().click();
    }

    public void selectCountry() {
        selectBoundaryOption("Country", ConfigReader.get("COUNTRY"));
    }

    public void selectState() {
        selectBoundaryOption("Province", ConfigReader.get("STATE"));
    }

    public void selectLGA() {
        selectBoundaryOption("District", ConfigReader.get("LGA"));
    }

    public void selectWard() {
        selectBoundaryOption("Administrative Post", ConfigReader.get("WARD"));
    }

    public void selectVillage() {
        selectBoundaryOption("Locality", ConfigReader.get("VILLAGE"));
    }

    public void selectArea() {
        selectBoundaryOption("Village", ConfigReader.get("AREA"));
    }

    public void selectComplainant() {
        complainantRadio.click();
        // Selecting "Myself" auto-fills these from the logged-in user's profile and
        // disables them; only fill in manually if the app didn't do that for us.
        if (!complainantNameInput.isDisabled()) {
            complainantNameInput.fill("Automation Test");
        }
        if (!complainantContactInput.isDisabled()) {
            complainantContactInput.fill(HRMSPage.generateMobile());
        }
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
        // A freshly-created complaint can take a while to be searchable/indexed on
        // this environment, well past the default 30s timeout.
        Locator link = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(complaintNumber));
        link.waitFor(new Locator.WaitForOptions().setTimeout(60000));
        link.click();
    }

    public void takeAction() {
        waitForOverlayToHide();
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
        rejectionReasonDropdown.click(new Locator.ClickOptions().setForce(true));
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
        selectEmployeeDropdown.click(new Locator.ClickOptions().setForce(true));
        wait(500);
        String employeeName = TestDataReader.get("ASSIGN_EMPLOYEE");
        // Type to filter the dropdown to only matching employees, avoiding false matches
        // from other page elements (e.g. the logged-in user name in the header).
        selectEmployeeDropdown.fill(employeeName);
        wait(1000);
        try {
            Locator option = page.locator("li")
                    .filter(new Locator.FilterOptions().setHasText(employeeName))
                    .first();
            option.waitFor(new Locator.WaitForOptions().setTimeout(5000));
            option.click();
        } catch (Exception e) {
            System.out.println("[Complaint] Employee '" + employeeName + "' not found in filtered list, using keyboard selection");
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
        try {
            page.locator(".digit-popup-overlay")
                    .waitFor(new Locator.WaitForOptions()
                            .setState(com.microsoft.playwright.options.WaitForSelectorState.HIDDEN)
                            .setTimeout(15000));
        } catch (Exception ignored) {
            wait(1000);
        }
    }
}
