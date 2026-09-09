package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

/**
 * "Select the boundaries where you want to run the campaign" step.
 * Two things about this screen drive the locator choices below:
 *
 * 1. The level fields are multi-select dropdowns whose inputs carry no name,
 *    id or placeholder. They are scoped by the wrapper class
 *    .selecting-boundaries-dropdown so a bare getByRole(TEXTBOX).nth(n) cannot
 *    drift onto some other textbox the screen grows later.
 *
 * 2. Only one dropdown is open at a time, and an open dropdown renders extra
 *    checkboxes that are NOT boundary options: level 2 and below prepend a
 *    "Select All" row plus two unclassed <input type=checkbox> elements. A
 *    plain getByRole(CHECKBOX).nth(n) therefore means a different thing on
 *    level 1 (no Select All) than on levels 2-6. Real options are the ones
 *    carrying .digit-multi-select-dropdown-menuitem, so that class is used to
 *    index options instead.
 */
public class BoundarySelectionPage extends BasePage {

    /** Wrapper around each boundary level's multi-select dropdown. */
    private static final String LEVEL_DROPDOWN = ".selecting-boundaries-dropdown";
    /** Panel rendered for whichever level dropdown is currently open. */
    private static final String OPEN_PANEL = ".digit-multiselectdropdown-server";
    /** A real boundary option inside the open panel (excludes "Select All"). */
    private static final String OPTION_CHECKBOX = "input.digit-multi-select-dropdown-menuitem";

    private Locator defineTargetButton;
    private Locator outsideClick;
    private Locator nextButton;
    private Locator submitButton;
    private Locator mandatoryFieldsToast;

    public BoundarySelectionPage(Page page) {
        super(page);
        this.defineTargetButton = page.locator(
                "#campaign-details-page-button-selecting-boundaries, "
                        + "#campaign-details-page-button-edit-selecting-boundaries").first();
        this.outsideClick = page.getByText("Select the boundaries where you want to run the campaign");
        this.nextButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next"));
        this.submitButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit"));
        this.mandatoryFieldsToast = page.getByText("Please fill all the mandatory fields.");
    }


    private Locator levelInput(int index) {
        return page.locator(LEVEL_DROPDOWN + " input").nth(index);
    }

    private Locator option(int index) {
        return page.locator(OPEN_PANEL + " " + OPTION_CHECKBOX).nth(index);
    }

    // --- Actions ---

    public void clickDefineTarget() {
        waitForVisible(defineTargetButton);
        wait(3000);
        defineTargetButton.click();
    }

    /**
     * Open the level at {@code levelIndex} (0-based), tick the option at
     * {@code optionIndex} (0-based, "Select All" excluded), then close the
     * dropdown by clicking the page heading.
     */
    public void selectLevelOption(int levelIndex, int optionIndex) {
        Locator input = levelInput(levelIndex);
        waitForVisible(input);
        wait(3000);
        input.click();
        Locator opt = option(optionIndex);
        waitForVisible(opt);
        wait(2000);
        opt.check();
        outsideClick.click();
        wait(2000);
    }

    public void clickFirstLevel()  { selectLevelOption(0, 0); }
    public void clickSecondLevel() { selectLevelOption(1, 0); }
    public void clickThirdLevel()  { selectLevelOption(2, 0); }
    public void clickFourthLevel() { selectLevelOption(3, 0); }
    public void clickFifthLevel()  { selectLevelOption(4, 0); }
    public void clickSixthLevel()  { selectLevelOption(5, 0); }

    
    public void clickSecondLevelWrong() {
        Locator input = levelInput(1);
        waitForVisible(input);
        wait(3000);
        input.click();
        Locator opts = page.locator(OPEN_PANEL + " " + OPTION_CHECKBOX);
        Locator last = opts.last();
        waitForVisible(last);
        wait(2000);
        last.check();
        outsideClick.click();
        wait(2000);
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

    // --- Verification ---

    public boolean isMandatoryFieldsToastVisible() {
        wait(500);
        waitForVisible(mandatoryFieldsToast);
        return mandatoryFieldsToast.isVisible();
    }
}
