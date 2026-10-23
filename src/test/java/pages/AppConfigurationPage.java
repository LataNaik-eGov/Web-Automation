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
    private Locator fieldLabelInput;
    private Locator errorToast;
    private Locator helpTextSwitch;
    private Locator helpTextInput;
    private Locator minCharactersInput;
    private Locator maxCharactersInput;
    private String lastFlowPage;

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
        // Field properties panel, shown after a form-field card is selected on the right.
        this.fieldLabelInput = page.locator("#campaign-new-app-configuration-redesign-standalone-label-field");
        // Only the error variant of the toast: its wrapper carries the digit-error class.
        this.errorToast = page.locator(".digit-error .toast-label");
        this.helpTextSwitch = page.locator("#digit-sidepanel-switch-wrap-helpText [role='switch']");
        this.helpTextInput = page.locator(
                ".drawer-toggle-field-container:has(#digit-sidepanel-switch-wrap-helpText) textarea");
        this.minCharactersInput = page.locator("#campaign-new-app-configuration-redesign-standalone-minimum-characters-field");
        this.maxCharactersInput = page.locator("#campaign-new-app-configuration-redesign-standalone-maximum-characters-field");
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
                waitForModuleListToReturn();
                return;
            } catch (Exception e) {
                System.out.println("[AppConfig] Submit not ready — refreshing (attempt " + attempt + ")");
                page.reload();
                wait(6000);
            }
        }
        submit.click();
    }

    private void waitForModuleListToReturn() {
        try {
            page.locator("div[id^='setup-mobile-app-card-']").first()
                    .waitFor(new Locator.WaitForOptions().setTimeout(60000));
        } catch (Exception e) {
            // Let the next module's own wait report the failure, with its own name.
            System.out.println("[AppConfig] module list did not return after save");
        }
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

    // --- Field label editing (module config screen) ---

    public void openFlowPage(String flowPageName) {
        // The module screen first renders its default page and can re-render it after a late fetch,
        // overwriting a flow page clicked too early. Let it settle before and after the click.
        waitForNetworkIdle();
        Locator item = flowItem(flowPageName);
        waitForVisible(item);
        item.click();
        waitForNetworkIdle();
        wait(3000);
        lastFlowPage = flowPageName;
    }

    private void waitForNetworkIdle() {
        try {
            page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE,
                    new Page.WaitForLoadStateOptions().setTimeout(30000));
        } catch (Exception e) {
            System.out.println("[AppConfig] network did not go idle within 30s — continuing");
        }
    }

    public void selectFormField(String fieldLabel) {
        Locator fieldCard = page.locator(".app-configuration-side-panel .app-config-field-wrapper:has("
                + ".appConfigLabelField-label span:text-is('" + fieldLabel + "'))");
        // If the flow page click was overwritten by a late re-render, the card never shows; re-open the page.
        for (int attempt = 1; attempt <= 3 && lastFlowPage != null; attempt++) {
            try {
                fieldCard.waitFor(new Locator.WaitForOptions().setTimeout(15000));
                break;
            } catch (Exception e) {
                System.out.println("[AppConfig] '" + fieldLabel + "' not shown on " + lastFlowPage
                        + " — re-opening the page (attempt " + attempt + ")");
                openFlowPage(lastFlowPage);
            }
        }
        waitForVisible(fieldCard);
        // A field switched off in the template (e.g. Address line 1 for MR-DN) does not open
        // Field properties when clicked, so switch it on first.
        Locator offSwitch = fieldCard.locator("[role='switch'][aria-checked='false']");
        if (offSwitch.count() > 0) {
            offSwitch.click();
            wait(2000);
        }
        fieldCard.click();
        waitForVisible(fieldLabelInput);
    }

    public void setFieldLabel(String label) {
        fieldLabelInput.fill(label);
        wait(2000);
    }

    public void clearFieldLabel() {
        setFieldLabel("");
    }

    public boolean isLabelShownInPreview(String label) {
        try {
            page.locator(".zoom-content label.label-styles", new Page.LocatorOptions().setHasText(label))
                    .first().waitFor(new Locator.WaitForOptions().setTimeout(10000));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Clicks Save and returns the error toast text, or null if no toast appears within 10s. */
    public String clickSaveExpectingErrorToast() {
        return clickExpectingErrorToast(saveConfigurationButton.last());
    }

    /** Clicks a flow page while the screen has errors; returns the toast text, or null if none appears. */
    public String clickFlowPageExpectingErrorToast(String flowPageName) {
        return clickExpectingErrorToast(flowItem(flowPageName));
    }

    private String clickExpectingErrorToast(Locator target) {
        target.click();
        try {
            errorToast.first().waitFor(new Locator.WaitForOptions().setTimeout(10000));
            return errorToast.first().innerText().trim();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isFlowPageActive(String flowPageName) {
        String cls = flowItem(flowPageName).getAttribute("class");
        return cls != null && cls.contains("full-config-wrapper__flow-item--active");
    }

    private Locator flowItem(String flowPageName) {
        return page.locator(".full-config-wrapper__flow-item",
                new Page.LocatorOptions().setHasText(flowPageName)).first();
    }

    public void setHelpText(String helpText) {
        if ("false".equals(helpTextSwitch.getAttribute("aria-checked"))) {
            helpTextSwitch.click();
            wait(1000);
        }
        waitForVisible(helpTextInput);
        helpTextInput.fill(helpText);
        wait(2000);
    }

    public void clearHelpText() {
        setHelpText("");
    }

    public boolean isHelpTextShownInPreview(String helpText) {
        try {
            page.locator(".zoom-content .digit-description p", new Page.LocatorOptions().setHasText(helpText))
                    .first().waitFor(new Locator.WaitForOptions().setTimeout(10000));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void openFieldTab(String tabName) {
        page.locator("button.configure-app-tab-head", new Page.LocatorOptions().setHasText(tabName)).click();
        wait(2000);
    }

    public void setLengthRange(String min, String max) {
        waitForVisible(minCharactersInput);
        minCharactersInput.fill(min);
        maxCharactersInput.fill(max);
        wait(2000);
    }

    /** Clicks Save and reports whether the module list came back, i.e. the save went through. */
    public boolean clickSaveAndConfirmModuleList() {
        saveConfigurationButton.last().click();
        try {
            page.locator("div[id^='setup-mobile-app-card-']").first()
                    .waitFor(new Locator.WaitForOptions().setTimeout(60000));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isOnModuleConfigScreen() {
        return saveConfigurationButton.last().isVisible()
                && page.locator("div[id^='setup-mobile-app-card-']").count() == 0;
    }

    public void clickGoBack() {
        goBackButton.click();
    }
}
