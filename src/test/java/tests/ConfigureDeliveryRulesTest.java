package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.ConfigureDeliveryRulesPage;
import utils.TestDataReader;

public class ConfigureDeliveryRulesTest extends BaseTest {

    // ==================== Configure Delivery Rules Flow ====================

    @Test(groups = { "workbench-ui", "sanity"})
    public void verifyConfigureDeliveryRules_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        verifyConfigureDeliveryRules();
    }

    @Test(groups = { "workbench-ui", "sanity"})
    public void verifyConfigureDeliveryRules_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        verifyConfigureDeliveryRules();
    }

    private void verifyConfigureDeliveryRules() {
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToConfigureDeliveryRules();

        deliveryRulesPage.clickConfigureDelivery();
        deliveryRulesPage.fillDates();
        deliveryRulesPage.clickNext();
        deliveryRulesPage.clickNext();
        deliveryRulesPage.clickSubmit();
    }

    // ==================== Negative Tests ====================

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyNextWithFirstStartDateOnly_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        verifyNextWithFirstStartDateOnly();
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyNextWithFirstStartDateOnly_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        verifyNextWithFirstStartDateOnly();
    }

    private void verifyNextWithFirstStartDateOnly() {
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToConfigureDeliveryRules();

        deliveryRulesPage.clickConfigureDelivery();
        deliveryRulesPage.fillStartDate();
        deliveryRulesPage.clickNext();

        Assert.assertTrue(deliveryRulesPage.isCycleDateToastVisible(),
                "Toast 'Please fill the cycle dates to move ahead.' should appear when only the start date is filled");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyNextWithoutFillingDates_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        verifyNextWithoutFillingDates();
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyNextWithoutFillingDates_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        verifyNextWithoutFillingDates();
    }

    private void verifyNextWithoutFillingDates() {
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToConfigureDeliveryRules();

        deliveryRulesPage.clickConfigureDelivery();
        deliveryRulesPage.clickNext();

        Assert.assertTrue(deliveryRulesPage.isCycleDateToastVisible(),
                "Toast 'Please fill the cycle dates to move ahead.' should appear when no dates are filled");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyDeliveryRulesWithInvalidInput_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        verifyDeliveryRulesWithInvalidInput();
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyDeliveryRulesWithInvalidInput_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        verifyDeliveryRulesWithInvalidInput();
    }

    private void verifyDeliveryRulesWithInvalidInput() {
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToDeliveryRulesSecondStep();

        deliveryRulesPage.fillNthTextbox(2, "30");
        deliveryRulesPage.clickNext();

        Assert.assertTrue(deliveryRulesPage.isDeliveryErrorToastVisible(),
                "Toast error should appear when invalid value '30' is entered in the second step");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyDeliveryRulesWithZeroInput_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        verifyDeliveryRulesWithZeroInput();
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyDeliveryRulesWithZeroInput_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        verifyDeliveryRulesWithZeroInput();
    }

    private void verifyDeliveryRulesWithZeroInput() {
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToDeliveryRulesSecondStep();

        deliveryRulesPage.fillNthTextbox(3, "0");
        deliveryRulesPage.clickNext();

        Assert.assertTrue(deliveryRulesPage.isDeliveryErrorToastVisible(),
                "Toast error should appear when invalid value '0' is entered in the second step");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyDeliveryRulesWithEmptyInput_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        verifyDeliveryRulesWithEmptyInput();
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyDeliveryRulesWithEmptyInput_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        verifyDeliveryRulesWithEmptyInput();
    }

    private void verifyDeliveryRulesWithEmptyInput() {
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToDeliveryRulesSecondStep();

        deliveryRulesPage.fillNthTextbox(2, "");
        deliveryRulesPage.fillNthTextbox(3, "");
        deliveryRulesPage.clickNext();

        Assert.assertTrue(deliveryRulesPage.isDeliveryErrorToastVisible(),
                "Toast error should appear when empty values are entered in the second step");
    }
}
