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
        page.waitForLoadState();
        landingPage.clickScratchCard();
        landingPage.clickContinue();
        page.waitForLoadState();
        page.waitForTimeout(4000);

        DraftCampaignPage draftPage = new DraftCampaignPage(page);
        draftPage.clickCampaignTypeDropdown();
        draftPage.selectCampaignType();
        draftPage.clickNext();
        page.waitForLoadState();
        page.waitForTimeout(3000);

        draftPage.clearAndEnterDynamicCampaignName();
        draftPage.clickNext();
        page.waitForLoadState();
        page.waitForTimeout(30000);

        draftPage.fillStartDate();
        page.waitForTimeout(1000);
        draftPage.fillEndDate();
        page.waitForTimeout(1000);
        draftPage.clickSubmit();
        page.waitForLoadState();
        page.waitForTimeout(4000);

        BoundarySelectionPage boundaryPage = new BoundarySelectionPage(page);
        boundaryPage.clickDefineTarget();
        page.waitForLoadState();
        page.waitForTimeout(3000);

        boundaryPage.clickfirstlevel();
        page.waitForTimeout(3000);
        boundaryPage.clicksecondlevel();
        page.waitForTimeout(3000);
        boundaryPage.clickthirdlevel();
        page.waitForTimeout(3000);
        boundaryPage.clickfourthlevel();
        page.waitForLoadState();
        page.waitForTimeout(3000);

        ConfigureDeliveryRulesPage deliveryRulesPage = new ConfigureDeliveryRulesPage(page);
        deliveryRulesPage.clickConfigureDelivery();
        page.waitForLoadState();
        page.waitForTimeout(3000);

        deliveryRulesPage.fillDates();
        page.waitForTimeout(1000);

        deliveryRulesPage.clickNext();
        page.waitForTimeout(3000);

        deliveryRulesPage.clickNext();
        page.waitForTimeout(3000);

        deliveryRulesPage.clickSubmit();
        page.waitForTimeout(3000);

        return new AppConfigurationPage(page);
    }

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifyProximitySearchWithEmptyLabel() {
        AppConfigurationPage appConfigPage = setupAppConfigPage();

        appConfigPage.clickSetUpMobileApp();
        page.waitForLoadState();
        page.waitForTimeout(2000);

        appConfigPage.clickRegistrationAndDeliveryConfigure();
        appConfigPage.clickSearchBeneficiaryFlow();
        appConfigPage.clickProximitySearchElement();
        appConfigPage.clearLabelField();
        page.waitForTimeout(500);

        appConfigPage.clickSaveConfiguration();
        page.waitForTimeout(2000);

        Assert.assertTrue(appConfigPage.isLabelLocalizationToastVisible(),
                "Toast 'Label localization is empty for field' should appear when Proximity Search label is cleared");
    }

    @Test(groups = {"regression", "workbench-ui", "sanity"})
    public void verifyAppConfiguration() {
        AppConfigurationPage appConfigPage = setupAppConfigPage();

        appConfigPage.clickSetUpMobileApp();
        page.waitForLoadState();
        page.waitForTimeout(2000);

        appConfigPage.configureRegistrationAndDelivery();
        page.waitForTimeout(2000);

        appConfigPage.configureCloseHousehold();
        page.waitForTimeout(2000);

        appConfigPage.configureComplaints();
        page.waitForTimeout(2000);

        appConfigPage.configureInventory();
        page.waitForTimeout(2000);

        appConfigPage.configureStockReconciliation();
        page.waitForTimeout(2000);

        appConfigPage.configureReports();
        page.waitForTimeout(2000);

        appConfigPage.configurePermissionHandler();
        page.waitForTimeout(2000);

        appConfigPage.clickGoBack();
        page.waitForTimeout(2000);
    }
}
