package tests;

import com.microsoft.playwright.Download;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.UploadFilePage;
import utils.ConfigReader;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class UploadFileTest extends BaseTest {

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifySubmitWithoutFile() {
        UploadFilePage uploadFilePage = nav.goToUploadFile();

        uploadFilePage.clickUploadData();

        uploadFilePage.closePopupByClickingOutside();
        uploadFilePage.clickSubmit();

        Assert.assertTrue(uploadFilePage.isNoFileToastVisible(),
                "Toast 'Please upload a file' should appear when Submit is clicked without uploading a file");
    }

    @Test(groups = {"regression", "workbench-ui", "sanity"})
    public void verifyUploadFile() throws URISyntaxException {
        UploadFilePage uploadFilePage = nav.goToUploadFile();

        uploadFilePage.clickUploadData();
        uploadFilePage.closePopupByClickingOutside();

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

        uploadFilePage.closePopupByClickingOutside();
        uploadFilePage.clickSubmit();
    }
}
