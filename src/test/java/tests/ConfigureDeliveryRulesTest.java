package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.ConfigureDeliveryRulesPage;
import utils.TestDataReader;

public class ConfigureDeliveryRulesTest extends BaseTest {

    @Test(groups = { "workbench-ui", "sanity"})
    public void verifyConfigureDeliveryRules_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToConfigureDeliveryRules();

        deliveryRulesPage.clickConfigureDelivery();

        // Opens on the cycles / deliveries / observation-strategy screen.
        deliveryRulesPage.clickNext();

        // Cycle dates are only a step for campaign types that have one.
        deliveryRulesPage.fillDatesAndNextIfPresent();

        // Delivery conditions -> summary.
        deliveryRulesPage.clickNext();

        deliveryRulesPage.clickSubmit();

        Assert.assertTrue(deliveryRulesPage.isConfigureDeliveryButtonVisible(),
                "Should return to the campaign details page after submitting the delivery strategy");
    }

    @Test(groups = { "workbench-ui", "sanity"})
    public void verifyConfigureDeliveryRules_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToConfigureDeliveryRules();

        deliveryRulesPage.clickConfigureDelivery();

        // Opens on the cycles / deliveries / observation-strategy screen.
        deliveryRulesPage.clickNext();

        // Cycle dates are only a step for campaign types that have one.
        deliveryRulesPage.fillDatesAndNextIfPresent();

        // Delivery conditions -> summary.
        deliveryRulesPage.clickNext();

        deliveryRulesPage.clickSubmit();

        Assert.assertTrue(deliveryRulesPage.isConfigureDeliveryButtonVisible(),
                "Should return to the campaign details page after submitting the delivery strategy");
    }

    // Negative tests
    //
    // The cycle-date cases are MR-DN only: as of the 2026-09-09 hcm-demo build a
    // single-cycle type such as BEDNET has no cycle-date screen at all, so there
    // is no "fill the cycle dates" validation to assert for it.


    @Test(groups = {"negative", "workbench-ui"})
    public void verifyNextWithFirstStartDateOnly_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToConfigureDeliveryRules();

        deliveryRulesPage.clickConfigureDelivery();

        // Delivery config opens on the cycles / deliveries screen; the cycle dates
        // are the screen after it. MR-DN is the only type that still has this step.
        deliveryRulesPage.clickNext();

        deliveryRulesPage.fillStartDate();

        deliveryRulesPage.clickNext();

        Assert.assertTrue(deliveryRulesPage.isCycleDateToastVisible(),
                "Toast 'Please fill the cycle dates to move ahead.' should appear when only the start date is filled");
    }


    @Test(groups = {"negative", "workbench-ui"})
    public void verifyNextWithoutFillingDates_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToConfigureDeliveryRules();

        deliveryRulesPage.clickConfigureDelivery();

        // Advance past the cycles / deliveries screen onto the cycle-date screen,
        // then try to leave it with no dates filled.
        deliveryRulesPage.clickNext();

        deliveryRulesPage.clickNext();

        Assert.assertTrue(deliveryRulesPage.isCycleDateToastVisible(),
                "Toast 'Please fill the cycle dates to move ahead.' should appear when no dates are filled");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyDeliveryRulesWithInvalidInput_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToDeliveryRulesSecondStep();

        deliveryRulesPage.fillNthTextbox(2, "30");

        deliveryRulesPage.clickNext();

        Assert.assertTrue(deliveryRulesPage.isDeliveryErrorToastVisible(),
                "Toast error should appear when invalid value '30' is entered in the second step");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyDeliveryRulesWithInvalidInput_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToDeliveryRulesSecondStep();

        deliveryRulesPage.fillNthTextbox(2, "30");

        deliveryRulesPage.clickNext();

        Assert.assertTrue(deliveryRulesPage.isDeliveryErrorToastVisible(),
                "Toast error should appear when invalid value '30' is entered in the second step");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyDeliveryRulesWithZeroInput_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToDeliveryRulesSecondStep();

        deliveryRulesPage.fillNthTextbox(3, "0");

        deliveryRulesPage.clickNext();

        Assert.assertTrue(deliveryRulesPage.isDeliveryErrorToastVisible(),
                "Toast error should appear when invalid value '30' is entered in the second step");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyDeliveryRulesWithZeroInput_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToDeliveryRulesSecondStep();

        deliveryRulesPage.fillNthTextbox(3, "0");

        deliveryRulesPage.clickNext();

        Assert.assertTrue(deliveryRulesPage.isDeliveryErrorToastVisible(),
                "Toast error should appear when invalid value '30' is entered in the second step");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyDeliveryRulesWithEmptyInput_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToDeliveryRulesSecondStep();
        deliveryRulesPage.fillNthTextbox(2, "");

        deliveryRulesPage.fillNthTextbox(3, "");

        deliveryRulesPage.clickNext();

        Assert.assertTrue(deliveryRulesPage.isDeliveryErrorToastVisible(),
                "Toast error should appear when invalid value '30' is entered in the second step");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyDeliveryRulesWithEmptyInput_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToDeliveryRulesSecondStep();
        deliveryRulesPage.fillNthTextbox(2, "");

        deliveryRulesPage.fillNthTextbox(3, "");

        deliveryRulesPage.clickNext();

        Assert.assertTrue(deliveryRulesPage.isDeliveryErrorToastVisible(),
                "Toast error should appear when invalid value '30' is entered in the second step");
    }
}
