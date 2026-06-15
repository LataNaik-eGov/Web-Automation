package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import utils.TestDataReader;

import java.util.Map;

public class AppConfigurationPage extends BasePage {

    private static final Map<String, String> CAMPAIGN_DISPLAY_NAMES = Map.of(
            "BEDNET", "Bednet Distribution",
            "MR-DN", "Seasonal Malaria Chemoprevention (SMC)"
    );

    private final String campaignType;
    private final String campaignDisplayName;

    // App configuration elements
    private Locator setUpMobileAppButton;
    private Locator registrationAndDeliveryModule;
    private Locator deliveryTypeDropdown;
    private Locator closeHouseholdModule;
    private Locator referralModule;
    private Locator complaintsModule;
    private Locator inventoryModule;
    private Locator stockReconciliationModule;
    private Locator reportsModule;
    private Locator permissionHandlerModule;
    private Locator saveConfigurationButton;
    private Locator goBackButton;
    private Locator searchBeneficiaryFlow;
    private Locator proximitySearchElement;
    private Locator labelInput;
    private Locator labelLocalizationToast;
    private Locator firstToggleSwitchOn;

    public AppConfigurationPage(Page page) {
        super(page);
        this.campaignType = TestDataReader.getSessionValue("CAMPAIGN_TYPE");
        this.campaignDisplayName = CAMPAIGN_DISPLAY_NAMES.getOrDefault(campaignType, campaignType);
        this.deliveryTypeDropdown = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Select an option"));
        this.setUpMobileAppButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Set App Configurations"));
        this.registrationAndDeliveryModule = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Register eligible children")).getByLabel("Configure");

        this.closeHouseholdModule = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Record households that were")).getByLabel("Configure");
        this.referralModule = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Record and manage referrals")).getByLabel("Configure");
        this.complaintsModule = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Let field workers log issues")).getByLabel("Configure");
        this.inventoryModule = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Track and update resources")).getByLabel("Configure");
        this.stockReconciliationModule = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Compare reported stock")).getByLabel("Configure");
        this.reportsModule = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Access summary and detailed")).getByLabel("Configure");
        this.permissionHandlerModule = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Manages user permissions")).getByLabel("Configure");
        this.saveConfigurationButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Submit"));
        this.goBackButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Go Back"));
        this.searchBeneficiaryFlow = page.getByText("Search Beneficiary").first();
        this.proximitySearchElement = page.getByText("Search by proximity").locator("..");
        this.labelInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Label"));
        this.labelLocalizationToast = page.getByText("Label localization is empty for field");
        this.firstToggleSwitchOn = page.getByRole(AriaRole.SWITCH,
                new Page.GetByRoleOptions().setName("Toggle switch on")).first();
    }

    // --- Private helper ---

    private void configureModule(Locator module) {
        waitForVisible(module);
        module.click();
        saveConfigurationButton.last().waitFor();
        saveConfigurationButton.last().click();
    }

    // --- Actions ---

    public void clickSetUpMobileApp() {
        waitForVisible(setUpMobileAppButton);
        setUpMobileAppButton.click();
    }

    public void clickSaveConfiguration() {
        saveConfigurationButton.last().click();
    }

    public void selectDeliveryType() {
        deliveryTypeDropdown.click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(campaignDisplayName).setExact(true)).click();
    }

    public void configureRegistrationAndDelivery() {
        configureModule(registrationAndDeliveryModule);
    }

    public void configureCloseHousehold() {
        configureModule(closeHouseholdModule);
    }

    public void configureReferral() {
        if (!"MR-DN".equals(campaignType)) return;
        configureModule(referralModule);
    }

    public void configureComplaints() {
        configureModule(complaintsModule);
    }

    public void configureInventory() {
        configureModule(inventoryModule);
    }

    public void configureStockReconciliation() {
        configureModule(stockReconciliationModule);
    }

    public void configureReports() {
        configureModule(reportsModule);
    }

    public void configurePermissionHandler() {
        configureModule(permissionHandlerModule);
    }

    public void clickRegistrationAndDeliveryConfigure() {
        waitForVisible(registrationAndDeliveryModule);
        registrationAndDeliveryModule.click();
    }

    public void clickSearchBeneficiaryFlow() {
        searchBeneficiaryFlow.click();
    }

    public void clickProximitySearchElement() {
        proximitySearchElement.dispatchEvent("click");
    }

    public void clearLabelField() {
        labelInput.click();
        labelInput.fill("");
    }

    public void fillLabelField(String value) {
        labelInput.dblclick();
        labelInput.fill(value);
    }

    public void clickFirstToggleSwitchOff() {
        firstToggleSwitchOn.click();
    }

    public boolean isLabelLocalizationToastVisible() {
        return waitAndCheckVisible(labelLocalizationToast, 5000);
    }

    public void clickGoBack() {
        goBackButton.click();
    }
}
