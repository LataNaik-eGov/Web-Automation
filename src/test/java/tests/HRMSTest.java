package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HRMSPage;
import utils.ConfigReader;

import java.util.concurrent.ThreadLocalRandom;

public class HRMSTest extends BaseTest {

    @Test(groups = {"payments-ui"})
    public void createHRMS() {
        String username = "Test-" + System.currentTimeMillis() % 100000;
        String mobile = "8" + String.format("%09d", ThreadLocalRandom.current().nextInt(100000000, 999999999));

        System.out.println("[HRMS] Username: " + username + " | Mobile: " + mobile);

        HRMSPage hrms = homePage.goToCreateUser();

        hrms.selectHierarchyType(
                ConfigReader.get("hrms.hierarchy.search"),
                ConfigReader.get("hrms.hierarchy.option"))
            .clickNext()
            .fillLoginDetails(
                username,
                ConfigReader.get("hrms.emp.password"))
            .fillPersonalDetails(
                ConfigReader.get("hrms.emp.name"),
                mobile,
                ConfigReader.get("hrms.emp.gender"),
                ConfigReader.get("hrms.emp.dob"),
                ConfigReader.get("hrms.emp.email"),
                ConfigReader.get("hrms.emp.address"))
            .fillEmploymentDetails(
                ConfigReader.get("hrms.emp.type"),
                ConfigReader.get("hrms.emp.doa"),
                ConfigReader.get("hrms.emp.department"),
                ConfigReader.get("hrms.emp.designation"),
                ConfigReader.get("hrms.emp.role"),
                ConfigReader.get("hrms.emp.jurisdiction"));

        hrms.submitForm();

        Assert.assertTrue(hrms.isEmployeeCreatedSuccessfully(),
                "Employee should be created successfully");

        System.out.println("[HRMS] Created employee ID: " + hrms.getEmployeeId());
    }

    @Test(priority = 1, groups = {"payments-ui"})
    public void editEmployee() {
        HRMSPage hrms = nav.goToSearchEmployee();

        hrms.searchEmployee(ConfigReader.get("search.emp.id"))
                .openEmployeeResult()
                .openTakeActionMenu()
                .clickEditEmployee()
                .editEmployeeName()
                .fillRequiredEditFields()
                .saveEmployeeEdit();

        Assert.assertTrue(
                hrms.isSuccessMessageVisible("Employee Details Updated Successfully"),
                "Edit Employee: success message not found");

        hrms.goBackToHome();
    }

    @Test(priority = 2, groups = {"payments-ui"})
    public void deactivateEmployee() {
        HRMSPage hrms = nav.goToSearchEmployee();

        hrms.searchEmployee(ConfigReader.get("search.emp.id"))
                .openEmployeeResult()
                .openTakeActionMenu()
                .clickDeactivateEmployee()
                .selectDeactivateReason()
                .confirmDeactivate();

        Assert.assertTrue(
                hrms.isSuccessMessageVisible("Employee Deactivated Successfully"),
                "Deactivate Employee: success message not found");

        hrms.goBackToHome();
    }

    @Test(priority = 3, groups = {"payments-ui"})
    public void searchAndVerifyEmployee() {
        String empId = ConfigReader.get("search.emp.id");
        HRMSPage hrms = nav.goToSearchEmployee();

        hrms.searchEmployee(empId);

        String[] selectors = {
                "table tbody tr td:first-child a",
                "tbody tr td a",
                "tbody a"
        };

        boolean resultFound = false;
        for (String sel : selectors) {
            int count = page.locator(sel).count();
            System.out.println("[SearchVerify] Selector '" + sel + "' count: " + count);
            if (count > 0) {
                resultFound = true;
                System.out.println("[SearchVerify] Result link found with: " + sel);
                break;
            }
        }

        Assert.assertTrue(resultFound,
                "Employee '" + empId + "' result link not found in search results table");
    }
}
