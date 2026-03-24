package utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * FormHelper - Easy-to-use utility for form interactions in UI tests.
 *
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║ QUICK START GUIDE ║
 * ╠══════════════════════════════════════════════════════════════════════════════╣
 * ║ FormHelper form = new FormHelper(page); ║
 * ║ ║
 * ║ // Text Input ║
 * ║ form.enterText("#email", "test@example.com"); ║
 * ║ ║
 * ║ // Dropdown (HTML <select>) ║
 * ║ form.selectByText("#country", "India"); ║
 * ║ ║
 * ║ // Custom Dropdown (React/Angular) ║
 * ║ form.clickAndSelect("#dropdown-btn", "text=Option 1"); ║
 * ║ ║
 * ║ // Checkbox & Radio ║
 * ║ form.check("#agree-checkbox"); ║
 * ║ form.click("#male-radio"); ║
 * ║ ║
 * ║ // File Upload ║
 * ║ form.uploadFile("#file-input", "/path/to/file.pdf"); ║
 * ║ ║
 * ║ // Date Picker ║
 * ║ form.enterText("#date", "2024-01-15"); ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 */
public class FormHelper {

    private final Page page;

    /**
     * Create a new FormHelper instance.
     *
     * @param page Playwright Page object from your test
     *
     *             Example:
     *             Page page = browser.newPage();
     *             FormHelper form = new FormHelper(page);
     */
    public FormHelper(Page page) {
        this.page = page;
    }

    // ============================================================
    // TEXT INPUT
    // ============================================================

    /**
     * Enter text into any input field. Clears existing text first.
     *
     * @param selector CSS selector (e.g., "#email", ".username",
     *                 "input[name='phone']")
     * @param text     Text to enter
     *
     *                 Example:
     *                 form.enterText("#email", "john@example.com");
     *                 form.enterText("input[name='password']", "secret123");
     */
    public void enterText(String selector, String text) {
        page.locator(selector).fill(text);
    }

    /**
     * Type text character by character. Use for fields with auto-complete.
     *
     * @param selector CSS selector for the input field
     * @param text     Text to type
     *
     *                 Example:
     *                 form.typeSlowly("#search", "laptop"); // Triggers
     *                 autocomplete suggestions
     */
    public void typeSlowly(String selector, String text) {
        page.locator(selector).click();
        page.locator(selector).pressSequentially(text);
    }

    /**
     * Clear text from an input field.
     *
     * @param selector CSS selector for the input field
     */
    public void clearText(String selector) {
        page.locator(selector).clear();
    }

    // ============================================================
    // DROPDOWN (HTML <select>)
    // ============================================================

    /**
     * Select dropdown option by visible text.
     *
     * @param selector CSS selector for <select> element
     * @param text     Visible text of the option
     *
     *                 Example:
     *                 form.selectByText("#country", "India");
     *                 form.selectByText("#state", "Karnataka");
     */
    public void selectByText(String selector, String text) {
        page.locator(selector).selectOption(new SelectOption().setLabel(text));
    }

    /**
     * Select dropdown option by value attribute.
     *
     * @param selector CSS selector for <select> element
     * @param value    Value attribute of the option
     *
     *                 Example:
     *                 HTML: <option value="IN">India</option>
     *                 form.selectByValue("#country", "IN");
     */
    public void selectByValue(String selector, String value) {
        page.locator(selector).selectOption(value);
    }

    /**
     * Select dropdown option by position (starting from 0).
     *
     * @param selector CSS selector for <select> element
     * @param index    Position of option (0 = first option)
     *
     *                 Example:
     *                 form.selectByIndex("#country", 0); // Selects first option
     */
    public void selectByIndex(String selector, int index) {
        page.locator(selector).selectOption(new SelectOption().setIndex(index));
    }

    // ============================================================
    // CUSTOM DROPDOWN (React/Angular/Vue)
    // ============================================================

    /**
     * Select from custom dropdown: clicks to open, then clicks the option.
     *
     * @param dropdownSelector Selector for the dropdown button/trigger
     * @param optionSelector   Selector for the option to select
     *
     *                         Example:
     *                         form.clickAndSelect("#country-dropdown",
     *                         "text=India");
     *                         form.clickAndSelect(".dropdown-toggle",
     *                         "li:has-text('Option 1')");
     */
    public void clickAndSelect(String dropdownSelector, String optionSelector) {
        page.locator(dropdownSelector).click();
        page.locator(optionSelector).click();
    }

    /**
     * Select multiple options from custom dropdown.
     *
     * @param dropdownSelector Selector for the dropdown button/trigger
     * @param optionTexts      List of option texts to select
     *
     *                         Example:
     *                         form.selectMultiple("#tags-dropdown", List.of("Java",
     *                         "Python", "Go"));
     */
    public void selectMultiple(String dropdownSelector, List<String> optionTexts) {
        page.locator(dropdownSelector).click();
        for (String text : optionTexts) {
            page.locator("text=" + text).click();
        }
        page.keyboard().press("Escape"); // Close dropdown
    }

    // ============================================================
    // CHECKBOX
    // ============================================================

    /**
     * Check a checkbox (does nothing if already checked).
     *
     * @param selector CSS selector for the checkbox
     *
     *                 Example:
     *                 form.check("#terms-checkbox");
     *                 form.check("input[name='newsletter']");
     */
    public void check(String selector) {
        page.locator(selector).check();
    }

    /**
     * Uncheck a checkbox (does nothing if already unchecked).
     *
     * @param selector CSS selector for the checkbox
     */
    public void uncheck(String selector) {
        page.locator(selector).uncheck();
    }

    /**
     * Check if a checkbox is currently checked.
     *
     * @param selector CSS selector for the checkbox
     * @return true if checked, false otherwise
     */
    public boolean isChecked(String selector) {
        return page.locator(selector).isChecked();
    }

    // ============================================================
    // RADIO BUTTON
    // ============================================================

    /**
     * Select a radio button by clicking it.
     *
     * @param selector CSS selector for the radio button
     *
     *                 Example:
     *                 form.click("#male");
     *                 form.click("input[name='gender'][value='male']");
     */
    // Note: Use the click() method for radio buttons

    /**
     * Select radio button by its label text.
     *
     * @param labelText Text shown next to the radio button
     *
     *                  Example:
     *                  form.selectRadioByLabel("Male");
     *                  form.selectRadioByLabel("Credit Card");
     */
    public void selectRadioByLabel(String labelText) {
        page.locator("label:has-text('" + labelText + "')").click();
    }

    // ============================================================
    // FILE UPLOAD
    // ============================================================

    /**
     * Upload a file to a file input element.
     *
     * @param selector CSS selector for the file input (<input type="file">)
     * @param filePath Full path to the file
     *
     *                 Example:
     *                 form.uploadFile("#resume", "/home/user/resume.pdf");
     *                 form.uploadFile("input[type='file']", "C:\\docs\\photo.jpg");
     */
    public void uploadFile(String selector, String filePath) {
        page.locator(selector).setInputFiles(Paths.get(filePath));
    }

    /**
     * Upload multiple files at once.
     *
     * @param selector  CSS selector for the file input
     * @param filePaths List of file paths
     *
     *                  Example:
     *                  form.uploadFiles("#photos", List.of("/path/photo1.jpg",
     *                  "/path/photo2.jpg"));
     */
    public void uploadFiles(String selector, List<String> filePaths) {
        Path[] paths = filePaths.stream().map(Paths::get).toArray(Path[]::new);
        page.locator(selector).setInputFiles(paths);
    }

    /**
     * Clear uploaded files from file input.
     *
     * @param selector CSS selector for the file input
     */
    public void clearFiles(String selector) {
        page.locator(selector).setInputFiles(new Path[0]);
    }

    // ============================================================
    // COMMON ACTIONS
    // ============================================================

    /**
     * Click any element (button, link, radio, etc.).
     *
     * @param selector CSS selector for the element
     *
     *                 Example:
     *                 form.click("#submit-btn");
     *                 form.click("button:has-text('Save')");
     */
    public void click(String selector) {
        page.locator(selector).click();
    }

    /**
     * Get text content of any element.
     *
     * @param selector CSS selector for the element
     * @return Text content
     *
     *         Example:
     *         String error = form.getText(".error-message");
     */
    public String getText(String selector) {
        return page.locator(selector).textContent();
    }

    /**
     * Get current value of an input field.
     *
     * @param selector CSS selector for the input
     * @return Current input value
     *
     *         Example:
     *         String email = form.getValue("#email");
     */
    public String getValue(String selector) {
        return page.locator(selector).inputValue();
    }

    /**
     * Check if an element is visible on the page.
     *
     * @param selector CSS selector for the element
     * @return true if visible
     */
    public boolean isVisible(String selector) {
        return page.locator(selector).isVisible();
    }

    /**
     * Check if an element is enabled (not disabled).
     *
     * @param selector CSS selector for the element
     * @return true if enabled
     */
    public boolean isEnabled(String selector) {
        return page.locator(selector).isEnabled();
    }

    /**
     * Wait for an element to appear on the page.
     *
     * @param selector  CSS selector for the element
     * @param timeoutMs Maximum time to wait in milliseconds
     *
     *                  Example:
     *                  form.waitFor("#success-message", 5000); // Wait up to 5
     *                  seconds
     */
    public void waitFor(String selector, int timeoutMs) {
        page.locator(selector).waitFor(new Locator.WaitForOptions().setTimeout(timeoutMs));
    }

    /**
     * Wait for element with default 30 second timeout.
     *
     * @param selector CSS selector for the element
     */
    public void waitFor(String selector) {
        waitFor(selector, 30000);
    }

    /**
     * Press a keyboard key.
     *
     * @param key Key name (e.g., "Enter", "Tab", "Escape", "ArrowDown")
     *
     *            Example:
     *            form.pressKey("Enter"); // Submit form
     *            form.pressKey("Escape"); // Close modal
     */
    public void pressKey(String key) {
        page.keyboard().press(key);
    }

    /**
     * Submit a form by pressing Enter on an input field.
     *
     * @param selector CSS selector for the input field
     *
     *                 Example:
     *                 form.submitWithEnter("#search-box");
     */
    public void submitWithEnter(String selector) {
        page.locator(selector).press("Enter");
    }

    // Fill with force — bypasses overlays
    public void fillForce(String selector, String value) {
        page.locator(selector).first()
                .fill(value, new Locator.FillOptions().setForce(true));
        System.out.println("[FormHelper] Force filled: " + selector + " = " + value);
    }

    // Fill nth element with force
    public void fillForceNth(String selector, int index, String value) {
        page.locator(selector).nth(index)
                .fill(value, new Locator.FillOptions().setForce(true));
        System.out.println("[FormHelper] Force filled nth(" + index + "): " + selector);
    }

    // Fill any text input safely
    public void fill(String selector, String value) {
        page.locator(selector).first().waitFor();
        page.locator(selector).first().fill(value);
        System.out.println("[FormHelper] Filled: " + selector + " = " + value);
    }

    // Open dropdown by clicking SVG and select option by index
    public void selectDropdown(int svgIndex, int optionIndex) {
        page.locator("div.select").nth(svgIndex).scrollIntoViewIfNeeded();

        // Click the SVG arrow to open
        Locator svg = page.locator("div.select svg.cp");
        if (svg.count() > svgIndex) {
            svg.nth(svgIndex).dispatchEvent("click");
        } else {
            page.locator("div.select").nth(svgIndex).dispatchEvent("click");
        }

        // Wait for dropdown options to appear
        page.locator(".profile-dropdown--item").first()
                .waitFor(new Locator.WaitForOptions().setTimeout(10000));

        // Select option
        int count = page.locator(".profile-dropdown--item").count();
        System.out.println("[FormHelper] Dropdown options visible: " + count);
        if (count > optionIndex) {
            page.locator(".profile-dropdown--item").nth(optionIndex).dispatchEvent("click");
            System.out.println("[FormHelper] Selected option index: " + optionIndex);
        }

        // Wait for dropdown to close before pressing Escape
        page.locator(".profile-dropdown--item").first()
                .waitFor(new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.HIDDEN)
                        .setTimeout(5000));
        page.keyboard().press("Escape");
    }

    // Click using dispatchEvent — works on SVG and React elements
    public void clickDispatch(String selector) {
        page.locator(selector).first().dispatchEvent("click");
        System.out.println("[FormHelper] Dispatch clicked: " + selector);
    }

    // Click nth using dispatchEvent
    public void clickDispatchNth(String selector, int index) {
        page.locator(selector).nth(index).dispatchEvent("click");
        System.out.println("[FormHelper] Dispatch clicked nth(" + index + "): " + selector);
    }

    // Scroll to element
    public void scrollTo(String selector) {
        page.locator(selector).first().scrollIntoViewIfNeeded();
    }
}
