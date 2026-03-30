package tests;

import com.microsoft.playwright.Download;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pages.UploadFilePage;
import utils.ConfigReader;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class UploadFileTest extends AppConfigurationTest {

    protected UploadFilePage uploadFilePage;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "navigateToAppConfiguration")
    public void navigateToUploadFile() {
        // Complete app configuration to reach upload file page
        appConfigPage.clickSetUpMobileApp();
        page.waitForLoadState();
        page.waitForTimeout(2000);

        appConfigPage.configureRegistrationAndDelivery();
        page.waitForTimeout(2000);

        appConfigPage.configureCloseHousehold();
        page.waitForTimeout(2000);

        appConfigPage.configureComplaints();
        page.waitForTimeout(2000);

        appConfigPage.configureInventory();
        page.waitForTimeout(2000);

        appConfigPage.configureStockReconciliation();
        page.waitForTimeout(2000);

        appConfigPage.configureReports();
        page.waitForTimeout(2000);

        appConfigPage.configurePermissionHandler();
        page.waitForTimeout(2000);

        appConfigPage.clickGoBack();
        page.waitForTimeout(2000);

        uploadFilePage = new UploadFilePage(page);
    }

    @Override
    @Test(enabled = false)
    public void verifyAppConfiguration() {}

    @Test(groups = {"regression"})
    public void verifyUploadFile() throws URISyntaxException {
        // Step 1: Click Upload Data
        uploadFilePage.clickUploadData();
        page.waitForLoadState();
        page.waitForTimeout(2000);

        // Step 2: Download Template
        Download download = uploadFilePage.downloadTemplate();
        Assert.assertNotNull(download, "Template download should have started");
        page.waitForTimeout(2000);

        // Step 3: Upload filled file from resources
        String templateFile = ConfigReader.getTemplateFileName();
        URL resource = getClass().getClassLoader().getResource(templateFile);
        Assert.assertNotNull(resource, templateFile + " should exist in test resources");
        String filePath = Paths.get(resource.toURI()).toString();
        uploadFilePage.uploadFile(filePath);
        page.waitForTimeout(2000);
    }
}
