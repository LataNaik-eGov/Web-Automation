package pages;

import java.util.regex.Pattern;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;


public class BoundarySelectionPage extends BasePage {

    // Campaign template step elements
    private Locator defineTarget;
    private Locator firstBoundaryLevel;
    private Locator secondBoundaryLevel;
    private Locator thirdBoundaryLevel;
    private Locator fourthBoundaryLevel;
    private Locator outsideClick;
    private Locator firstCheckbox;
    private Locator secondCheckbox;
    private Locator thirdCheckbox;
    private Locator nextButton;
    private Locator submitButton;
    private Locator mandatoryFieldsToast;


    public BoundarySelectionPage(Page page) {
        super(page);
        this.defineTarget = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Define Target Areas"));
        this.firstBoundaryLevel = page.getByRole(AriaRole.TEXTBOX).first();
        this.secondBoundaryLevel = page.getByRole(AriaRole.TEXTBOX).nth(1);
        this.thirdBoundaryLevel = page.getByRole(AriaRole.TEXTBOX).nth(2);
        this.fourthBoundaryLevel = page.getByRole(AriaRole.TEXTBOX).nth(3);
        this.outsideClick = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName(Pattern.compile("Country")));
        this.firstCheckbox = page.getByRole(AriaRole.CHECKBOX).first();
        this.secondCheckbox = page.getByRole(AriaRole.CHECKBOX).nth(2);
        this.thirdCheckbox = page.getByRole(AriaRole.CHECKBOX).nth(2);
        this.nextButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next"));
        this.submitButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit"));
        this.mandatoryFieldsToast = page.getByText("Please fill all the mandatory fields.");
    }

    // --- Actions ---

    public void clickDefineTarget() {
        defineTarget.click();
    }

    public void clickFirstLevel() {
        waitForVisible(firstBoundaryLevel);
        wait(3000);
        firstBoundaryLevel.click();
        waitForVisible(firstCheckbox);
        wait(3000);
        firstCheckbox.check();
        outsideClick.click();
    }

    public void clickSecondLevel() {
        waitForVisible(secondBoundaryLevel);
        wait(3000);
        secondBoundaryLevel.click();
        waitForVisible(secondCheckbox);
        wait(3000);
        secondCheckbox.check();
        outsideClick.click();
    }

    public void clickThirdLevel() {
        waitForVisible(thirdBoundaryLevel);
        wait(3000);
        thirdBoundaryLevel.click();
        waitForVisible(thirdCheckbox);
        wait(3000);
        thirdCheckbox.check();
        outsideClick.click();
    }

    public void clickFourthLevel() {
        waitForVisible(fourthBoundaryLevel);
        wait(3000);
        fourthBoundaryLevel.click();
        waitForVisible(thirdCheckbox);
        wait(3000);
        thirdCheckbox.check();
        outsideClick.click();
    }

    public void clickNextButton() {
        waitForVisible(nextButton);
        wait(3000);
        nextButton.click();
    }

    public void clickSubmitButton() {
        waitForVisible(submitButton);
        wait(3000);
        submitButton.click();
    }

    public boolean isMandatoryFieldsToastVisible() {
        mandatoryFieldsToast.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        return mandatoryFieldsToast.isVisible();
    }
}