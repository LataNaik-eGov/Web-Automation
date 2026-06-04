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
        page.waitForLoadState();
        landingPage.clickScratchCard();
        landingPage.clickContinue();
        page.waitForLoadState();
        page.waitForTimeout(4000);
        return new DraftCampaignPage(page);
    }

    private DraftCampaignPage goToCampaignNameStep() {
        DraftCampaignPage draftPage = setupDraftPage();
        draftPage.clickCampaignTypeDropdown();
        draftPage.selectCampaignType();
        draftPage.clickNext();
        page.waitForLoadState();
        page.waitForTimeout(2000);
        return draftPage;
    }

    private DraftCampaignPage goToDateStep() {
        DraftCampaignPage draftPage = goToCampaignNameStep();
        draftPage.clearAndEnterDynamicCampaignName();
        draftPage.clickNext();
        page.waitForLoadState();
        page.waitForTimeout(6000);
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
        page.waitForLoadState();
        Assert.assertTrue(page.url().contains("create-campaign"),
                "Should remain on create campaign flow after selecting campaign type and clicking Next");

        page.waitForTimeout(2000);
        draftPage.clearAndEnterDynamicCampaignName();
        draftPage.clickNext();
        page.waitForLoadState();
        page.waitForTimeout(4000);

        draftPage.fillStartDate();
        page.waitForTimeout(1000);
        Assert.assertFalse(draftPage.getStartDateValue().isEmpty(),
                "Start date input should not be empty after filling");

        draftPage.fillEndDate();
        page.waitForTimeout(1000);
        Assert.assertFalse(draftPage.getEndDateValue().isEmpty(),
                "End date input should not be empty after filling");

        draftPage.clickSubmit();
        page.waitForLoadState();
        page.waitForTimeout(2000);
        Assert.assertTrue(page.url().contains("create-campaign"),
                "Should remain in the create campaign flow after submitting the full draft form");
    }

    // ==================== Campaign Name Negative Test Cases ====================

    @Test(groups = {"regression", "workbench-ui"})
    public void verifyValidCampaignName() {
        DraftCampaignPage draftPage = goToCampaignNameStep();
        draftPage.clearAndEnterDynamicCampaignName();
        draftPage.clickNext();
        page.waitForLoadState();
        page.waitForTimeout(2000);
        Assert.assertTrue(page.url().contains("create-campaign"),
                "Should proceed to date step after entering a valid campaign name");
    }

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifyCampaignNameTooLong() {
        DraftCampaignPage draftPage = goToCampaignNameStep();
        draftPage.enterCampaignName("ThisCampaignNameIsWayTooLong123"); // 31 chars - above maximum of 30
        draftPage.clickNext();
        page.waitForTimeout(500);
        Assert.assertTrue(draftPage.isCampaignNameErrorVisible(),
                "Error should be shown for campaign name longer than 30 characters");
        page.waitForTimeout(2000);
    }

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifyCampaignNameStartsWithSpecialChar() {
        DraftCampaignPage draftPage = goToCampaignNameStep();
        draftPage.enterCampaignName("_Campaign");
        draftPage.clickNext();
        page.waitForTimeout(500);
        Assert.assertTrue(draftPage.isCampaignNameErrorVisible(),
                "Error should be shown for campaign name starting with an underscore");
        page.waitForTimeout(2000);
    }

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifyCampaignNameContainsEmoji() {
        DraftCampaignPage draftPage = goToCampaignNameStep();
        draftPage.enterCampaignName("Camp🎉ign1"); // 🎉 emoji
        draftPage.clickNext();
        page.waitForTimeout(500);
        Assert.assertTrue(draftPage.isCampaignNameErrorVisible(),
                "Error should be shown for campaign name containing an emoji");
        page.waitForTimeout(2000);
    }

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifyCampaignNameConsecutiveUnderscores() {
        DraftCampaignPage draftPage = goToCampaignNameStep();
        draftPage.enterCampaignName("Camp__aign");
        draftPage.clickNext();
        page.waitForTimeout(500);
        Assert.assertTrue(draftPage.isCampaignNameErrorVisible(),
                "Error should be shown for campaign name with consecutive underscores");
        page.waitForTimeout(2000);
    }

    // ==================== Date Negative Test Cases ====================

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifySubmitWithStartDateOnly() {
        DraftCampaignPage draftPage = goToDateStep();
        draftPage.fillStartDate();
        page.waitForTimeout(500);
        draftPage.clickSubmit();
        page.waitForTimeout(500);
        Assert.assertTrue(draftPage.isDateToastErrorVisible(),
                "Toast error should appear when submitting with only start date filled");
        page.waitForTimeout(2000);
    }

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifySubmitWithEndDateOnly() {
        DraftCampaignPage draftPage = goToDateStep();
        draftPage.fillEndDate();
        page.waitForTimeout(500);
        draftPage.clickSubmit();
        page.waitForTimeout(500);
        Assert.assertTrue(draftPage.isDateToastErrorVisible(),
                "Toast error should appear when submitting with only end date filled");
        page.waitForTimeout(2000);
    }
}
