package tests;

import org.testng.annotations.Test;

import base.BaseTest;
import pages.HomePage;


public class HomeTest extends BaseTest {

    @Test(groups = {"payments-ui", "workbench-ui"})
    public void navigateToCreateComplaint() {
       
        HomePage homePage = new HomePage(page);
        homePage.navigateToCreateComplaint();
    }
    
}
