package tests;

import com.microsoft.playwright.Download;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.UploadFilePage;
import utils.TestDataReader;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class UploadFileTest extends BaseTest {

    // ==================== Upload File Flow ====================

    @Test(groups = { "workbench-ui", "sanity"})
    public void verifyUploadFile_BEDNET() throws URISyntaxException {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        verifyUploadFile();
    }

    @Test(groups = { "workbench-ui", "sanity"})
    public void verifyUploadFile_MR_DN() throws URISyntaxException {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        verifyUploadFile();
    }

    private void verifyUploadFile() throws URISyntaxException {
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

    // ==================== Negative Tests ====================

    @Test(groups = {"negative", "workbench-ui"})
    public void verifySubmitWithoutFile_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        verifySubmitWithoutFile();
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifySubmitWithoutFile_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        verifySubmitWithoutFile();
    }

    private void verifySubmitWithoutFile() {
        UploadFilePage uploadFilePage = nav.goToUploadFile();

        uploadFilePage.clickUploadData();
        uploadFilePage.closePopup();
        uploadFilePage.clickSubmit();

        Assert.assertTrue(uploadFilePage.isNoFileToastVisible(),
                "Toast 'Please upload a file' should appear when Submit is clicked without uploading a file");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyUploadInvalidFileType_BEDNET() throws URISyntaxException {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        verifyUploadInvalidFileType();
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyUploadInvalidFileType_MR_DN() throws URISyntaxException {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        verifyUploadInvalidFileType();
    }

    private void verifyUploadInvalidFileType() throws URISyntaxException {
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

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyUploadInvalidExcelFile_BEDNET() throws URISyntaxException {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        verifyUploadInvalidExcelFile();
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyUploadInvalidExcelFile_MR_DN() throws URISyntaxException {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        verifyUploadInvalidExcelFile();
    }

    private void verifyUploadInvalidExcelFile() throws URISyntaxException {
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

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyWithInvalidInputInFile_BEDNET() throws URISyntaxException {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        verifyWithInvalidInputInFile();
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyWithInvalidInputInFile_MR_DN() throws URISyntaxException {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        verifyWithInvalidInputInFile();
    }

    private void verifyWithInvalidInputInFile() throws URISyntaxException {
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
