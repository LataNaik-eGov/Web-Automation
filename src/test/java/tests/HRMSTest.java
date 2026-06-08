package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HRMSPage;

public class HRMSTest extends BaseTest {

    @Test(groups = {"payments-ui"})
    public void createHRMS() {
        HRMSPage hrms = homePage.goToCreateUser();
        String createdUsername = hrms.createEmployee();

        Assert.assertTrue(hrms.isEmployeeCreatedSuccessfully(),
                "Employee should be created successfully");
        Assert.assertNotNull(createdUsername,
                "Username should be visible on the success screen");
        Assert.assertTrue(hrms.searchAndVerifyEmployee(createdUsername),
                "Employee '" + createdUsername + "' should be searchable and verified on Employee Details screen");
    }

    @Test(groups = {"payments-ui"})
    public void editEmployee() {
        HRMSPage hrms = homePage.goToCreateUser();
        String createdUsername = hrms.createEmployee();

        Assert.assertTrue(hrms.isEmployeeCreatedSuccessfully(),
                "Employee should be created before editing");
        Assert.assertNotNull(createdUsername,
                "Username should be visible on the success screen");

        hrms.goBackToHome();
        hrms.goToSearchFromHome();
        page.waitForTimeout(3000);

        hrms.searchEmployee(createdUsername)
                .openEmployeeByUsername(createdUsername)
                .editAndSave();

        Assert.assertTrue(
                hrms.isSuccessMessageVisible("Employee Details Updated"),
                "Edit Employee: success message not found");

        hrms.goBackToHome();
    }

    @Test(groups = {"payments-ui"})
    public void createEmployeeWithDuplicateUsername() {
        // Step 1: Create first employee and capture username
        HRMSPage hrms = homePage.goToCreateUser();
        String username = hrms.createEmployee();

        Assert.assertTrue(hrms.isEmployeeCreatedSuccessfully(),
                "First employee should be created successfully");
        Assert.assertNotNull(username,
                "Username should be visible on the success screen");

        // Step 2: Attempt to create second employee with the same username
        hrms.goBackToHome();
        hrms = homePage.goToCreateUser();

        Assert.assertTrue(hrms.createEmployeeWithDuplicateUsername(username),
                "Creating employee with duplicate username '" + username + "' should be blocked");
    }

    @Test(groups = {"payments-ui"})
    public void createEmployeeWithDuplicateMobile() {
        String mobile = HRMSPage.generateMobile();

        // Create first employee with this mobile
        HRMSPage hrms = homePage.goToCreateUser();
        String username = hrms.createEmployeeWithMobile(mobile);

        Assert.assertTrue(hrms.isEmployeeCreatedSuccessfully(),
                "First employee should be created successfully");
        Assert.assertNotNull(username,
                "Username should be visible on the success screen");

        // Attempt to create second employee with the same mobile
        hrms.goBackToHome();
        hrms = homePage.goToCreateUser();

        Assert.assertTrue(hrms.createEmployeeWithDuplicateMobile(mobile),
                "Creating employee with duplicate mobile '" + mobile + "' should be blocked");
    }

    @Test(groups = {"payments-ui"})
    public void deactivateEmployee() {
        HRMSPage hrms = homePage.goToCreateUser();
        String createdUsername = hrms.createEmployee();

        Assert.assertTrue(hrms.isEmployeeCreatedSuccessfully(),
                "Employee should be created before deactivating");
        Assert.assertNotNull(createdUsername,
                "Username should be visible on the success screen");

        hrms.goBackToHome();
        hrms.goToSearchFromHome();
        page.waitForTimeout(3000);

        hrms.searchEmployee(createdUsername)
                .openEmployeeByUsername(createdUsername);

        Assert.assertTrue(hrms.performDeactivate(createdUsername),
                "Employee '" + createdUsername + "' should be deactivated successfully");
    }
}
