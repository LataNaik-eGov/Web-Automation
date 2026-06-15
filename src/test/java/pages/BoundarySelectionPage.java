package pages;


import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;


public class BoundarySelectionPage extends BasePage {

    // Campaign template step elements
    private Locator defineTargetButton;
    private Locator firstBoundaryLevel;
    private Locator secondBoundaryLevel;
    private Locator thirdBoundaryLevel;
    private Locator fourthBoundaryLevel;
    private Locator outsideClick;
    private Locator firstCheckbox;
    private Locator secondCheckbox;
    private Locator secondCheckboxWrong;
    private Locator thirdCheckbox;
    private Locator nextButton;
    private Locator submitButton;
    private Locator mandatoryFieldsToast;


    public BoundarySelectionPage(Page page) {
        super(page);
        this.defineTargetButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Define Target Areas"));
        this.firstBoundaryLevel = page.getByRole(AriaRole.TEXTBOX).first();
        this.secondBoundaryLevel = page.getByRole(AriaRole.TEXTBOX).nth(1);
        this.thirdBoundaryLevel = page.getByRole(AriaRole.TEXTBOX).nth(2);
        this.fourthBoundaryLevel = page.getByRole(AriaRole.TEXTBOX).nth(3);
        this.outsideClick = page.getByText("Select the boundaries where you want to run the campaign");
        this.firstCheckbox = page.getByRole(AriaRole.CHECKBOX).first();
        this.secondCheckbox = page.getByRole(AriaRole.CHECKBOX).nth(2);
        this.secondCheckboxWrong = page.getByRole(AriaRole.CHECKBOX).nth(1);
        this.thirdCheckbox = page.getByRole(AriaRole.CHECKBOX).nth(2);
        this.nextButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next"));
        this.submitButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit"));
        this.mandatoryFieldsToast = page.getByText("Please fill all the mandatory fields.");
    }

    // --- Private helper ---

    private void clickBoundaryLevel(Locator level, Locator checkbox) {
        waitForVisible(level);
        level.click();
        waitForVisible(checkbox);
        checkbox.check();
        outsideClick.click();
    }

    // --- Actions ---

    public void clickDefineTarget() {
        defineTargetButton.click();
    }

    public void clickFirstLevel() {
        clickBoundaryLevel(firstBoundaryLevel, firstCheckbox);
    }

    public void clickSecondLevel() {
        clickBoundaryLevel(secondBoundaryLevel, secondCheckbox);
    }

    public void clickSecondLevelWrong() {
        clickBoundaryLevel(secondBoundaryLevel, secondCheckboxWrong);
    }

    public void clickThirdLevel() {
        clickBoundaryLevel(thirdBoundaryLevel, thirdCheckbox);
    }

    public void clickFourthLevel() {
        clickBoundaryLevel(fourthBoundaryLevel, thirdCheckbox);
    }

    public void clickNextButton() {
        waitForVisible(nextButton);
        nextButton.click();
    }

    public void clickSubmitButton() {
        waitForVisible(submitButton);
        submitButton.click();
    }

    public boolean isMandatoryFieldsToastVisible() {
        return waitAndCheckVisible(mandatoryFieldsToast, 5000);
    }
}
