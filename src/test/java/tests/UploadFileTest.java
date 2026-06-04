package tests;

import com.microsoft.playwright.Download;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.AppConfigurationPage;
import pages.BoundarySelectionPage;
import pages.CampaignLandingPage;
import pages.ConfigureDeliveryRulesPage;
import pages.DraftCampaignPage;
import pages.UploadFilePage;
import utils.ConfigReader;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class UploadFileTest extends BaseTest {

    private UploadFilePage setupUploadFilePage() {
        CampaignLandingPage landingPage = new CampaignLandingPage(page);
        landingPage.clickCreateCampaign();
        landingPage.clickScratchCard();
        landingPage.clickContinue();

        DraftCampaignPage draftPage = new DraftCampaignPage(page);
        draftPage.clickCampaignTypeDropdown();
        draftPage.selectCampaignType();
        draftPage.clickNext();

        draftPage.clearAndEnterDynamicCampaignName();
        draftPage.clickNext();

        draftPage.fillStartDate();
        draftPage.fillEndDate();
        draftPage.clickSubmit();

        BoundarySelectionPage boundaryPage = new BoundarySelectionPage(page);
        boundaryPage.clickDefineTarget();

        boundaryPage.clickfirstlevel();
        boundaryPage.clicksecondlevel();
        boundaryPage.clickthirdlevel();
        boundaryPage.clickfourthlevel();

        ConfigureDeliveryRulesPage deliveryRulesPage = new ConfigureDeliveryRulesPage(page);
        deliveryRulesPage.clickConfigureDelivery();

        deliveryRulesPage.fillDates();

        deliveryRulesPage.clickNext();

        deliveryRulesPage.clickNext();

        deliveryRulesPage.clickSubmit();

        AppConfigurationPage appConfigPage = new AppConfigurationPage(page);
        appConfigPage.clickSetUpMobileApp();

        appConfigPage.configureRegistrationAndDelivery();

        appConfigPage.configureCloseHousehold();

        appConfigPage.configureComplaints();

        appConfigPage.configureInventory();

        appConfigPage.configureStockReconciliation();

        appConfigPage.configureReports();

        appConfigPage.configurePermissionHandler();

        appConfigPage.clickGoBack();

        return new UploadFilePage(page);
    }

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifySubmitWithoutFile() {
        UploadFilePage uploadFilePage = setupUploadFilePage();

        uploadFilePage.clickUploadData();

        uploadFilePage.closePopupByClickingOutside();
        uploadFilePage.clickSubmit();

        Assert.assertTrue(uploadFilePage.isNoFileToastVisible(),
                "Toast 'Please upload a file' should appear when Submit is clicked without uploading a file");
    }

    @Test(groups = {"regression", "workbench-ui", "sanity"})
    public void verifyUploadFile() throws URISyntaxException {
        UploadFilePage uploadFilePage = setupUploadFilePage();

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
