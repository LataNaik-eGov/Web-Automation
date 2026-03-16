package tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pages.AppConfigurationPage;

public class AppConfigurationTest extends ConfigureDeliveryRulesTest {

    protected AppConfigurationPage appConfigPage;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "navigateToConfigureDeliveryRules")
    public void navigateToAppConfiguration() {
        // Complete delivery rules to reach app configuration page
        deliveryRulesPage.clickConfigureDelivery();
        page.waitForLoadState();
        page.waitForTimeout(2000);

        deliveryRulesPage.fillStartDate();
        page.waitForTimeout(1000);

        deliveryRulesPage.fillEndDate();
        page.waitForTimeout(1000);

        deliveryRulesPage.clickNext();
        page.waitForTimeout(2000);

        deliveryRulesPage.clickNext();
        page.waitForTimeout(2000);

        deliveryRulesPage.clickSubmit();
        page.waitForTimeout(2000);

        appConfigPage = new AppConfigurationPage(page);
    }

    @Override
    @Test(enabled = false)
    public void verifyConfigureDeliveryRules() {}

    @Test(groups = {"regression"})
    public void verifyAppConfiguration() {
        // Step 1: Click Set Up Mobile App
        appConfigPage.clickSetUpMobileApp();
        page.waitForLoadState();
        page.waitForTimeout(2000);

        // Step 2: Configure Registration & Delivery
        appConfigPage.configureRegistrationAndDelivery();
        page.waitForTimeout(2000);

        // Step 3: Configure Close Household
        appConfigPage.configureCloseHousehold();
        page.waitForTimeout(2000);

        // Step 4: Configure Complaints
        appConfigPage.configureComplaints();
        page.waitForTimeout(2000);

        // Step 5: Configure Inventory
        appConfigPage.configureInventory();
        page.waitForTimeout(2000);

        // Step 6: Configure Stock Reconciliation
        appConfigPage.configureStockReconciliation();
        page.waitForTimeout(2000);

        // Step 7: Configure Reports
        appConfigPage.configureReports();
        page.waitForTimeout(2000);

        // Step 8: Configure Permission Handler
        appConfigPage.configurePermissionHandler();
        page.waitForTimeout(2000);

        // Step 9: Click Go Back
        appConfigPage.clickGoBack();
        page.waitForTimeout(2000);
    }
}
