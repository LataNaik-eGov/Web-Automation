package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.BoundarySelectionPage;
import pages.CampaignLandingPage;
import pages.ConfigureDeliveryRulesPage;
import pages.DraftCampaignPage;

public class ConfigureDeliveryRulesTest extends BaseTest {

    private ConfigureDeliveryRulesPage setupDeliveryRulesPage() {
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

        return new ConfigureDeliveryRulesPage(page);
    }

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifyNextWithFirstStartDateOnly() {
        ConfigureDeliveryRulesPage deliveryRulesPage = setupDeliveryRulesPage();

        deliveryRulesPage.clickConfigureDelivery();

        deliveryRulesPage.fillStartDate();

        deliveryRulesPage.clickNext();

        Assert.assertTrue(deliveryRulesPage.isCycleDateToastVisible(),
                "Toast 'Please fill the cycle dates to move ahead.' should appear when only the start date is filled");
    }

    @Test(groups = {"regression", "workbench-ui", "sanity"})
    public void verifyConfigureDeliveryRules() {
        ConfigureDeliveryRulesPage deliveryRulesPage = setupDeliveryRulesPage();

        deliveryRulesPage.clickConfigureDelivery();

        deliveryRulesPage.fillDates();

        deliveryRulesPage.clickNext();

        deliveryRulesPage.clickNext();

        deliveryRulesPage.clickSubmit();
    }
}
