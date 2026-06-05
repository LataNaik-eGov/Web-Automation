package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
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
    private Locator registrationAndDeliveryModule;
    private Locator deliveryTypeDropdown;
    private Locator closeHouseholdModule;
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

    public AppConfigurationPage(Page page) {
        this.page = page;
        String campaignType = ConfigReader.get("CAMPAIGN_TYPE");
        this.campaignDisplayName = CAMPAIGN_DISPLAY_NAMES.getOrDefault(campaignType, campaignType);
        this.deliveryTypeDropdown = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Select an option"));
        this.setUpMobileAppButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Set App Configurations"));
        this.registrationAndDeliveryModule = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Register eligible children")).getByLabel("Configure");
        this.closeHouseholdModule = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Record households that were")).getByLabel("Configure");
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
    }

    // --- Actions ---

    public void clickSetUpMobileApp() {
        setUpMobileAppButton.waitFor(new Locator.WaitForOptions().setTimeout(15000));
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
        registrationAndDeliveryModule.click();
        clickSaveConfiguration();
    }

    public void configureCloseHousehold() {
        closeHouseholdModule.click();
        clickSaveConfiguration();
    }

    public void configureComplaints() {
        complaintsModule.click();
        clickSaveConfiguration();
    }

    public void configureInventory() {
        inventoryModule.click();
        clickSaveConfiguration();
    }

    public void configureStockReconciliation() {
        stockReconciliationModule.click();
        clickSaveConfiguration();
    }

    public void configureReports() {
        reportsModule.click();
        clickSaveConfiguration();
    }

    public void configurePermissionHandler() {
        permissionHandlerModule.click();
        clickSaveConfiguration();
    }

    public void clickRegistrationAndDeliveryConfigure() {
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

    public boolean isLabelLocalizationToastVisible() {
        labelLocalizationToast.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        return labelLocalizationToast.isVisible();
    }

    public void clickGoBack() {
        goBackButton.click();
    }
}
