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

  

    @Test(groups = { "workbench-ui", "sanity"})
    public void verifyUploadFile() throws URISyntaxException {
        UploadFilePage uploadFilePage = nav.goToUploadFile();

        uploadFilePage.clickUploadData();

        uploadFilePage.closePopup();

        Download download = uploadFilePage.downloadTemplate();
        Assert.assertNotNull(download, "Template download should have started");

        String templateFile = ConfigReader.getTemplateFileName();
        URL resource = getClass().getClassLoader().getResource(templateFile);
        Assert.assertNotNull(resource, templateFile + " should exist in test resources");
        String filePath = Paths.get(resource.toURI()).toString();

        uploadFilePage.uploadFile(filePath);

        uploadFilePage.waitForUploadSuccessToast();

        uploadFilePage.clickSubmit();
    }

//Negative tests

      @Test(groups = {"negative",  "workbench-ui"})
    public void verifySubmitWithoutFile() {
        UploadFilePage uploadFilePage = nav.goToUploadFile();

        uploadFilePage.clickUploadData();

        uploadFilePage.closePopup();
        uploadFilePage.clickSubmit();

        Assert.assertTrue(uploadFilePage.isNoFileToastVisible(),
                "Toast 'Please upload a file' should appear when Submit is clicked without uploading a file");
    }

    @Test(groups = {"negative",  "workbench-ui"})
    public void verifyUploadInvalidFileType() throws URISyntaxException {
        UploadFilePage uploadFilePage = nav.goToUploadFile();

        uploadFilePage.clickUploadData();

        uploadFilePage.closePopup();

        URL resource = getClass().getClassLoader().getResource("complaint.pdf");
        Assert.assertNotNull(resource, "complaint.pdf should exist in test resources");
        String filePath = Paths.get(resource.toURI()).toString();

        uploadFilePage.uploadFile(filePath);

        uploadFilePage.clickSubmit();

        Assert.assertTrue(uploadFilePage.isFileErrorToastVisible(),
                "Error toast should appear when an invalid file type (PDF) is uploaded");
    }

    @Test(groups = {"negative",  "workbench-ui"})
    public void verifyUploadInvalidExcelFile() throws URISyntaxException {
        UploadFilePage uploadFilePage = nav.goToUploadFile();

        uploadFilePage.clickUploadData();

        uploadFilePage.closePopup();


        URL resource = getClass().getClassLoader().getResource("InvalidFile.xlsx");
        Assert.assertNotNull(resource, "InvalidFile.xlsx should exist in test resources");
        String filePath = Paths.get(resource.toURI()).toString();

        uploadFilePage.uploadFile(filePath);

        uploadFilePage.clickSubmit();

        Assert.assertTrue(uploadFilePage.isFileErrorToastVisible(),
                "Error toast should appear when an invalid Excel file is uploaded");
    }


    @Test(groups = {"negative",  "workbench-ui"})
    public void verifyWithInvalidInputInFile() throws URISyntaxException {
        UploadFilePage uploadFilePage = nav.goToUploadFile();

        uploadFilePage.clickUploadData();

        uploadFilePage.closePopup();

        URL resource = getClass().getClassLoader().getResource("InvalidInputFile.xlsx");
        Assert.assertNotNull(resource, "InvalidInputFile.xlsx should exist in test resources");
        String filePath = Paths.get(resource.toURI()).toString();

        uploadFilePage.uploadFile(filePath);

        uploadFilePage.clickSubmit();

        Assert.assertTrue(uploadFilePage.isFileErrorToastVisible(),
                "Error toast should appear when a file with invalid input data is uploaded");
    }
}
