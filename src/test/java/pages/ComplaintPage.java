package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.nio.file.Paths;
import java.util.regex.Pattern;

import utils.ConfigReader;

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
    private final Locator fileInput;
    private final Locator submitButton;

    // Confirmation locator
    private final Locator complaintNumberLabel;
    private final Locator backToComplaintsInbox;

    // Search, resolve & reject complaint locators
    private final Locator complaintNumberInput;
    private final Locator searchButton;
    private final Locator takeActionButton;
    private final Locator resolveOption;
    private final Locator rejectOption;
    private final Locator rejectionReasonDropdown;
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
        this.fileInput = page.locator("input[type=\"file\"]");
        this.submitButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit"));
        this.complaintNumberLabel = page.locator(".digit-panel-response");
        this.backToComplaintsInbox = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search Complaint"));
        this.complaintNumberInput = page.locator("input[name=\"complaintNumber\"]");
        this.searchButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search").setExact(true));
        this.takeActionButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Take Action"));
        this.resolveOption = page.getByText("Resolve");
        this.rejectOption = page.getByText("Reject");
        this.rejectionReasonDropdown = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Rejection Reason"));
        this.employeeComments = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Employee Comments"));
    }

    // ==================== MAIN ACTIONS ====================

    public String fillForm(String date, String description) {
        // Wait for page auto-refresh to complete before interacting
        waitForVisible(complaintTypeDropdown);
        wait(10000);
        page.waitForLoadState();
        waitForVisible(complaintTypeDropdown);
        selectComplaintType();
        selectDate(date);
        selectCountry();
        selectState();
        selectLGA();
        selectWard();
        selectVillage();
        selectArea();
        selectComplainant();
        enterDescription(description);
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

    // ==================== INDIVIDUAL ACTIONS ====================

    public void selectComplaintType() {
        complaintTypeDropdown.click();
        complaintTypeDropdown.click();
        wait(1000);
        page.getByText("Performance Issue: Dept-").click();
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
        fileInput.setInputFiles(Paths.get(filePath));
    }

    public void clickSubmit() {
        submitButton.click();
    }

    // ==================== SEARCH & RESOLVE ====================

    public ComplaintPage searchAndResolve(String complaintNumber, String comments) {
        searchComplaint(complaintNumber);
        openComplaint(complaintNumber);
        resolve(comments);
        return this;
    }

    public void searchComplaint(String complaintNumber) {
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

    public void resolve(String comments) {
        takeAction();
        clickResolve();
        enterComments(comments);
        clickSubmit();
    }

    // ==================== REJECT ====================

    public ComplaintPage searchAndReject(String complaintNumber, String comments) {
        searchComplaint(complaintNumber);
        openComplaint(complaintNumber);
        reject(comments);
        return this;
    }

    public void clickReject() {
        rejectOption.click();
    }

    public void selectRejectionReason() {
        rejectionReasonDropdown.click();
        wait(1000);
        page.getByText("CS_REJECTION__REASON3").click();
    }

    public void reject(String comments) {
        takeAction();
        clickReject();
        selectRejectionReason();
        enterComments(comments);
        clickSubmit();
    }
}
