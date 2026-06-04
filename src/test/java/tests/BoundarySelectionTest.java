package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.BoundarySelectionPage;
import pages.ConfigureDeliveryRulesPage;

public class BoundarySelectionTest extends BaseTest {

    @Test(groups = {"regression", "workbench-ui", "sanity"})
    public void verifyBoundarySelection() {
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

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifyBoundarySelectionWithPartialSelection() {
        BoundarySelectionPage boundaryPage = nav.goToBoundarySelection();

        boundaryPage.clickFirstLevel();
        boundaryPage.clickSecondLevel();

        boundaryPage.clickNextButton();

        Assert.assertTrue(boundaryPage.isMandatoryFieldsToastVisible(),
                "Toast error 'Please fill all the mandatory fields.' should appear when District and Administrative Post are not selected");
    }
}
