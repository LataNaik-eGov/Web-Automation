package tests;

import org.testng.annotations.Test;

import base.BaseTest;

public class CampaignLandingTest extends BaseTest {

    @Test(groups = { "workbench-ui", "sanity"})
    public void verifyCreateCampaignFromScratch() {
        nav.goToCampaignDraft();
    }
}
