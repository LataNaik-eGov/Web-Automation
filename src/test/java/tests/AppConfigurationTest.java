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

    // Field label / help text editing on Registration > Household Registration > Address line 1.
    // Address line 1 is optional and switched off by default for MR-DN; selectFormField switches it on.
    private static final String FLOW_PAGE = "Household Registration";
    private static final String FIELD = "Address line 1";

    private AppConfigurationPage openRegistrationField(String campaignType) {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", campaignType);
        AppConfigurationPage appConfigPage = nav.goToAppConfiguration();
        appConfigPage.clickSetUpMobileApp();
        appConfigPage.clickRegistrationAndDeliveryConfigure();
        appConfigPage.openFlowPage(FLOW_PAGE);
        appConfigPage.selectFormField(FIELD);
        return appConfigPage;
    }

    private static void assertToast(String toast, String expectedRegex) {
        Assert.assertNotNull(toast, "Expected an error toast matching '" + expectedRegex + "' but none appeared");
        Assert.assertTrue(toast.matches(expectedRegex), "Unexpected toast text: " + toast);
    }

    @Test(groups = { "workbench-ui", "sanity"})
    public void verifyAppConfigurationLabelChange_BEDNET() {
        AppConfigurationPage appConfigPage = openRegistrationField("BEDNET");

        String newLabel = "Address line 1 Edited";
        appConfigPage.setFieldLabel(newLabel);
        Assert.assertTrue(appConfigPage.isLabelShownInPreview(newLabel),
                "Edited label '" + newLabel + "' should appear in the mobile preview");

        Assert.assertTrue(appConfigPage.clickSaveAndConfirmModuleList(),
                "Saving a changed label should succeed and return to the module list");
    }

    @Test(groups = { "workbench-ui", "sanity"})
    public void verifyAppConfigurationLabelChange_MR_DN() {
        AppConfigurationPage appConfigPage = openRegistrationField("MR-DN");

        String newLabel = "Address line 1 Edited";
        appConfigPage.setFieldLabel(newLabel);
        Assert.assertTrue(appConfigPage.isLabelShownInPreview(newLabel),
                "Edited label '" + newLabel + "' should appear in the mobile preview");

        Assert.assertTrue(appConfigPage.clickSaveAndConfirmModuleList(),
                "Saving a changed label should succeed and return to the module list");
    }

    @Test(groups = { "workbench-ui", "sanity"})
    public void verifyAppConfigurationHelpTextChange_BEDNET() {
        AppConfigurationPage appConfigPage = openRegistrationField("BEDNET");

        String newHelpText = "Help text edited by automation";
        appConfigPage.setHelpText(newHelpText);
        Assert.assertTrue(appConfigPage.isHelpTextShownInPreview(newHelpText),
                "Edited help text should appear in the mobile preview");

        Assert.assertTrue(appConfigPage.clickSaveAndConfirmModuleList(),
                "Saving a changed help text should succeed and return to the module list");
    }

    @Test(groups = { "workbench-ui", "sanity"})
    public void verifyAppConfigurationHelpTextChange_MR_DN() {
        AppConfigurationPage appConfigPage = openRegistrationField("MR-DN");

        String newHelpText = "Help text edited by automation";
        appConfigPage.setHelpText(newHelpText);
        Assert.assertTrue(appConfigPage.isHelpTextShownInPreview(newHelpText),
                "Edited help text should appear in the mobile preview");

        Assert.assertTrue(appConfigPage.clickSaveAndConfirmModuleList(),
                "Saving a changed help text should succeed and return to the module list");
    }

    // Negative tests

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyAppConfigurationWithEmptyLabel_BEDNET() {
        AppConfigurationPage appConfigPage = openRegistrationField("BEDNET");
        appConfigPage.clearFieldLabel();

        assertToast(appConfigPage.clickSaveExpectingErrorToast(), "(?i).*label.*empty.*addressLine1.*");
        Assert.assertTrue(appConfigPage.isOnModuleConfigScreen(),
                "Save should be blocked and stay on the module config screen");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyAppConfigurationWithEmptyLabel_MR_DN() {
        AppConfigurationPage appConfigPage = openRegistrationField("MR-DN");
        appConfigPage.clearFieldLabel();

        assertToast(appConfigPage.clickSaveExpectingErrorToast(), "(?i).*label.*empty.*addressLine1.*");
        Assert.assertTrue(appConfigPage.isOnModuleConfigScreen(),
                "Save should be blocked and stay on the module config screen");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyAppConfigurationEmptyLabelBlocksPageSwitch_BEDNET() {
        AppConfigurationPage appConfigPage = openRegistrationField("BEDNET");
        appConfigPage.clearFieldLabel();

        assertToast(appConfigPage.clickFlowPageExpectingErrorToast("Household Overview"),
                "(?i).*label.*empty.*addressLine1.*");
        Assert.assertTrue(appConfigPage.isFlowPageActive(FLOW_PAGE),
                "Switching flow page should be blocked while a label is empty");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyAppConfigurationEmptyLabelBlocksPageSwitch_MR_DN() {
        AppConfigurationPage appConfigPage = openRegistrationField("MR-DN");
        appConfigPage.clearFieldLabel();

        assertToast(appConfigPage.clickFlowPageExpectingErrorToast("Household Overview"),
                "(?i).*label.*empty.*addressLine1.*");
        Assert.assertTrue(appConfigPage.isFlowPageActive(FLOW_PAGE),
                "Switching flow page should be blocked while a label is empty");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyAppConfigurationSaveAfterRestoringEmptyLabel_BEDNET() {
        AppConfigurationPage appConfigPage = openRegistrationField("BEDNET");
        appConfigPage.clearFieldLabel();
        assertToast(appConfigPage.clickSaveExpectingErrorToast(), "(?i).*label.*empty.*addressLine1.*");

        appConfigPage.setFieldLabel(FIELD);
        Assert.assertTrue(appConfigPage.clickSaveAndConfirmModuleList(),
                "Save should go through once the empty label is filled again");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyAppConfigurationSaveAfterRestoringEmptyLabel_MR_DN() {
        AppConfigurationPage appConfigPage = openRegistrationField("MR-DN");
        appConfigPage.clearFieldLabel();
        assertToast(appConfigPage.clickSaveExpectingErrorToast(), "(?i).*label.*empty.*addressLine1.*");

        appConfigPage.setFieldLabel(FIELD);
        Assert.assertTrue(appConfigPage.clickSaveAndConfirmModuleList(),
                "Save should go through once the empty label is filled again");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyAppConfigurationWithEmptyHelpText_BEDNET() {
        AppConfigurationPage appConfigPage = openRegistrationField("BEDNET");
        appConfigPage.clearHelpText();

        assertToast(appConfigPage.clickSaveExpectingErrorToast(), "(?i).*help text.*empty.*");
        Assert.assertTrue(appConfigPage.isOnModuleConfigScreen(),
                "Save should be blocked while help text is switched on but empty");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyAppConfigurationWithEmptyHelpText_MR_DN() {
        AppConfigurationPage appConfigPage = openRegistrationField("MR-DN");
        appConfigPage.clearHelpText();

        assertToast(appConfigPage.clickSaveExpectingErrorToast(), "(?i).*help text.*empty.*");
        Assert.assertTrue(appConfigPage.isOnModuleConfigScreen(),
                "Save should be blocked while help text is switched on but empty");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyAppConfigurationWithMinLengthAboveMax_BEDNET() {
        AppConfigurationPage appConfigPage = openRegistrationField("BEDNET");
        appConfigPage.openFieldTab("Logic");
        appConfigPage.setLengthRange("5", "2");

        assertToast(appConfigPage.clickSaveExpectingErrorToast(), "(?i).*min.*length.*less than.*max.*length.*");
        Assert.assertTrue(appConfigPage.isOnModuleConfigScreen(),
                "Save should be blocked when minimum length is above maximum length");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyAppConfigurationWithMinLengthAboveMax_MR_DN() {
        AppConfigurationPage appConfigPage = openRegistrationField("MR-DN");
        appConfigPage.openFieldTab("Logic");
        appConfigPage.setLengthRange("5", "2");

        assertToast(appConfigPage.clickSaveExpectingErrorToast(), "(?i).*min.*length.*less than.*max.*length.*");
        Assert.assertTrue(appConfigPage.isOnModuleConfigScreen(),
                "Save should be blocked when minimum length is above maximum length");
    }
}
