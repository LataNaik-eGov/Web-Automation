package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.AppConfigurationPage;

public class AppConfigurationTest extends BaseTest {

   

    @Test(groups = {"regression", "workbench-ui", "sanity"})
    public void verifyAppConfiguration() {
        AppConfigurationPage appConfigPage = nav.goToAppConfiguration();

        appConfigPage.clickSetUpMobileApp();

        appConfigPage.configureRegistrationAndDelivery();

        appConfigPage.configureCloseHousehold();

        appConfigPage.configureReferral();

        appConfigPage.configureComplaints();

        appConfigPage.configureInventory();

        appConfigPage.configureStockReconciliation();

        appConfigPage.configureReports();

        appConfigPage.configurePermissionHandler();

        appConfigPage.clickGoBack();
    }

    @Test(groups = {"regression", "workbench-ui", "sanity"})
    public void verifyAppConfigurationByLabelChange() {
        AppConfigurationPage appConfigPage = nav.goToAppConfiguration();

        appConfigPage.clickSetUpMobileApp();

        appConfigPage.clickRegistrationAndDeliveryConfigure();
        appConfigPage.clickSearchBeneficiaryFlow();
        appConfigPage.clickProximitySearchElement();
        appConfigPage.fillLabelField("Near by Beneficiary");

        appConfigPage.clickSaveConfiguration();
    }

    @Test(groups = {"regression", "workbench-ui" ,"sanity"})
    public void verifyAppConfigurationBySwitchingToggleOff() {
        AppConfigurationPage appConfigPage = nav.goToAppConfiguration();

        appConfigPage.clickSetUpMobileApp();

        appConfigPage.clickRegistrationAndDeliveryConfigure();
        appConfigPage.clickFirstToggleSwitchOff();
        appConfigPage.clickFirstToggleSwitchOff();
        appConfigPage.clickFirstToggleSwitchOff();
        appConfigPage.clickFirstToggleSwitchOff();

        appConfigPage.clickSaveConfiguration();
    }

// Negative test
     @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifyAppConfigurationWithEmptyLabel() {
        AppConfigurationPage appConfigPage = nav.goToAppConfiguration();

        appConfigPage.clickSetUpMobileApp();

        appConfigPage.clickRegistrationAndDeliveryConfigure();
        appConfigPage.clickSearchBeneficiaryFlow();
        appConfigPage.clickProximitySearchElement();
        appConfigPage.clearLabelField();

        appConfigPage.clickSaveConfiguration();

        Assert.assertTrue(appConfigPage.isLabelLocalizationToastVisible(),
                "Toast 'Label localization is empty for field' should appear when Proximity Search label is cleared");
    }
}
