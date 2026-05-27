package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;


public class BoundarySelectionPage {

    private Page page;

    // Campaign template step elements
    private Locator DefineTarget;
    private Locator firstBoundaryLevel;
    private Locator secondBoundaryLevel;
    private Locator thirdBoundaryLevel;
    private Locator fourthBoundaryLevel;
    private Locator outsideClick;
    private Locator firstCheckbox;
    private Locator thirdCheckbox;
    private Locator nextButton;
    private Locator submitButton;
    private Locator mandatoryFieldsToast;


    public BoundarySelectionPage(Page page) {

        this.page = page;
        this.DefineTarget = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Define Target Areas"));
        this.firstBoundaryLevel = page.getByRole(AriaRole.TEXTBOX).first();
        this.secondBoundaryLevel = page.getByRole(AriaRole.TEXTBOX).nth(1);
        this.thirdBoundaryLevel = page.getByRole(AriaRole.TEXTBOX).nth(2);
        this.fourthBoundaryLevel = page.getByRole(AriaRole.TEXTBOX).nth(3);
        this.outsideClick = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Country*"));
        this.firstCheckbox = page.getByRole(AriaRole.CHECKBOX).first();
        this.thirdCheckbox = page.getByRole(AriaRole.CHECKBOX).nth(2);
        this.nextButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next"));
        this.submitButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit"));
        this.mandatoryFieldsToast = page.getByText("Please fill all the mandatory fields.");
    }

    // --- Actions ---

    public void clickDefineTarget() {
        DefineTarget.click();
        page.waitForTimeout(1000);
    }

    public void clickfirstlevel() {
        firstBoundaryLevel.click();
        firstCheckbox.check();
        page.waitForTimeout(1000);
        outsideClick.click();
        page.waitForTimeout(1000);
    }

    public void clicksecondlevel() {
        secondBoundaryLevel.click();
        thirdCheckbox.check();
        page.waitForTimeout(1000);
        outsideClick.click();
        page.waitForTimeout(1000);
    }

    public void clickthirdlevel() {
        thirdBoundaryLevel.click();
        page.waitForTimeout(1000);
        thirdCheckbox.check();
        page.waitForTimeout(1000);
        outsideClick.click();
        page.waitForTimeout(1000);
    }

    public void clickfourthlevel() {
        fourthBoundaryLevel.click();
        page.waitForTimeout(1000);
        thirdCheckbox.check();
        page.waitForTimeout(1000);
        outsideClick.click();
        page.waitForTimeout(1000);
        nextButton.click();
        page.waitForTimeout(1000);
        submitButton.click();
        page.waitForTimeout(1000);
    }

    public void clickNextButton() {
        nextButton.click();
    }

    public boolean isMandatoryFieldsToastVisible() {
        mandatoryFieldsToast.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        return mandatoryFieldsToast.isVisible();
    }
}