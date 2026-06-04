package tests;

import com.microsoft.playwright.Download;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.AppConfigurationPage;
import pages.BoundarySelectionPage;
import pages.CampaignLandingPage;
import pages.ConfigureDeliveryRulesPage;
import pages.CreateChecklist;
import pages.DraftCampaignPage;
import pages.UploadFilePage;
import utils.ConfigReader;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class CreateChecklistTest extends BaseTest {

    private CreateChecklist setupCreateChecklistPage() throws URISyntaxException {
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

        UploadFilePage uploadFilePage = new UploadFilePage(page);
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

        uploadFilePage.closePopupByClickingOutside();
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
