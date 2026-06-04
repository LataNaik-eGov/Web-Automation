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
        return boundaryPage;
    }

    @Test(groups = {"regression", "workbench-ui", "sanity"})
    public void verifyBoundarySelection() {
        BoundarySelectionPage boundaryPage = setupBoundaryPage();

        boundaryPage.clickfirstlevel();
        boundaryPage.clicksecondlevel();
        boundaryPage.clickthirdlevel();
        boundaryPage.clickfourthlevel();
    }

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifyBoundarySelectionWithPartialSelection() {
        BoundarySelectionPage boundaryPage = setupBoundaryPage();

        boundaryPage.clickfirstlevel();
        boundaryPage.clicksecondlevel();

        boundaryPage.clickNextButton();

        Assert.assertTrue(boundaryPage.isMandatoryFieldsToastVisible(),
                "Toast error 'Please fill all the mandatory fields.' should appear when District and Administrative Post are not selected");
    }
}
