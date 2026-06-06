package tests;

import com.microsoft.playwright.Download;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.CreateChecklist;
import pages.UploadFilePage;
import utils.ConfigReader;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class CreateChecklistTest extends BaseTest {

    private CreateChecklist setupCreateChecklistPage() throws URISyntaxException {
        UploadFilePage uploadFilePage = nav.goToUploadFile();

        uploadFilePage.clickUploadData();

        Download download = uploadFilePage.downloadTemplate();
        Assert.assertNotNull(download, "Template download should have started");

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

        uploadFilePage.closePopup();
        uploadFilePage.clickSubmit();

        return new CreateChecklist(page);
    }

    @Test(groups = {"regression", "workbench-ui", "sanity"})
    public void verifyCreateChecklist() throws URISyntaxException {
        CreateChecklist createChecklistPage = setupCreateChecklistPage();

        createChecklistPage.clickCreateChecklist();

        createChecklistPage.clickConfigureList();

        createChecklistPage.clickConfigureChecklist();

        createChecklistPage.clickConfirmChecklist();

        createChecklistPage.clickBackToHomepage();

        createChecklistPage.clickCreateCampaign();
    }
}
