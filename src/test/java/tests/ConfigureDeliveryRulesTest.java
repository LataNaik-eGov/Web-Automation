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

        return new ConfigureDeliveryRulesPage(page);
    }

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifyNextWithFirstStartDateOnly() {
        ConfigureDeliveryRulesPage deliveryRulesPage = setupDeliveryRulesPage();

        deliveryRulesPage.clickConfigureDelivery();
        page.waitForLoadState();
        page.waitForTimeout(2000);

        deliveryRulesPage.fillStartDate();
        page.waitForTimeout(1000);

        deliveryRulesPage.clickNext();
        page.waitForTimeout(2000);

        Assert.assertTrue(deliveryRulesPage.isCycleDateToastVisible(),
                "Toast 'Please fill the cycle dates to move ahead.' should appear when only the start date is filled");
    }

    @Test(groups = {"regression", "workbench-ui", "sanity"})
    public void verifyConfigureDeliveryRules() {
        ConfigureDeliveryRulesPage deliveryRulesPage = setupDeliveryRulesPage();

        deliveryRulesPage.clickConfigureDelivery();
        page.waitForLoadState();
        page.waitForTimeout(2000);

        deliveryRulesPage.fillDates();
        page.waitForTimeout(1000);

        deliveryRulesPage.clickNext();
        page.waitForTimeout(2000);

        deliveryRulesPage.clickNext();
        page.waitForTimeout(2000);

        deliveryRulesPage.clickSubmit();
        page.waitForTimeout(2000);
    }
}
