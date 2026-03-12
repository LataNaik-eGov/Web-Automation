package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;


public class BoundarySelectionPage {

    private Page page;

    // Campaign template step elements
    private Locator DefineTarget;
    private Locator firstlevel;
    private Locator secondlevel;
    private Locator thirdlevel;
    private Locator fourthlevel;
    private Locator firstoption;
    private Locator outside;
    private Locator firstCheckbox;
    private Locator thirdCheckbox;
    private Locator nextButton;


    public BoundarySelectionPage(Page page) {

        this.page = page;
        this.DefineTarget = page.locator("#campaign-details-page-button-selecting-boundaries");
        this.firstlevel = page.locator(".selecting-boundaries-dropdown input").nth(0);
        this.secondlevel = page.locator(".selecting-boundaries-dropdown input").nth(1);
        this.thirdlevel = page.locator(".selecting-boundaries-dropdown input").nth(2);
        this.fourthlevel = page.locator(".selecting-boundaries-dropdown input").nth(3);
        this.firstoption = page.locator(".digit-multi-select-dropdown-menuitem");
        this.outside=page.locator(".dates-description");
        this.firstCheckbox = page.getByRole(AriaRole.CHECKBOX);
        this.thirdCheckbox = page.getByRole(AriaRole.CHECKBOX).nth(2);
        this.nextButton = page.locator("#campaign-setup-campaign-standalone-setup-campaign-field-primary");
    }

    // --- Actions ---

    public void clickDefineTarget() {
        DefineTarget.click();
        page.waitForTimeout(1000);
    }

     public void clickfirstlevel() {
    
        // firstlevel.first().click();
        firstlevel.click();
        firstoption.click();
        page.waitForTimeout(1000);
        outside.click();
        page.waitForTimeout(1000);
    }

    public void clicksecondlevel() {
     
        secondlevel.click();
        thirdCheckbox.click();
        page.waitForTimeout(1000);
        outside.click();
        page.waitForTimeout(1000);
    }

    public void clickthirdlevel() {
   
        thirdlevel.click();
        page.waitForTimeout(1000);
        thirdCheckbox.click();
        page.waitForTimeout(1000);
        outside.click();
        page.waitForTimeout(1000);
    }

    public void clickfourthlevel() {

        fourthlevel.click();
        page.waitForTimeout(1000);
        thirdCheckbox.click();
        page.waitForTimeout(1000);
        outside.click();
        page.waitForTimeout(1000);
        nextButton.click();
        page.waitForTimeout(1000);
        nextButton.click();
        page.waitForTimeout(1000);
    }
}