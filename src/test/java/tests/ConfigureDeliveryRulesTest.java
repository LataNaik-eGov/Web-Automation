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

        deliveryRulesPage.fillDates();

        deliveryRulesPage.clickNext();

        deliveryRulesPage.clickNext();

        deliveryRulesPage.clickSubmit();
    }

    @Test(groups = { "workbench-ui", "sanity"})
    public void verifyConfigureDeliveryRules_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToConfigureDeliveryRules();

        deliveryRulesPage.clickConfigureDelivery();

        deliveryRulesPage.fillDates();

        deliveryRulesPage.clickNext();

        deliveryRulesPage.clickNext();

        deliveryRulesPage.clickSubmit();
    }

    // Negative tests

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyNextWithFirstStartDateOnly_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToConfigureDeliveryRules();

        deliveryRulesPage.clickConfigureDelivery();

        deliveryRulesPage.fillStartDate();

        deliveryRulesPage.clickNext();

        Assert.assertTrue(deliveryRulesPage.isCycleDateToastVisible(),
                "Toast 'Please fill the cycle dates to move ahead.' should appear when only the start date is filled");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyNextWithFirstStartDateOnly_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
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
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToConfigureDeliveryRules();

        deliveryRulesPage.clickConfigureDelivery();

        deliveryRulesPage.clickNext();

        Assert.assertTrue(deliveryRulesPage.isCycleDateToastVisible(),
                "Toast 'Please fill the cycle dates to move ahead.' should appear when no dates are filled");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyNextWithoutFillingDates_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToConfigureDeliveryRules();

        deliveryRulesPage.clickConfigureDelivery();

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
