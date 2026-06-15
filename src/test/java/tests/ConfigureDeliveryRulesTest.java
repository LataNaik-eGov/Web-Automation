package tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.ConfigureDeliveryRulesPage;
import utils.TestDataReader;

public class ConfigureDeliveryRulesTest extends BaseTest {

    @DataProvider(name = "campaignTypes")
    public Object[][] campaignTypes() {
        return new Object[][]{{"BEDNET"}, {"MR-DN"}};
    }

    @Test(dataProvider = "campaignTypes", groups = { "workbench-ui", "sanity"})
    public void verifyConfigureDeliveryRules(String campaignType) {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", campaignType);
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToConfigureDeliveryRules();

        deliveryRulesPage.clickConfigureDelivery();

        deliveryRulesPage.fillDates();

        deliveryRulesPage.clickNext();

        deliveryRulesPage.clickNext();

        deliveryRulesPage.clickSubmit();
    }

    // Negative tests

    @Test(dataProvider = "campaignTypes", groups = {"negative", "workbench-ui"})
    public void verifyNextWithFirstStartDateOnly(String campaignType) {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", campaignType);
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToConfigureDeliveryRules();

        deliveryRulesPage.clickConfigureDelivery();

        deliveryRulesPage.fillStartDate();

        deliveryRulesPage.clickNext();

        Assert.assertTrue(deliveryRulesPage.isCycleDateToastVisible(),
                "Toast 'Please fill the cycle dates to move ahead.' should appear when only the start date is filled");
    }

    @Test(dataProvider = "campaignTypes", groups = {"negative", "workbench-ui"})
    public void verifyNextWithoutFillingDates(String campaignType) {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", campaignType);
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToConfigureDeliveryRules();

        deliveryRulesPage.clickConfigureDelivery();

        deliveryRulesPage.clickNext();

        Assert.assertTrue(deliveryRulesPage.isCycleDateToastVisible(),
                "Toast 'Please fill the cycle dates to move ahead.' should appear when no dates are filled");
    }

    @Test(dataProvider = "campaignTypes", groups = {"negative", "workbench-ui"})
    public void verifyDeliveryRulesWithInvalidInput(String campaignType) {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", campaignType);
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToDeliveryRulesSecondStep();

        deliveryRulesPage.fillNthTextbox(2, "30");

        deliveryRulesPage.clickNext();

        Assert.assertTrue(deliveryRulesPage.isDeliveryErrorToastVisible(),
                "Toast error should appear when invalid value '30' is entered in the second step");
    }

    @Test(dataProvider = "campaignTypes", groups = {"negative", "workbench-ui"})
    public void verifyDeliveryRulesWithZeroInput(String campaignType) {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", campaignType);
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToDeliveryRulesSecondStep();

        deliveryRulesPage.fillNthTextbox(3, "0");

        deliveryRulesPage.clickNext();

        Assert.assertTrue(deliveryRulesPage.isDeliveryErrorToastVisible(),
                "Toast error should appear when invalid value '30' is entered in the second step");
    }

    @Test(dataProvider = "campaignTypes", groups = {"negative", "workbench-ui"})
    public void verifyDeliveryRulesWithEmptyInput(String campaignType) {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", campaignType);
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToDeliveryRulesSecondStep();
        deliveryRulesPage.fillNthTextbox(2, "");

        deliveryRulesPage.fillNthTextbox(3, "");

        deliveryRulesPage.clickNext();

        Assert.assertTrue(deliveryRulesPage.isDeliveryErrorToastVisible(),
                "Toast error should appear when invalid value '30' is entered in the second step");
    }
}
