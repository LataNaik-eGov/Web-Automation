package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.AppConfigurationPage;
import pages.BoundarySelectionPage;
import pages.CampaignLandingPage;
import pages.ConfigureDeliveryRulesPage;
import pages.DraftCampaignPage;

public class AppConfigurationTest extends BaseTest {

    private AppConfigurationPage setupAppConfigPage() {
        CampaignLandingPage landingPage = new CampaignLandingPage(page);
        landingPage.clickCreateCampaign();
        landingPage.clickScratchCard();
        landingPage.clickContinue();

        DraftCampaignPage draftPage = new DraftCampaignPage(page);
        draftPage.clickCampaignTypeDropdown();
        draftPage.selectCampaignType();
        draftPage.clickNext();

        draftPage.clearAndEnterDynamicCampaignName();
        draftPage.clickNext();

        draftPage.fillStartDate();
        draftPage.fillEndDate();
        draftPage.clickSubmit();

        BoundarySelectionPage boundaryPage = new BoundarySelectionPage(page);
        boundaryPage.clickDefineTarget();

        boundaryPage.clickfirstlevel();
        boundaryPage.clicksecondlevel();
        boundaryPage.clickthirdlevel();
        boundaryPage.clickfourthlevel();

        ConfigureDeliveryRulesPage deliveryRulesPage = new ConfigureDeliveryRulesPage(page);
        deliveryRulesPage.clickConfigureDelivery();

        deliveryRulesPage.fillDates();

        deliveryRulesPage.clickNext();

        deliveryRulesPage.clickNext();

        deliveryRulesPage.clickSubmit();

        return new AppConfigurationPage(page);
    }

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifyProximitySearchWithEmptyLabel() {
        AppConfigurationPage appConfigPage = setupAppConfigPage();

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
        AppConfigurationPage appConfigPage = setupAppConfigPage();

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
