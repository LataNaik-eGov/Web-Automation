package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CreateChecklist extends BasePage {

    // Create checklist
    private Locator createChecklist;
    private Locator configureList;
    private Locator configureChecklist;
    private Locator confirmChecklist;
    private Locator backToHomepage;
    private Locator createCampaign;
    private Locator goMyCampaign;

    public CreateChecklist(Page page) {
        super(page);
        this.createChecklist = page.locator("#campaign-details-page-button-checklist");
        this.configureList = page.locator(".digit-button-secondary.medium").first();
        this.configureChecklist = page.locator("#campaign-checklist-create-standalone-form-field-primary");
        this.confirmChecklist = page.locator(".digit-button-primary.large");
        this.backToHomepage = page.locator(".digit-button-primary.large");
        this.createCampaign = page.locator("#campaign-details-page-final-save-campaign");
         this.goMyCampaign = page.locator(".digit-button-primary.large");
    }

    // --- Actions ---

    public void clickCreateChecklist() {
        createChecklist.click();
    }

    public void clickConfigureList() {
        configureList.click();
    }

    public void clickConfigureChecklist() {
        configureChecklist.click();
    }

    public void clickConfirmChecklist() {
        confirmChecklist.click();
    }

    public void clickBackToHomepage() {
        backToHomepage.click();
    }

    public void clickCreateCampaign() {
        page.mouse().wheel(0, 500);
        createCampaign.click();
        goMyCampaign.click();
    }
}
