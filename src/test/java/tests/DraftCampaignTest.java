package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.DraftCampaignPage;

public class DraftCampaignTest extends BaseTest {

    @Test(groups = {"regression", "workbench-ui", "sanity"})
    public void verifyDraftCampaignFlow() {
        DraftCampaignPage draftPage = nav.goToCampaignDraft();

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

//     @Test(groups = {"regression", "workbench-ui"})
//     public void verifyValidCampaignName() {
//         DraftCampaignPage draftPage = nav.goToCampaignNameStep();
//         draftPage.clearAndEnterDynamicCampaignName();
//         draftPage.clickNext();
//         Assert.assertTrue(page.url().contains("create-campaign"),
//                 "Should proceed to date step after entering a valid campaign name");
//     }

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifyCampaignNameTooLong() {
        DraftCampaignPage draftPage = nav.goToCampaignNameStep();
        draftPage.enterCampaignName("ThisCampaignNameIsWayTooLong123"); // 31 chars - above maximum of 30
        draftPage.clickNext();
        Assert.assertTrue(draftPage.isCampaignNameErrorVisible(),
                "Error should be shown for campaign name longer than 30 characters");
    }

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifyCampaignNameStartsWithSpecialChar() {
        DraftCampaignPage draftPage = nav.goToCampaignNameStep();
        draftPage.enterCampaignName("_Campaign");
        draftPage.clickNext();
        Assert.assertTrue(draftPage.isCampaignNameErrorVisible(),
                "Error should be shown for campaign name starting with an underscore");
    }

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifyCampaignNameContainsEmoji() {
        DraftCampaignPage draftPage = nav.goToCampaignNameStep();
        draftPage.enterCampaignName("Camp🎉ign1"); // 🎉 emoji
        draftPage.clickNext();
        Assert.assertTrue(draftPage.isCampaignNameErrorVisible(),
                "Error should be shown for campaign name containing an emoji");
    }

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifyCampaignNameConsecutiveUnderscores() {
        DraftCampaignPage draftPage = nav.goToCampaignNameStep();
        draftPage.enterCampaignName("Camp__aign");
        draftPage.clickNext();
        Assert.assertTrue(draftPage.isCampaignNameErrorVisible(),
                "Error should be shown for campaign name with consecutive underscores");
    }

    // ==================== Date Negative Test Cases ====================

        @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifySubmitWithoutFillingDates() {
         DraftCampaignPage draftPage = nav.goToCampaignDateStep();
        draftPage.clickSubmit();
        Assert.assertTrue(draftPage.isDateToastErrorVisible(),
                "Toast error should appear without any date filled when submitting the form");
    }

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifySubmitWithStartDateOnly() {
        DraftCampaignPage draftPage = nav.goToCampaignDateStep();
        draftPage.fillStartDate();
        draftPage.clickSubmit();
        Assert.assertTrue(draftPage.isDateToastErrorVisible(),
                "Toast error should appear when submitting with only start date filled");
    }

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifySubmitWithEndDateOnly() {
        DraftCampaignPage draftPage = nav.goToCampaignDateStep();
        draftPage.fillEndDate();
        draftPage.clickSubmit();
        Assert.assertTrue(draftPage.isDateToastErrorVisible(),
                "Toast error should appear when submitting with only end date filled");
    }
}
