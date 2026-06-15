package tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.CreateChecklist;
import utils.TestDataReader;

public class CreateChecklistTest extends BaseTest {

    @DataProvider(name = "campaignTypes")
    public Object[][] campaignTypes() {
        return new Object[][]{{"BEDNET"}, {"MR-DN"}};
    }

    @Test(dataProvider = "campaignTypes", groups = { "workbench-ui", "sanity", "smoke"})
    public void verifyCreateChecklist(String campaignType) {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", campaignType);
        CreateChecklist createChecklistPage = nav.goToCreateChecklist();

        createChecklistPage.clickCreateChecklist();

        createChecklistPage.clickConfigureList();

        createChecklistPage.clickConfigureChecklist();

        createChecklistPage.clickConfirmChecklist();

        createChecklistPage.clickBackToHomepage();

        createChecklistPage.clickCreateCampaign();
    }
}
