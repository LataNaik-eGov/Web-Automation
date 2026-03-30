package tests;

import com.microsoft.playwright.Download;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pages.CreateChecklist;
import utils.ConfigReader;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class CreateChecklistTest extends UploadFileTest {

    protected CreateChecklist createChecklistPage;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "navigateToUploadFile")
    public void navigateToCreateChecklist() throws URISyntaxException {
        // Run upload file steps first
        uploadFilePage.clickUploadData();
        page.waitForLoadState();
        page.waitForTimeout(2000);

        Download download = uploadFilePage.downloadTemplate();
        Assert.assertNotNull(download, "Template download should have started");
        page.waitForTimeout(2000);

        String baseUrl = ConfigReader.get("BASE_URL");
        String templateFile;
        if (baseUrl != null && baseUrl.contains("bauchi")) {
            templateFile = "bauchi-unifiedtemplate.xlsx";
        } else {
            templateFile = ConfigReader.getTemplateFileName();
        }
        URL resource = getClass().getClassLoader().getResource(templateFile);
        Assert.assertNotNull(resource, templateFile + " should exist in test resources");
        String filePath = Paths.get(resource.toURI()).toString();
        uploadFilePage.uploadFile(filePath);
        page.waitForTimeout(2000);

        createChecklistPage = new CreateChecklist(page);
    }

    @Override
    @Test(enabled = false)
    public void verifyUploadFile() {}

    @Test(groups = {"regression"})
    public void verifyCreateChecklist() {
        // Step 1: Click Create Checklist
        createChecklistPage.clickCreateChecklist();
        page.waitForLoadState();
        page.waitForTimeout(2000);

        // Step 2: Configure List
        createChecklistPage.clickConfigureList();
        page.waitForTimeout(2000);

        // Step 3: Configure Checklist
        createChecklistPage.clickConfigureChecklist();
        page.waitForTimeout(2000);

        // Step 4: Confirm Checklist
        createChecklistPage.clickConfirmChecklist();
        page.waitForTimeout(2000);

        // Step 5: Back to Homepage
        createChecklistPage.clickBackToHomepage();
        page.waitForTimeout(2000);

        // Step 6: Create Campaign
        createChecklistPage.clickCreateCampaign();
        page.waitForTimeout(2000);
    }
}
