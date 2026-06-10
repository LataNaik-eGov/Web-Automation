package tests;

import org.testng.annotations.Test;

import base.BaseTest;
import utils.TestDataReader;

public class CampaignLandingTest extends BaseTest {

    @Test(groups = { "workbench-ui", "sanity"})
    public void verifyCreateCampaignFromScratch_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        nav.goToCampaignDraft();
    }

    @Test(groups = { "workbench-ui", "sanity"})
    public void verifyCreateCampaignFromScratch_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        nav.goToCampaignDraft();
    }
}
