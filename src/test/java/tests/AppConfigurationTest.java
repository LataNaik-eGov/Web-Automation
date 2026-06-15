package tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.AppConfigurationPage;
import utils.TestDataReader;

public class AppConfigurationTest extends BaseTest {

    @DataProvider(name = "campaignTypes")
    public Object[][] campaignTypes() {
        return new Object[][]{{"BEDNET"}, {"MR-DN"}};
    }

    @Test(dataProvider = "campaignTypes", groups = { "workbench-ui", "sanity"})
    public void verifyAppConfiguration(String campaignType) {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", campaignType);
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

    @Test(dataProvider = "campaignTypes", groups = { "workbench-ui", "sanity"})
    public void verifyAppConfigurationByLabelChange(String campaignType) {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", campaignType);
        AppConfigurationPage appConfigPage = nav.goToAppConfiguration();

        appConfigPage.clickSetUpMobileApp();

        appConfigPage.clickRegistrationAndDeliveryConfigure();
        appConfigPage.clickSearchBeneficiaryFlow();
        appConfigPage.clickProximitySearchElement();
        appConfigPage.fillLabelField("Near by Beneficiary");

        appConfigPage.clickSaveConfiguration();
    }

    @Test(dataProvider = "campaignTypes", groups = { "workbench-ui", "sanity"})
    public void verifyAppConfigurationBySwitchingToggleOff(String campaignType) {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", campaignType);
        AppConfigurationPage appConfigPage = nav.goToAppConfiguration();

        appConfigPage.clickSetUpMobileApp();

        appConfigPage.clickRegistrationAndDeliveryConfigure();
        appConfigPage.clickFirstToggleSwitchOff();
        appConfigPage.clickFirstToggleSwitchOff();
        appConfigPage.clickFirstToggleSwitchOff();
        appConfigPage.clickFirstToggleSwitchOff();

        appConfigPage.clickSaveConfiguration();
    }

    // Negative tests
    @Test(dataProvider = "campaignTypes", groups = {"negative", "workbench-ui"})
    public void verifyAppConfigurationWithEmptyLabel(String campaignType) {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", campaignType);
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
