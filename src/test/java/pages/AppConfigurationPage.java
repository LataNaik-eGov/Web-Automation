package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class AppConfigurationPage {

    private Page page;

    // App configuration elements
    private Locator setUpMobileAppButton;
    private Locator registrationAndDelivery;
    private Locator closeHousehold;
    private Locator complaints;
    private Locator inventory;
    private Locator stockReconciliation;
    private Locator reports;
    private Locator permissionHandler;
    private Locator configureExactButton;
    private Locator saveConfigurationButton;
    private Locator goBackButton;

    public AppConfigurationPage(Page page) {
        this.page = page;
        this.setUpMobileAppButton = page.locator("#campaign-details-page-button-setup-mobile-app");
        this.registrationAndDelivery = page.locator("div#setup-mobile-app-card-REGISTRATION").locator("button");
        this.closeHousehold = page.locator("div#setup-mobile-app-card-CLOSEHOUSEHOLD").locator("button");
        this.complaints = page.locator("div#setup-mobile-app-card-COMPLAINTS").locator("button");
        this.inventory = page.locator("div#setup-mobile-app-card-INVENTORY").locator("button");
        this.stockReconciliation = page.locator("div#setup-mobile-app-card-STOCKRECONCILIATION").locator("button");
        this.reports = page.locator("div#setup-mobile-app-card-STOCKREPORTS").locator("button");
        this.permissionHandler = page.locator("div#setup-mobile-app-card-PERMISSIONHANDLER").locator("button");
        this.configureExactButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Configure").setExact(true));
        this.saveConfigurationButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Save Configuration"));
        this.goBackButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Go Back"));
    }

    // --- Actions ---

    public void clickSetUpMobileApp() {
        setUpMobileAppButton.click();
        page.waitForTimeout(1000);
    }

    public void clickSaveConfiguration() {
        saveConfigurationButton.click();
        page.waitForTimeout(1000);
    }

    public void configureRegistrationAndDelivery() {
        registrationAndDelivery.click();
        page.waitForTimeout(1000);
        clickSaveConfiguration();
    }

    public void configureCloseHousehold() {
        closeHousehold.click();
        page.waitForTimeout(1000);
        clickSaveConfiguration();
    }

    public void configureComplaints() {
        complaints.click();
        page.waitForTimeout(1000);
        clickSaveConfiguration();
    }

    public void configureInventory() {
        inventory.click();
        page.waitForTimeout(1000);
        clickSaveConfiguration();
    }

    public void configureStockReconciliation() {
        stockReconciliation.click();
        page.waitForTimeout(1000);
        clickSaveConfiguration();
    }

    public void configureReports() {
        reports.click();
        page.waitForTimeout(1000);
        clickSaveConfiguration();
    }

    public void configurePermissionHandler() {
        permissionHandler.click();
        page.waitForTimeout(1000);
        clickSaveConfiguration();
    }

    public void clickConfigureExact() {
        configureExactButton.click();
        page.waitForTimeout(1000);
        clickSaveConfiguration();
    }

    public void clickGoBack() {
        goBackButton.click();
        page.waitForTimeout(1000);
    }
}
