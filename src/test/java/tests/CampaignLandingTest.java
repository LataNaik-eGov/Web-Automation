package tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.CampaignLandingPage;

public class CampaignLandingTest extends BaseTest {

    protected CampaignLandingPage landingPage;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "setup")
    public void navigateToLandingPage() {
        landingPage = new CampaignLandingPage(page);
    }

    @Test(groups = {"regression", "workbench-ui"})
    public void verifyCreateCampaignFromScratch() {
        landingPage.clickCreateCampaign();
        page.waitForLoadState();
        landingPage.clickScratchCard();
        page.waitForLoadState();
        landingPage.clickContinue();
        page.waitForLoadState();
    }
}
