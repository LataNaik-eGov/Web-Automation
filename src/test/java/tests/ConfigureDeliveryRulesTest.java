package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.ConfigureDeliveryRulesPage;

public class ConfigureDeliveryRulesTest extends BaseTest {


    @Test(groups = { "workbench-ui", "sanity"})
    public void verifyConfigureDeliveryRules() {
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToConfigureDeliveryRules();

        deliveryRulesPage.clickConfigureDelivery();

        deliveryRulesPage.fillDates();

        deliveryRulesPage.clickNext();

        deliveryRulesPage.clickNext();

        deliveryRulesPage.clickSubmit();
    }
// Negative tests

   @Test(groups = {"negative",  "workbench-ui"})
    public void verifyNextWithFirstStartDateOnly() {
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToConfigureDeliveryRules();

        deliveryRulesPage.clickConfigureDelivery();

        deliveryRulesPage.fillStartDate();

        deliveryRulesPage.clickNext();

        Assert.assertTrue(deliveryRulesPage.isCycleDateToastVisible(),
                "Toast 'Please fill the cycle dates to move ahead.' should appear when only the start date is filled");
    }

       @Test(groups = {"negative",  "workbench-ui"})
    public void verifyNextWithoutFillingDates() {
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToConfigureDeliveryRules();

        deliveryRulesPage.clickConfigureDelivery();

        deliveryRulesPage.clickNext();

        Assert.assertTrue(deliveryRulesPage.isCycleDateToastVisible(),
                "Toast 'Please fill the cycle dates to move ahead.' should appear when no dates are filled");
    }

    @Test(groups = {"negative",  "workbench-ui"})
    public void verifyDeliveryRulesWithInvalidInput() {
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToDeliveryRulesSecondStep();

        deliveryRulesPage.fillNthTextbox(2, "30");

        deliveryRulesPage.clickNext();

        Assert.assertTrue(deliveryRulesPage.isDeliveryErrorToastVisible(),
                "Toast error should appear when invalid value '30' is entered in the second step");
    }

       @Test(groups = {"negative",  "workbench-ui"})
    public void verifyDeliveryRulesWithZeroInput() {
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToDeliveryRulesSecondStep();

        deliveryRulesPage.fillNthTextbox(3, "0");

        deliveryRulesPage.clickNext();

        Assert.assertTrue(deliveryRulesPage.isDeliveryErrorToastVisible(),
                "Toast error should appear when invalid value '30' is entered in the second step");
    }


           @Test(groups = {"negative",  "workbench-ui"})
    public void verifyDeliveryRulesWithEmptyInput() {
        ConfigureDeliveryRulesPage deliveryRulesPage = nav.goToDeliveryRulesSecondStep();
        deliveryRulesPage.fillNthTextbox(2, "");

        deliveryRulesPage.fillNthTextbox(3, "");

        deliveryRulesPage.clickNext();

        Assert.assertTrue(deliveryRulesPage.isDeliveryErrorToastVisible(),
                "Toast error should appear when invalid value '30' is entered in the second step");
    }

}
