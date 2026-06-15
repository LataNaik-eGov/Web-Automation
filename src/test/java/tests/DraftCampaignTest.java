package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.DraftCampaignPage;
import utils.TestDataReader;

public class DraftCampaignTest extends BaseTest {

    // ==================== Draft Campaign Flow ====================

    @Test(groups = { "workbench-ui", "sanity"})
    public void verifyDraftCampaignFlow_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        verifyDraftCampaignFlow();
    }

    @Test(groups = { "workbench-ui", "sanity"})
    public void verifyDraftCampaignFlow_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        verifyDraftCampaignFlow();
    }

    private void verifyDraftCampaignFlow() {
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

        draftPage.clickNext();
        Assert.assertTrue(page.url().contains("create-campaign"),
                "Should remain in the create campaign flow after submitting the full draft form");
    }

    // ==================== Campaign Name Negative Test Cases ====================

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyCampaignNameTooLong_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        verifyCampaignNameTooLong();
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyCampaignNameTooLong_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        verifyCampaignNameTooLong();
    }

    private void verifyCampaignNameTooLong() {
        DraftCampaignPage draftPage = nav.goToCampaignNameStep();
        draftPage.enterCampaignName("ThisCampaignNameIsWayTooLong123"); // 31 chars - above maximum of 30
        draftPage.clickNext();
        Assert.assertTrue(draftPage.isCampaignNameErrorVisible(),
                "Error should be shown for campaign name longer than 30 characters");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyCampaignNameStartsWithSpecialChar_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        verifyCampaignNameStartsWithSpecialChar();
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyCampaignNameStartsWithSpecialChar_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        verifyCampaignNameStartsWithSpecialChar();
    }

    private void verifyCampaignNameStartsWithSpecialChar() {
        DraftCampaignPage draftPage = nav.goToCampaignNameStep();
        draftPage.enterCampaignName("_Campaign");
        draftPage.clickNext();
        Assert.assertTrue(draftPage.isCampaignNameErrorVisible(),
                "Error should be shown for campaign name starting with an underscore");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyCampaignNameContainsEmoji_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        verifyCampaignNameContainsEmoji();
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyCampaignNameContainsEmoji_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        verifyCampaignNameContainsEmoji();
    }

    private void verifyCampaignNameContainsEmoji() {
        DraftCampaignPage draftPage = nav.goToCampaignNameStep();
        draftPage.enterCampaignName("Camp🎉ign1"); // 🎉 emoji
        draftPage.clickNext();
        Assert.assertTrue(draftPage.isCampaignNameErrorVisible(),
                "Error should be shown for campaign name containing an emoji");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyCampaignNameConsecutiveUnderscores_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        verifyCampaignNameConsecutiveUnderscores();
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyCampaignNameConsecutiveUnderscores_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        verifyCampaignNameConsecutiveUnderscores();
    }

    private void verifyCampaignNameConsecutiveUnderscores() {
        DraftCampaignPage draftPage = nav.goToCampaignNameStep();
        draftPage.enterCampaignName("Camp__aign");
        draftPage.clickNext();
        Assert.assertTrue(draftPage.isCampaignNameErrorVisible(),
                "Error should be shown for campaign name with consecutive underscores");
    }

    // ==================== Date Negative Test Cases ====================

    @Test(groups = {"negative", "workbench-ui"})
    public void verifySubmitWithoutFillingDates_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        verifySubmitWithoutFillingDates();
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifySubmitWithoutFillingDates_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        verifySubmitWithoutFillingDates();
    }

    private void verifySubmitWithoutFillingDates() {
        DraftCampaignPage draftPage = nav.goToCampaignDateStep();
        draftPage.clickSubmit();
        Assert.assertTrue(draftPage.isDateToastErrorVisible(),
                "Toast error should appear without any date filled when submitting the form");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifySubmitWithStartDateOnly_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        verifySubmitWithStartDateOnly();
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifySubmitWithStartDateOnly_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        verifySubmitWithStartDateOnly();
    }

    private void verifySubmitWithStartDateOnly() {
        DraftCampaignPage draftPage = nav.goToCampaignDateStep();
        draftPage.fillStartDate();
        draftPage.clickSubmit();
        Assert.assertTrue(draftPage.isDateToastErrorVisible(),
                "Toast error should appear when submitting with only start date filled");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifySubmitWithEndDateOnly_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        verifySubmitWithEndDateOnly();
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifySubmitWithEndDateOnly_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        verifySubmitWithEndDateOnly();
    }

    private void verifySubmitWithEndDateOnly() {
        DraftCampaignPage draftPage = nav.goToCampaignDateStep();
        draftPage.fillEndDate();
        draftPage.clickSubmit();
        Assert.assertTrue(draftPage.isDateToastErrorVisible(),
                "Toast error should appear when submitting with only end date filled");
    }
}
