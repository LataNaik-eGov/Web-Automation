package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.EmployeeInboxPage;
import utils.ConfigReader;

public class EmployeeInboxTest extends BaseTest {

    // ---------------------------------------------------------------
    // Test Case 1 — Edit Employee
    // ---------------------------------------------------------------
    @Test(priority = 1, groups = { "payments-ui" })
    public void editEmployee() {
        EmployeeInboxPage searchPage = nav.goToSearchEmployee();

        searchPage.searchEmployee(ConfigReader.get("search.emp.id"));

        searchPage.openEmployeeResult();

        searchPage.openTakeActionMenu();

        searchPage.clickEditEmployee();

        searchPage.editEmployeeName();

        searchPage.fillRequiredEditFields();

        searchPage.saveEmployeeEdit();

        boolean success = searchPage.isSuccessMessageVisible("Employee Details Updated Successfully");

        Assert.assertTrue(success, "Edit Employee: success message not found");

        searchPage.goBackToHome();
    }

    // ---------------------------------------------------------------
    // Test Case 2 — Deactivate Employee
    // ---------------------------------------------------------------
    @Test(priority = 2, groups = { "payments-ui" })
    public void deactivateEmployee() {
        EmployeeInboxPage searchPage = nav.goToSearchEmployee();

        searchPage.searchEmployee(ConfigReader.get("search.emp.id"));

        searchPage.openEmployeeResult();

        searchPage.openTakeActionMenu();

        searchPage.clickDeactivateEmployee();

        searchPage.selectDeactivateReason();

        searchPage.confirmDeactivate();

        boolean success = searchPage.isSuccessMessageVisible("Employee Deactivated Successfully");

        Assert.assertTrue(success, "Deactivate Employee: success message not found");

        searchPage.goBackToHome();
    }

    // ---------------------------------------------------------------
    // Test Case 3 — Search and Verify Employee Appears in Results
    // ---------------------------------------------------------------
    @Test(priority = 3, groups = { "payments-ui" })
    public void searchAndVerifyEmployee() {
        String empId = ConfigReader.get("search.emp.id");

        EmployeeInboxPage searchPage = nav.goToSearchEmployee();

        searchPage.searchEmployee(empId);

        // Verify a result link appears in the table — try most specific to broadest
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