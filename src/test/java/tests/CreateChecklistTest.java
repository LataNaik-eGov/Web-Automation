package tests;

import org.testng.annotations.Test;

import base.BaseTest;
import pages.CreateChecklist;
import utils.TestDataReader;

public class CreateChecklistTest extends BaseTest {

    @Test(groups = { "workbench-ui", "sanity", "smoke"})
    public void verifyCreateChecklist_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        CreateChecklist createChecklistPage = nav.goToCreateChecklist();

        createChecklistPage.clickCreateChecklist();

        createChecklistPage.clickConfigureList();

        createChecklistPage.clickConfigureChecklist();

        createChecklistPage.clickConfirmChecklist();

        createChecklistPage.clickBackToHomepage();

        createChecklistPage.clickCreateCampaign();
    }

    @Test(groups = { "workbench-ui", "sanity", "smoke"})
    public void verifyCreateChecklist_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        CreateChecklist createChecklistPage = nav.goToCreateChecklist();

        createChecklistPage.clickCreateChecklist();

        createChecklistPage.clickConfigureList();

        createChecklistPage.clickConfigureChecklist();

        createChecklistPage.clickConfirmChecklist();

        createChecklistPage.clickBackToHomepage();

        createChecklistPage.clickCreateCampaign();
    }
}
