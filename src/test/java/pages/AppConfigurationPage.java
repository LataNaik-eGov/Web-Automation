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
    private Locator firstToggleSwitchOn;
    private Locator noFlowConfigError;

    public AppConfigurationPage(Page page) {
        super(page);
        this.campaignType = TestDataReader.getSessionValue("CAMPAIGN_TYPE");
        this.campaignDisplayName = CAMPAIGN_DISPLAY_NAMES.getOrDefault(campaignType, campaignType);
        this.deliveryTypeDropdown = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Select an option"));
        this.setUpMobileAppButton = page.locator(
                "#campaign-details-page-button-setup-mobile-app, "
                        + "#campaign-details-page-button-edit-mobile-app").first();
        this.registrationAndDeliveryModule = moduleConfigureButton("REGISTRATION");
        this.closeHouseholdModule = moduleConfigureButton("CLOSEHOUSEHOLD");
        this.referralModule = page.locator("button[id='setup-mobile-app-card-REFERRAL']")
                .or(page.getByRole(AriaRole.BUTTON,
                        new Page.GetByRoleOptions().setName("Record and manage referrals"))
                        .getByLabel("Configure"))
                .first();
        this.complaintsModule = moduleConfigureButton("COMPLAINTS");
        this.inventoryModule = moduleConfigureButton("INVENTORY");
        this.stockReconciliationModule = moduleConfigureButton("STOCKRECONCILIATION");
        this.reportsModule = moduleConfigureButton("STOCKREPORTS");
        this.permissionHandlerModule = moduleConfigureButton("PERMISSIONHANDLER");
        this.saveConfigurationButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Save Configuration"));
        this.goBackButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Go Back"));
        this.firstToggleSwitchOn = page.getByRole(AriaRole.SWITCH,
                new Page.GetByRoleOptions().setName("Toggle switch on")).first();
        this.noFlowConfigError = page.getByText("No flow configuration found");
    }


    private Locator moduleConfigureButton(String moduleId) {
        return page.locator("button[id='setup-mobile-app-card-" + moduleId + "']");
    }

    // --- Actions ---

    public void clickSetUpMobileApp() {
        waitForVisible(setUpMobileAppButton);
      wait(6000);
        setUpMobileAppButton.click();
    }

    public void clickSaveConfiguration() {
        Locator submit = saveConfigurationButton.last();
        // The module config screen intermittently renders "No flow configuration found";
        // refreshing the page reloads the config screen correctly. Retry a few times.
        for (int attempt = 1; attempt <= 3; attempt++) {
            if (noFlowConfigError.isVisible()) {
                System.out.println("[AppConfig] 'No flow configuration found' shown — refreshing (attempt " + attempt + ")");
                page.reload();
                wait(6000);
            }
            try {
                submit.waitFor(new Locator.WaitForOptions().setTimeout(15000));
                submit.click();
                return;
            } catch (Exception e) {
                System.out.println("[AppConfig] Submit not ready — refreshing (attempt " + attempt + ")");
                page.reload();
                wait(6000);
            }
        }
        submit.click();
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

    public void clickFirstToggleSwitchOff() {
        firstToggleSwitchOn.click();
        wait(6000);
    }

    public void clickGoBack() {
        goBackButton.click();
    }
}
