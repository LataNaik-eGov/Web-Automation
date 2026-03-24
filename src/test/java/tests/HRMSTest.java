package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HRMSPage;
import utils.ConfigReader;

public class HRMSTest extends BaseTest {

    // ---------------------------------------------------------------
    // Test Case 1 — Create Employee (all fields)
    // ---------------------------------------------------------------
    @Test(priority = 1)
    public void createNewEmployee() {
        // HRMSPage hrmsPage = nav.goToCreateEmployee();

        // hrmsPage.fillLoginDetails(
        //         ConfigReader.getEmpUsername(),
        //         ConfigReader.getEmpPassword());

        // hrmsPage.fillPersonalDetails(
        //         ConfigReader.getEmpName(),
        //         ConfigReader.getEmpMobile(),
        //         ConfigReader.getEmpDob(),
        //         ConfigReader.getEmpEmail(),
        //         ConfigReader.getEmpAddress());
        // screenshot.take("03_personal_details_filled");

        // hrmsPage.fillEmployeeDetails();
        // screenshot.take("04_employee_details_filled");

        // hrmsPage.submitForm();
        // screenshot.take("05_form_submitted");

        // boolean success = hrmsPage.isEmployeeCreatedSuccessfully();
        // if (success) {
          
        //     System.out.println("New Employee ID: " + hrmsPage.getEmployeeId());
        // }

        // Assert.assertTrue(success, "Employee was not created successfully");
    }

    // ---------------------------------------------------------------
    // Test Case 2 — Create Employee (mandatory fields only)
    // ---------------------------------------------------------------
    @Test(priority = 2)
    public void createEmployeeMinimalData() {
        // Generate unique credentials for this test — avoids collision with
        // createNewEmployee
        // int rand = new java.util.Random().nextInt(9000) + 1000;
        // String minEmpId = "smc-oy-hd-min" + rand;
        // String minEmail = "mintest" + rand + "@test.com";

        // HRMSPage hrmsPage = nav.goToCreateEmployee();
        // screenshot.take("min_01_form_opened");

        // // Mandatory: employee login credentials
        // hrmsPage.fillLoginDetails(minEmpId, ConfigReader.getEmpPassword());
        // screenshot.take("min_02_login_details");

        // // Mandatory: name, gender (clicked inside method), dob
        // // Optional skipped: mobile (""), address ("")
        // // Email included — DIGIT API requires it even though not HTML-required
        // hrmsPage.fillPersonalDetails(
        //         ConfigReader.getEmpName(),
        //         "", // mobile — optional, skipped
        //         ConfigReader.getEmpDob(),
        //         minEmail,
        //         ""); // address — optional, skipped
        // screenshot.take("min_03_personal_details");

        // // Mandatory: employment type, date of appointment, role
        // hrmsPage.fillEmployeeDetails();
        // screenshot.take("min_04_employee_details");

        // hrmsPage.submitForm();
        // screenshot.take("min_05_submitted");

        // boolean success = hrmsPage.isEmployeeCreatedSuccessfully();
        // if (success) {
        //     screenshot.takeOnSuccess("createEmployeeMinimalData");
        //     System.out.println("[MinTest] New Employee ID: " + hrmsPage.getEmployeeId());
        // }

        // Assert.assertTrue(success, "Minimal employee was not created successfully");
    }
}