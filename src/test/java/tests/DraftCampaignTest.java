package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pages.DraftCampaignPage;

public class DraftCampaignTest extends CampaignLandingTest {

    protected DraftCampaignPage draftPage;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "navigateToLandingPage")
    public void navigateToCreateCampaign() {
        landingPage.clickCreateCampaign();
        page.waitForLoadState();

        landingPage.clickScratchCard();
        landingPage.clickContinue();
        page.waitForLoadState();
        page.waitForTimeout(4000);

        draftPage = new DraftCampaignPage(page);
    }

    private void goToCampaignNameStep() {
        draftPage.clickCampaignTypeDropdown();
        draftPage.selectCampaignType();
        draftPage.clickNext();
        page.waitForLoadState();
        page.waitForTimeout(2000);
    }

    private void goToDateStep() {
        goToCampaignNameStep();
        draftPage.clearAndEnterDynamicCampaignName();
        draftPage.clickNext();
        page.waitForLoadState();
        page.waitForTimeout(6000);
    }

    @Override
    @Test(enabled = false)
    public void verifyCreateCampaignFromScratch() {}

    @Test(groups = {"regression", "workbench-ui", "sanity"}, priority = 4)
    public void verifyDraftCampaignFlow() {
        // Step 1: Click campaign type dropdown and verify option is visible
        draftPage.clickCampaignTypeDropdown();
        Assert.assertTrue(draftPage.isCampaignTypeVisible(),
                draftPage.getCampaignDisplayName() + " option should be visible after clicking the campaign type dropdown");

        // Step 2: Select campaign type and click Next
        draftPage.selectCampaignType();
        draftPage.clickNext();
        page.waitForLoadState();
        Assert.assertTrue(page.url().contains("create-campaign"),
                "Should remain on create campaign flow after selecting campaign type and clicking Next");

        // Step 3: Enter campaign name and click Next
        page.waitForTimeout(2000);
        draftPage.clearAndEnterDynamicCampaignName();
        draftPage.clickNext();
        page.waitForLoadState();
        page.waitForTimeout(4000);

        // Step 4: Fill start date and verify
        draftPage.fillStartDate();
        page.waitForTimeout(1000);
        Assert.assertFalse(draftPage.getStartDateValue().isEmpty(),
                "Start date input should not be empty after filling");

        // Step 5: Fill end date and verify
        draftPage.fillEndDate();
        page.waitForTimeout(1000);
        Assert.assertFalse(draftPage.getEndDateValue().isEmpty(),
                "End date input should not be empty after filling");

        // Step 6: Click Submit after dates and verify still in create campaign flow
        draftPage.clickSubmit();
        page.waitForLoadState();
        page.waitForTimeout(2000);
        Assert.assertTrue(page.url().contains("create-campaign"),
                "Should remain in the create campaign flow after submitting the full draft form");
    }

    // ==================== Campaign Name Negative Test Cases ====================

    @Test(groups = {"regression", "workbench-ui"}, priority = 2)
    public void verifyValidCampaignName() {
        goToCampaignNameStep();
        draftPage.clearAndEnterDynamicCampaignName();
        draftPage.clickNext();
        page.waitForLoadState();
        page.waitForTimeout(2000);
        Assert.assertTrue(page.url().contains("create-campaign"),
                "Should proceed to date step after entering a valid campaign name");
    }

//     @Test(groups = {"negative", "regression", "workbench-ui"}, priority = 1)
//     public void verifyCampaignNameTooShort() {
//         goToCampaignNameStep();
//         draftPage.enterCampaignName("Camp"); // 4 chars - below minimum of 5
//         draftPage.clickNext();
//         page.waitForTimeout(500);
//         Assert.assertTrue(draftPage.isCampaignNameErrorVisible(),
//                 "Error should be shown for campaign name shorter than 5 characters");
//     }

    @Test(groups = {"negative", "regression", "workbench-ui"}, priority = 1)
    public void verifyCampaignNameTooLong() {
        goToCampaignNameStep();
        draftPage.enterCampaignName("ThisCampaignNameIsWayTooLong123"); // 31 chars - above maximum of 30
        draftPage.clickNext();
        page.waitForTimeout(500);
        Assert.assertTrue(draftPage.isCampaignNameErrorVisible(),
                "Error should be shown for campaign name longer than 30 characters");
        page.waitForTimeout(2000);
    }

//     @Test(groups = {"negative", "regression", "workbench-ui"}, priority = 1)
//     public void verifyCampaignNameStartsWithNumber() {
//         goToCampaignNameStep();
//         draftPage.enterCampaignName("1Campaign");
//         draftPage.clickNext();
//         page.waitForTimeout(500);
//         Assert.assertTrue(draftPage.isCampaignNameErrorVisible(),
//                 "Error should be shown for campaign name starting with a number");
//     }

    @Test(groups = {"negative", "regression", "workbench-ui"}, priority = 1)
    public void verifyCampaignNameStartsWithSpecialChar() {
        goToCampaignNameStep();
        draftPage.enterCampaignName("_Campaign");
        draftPage.clickNext();
        page.waitForTimeout(500);
        Assert.assertTrue(draftPage.isCampaignNameErrorVisible(),
                "Error should be shown for campaign name starting with an underscore");
        page.waitForTimeout(2000);
    }

//     @Test(groups = {"negative", "regression", "workbench-ui"}, priority = 1)
//     public void verifyCampaignNameEndsWithHyphen() {
//         goToCampaignNameStep();
//         draftPage.enterCampaignName("Campaign-");
//         draftPage.clickNext();
//         page.waitForTimeout(500);
//         Assert.assertTrue(draftPage.isCampaignNameErrorVisible(),
//                 "Error should be shown for campaign name ending with a hyphen");
//     }

    // @Test(groups = {"negative", "regression", "workbench-ui"}, priority = 1)
    // public void verifyCampaignNameEndsWithUnderscore() {
    //     goToCampaignNameStep();
    //     draftPage.enterCampaignName("Campaign_");
    //     draftPage.clickNext();
    //     page.waitForTimeout(500);
    //     Assert.assertTrue(draftPage.isCampaignNameErrorVisible(),
    //             "Error should be shown for campaign name ending with an underscore");
    //     page.waitForTimeout(2000);
    // }

//     @Test(groups = {"negative", "regression", "workbench-ui"}, priority = 1)
//     public void verifyCampaignNameContainsDisallowedSymbol() {
//         goToCampaignNameStep();
//         draftPage.enterCampaignName("Camp@ign1");
//         draftPage.clickNext();
//         page.waitForTimeout(500);
//         Assert.assertTrue(draftPage.isCampaignNameErrorVisible(),
//                 "Error should be shown for campaign name containing '@' symbol");
//     }

    // @Test(groups = {"negative", "regression", "workbench-ui"}, priority = 1)
    // public void verifyCampaignNameContainsHashSymbol() {
    //     goToCampaignNameStep();
    //     draftPage.enterCampaignName("Camp#ign1");
    //     draftPage.clickNext();
    //     page.waitForTimeout(500);
    //     Assert.assertTrue(draftPage.isCampaignNameErrorVisible(),
    //             "Error should be shown for campaign name containing '#' symbol");
    //     page.waitForTimeout(2000);
    // }

    @Test(groups = {"negative", "regression", "workbench-ui"}, priority = 1)
    public void verifyCampaignNameContainsEmoji() {
        goToCampaignNameStep();
        draftPage.enterCampaignName("Camp🎉ign1"); // 🎉 emoji
        draftPage.clickNext();
        page.waitForTimeout(500);
        Assert.assertTrue(draftPage.isCampaignNameErrorVisible(),
                "Error should be shown for campaign name containing an emoji");
        page.waitForTimeout(2000);
    }

//     @Test(groups = {"negative", "regression", "workbench-ui"}, priority = 1)
//     public void verifyCampaignNameConsecutiveSpaces() {
//         goToCampaignNameStep();
//         draftPage.enterCampaignName("Camp  aign"); // two consecutive spaces
//         draftPage.clickNext();
//         page.waitForTimeout(500);
//         Assert.assertTrue(draftPage.isCampaignNameErrorVisible(),
//                 "Error should be shown for campaign name with consecutive spaces");
//     }

//     @Test(groups = {"negative", "regression", "workbench-ui"}, priority = 1)
//     public void verifyCampaignNameConsecutiveHyphens() {
//         goToCampaignNameStep();
//         draftPage.enterCampaignName("Camp--aign");
//         draftPage.clickNext();
//         page.waitForTimeout(500);
//         Assert.assertTrue(draftPage.isCampaignNameErrorVisible(),
//                 "Error should be shown for campaign name with consecutive hyphens");
//     }

    @Test(groups = {"negative", "regression", "workbench-ui"}, priority = 1)
    public void verifyCampaignNameConsecutiveUnderscores() {
        goToCampaignNameStep();
        draftPage.enterCampaignName("Camp__aign");
        draftPage.clickNext();
        page.waitForTimeout(500);
        Assert.assertTrue(draftPage.isCampaignNameErrorVisible(),
                "Error should be shown for campaign name with consecutive underscores");
        page.waitForTimeout(2000);
    }

    // ==================== Date Negative Test Cases ====================

    @Test(groups = {"negative", "regression", "workbench-ui"}, priority = 3)
    public void verifySubmitWithStartDateOnly() {
        goToDateStep();
        draftPage.fillStartDate();
        page.waitForTimeout(500);
        draftPage.clickSubmit();
        page.waitForTimeout(500);
        Assert.assertTrue(draftPage.isDateToastErrorVisible(),
                "Toast error should appear when submitting with only start date filled");
        page.waitForTimeout(2000);
    }

    @Test(groups = {"negative", "regression", "workbench-ui"}, priority = 3)
    public void verifySubmitWithEndDateOnly() {
        goToDateStep();
        draftPage.fillEndDate();
        page.waitForTimeout(500);
        draftPage.clickSubmit();
        page.waitForTimeout(500);
        Assert.assertTrue(draftPage.isDateToastErrorVisible(),
                "Toast error should appear when submitting with only end date filled");
        page.waitForTimeout(2000);
    }
}
