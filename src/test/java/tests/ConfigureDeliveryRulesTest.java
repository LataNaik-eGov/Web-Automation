package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pages.ConfigureDeliveryRulesPage;

public class ConfigureDeliveryRulesTest extends BoundarySelectionTest {

    protected ConfigureDeliveryRulesPage deliveryRulesPage;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "navigateToBoundarySelection")
    public void navigateToConfigureDeliveryRules() {
        // Complete boundary selection to reach delivery rules page
        boundaryPage.clickfirstlevel();
        page.waitForTimeout(3000);

        boundaryPage.clicksecondlevel();
        page.waitForTimeout(3000);

        boundaryPage.clickthirdlevel();
        page.waitForTimeout(3000);

        boundaryPage.clickfourthlevel();
        page.waitForLoadState();
        page.waitForTimeout(3000);

        deliveryRulesPage = new ConfigureDeliveryRulesPage(page);
    }

    @Override
    @Test(enabled = false)
    public void verifyBoundarySelection() {}

    @Override @Test(enabled = false) public void verifyBoundarySelectionWithPartialSelection() {}

    @Override @Test(enabled = false) public void verifyValidCampaignName() {}
    @Override @Test(enabled = false) public void verifyCampaignNameTooLong() {}
    @Override @Test(enabled = false) public void verifyCampaignNameStartsWithSpecialChar() {}
    @Override @Test(enabled = false) public void verifyCampaignNameContainsEmoji() {}
    @Override @Test(enabled = false) public void verifyCampaignNameConsecutiveUnderscores() {}
    @Override @Test(enabled = false) public void verifySubmitWithStartDateOnly() {}
    @Override @Test(enabled = false) public void verifySubmitWithEndDateOnly() {}

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifyNextWithFirstStartDateOnly() {
        // Click Configure Delivery to open the delivery rules form
        deliveryRulesPage.clickConfigureDelivery();
        page.waitForLoadState();
        page.waitForTimeout(2000);

        // Fill only the first start date, leaving all other dates empty
        deliveryRulesPage.fillFirstStartDateOnly();
        page.waitForTimeout(1000);

        // Click Next without completing remaining cycle dates
        deliveryRulesPage.clickNext();
        page.waitForTimeout(2000);

        // Verify toast error appears
        Assert.assertTrue(deliveryRulesPage.isCycleDateToastVisible(),
                "Toast 'Please fill the cycle dates to move ahead.' should appear when only the first start date is filled");
    }

    @Test(groups = {"regression", "workbench-ui"})
    public void verifyConfigureDeliveryRules() {
        // Step 1: Click Configure Delivery button on campaign dashboard
        deliveryRulesPage.clickConfigureDelivery();
        page.waitForLoadState();
        page.waitForTimeout(2000);

        // Step 2: Fill dates (BEDNET: 1 start/end, MR-DN: 3 cycles with 1-week gaps)
        deliveryRulesPage.fillDates();
        page.waitForTimeout(1000);

        // Step 4: Click Next
        deliveryRulesPage.clickNext();
        page.waitForTimeout(2000);

        // Step 5: Click Next again
        deliveryRulesPage.clickNext();
        page.waitForTimeout(2000);

        // Step 6: Click Submit
        deliveryRulesPage.clickSubmit();
        page.waitForTimeout(2000);
    }
}
