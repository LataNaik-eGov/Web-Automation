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
        page.waitForURL("**/campaign/campaign-home", new com.microsoft.playwright.Page.WaitForURLOptions().setTimeout(30000));
        page.waitForLoadState();

        landingPage.clickScratchCard();
        landingPage.clickContinue();
        page.waitForLoadState();
        page.waitForTimeout(3000);

        draftPage = new DraftCampaignPage(page);
    }

    @Override
    @Test(enabled = false)
    public void verifyCreateCampaignFromScratch() {}

    @Test(groups = {"regression"})
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
        page.waitForTimeout(2000);

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

        // Step 6: Click Next after dates and verify still in create campaign flow
        draftPage.clickNext();
        page.waitForLoadState();
        page.waitForTimeout(2000);
        Assert.assertTrue(page.url().contains("create-campaign"),
                "Should remain in the create campaign flow after submitting the full draft form");
    }
}
