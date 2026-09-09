package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class CampaignLandingPage extends BasePage {

    private Locator createCampaignLink;
    private Locator scratchCard;
    private Locator continueButton;

    public CampaignLandingPage(Page page) {
        super(page);
        this.createCampaignLink = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Create campaign"));
        this.scratchCard = page.locator(".digit-campaign-home-card")
                .filter(new Locator.FilterOptions().setHasText("Create a new campaign from scratch"));
        this.continueButton = page.locator(
                "#campaign-campaign-home-standalone-create-new-campaign-from-scratch-btn, button[aria-label='Continue']")
                .first();
    }

    // --- Actions ---

    public void clickCreateCampaign() {
        waitForVisible(createCampaignLink);
        createCampaignLink.click();
    }

    public void clickScratchCard() {
        waitForVisible(scratchCard);
        wait(2000);
        scratchCard.click();
    }

 
    public void clickContinue() {
        waitForVisible(continueButton);
        wait(1000);
        continueButton.click();
        waitForOverlayToHide();
    }
}
