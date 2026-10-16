package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class CampaignLandingPage extends BasePage {

    private Locator createCampaignLink;
    private Locator scratchCardTitle;
    private Locator continueButton;

    public CampaignLandingPage(Page page) {
        super(page);
        this.createCampaignLink = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Create campaign"));
        this.scratchCardTitle = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Create a New Campaign from"));
        this.continueButton = page.locator("button[aria-label='Proceed']");
    }

    // --- Actions ---

    public void clickCreateCampaign() {
        createCampaignLink.click();
    }

    public void clickScratchCard() {
        scratchCardTitle.click();
    }

    public void clickContinue() {
        continueButton.click();
    }
}