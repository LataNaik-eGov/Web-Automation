package tests;

import com.microsoft.playwright.Download;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.UploadFilePage;
import utils.TestDataReader;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UploadFileTest extends BaseTest {

    @Test(groups = { "workbench-ui", "sanity"})
    public void verifyUploadFile_BEDNET() throws URISyntaxException {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        UploadFilePage uploadFilePage = nav.goToUploadFile();

        uploadFilePage.clickUploadData();

        uploadFilePage.closePopup();

        // Round-trips the campaign's own generated template: download, fill the
        // mandatory target/user data, upload. A committed template cannot be used
        // because the workbook is campaign-specific.
        Path filled = uploadFilePage.downloadFillAndUploadTemplate();
        Assert.assertTrue(Files.exists(filled),
                "Filled template should have been written to " + filled);

        uploadFilePage.waitForUploadSuccess();

        uploadFilePage.clickSubmit();
    }

    @Test(groups = { "workbench-ui", "sanity"})
    public void verifyUploadFile_MR_DN() throws URISyntaxException {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        UploadFilePage uploadFilePage = nav.goToUploadFile();

        uploadFilePage.clickUploadData();

        uploadFilePage.closePopup();

        // Round-trips the campaign's own generated template: download, fill the
        // mandatory target/user data, upload. A committed template cannot be used
        // because the workbook is campaign-specific.
        Path filled = uploadFilePage.downloadFillAndUploadTemplate();
        Assert.assertTrue(Files.exists(filled),
                "Filled template should have been written to " + filled);

        uploadFilePage.waitForUploadSuccess();

        uploadFilePage.clickSubmit();
    }

    // Negative tests

    @Test(groups = {"negative", "workbench-ui"})
    public void verifySubmitWithoutFile_BEDNET() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        UploadFilePage uploadFilePage = nav.goToUploadFile();

        uploadFilePage.clickUploadData();

        uploadFilePage.closePopup();
        uploadFilePage.clickSubmit();

        Assert.assertTrue(uploadFilePage.isNoFileToastVisible(),
                "Toast 'Please upload a file' should appear when Submit is clicked without uploading a file");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifySubmitWithoutFile_MR_DN() {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
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
        UploadFilePage uploadFilePage = nav.goToUploadFile();

        uploadFilePage.clickUploadData();

        uploadFilePage.closePopup();

        URL resource = getClass().getClassLoader().getResource("complaint.pdf");
        Assert.assertNotNull(resource, "complaint.pdf should exist in test resources");
        String filePath = Paths.get(resource.toURI()).toString();

        uploadFilePage.uploadFile(filePath);

        uploadFilePage.clickSubmit();

        Assert.assertTrue(uploadFilePage.isFileRejectedToastVisible(),
                "Rejection toast should appear when an invalid file type (PDF) is uploaded");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyUploadInvalidFileType_MR_DN() throws URISyntaxException {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        UploadFilePage uploadFilePage = nav.goToUploadFile();

        uploadFilePage.clickUploadData();

        uploadFilePage.closePopup();

        URL resource = getClass().getClassLoader().getResource("complaint.pdf");
        Assert.assertNotNull(resource, "complaint.pdf should exist in test resources");
        String filePath = Paths.get(resource.toURI()).toString();

        uploadFilePage.uploadFile(filePath);

        uploadFilePage.clickSubmit();

        Assert.assertTrue(uploadFilePage.isFileRejectedToastVisible(),
                "Rejection toast should appear when an invalid file type (PDF) is uploaded");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyUploadInvalidExcelFile_BEDNET() throws URISyntaxException {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        UploadFilePage uploadFilePage = nav.goToUploadFile();

        uploadFilePage.clickUploadData();

        uploadFilePage.closePopup();

        URL resource = getClass().getClassLoader().getResource("InvalidFile.xlsx");
        Assert.assertNotNull(resource, "InvalidFile.xlsx should exist in test resources");
        String filePath = Paths.get(resource.toURI()).toString();

        uploadFilePage.uploadFile(filePath);

        uploadFilePage.clickSubmit();

        Assert.assertTrue(uploadFilePage.isValidationErrorCardVisible(),
                "Error toast should appear when an invalid Excel file is uploaded");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyUploadInvalidExcelFile_MR_DN() throws URISyntaxException {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        UploadFilePage uploadFilePage = nav.goToUploadFile();

        uploadFilePage.clickUploadData();

        uploadFilePage.closePopup();

        URL resource = getClass().getClassLoader().getResource("InvalidFile.xlsx");
        Assert.assertNotNull(resource, "InvalidFile.xlsx should exist in test resources");
        String filePath = Paths.get(resource.toURI()).toString();

        uploadFilePage.uploadFile(filePath);

        uploadFilePage.clickSubmit();

        Assert.assertTrue(uploadFilePage.isValidationErrorCardVisible(),
                "Error toast should appear when an invalid Excel file is uploaded");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyWithInvalidInputInFile_BEDNET() throws URISyntaxException {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "BEDNET");
        UploadFilePage uploadFilePage = nav.goToUploadFile();

        uploadFilePage.clickUploadData();

        uploadFilePage.closePopup();

        URL resource = getClass().getClassLoader().getResource("InvalidInputFile.xlsx");
        Assert.assertNotNull(resource, "InvalidInputFile.xlsx should exist in test resources");
        String filePath = Paths.get(resource.toURI()).toString();

        uploadFilePage.uploadFile(filePath);

        uploadFilePage.clickSubmit();

        Assert.assertTrue(uploadFilePage.isValidationErrorCardVisible(),
                "Error toast should appear when a file with invalid input data is uploaded");
    }

    @Test(groups = {"negative", "workbench-ui"})
    public void verifyWithInvalidInputInFile_MR_DN() throws URISyntaxException {
        TestDataReader.setSessionValue("CAMPAIGN_TYPE", "MR-DN");
        UploadFilePage uploadFilePage = nav.goToUploadFile();

        uploadFilePage.clickUploadData();

        uploadFilePage.closePopup();

        URL resource = getClass().getClassLoader().getResource("InvalidInputFile.xlsx");
        Assert.assertNotNull(resource, "InvalidInputFile.xlsx should exist in test resources");
        String filePath = Paths.get(resource.toURI()).toString();

        uploadFilePage.uploadFile(filePath);

        uploadFilePage.clickSubmit();

        Assert.assertTrue(uploadFilePage.isValidationErrorCardVisible(),
                "Error toast should appear when a file with invalid input data is uploaded");
    }
}
