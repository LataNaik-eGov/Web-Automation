package tests;

import org.testng.annotations.Test;

import base.BaseTest;


public class HomeTest extends BaseTest {

    @Test(groups = {"payments-ui", "workbench-ui"})
    public void navigateToCreateComplaint() {
        homePage.navigateToCreateComplaint();
    }
}
