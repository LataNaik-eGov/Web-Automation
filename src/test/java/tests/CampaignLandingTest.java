package tests;

import org.testng.annotations.Test;

import base.BaseTest;
import pages.CampaignLandingPage;

 public class CampaignLandingTest extends BaseTest {

    @Test(groups = {"regression", "workbench-ui", "sanity"})
    public void verifyCreateCampaignFromScratch() {
        CampaignLandingPage landingPage = new CampaignLandingPage(page);
        landingPage.clickCreateCampaign();
        landingPage.clickScratchCard();
        landingPage.clickContinue();
    }
}
