package tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pages.BoundarySelectionPage;

public class BoundarySelectionTest extends DraftCampaignTest {

    protected BoundarySelectionPage boundaryPage;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "navigateToCreateCampaign")
    public void navigateToBoundarySelection() {
        draftPage.clickCampaignTypeDropdown();
        draftPage.selectCampaignType();
        draftPage.clickNext();
        page.waitForLoadState();
        page.waitForTimeout(2000);

        draftPage.clearAndEnterDynamicCampaignName();
        draftPage.clickNext();
        page.waitForLoadState();
        page.waitForTimeout(2000);

        draftPage.fillStartDate();
        page.waitForTimeout(1000);
        draftPage.fillEndDate();
        page.waitForTimeout(1000);
        draftPage.clickNext();
        page.waitForLoadState();
        page.waitForTimeout(2000);

        boundaryPage = new BoundarySelectionPage(page);
        boundaryPage.clickDefineTarget();
        page.waitForLoadState();
        page.waitForTimeout(2000);
    }

    @Override
    @Test(enabled = false)
    public void verifyDraftCampaignFlow() {}

    @Test(groups = {"regression", "workbench-ui"})
    public void verifyBoundarySelection() {
        // Step 1: Select first boundary level
        boundaryPage.clickfirstlevel();
        page.waitForTimeout(2000);

        // Step 2: Select second boundary level
        boundaryPage.clicksecondlevel();
        page.waitForTimeout(2000);

        // Step 3: Select third boundary level
        boundaryPage.clickthirdlevel();
        page.waitForTimeout(2000);

        // Step 4: Select fourth boundary level
        boundaryPage.clickfourthlevel();
        page.waitForTimeout(2000);

    }
}
