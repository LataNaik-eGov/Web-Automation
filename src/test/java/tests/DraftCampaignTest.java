package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.CampaignLandingPage;
import pages.DraftCampaignPage;

public class DraftCampaignTest extends BaseTest {

    private DraftCampaignPage setupDraftPage() {
        CampaignLandingPage landingPage = new CampaignLandingPage(page);
        landingPage.clickCreateCampaign();
        landingPage.clickScratchCard();
        landingPage.clickContinue();
        return new DraftCampaignPage(page);
    }

    private DraftCampaignPage goToCampaignNameStep() {
        DraftCampaignPage draftPage = setupDraftPage();
        draftPage.clickCampaignTypeDropdown();
        draftPage.selectCampaignType();
        draftPage.clickNext();
        return draftPage;
    }

    private DraftCampaignPage goToDateStep() {
        DraftCampaignPage draftPage = goToCampaignNameStep();
        draftPage.clearAndEnterDynamicCampaignName();
        draftPage.clickNext();
        return draftPage;
    }

    @Test(groups = {"regression", "workbench-ui", "sanity"})
    public void verifyDraftCampaignFlow() {
        DraftCampaignPage draftPage = setupDraftPage();

        draftPage.clickCampaignTypeDropdown();
        Assert.assertTrue(draftPage.isCampaignTypeVisible(),
                draftPage.getCampaignDisplayName() + " option should be visible after clicking the campaign type dropdown");

        draftPage.selectCampaignType();
        draftPage.clickNext();
        Assert.assertTrue(page.url().contains("create-campaign"),
                "Should remain on create campaign flow after selecting campaign type and clicking Next");

        draftPage.clearAndEnterDynamicCampaignName();
        draftPage.clickNext();

        draftPage.fillStartDate();
        Assert.assertFalse(draftPage.getStartDateValue().isEmpty(),
                "Start date input should not be empty after filling");

        draftPage.fillEndDate();
        Assert.assertFalse(draftPage.getEndDateValue().isEmpty(),
                "End date input should not be empty after filling");

        draftPage.clickSubmit();
        Assert.assertTrue(page.url().contains("create-campaign"),
                "Should remain in the create campaign flow after submitting the full draft form");
    }

    // ==================== Campaign Name Negative Test Cases ====================

    @Test(groups = {"regression", "workbench-ui"})
    public void verifyValidCampaignName() {
        DraftCampaignPage draftPage = goToCampaignNameStep();
        draftPage.clearAndEnterDynamicCampaignName();
        draftPage.clickNext();
        Assert.assertTrue(page.url().contains("create-campaign"),
                "Should proceed to date step after entering a valid campaign name");
    }

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifyCampaignNameTooLong() {
        DraftCampaignPage draftPage = goToCampaignNameStep();
        draftPage.enterCampaignName("ThisCampaignNameIsWayTooLong123"); // 31 chars - above maximum of 30
        draftPage.clickNext();
        Assert.assertTrue(draftPage.isCampaignNameErrorVisible(),
                "Error should be shown for campaign name longer than 30 characters");
    }

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifyCampaignNameStartsWithSpecialChar() {
        DraftCampaignPage draftPage = goToCampaignNameStep();
        draftPage.enterCampaignName("_Campaign");
        draftPage.clickNext();
        Assert.assertTrue(draftPage.isCampaignNameErrorVisible(),
                "Error should be shown for campaign name starting with an underscore");
    }

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifyCampaignNameContainsEmoji() {
        DraftCampaignPage draftPage = goToCampaignNameStep();
        draftPage.enterCampaignName("Camp🎉ign1"); // 🎉 emoji
        draftPage.clickNext();
        Assert.assertTrue(draftPage.isCampaignNameErrorVisible(),
                "Error should be shown for campaign name containing an emoji");
    }

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifyCampaignNameConsecutiveUnderscores() {
        DraftCampaignPage draftPage = goToCampaignNameStep();
        draftPage.enterCampaignName("Camp__aign");
        draftPage.clickNext();
        Assert.assertTrue(draftPage.isCampaignNameErrorVisible(),
                "Error should be shown for campaign name with consecutive underscores");
    }

    // ==================== Date Negative Test Cases ====================

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifySubmitWithStartDateOnly() {
        DraftCampaignPage draftPage = goToDateStep();
        draftPage.fillStartDate();
        draftPage.clickSubmit();
        Assert.assertTrue(draftPage.isDateToastErrorVisible(),
                "Toast error should appear when submitting with only start date filled");
    }

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifySubmitWithEndDateOnly() {
        DraftCampaignPage draftPage = goToDateStep();
        draftPage.fillEndDate();
        draftPage.clickSubmit();
        Assert.assertTrue(draftPage.isDateToastErrorVisible(),
                "Toast error should appear when submitting with only end date filled");
    }
}
