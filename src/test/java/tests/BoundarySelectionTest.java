package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.BoundarySelectionPage;
import pages.ConfigureDeliveryRulesPage;
import utils.TestDataReader;

public class BoundarySelectionTest extends BaseTest {

    // ==================== Boundary Selection Flow ====================

    @Test(groups = { "workbench-ui", "sanity"})
    public void verifyBoundarySelection_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        verifyBoundarySelection();
    }

    @Test(groups = { "workbench-ui", "sanity"})
    public void verifyBoundarySelection_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        verifyBoundarySelection();
    }

    private void verifyBoundarySelection() {
        BoundarySelectionPage boundaryPage = nav.goToBoundarySelection();

        boundaryPage.clickFirstLevel();
        boundaryPage.clickSecondLevel();
        boundaryPage.clickThirdLevel();
        boundaryPage.clickFourthLevel();
        boundaryPage.clickNextButton();
        boundaryPage.clickSubmitButton();

        ConfigureDeliveryRulesPage deliveryPage = new ConfigureDeliveryRulesPage(page);
        Assert.assertTrue(deliveryPage.isConfigureDeliveryButtonVisible(),
                "Should navigate to Configure Delivery Rules page after completing boundary selection");
    }

    // ==================== Negative Tests ====================

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyBoundarySelectionWithPartialSelection_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        verifyBoundarySelectionWithPartialSelection();
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyBoundarySelectionWithPartialSelection_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        verifyBoundarySelectionWithPartialSelection();
    }

    private void verifyBoundarySelectionWithPartialSelection() {
        BoundarySelectionPage boundaryPage = nav.goToBoundarySelection();

        boundaryPage.clickFirstLevel();
        boundaryPage.clickSecondLevel();
        boundaryPage.clickNextButton();

        Assert.assertTrue(boundaryPage.isMandatoryFieldsToastVisible(),
                "Toast error 'Please fill all the mandatory fields.' should appear when District and Administrative Post are not selected");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyBoundarySelectionWithoutSelection_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        verifyBoundarySelectionWithoutSelection();
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyBoundarySelectionWithoutSelection_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        verifyBoundarySelectionWithoutSelection();
    }

    private void verifyBoundarySelectionWithoutSelection() {
        BoundarySelectionPage boundaryPage = nav.goToBoundarySelection();

        boundaryPage.clickNextButton();

        Assert.assertTrue(boundaryPage.isMandatoryFieldsToastVisible(),
                "Toast error 'Please fill all the mandatory fields.' should appear when no boundary is selected and Next button is clicked");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyBoundarySelectionWithMissingLowestLevel_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        verifyBoundarySelectionWithMissingLowestLevel();
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyBoundarySelectionWithMissingLowestLevel_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        verifyBoundarySelectionWithMissingLowestLevel();
    }

    private void verifyBoundarySelectionWithMissingLowestLevel() {
        BoundarySelectionPage boundaryPage = nav.goToBoundarySelection();

        boundaryPage.clickFirstLevel();
        boundaryPage.clickSecondLevelWrong();
        boundaryPage.clickThirdLevel();
        boundaryPage.clickFourthLevel();
        boundaryPage.clickNextButton();

        Assert.assertTrue(boundaryPage.isMandatoryFieldsToastVisible(),
                "Toast error 'Please fill all the mandatory fields.' should appear when lowest boundary level (4th) is not selected");
    }
}
