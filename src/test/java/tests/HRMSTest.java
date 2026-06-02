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
        // Generate unique values per run to satisfy uniqueness constraints
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
}
