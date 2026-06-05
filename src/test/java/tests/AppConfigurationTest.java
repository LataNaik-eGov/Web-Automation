package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.AppConfigurationPage;

public class AppConfigurationTest extends BaseTest {

    @BeforeClass
    public void waitForConfigsToLoad() throws InterruptedException {
        Thread.sleep(6000);
    }

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifyProximitySearchWithEmptyLabel() {
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

    @Test(groups = {"regression", "workbench-ui", "sanity"})
    public void verifyAppConfiguration() {
        AppConfigurationPage appConfigPage = nav.goToAppConfiguration();

        appConfigPage.clickSetUpMobileApp();

        appConfigPage.configureRegistrationAndDelivery();

        appConfigPage.configureCloseHousehold();

        appConfigPage.configureComplaints();

        appConfigPage.configureInventory();

        appConfigPage.configureStockReconciliation();

        appConfigPage.configureReports();

        appConfigPage.configurePermissionHandler();

        appConfigPage.clickGoBack();
    }
}
