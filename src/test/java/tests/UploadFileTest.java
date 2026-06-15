package tests;

import com.microsoft.playwright.Download;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.UploadFilePage;
import utils.TestDataReader;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class UploadFileTest extends BaseTest {

    @DataProvider(name = "campaignTypes")
    public Object[][] campaignTypes() {
        return new Object[][]{{"BEDNET"}, {"MR-DN"}};
    }

    @Test(dataProvider = "campaignTypes", groups = { "workbench-ui", "sanity"})
    public void verifyUploadFile(String campaignType) throws URISyntaxException {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", campaignType);
        UploadFilePage uploadFilePage = nav.goToUploadFile();

        uploadFilePage.clickUploadData();

        uploadFilePage.closePopup();

        Download download = uploadFilePage.downloadTemplate();
        Assert.assertNotNull(download, "Template download should have started");

        String templateFile = TestDataReader.getTemplateFileName();
        URL resource = getClass().getClassLoader().getResource(templateFile);
        Assert.assertNotNull(resource, templateFile + " should exist in test resources");
        String filePath = Paths.get(resource.toURI()).toString();

        uploadFilePage.uploadFile(filePath);

        uploadFilePage.waitForUploadSuccessToast();

        uploadFilePage.clickSubmit();
    }

    // Negative tests

    @Test(dataProvider = "campaignTypes", groups = {"negative", "workbench-ui"})
    public void verifySubmitWithoutFile(String campaignType) {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", campaignType);
        UploadFilePage uploadFilePage = nav.goToUploadFile();

        uploadFilePage.clickUploadData();

        uploadFilePage.closePopup();
        uploadFilePage.clickSubmit();

        Assert.assertTrue(uploadFilePage.isNoFileToastVisible(),
                "Toast 'Please upload a file' should appear when Submit is clicked without uploading a file");
    }

    @Test(dataProvider = "campaignTypes", groups = {"negative", "workbench-ui"})
    public void verifyUploadInvalidFileType(String campaignType) throws URISyntaxException {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", campaignType);
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

    @Test(dataProvider = "campaignTypes", groups = {"negative", "workbench-ui"})
    public void verifyUploadInvalidExcelFile(String campaignType) throws URISyntaxException {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", campaignType);
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

    @Test(dataProvider = "campaignTypes", groups = {"negative", "workbench-ui"})
    public void verifyWithInvalidInputInFile(String campaignType) throws URISyntaxException {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", campaignType);
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
