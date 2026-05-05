package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;

/**
 * Test class for Login functionality.
 *
 * Note: Login happens automatically in BaseTest.setup()
 * These tests verify the login was successful.
 */
public class LoginTest extends BaseTest {

    @Test(groups = {"common", "payments-ui", "workbench-ui"})
    public void verifyUserCanLogin() {
        // Login is handled automatically in BaseTest.setup()
        // homePage is available directly after login
        Assert.assertTrue(homePage.isHomeDisplayed(), "Home page should be displayed after login");
    }

    @Test(groups = {"common", "payments-ui", "workbench-ui"})
    public void verifyHomePageElements() {
        // Verify expected elements are visible on home page
        Assert.assertTrue(homePage.isCreateComplaintVisible(), "Create Complaint button should be visible");
        Assert.assertTrue(homePage.isSearchComplaintVisible(), "Search Complaint button should be visible");
    }

    @Test(groups = {"common", "payments-ui", "workbench-ui"})
    public void verifyNavigationFromHome() {
        // Test navigation to Create Complaint and back
        homePage.goToCreateComplaint();

        // Navigate back to home
        nav.goToHome();

        // Verify we're back on home page
        Assert.assertTrue(homePage.isHomeDisplayed(), "Should be back on home page");
    }
}
