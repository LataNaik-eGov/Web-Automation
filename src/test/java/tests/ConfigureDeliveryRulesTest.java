package tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pages.ConfigureDeliveryRulesPage;

public class ConfigureDeliveryRulesTest extends BoundarySelectionTest {

    protected ConfigureDeliveryRulesPage deliveryRulesPage;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "navigateToBoundarySelection")
    public void navigateToConfigureDeliveryRules() {
        // Complete boundary selection to reach delivery rules page
        boundaryPage.clickfirstlevel();
        page.waitForTimeout(2000);

        boundaryPage.clicksecondlevel();
        page.waitForTimeout(2000);

        boundaryPage.clickthirdlevel();
        page.waitForTimeout(2000);

        boundaryPage.clickfourthlevel();
        page.waitForLoadState();
        page.waitForTimeout(2000);

        deliveryRulesPage = new ConfigureDeliveryRulesPage(page);
    }

    @Override
    @Test(enabled = false)
    public void verifyBoundarySelection() {}

    @Test(groups = {"regression"})
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
