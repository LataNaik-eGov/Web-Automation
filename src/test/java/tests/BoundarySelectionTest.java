package tests;

import org.testng.Assert;
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
        page.waitForTimeout(3000);

        draftPage.clearAndEnterDynamicCampaignName();
        draftPage.clickNext();
        page.waitForLoadState();
        page.waitForTimeout(12000);

        draftPage.fillStartDate();
        page.waitForTimeout(1000);
        draftPage.fillEndDate();
        page.waitForTimeout(1000);
        draftPage.clickSubmit();
        page.waitForLoadState();
        page.waitForTimeout(4000);

        boundaryPage = new BoundarySelectionPage(page);
        boundaryPage.clickDefineTarget();
        page.waitForLoadState();
        page.waitForTimeout(3000);
    }

    @Override @Test(enabled = false) public void verifyDraftCampaignFlow() {}
    @Override @Test(enabled = false) public void verifyValidCampaignName() {}
    @Override @Test(enabled = false) public void verifyCampaignNameTooLong() {}
    @Override @Test(enabled = false) public void verifyCampaignNameStartsWithSpecialChar() {}
    @Override @Test(enabled = false) public void verifyCampaignNameContainsEmoji() {}
    @Override @Test(enabled = false) public void verifyCampaignNameConsecutiveUnderscores() {}
    @Override @Test(enabled = false) public void verifySubmitWithStartDateOnly() {}
    @Override @Test(enabled = false) public void verifySubmitWithEndDateOnly() {}

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

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifyBoundarySelectionWithPartialSelection() {
        // Select only first two boundary levels (Country and Province), leave District and Administrative Post empty
        boundaryPage.clickfirstlevel();
        page.waitForTimeout(2000);

        boundaryPage.clicksecondlevel();
        page.waitForTimeout(2000);

        // Click Next without selecting all mandatory boundary levels
        boundaryPage.clickNextButton();
        page.waitForTimeout(2000);

        // Verify toast error appears for unfilled mandatory fields
        Assert.assertTrue(boundaryPage.isMandatoryFieldsToastVisible(),
                "Toast error 'Please fill all the mandatory fields.' should appear when District and Administrative Post are not selected");
    }
}
