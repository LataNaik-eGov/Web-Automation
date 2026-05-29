package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import utils.ConfigReader;

import java.util.Map;

public class AppConfigurationPage {

    private Page page;

    private static final Map<String, String> CAMPAIGN_DISPLAY_NAMES = Map.of(
            "BEDNET", "Bednet Distribution",
            "MR-DN", "Seasonal Malaria Chemoprevention (SMC)"
    );

    private final String campaignDisplayName;

    // App configuration elements
    private Locator setUpMobileAppButton;
    private Locator registrationAndDelivery;
    private Locator deliveryTypeDropdown;
    private Locator closeHousehold;
    private Locator complaints;
    private Locator inventory;
    private Locator stockReconciliation;
    private Locator reports;
    private Locator permissionHandler;
    private Locator saveConfigurationButton;
    private Locator goBackButton;
    private Locator searchBeneficiaryFlow;
    private Locator proximitySearchElement;
    private Locator labelInput;
    private Locator labelLocalizationToast;

    public AppConfigurationPage(Page page) {
        this.page = page;
        String campaignType = ConfigReader.get("CAMPAIGN_TYPE");
        this.campaignDisplayName = CAMPAIGN_DISPLAY_NAMES.getOrDefault(campaignType, campaignType);
        this.deliveryTypeDropdown = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Select an option"));
        this.setUpMobileAppButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Set App Configurations"));
        this.registrationAndDelivery = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Register eligible children")).getByLabel("Configure");
        this.closeHousehold = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Record households that were")).getByLabel("Configure");
        this.complaints = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Let field workers log issues")).getByLabel("Configure");
        this.inventory = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Track and update resources")).getByLabel("Configure");
        this.stockReconciliation = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Compare reported stock")).getByLabel("Configure");
        this.reports = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Access summary and detailed")).getByLabel("Configure");
        this.permissionHandler = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Manages user permissions")).getByLabel("Configure");
        this.saveConfigurationButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Submit"));
        this.goBackButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Go Back"));
        this.searchBeneficiaryFlow = page.getByText("Search Beneficiary").first();
        this.proximitySearchElement = page.getByText("Search by proximity").locator("..");
        this.labelInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Label"));
        this.labelLocalizationToast = page.getByText("Label localization is empty for field");
    }

    // --- Actions ---

    public void clickSetUpMobileApp() {
        setUpMobileAppButton.click();
        page.waitForTimeout(2000);
    }

    public void clickSaveConfiguration() {
        saveConfigurationButton.last().click();
        page.waitForTimeout(2000);
    }

    public void selectDeliveryType() {
        deliveryTypeDropdown.click();
        page.waitForTimeout(1000);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(campaignDisplayName).setExact(true)).click();
        page.waitForTimeout(1000);
    }

    public void configureRegistrationAndDelivery() {
        registrationAndDelivery.click();
        page.waitForTimeout(4000);
        clickSaveConfiguration();
    }

    public void configureCloseHousehold() {
        closeHousehold.click();
        page.waitForTimeout(4000);
        clickSaveConfiguration();
    }

    public void configureComplaints() {
        complaints.click();
        page.waitForTimeout(4000);
        clickSaveConfiguration();
    }

    public void configureInventory() {
        inventory.click();
        page.waitForTimeout(4000);
        clickSaveConfiguration();
    }

    public void configureStockReconciliation() {
        stockReconciliation.click();
        page.waitForTimeout(4000);
        clickSaveConfiguration();
    }

    public void configureReports() {
        reports.click();
        page.waitForTimeout(4000);
        clickSaveConfiguration();
    }

    public void configurePermissionHandler() {
        permissionHandler.click();
        page.waitForTimeout(4000);
        clickSaveConfiguration();
    }

    public void clickRegistrationAndDeliveryConfigure() {
        registrationAndDelivery.click();
        page.waitForTimeout(4000);
    }

    public void clickSearchBeneficiaryFlow() {
        searchBeneficiaryFlow.click();
        page.waitForTimeout(2000);
    }

    public void clickProximitySearchElement() {
        proximitySearchElement.dispatchEvent("click");
        page.waitForTimeout(1000);
    }

    public void clearLabelField() {
        labelInput.click();
        page.waitForTimeout(500);
        labelInput.fill("");
        page.waitForTimeout(500);
    }

    public boolean isLabelLocalizationToastVisible() {
        labelLocalizationToast.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        return labelLocalizationToast.isVisible();
    }

    public void clickGoBack() {
        goBackButton.click();
        page.waitForTimeout(2000);
    }
}
