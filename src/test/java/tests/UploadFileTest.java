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
        page.waitForLoadState();
        landingPage.clickScratchCard();
        landingPage.clickContinue();
        page.waitForLoadState();
        page.waitForTimeout(4000);

        DraftCampaignPage draftPage = new DraftCampaignPage(page);
        draftPage.clickCampaignTypeDropdown();
        draftPage.selectCampaignType();
        draftPage.clickNext();
        page.waitForLoadState();
        page.waitForTimeout(3000);

        draftPage.clearAndEnterDynamicCampaignName();
        draftPage.clickNext();
        page.waitForLoadState();
        page.waitForTimeout(20000);

        draftPage.fillStartDate();
        page.waitForTimeout(1000);
        draftPage.fillEndDate();
        page.waitForTimeout(1000);
        draftPage.clickSubmit();
        page.waitForLoadState();
        page.waitForTimeout(4000);

        BoundarySelectionPage boundaryPage = new BoundarySelectionPage(page);
        boundaryPage.clickDefineTarget();
        page.waitForLoadState();
        page.waitForTimeout(3000);

        boundaryPage.clickfirstlevel();
        page.waitForTimeout(3000);
        boundaryPage.clicksecondlevel();
        page.waitForTimeout(3000);
        boundaryPage.clickthirdlevel();
        page.waitForTimeout(3000);
        boundaryPage.clickfourthlevel();
        page.waitForLoadState();
        page.waitForTimeout(3000);

        ConfigureDeliveryRulesPage deliveryRulesPage = new ConfigureDeliveryRulesPage(page);
        deliveryRulesPage.clickConfigureDelivery();
        page.waitForLoadState();
        page.waitForTimeout(3000);

        deliveryRulesPage.fillDates();
        page.waitForTimeout(1000);

        deliveryRulesPage.clickNext();
        page.waitForTimeout(3000);

        deliveryRulesPage.clickNext();
        page.waitForTimeout(3000);

        deliveryRulesPage.clickSubmit();
        page.waitForTimeout(3000);

        AppConfigurationPage appConfigPage = new AppConfigurationPage(page);
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
        page.waitForLoadState();
        page.waitForTimeout(3000);

        return new UploadFilePage(page);
    }

    @Test(groups = {"negative", "regression", "workbench-ui"})
    public void verifySubmitWithoutFile() {
        UploadFilePage uploadFilePage = setupUploadFilePage();

        uploadFilePage.clickUploadData();
        page.waitForLoadState();
        page.waitForTimeout(2000);

        uploadFilePage.closePopupByClickingOutside();
        uploadFilePage.clickSubmit();

        Assert.assertTrue(uploadFilePage.isNoFileToastVisible(),
                "Toast 'Please upload a file' should appear when Submit is clicked without uploading a file");
    }

    @Test(groups = {"regression", "workbench-ui", "sanity"})
    public void verifyUploadFile() throws URISyntaxException {
        UploadFilePage uploadFilePage = setupUploadFilePage();

        uploadFilePage.clickUploadData();
        page.waitForLoadState();
        page.waitForTimeout(2000);
        uploadFilePage.closePopupByClickingOutside();

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

        uploadFilePage.closePopupByClickingOutside();
        uploadFilePage.clickSubmit();
    }
}
