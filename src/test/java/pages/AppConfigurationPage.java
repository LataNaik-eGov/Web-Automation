package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import utils.ConfigReader;

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

    public AppConfigurationPage(Page page) {
        super(page);
        this.campaignType = ConfigReader.get("CAMPAIGN_TYPE");
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
    }

    // --- Actions ---

    public void clickSetUpMobileApp() {
        waitForVisible(setUpMobileAppButton);
      wait(6000);
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
        waitForVisible(registrationAndDeliveryModule);
      wait(6000);
        registrationAndDeliveryModule.click();
      wait(6000);
        clickSaveConfiguration();
    }

    public void configureCloseHousehold() {
        waitForVisible(closeHouseholdModule);
      wait(6000);
        closeHouseholdModule.click();
      wait(6000);
        clickSaveConfiguration();
    }

    public void configureReferral() {
        if (!"MR-DN".equals(campaignType)) return;
        waitForVisible(referralModule);
      wait(6000);
        referralModule.click();
      wait(6000);
        clickSaveConfiguration();
    }

    public void configureComplaints() {
        waitForVisible(complaintsModule);
      wait(6000);
        complaintsModule.click();
      wait(6000);
        clickSaveConfiguration();
    }

    public void configureInventory() {
        waitForVisible(inventoryModule);
      wait(6000);
        inventoryModule.click();
      wait(6000);
        clickSaveConfiguration();
    }

    public void configureStockReconciliation() {
        waitForVisible(stockReconciliationModule);
      wait(6000);
        stockReconciliationModule.click();
      wait(6000);
        clickSaveConfiguration();
    }

    public void configureReports() {
        waitForVisible(reportsModule);
      wait(6000);
        reportsModule.click();
      wait(6000);
        clickSaveConfiguration();
    }

    public void configurePermissionHandler() {
        waitForVisible(permissionHandlerModule);
      wait(6000);
        permissionHandlerModule.click();
      wait(6000);
        clickSaveConfiguration();
    }

    public void clickRegistrationAndDeliveryConfigure() {
        waitForVisible(registrationAndDeliveryModule);
      wait(6000);
        registrationAndDeliveryModule.click();
        wait(6000);
    }

    public void clickSearchBeneficiaryFlow() {
        searchBeneficiaryFlow.click();
        wait(6000);
    }

    public void clickProximitySearchElement() {
        proximitySearchElement.dispatchEvent("click");
        wait(6000);
    }

    public void clearLabelField() {
        labelInput.click();
        labelInput.fill("");
        wait(6000);
    }

    public boolean isLabelLocalizationToastVisible() {
        waitForVisible(labelLocalizationToast);
        wait(6000);
        return labelLocalizationToast.isVisible();
    }

    public void clickGoBack() {
        goBackButton.click();
    }
}
