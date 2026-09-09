package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.AppConfigurationPage;
import utils.TestDataReader;

public class AppConfigurationTest extends BaseTest {

    @Test(groups = { "workbench-ui", "sanity"})
    public void verifyAppConfiguration_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
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

    @Test(groups = { "workbench-ui", "sanity"})
    public void verifyAppConfiguration_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
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



    @Test(groups = { "workbench-ui", "sanity"})
    public void verifyAppConfigurationBySwitchingToggleOff_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        AppConfigurationPage appConfigPage = nav.goToAppConfiguration();

        appConfigPage.clickSetUpMobileApp();

        appConfigPage.clickRegistrationAndDeliveryConfigure();
        appConfigPage.clickFirstToggleSwitchOff();
        appConfigPage.clickFirstToggleSwitchOff();
        appConfigPage.clickFirstToggleSwitchOff();
        appConfigPage.clickFirstToggleSwitchOff();

        appConfigPage.clickSaveConfiguration();
    }

    @Test(groups = { "workbench-ui", "sanity"})
    public void verifyAppConfigurationBySwitchingToggleOff_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
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

}
