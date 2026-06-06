package tests;

import org.testng.annotations.Test;

import base.BaseTest;
import pages.CreateChecklist;

public class CreateChecklistTest extends BaseTest {

    @Test(groups = {"regression", "workbench-ui", "sanity", "smoke"})
  public void verifyCreateChecklist() {
        CreateChecklist createChecklistPage = nav.goToCreateChecklist();

        createChecklistPage.clickCreateChecklist();

        createChecklistPage.clickConfigureList();

        createChecklistPage.clickConfigureChecklist();

        createChecklistPage.clickConfirmChecklist();

        createChecklistPage.clickBackToHomepage();

        createChecklistPage.clickCreateCampaign();
    }
}
