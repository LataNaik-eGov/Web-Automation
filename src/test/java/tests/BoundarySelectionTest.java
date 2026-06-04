package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.BoundarySelectionPage;
import pages.CampaignLandingPage;
import pages.DraftCampaignPage;

public class BoundarySelectionTest extends BaseTest {

    private BoundarySelectionPage setupBoundaryPage() {
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
        return boundaryPage;
    }

    @Test(groups = {"regression", "workbench-ui", "sanity"})
    public void verifyBoundarySelection() {
        BoundarySelectionPage boundaryPage = setupBoundaryPage();

        boundaryPage.clickfirstlevel();
        page.waitForTimeout(2000);
        boundaryPage.clicksecondlevel();
        page.waitForTimeout(2000);
        boundaryPage.clickthirdlevel();
        page.waitForTimeout(2000);
        boundaryPage.clickfourthlevel();
        page.waitForTimeout(2000);
    }

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifyBoundarySelectionWithPartialSelection() {
        BoundarySelectionPage boundaryPage = setupBoundaryPage();

        boundaryPage.clickfirstlevel();
        page.waitForTimeout(2000);
        boundaryPage.clicksecondlevel();
        page.waitForTimeout(2000);

        boundaryPage.clickNextButton();
        page.waitForTimeout(2000);

        Assert.assertTrue(boundaryPage.isMandatoryFieldsToastVisible(),
                "Toast error 'Please fill all the mandatory fields.' should appear when District and Administrative Post are not selected");
    }
}
