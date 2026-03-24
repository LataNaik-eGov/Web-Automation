package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.FormHelper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EmployeeInboxPage extends BasePage {

    private final FormHelper form;

    // --- Search ---
    private final String searchInput = "input[name='codes']";
    private final String searchBtn = "button.submit-bar-search[type='submit']";

    // --- Action menu ---
    private final String takeActionBtn = "button.submit-bar[type='button']";
    private final String menuItem = "div.menu-wrap p";

    // --- Edit Employee ---
    private final String employeeName = "input[pattern*='1,50'][title*='Username']";
    private final String saveBtn = "div.action-bar-wrap button[type='submit']";

    // --- Edit Campaign Assignment ---
    private final String campaignDateInput = "input.employee-card-input[type='date'][min]";

    // --- Deactivate ---
    private final String reasonDropdownSvg = "div.select svg.cp";
    private final String deactivateConfirm = "button.selector-button-primary[type='submit']";

    // --- Success & Back ---
    private final String successMessage = "div.emp-success-wrap header";
    private final String goBackToHomeBtn = "div.emp-success-wrap button";

    public EmployeeInboxPage(Page page) {
        super(page);
        this.form = new FormHelper(page);
    }

    // ---------------------------------------------------------------
    // Shared: search by id, open first result,
    // open Take Action menu, click the named menu item
    // ---------------------------------------------------------------

    public EmployeeInboxPage searchEmployee(String empId) {
        form.waitFor(searchInput);
        form.fill(searchInput, empId);
        page.waitForTimeout(500);
        form.clickDispatch(searchBtn);
        page.waitForTimeout(2000);
        System.out.println("[Search] Searched for employee: " + empId);
        return this;
    }

    public EmployeeInboxPage openEmployeeResult() {
        page.waitForTimeout(2000);
        System.out.println("[Search] Page URL: " + page.url());

        // The Employee ID in the results table is a link in the first column.
        // Try selectors from most specific to broadest.
        String[] selectors = {
                "table tbody tr td:first-child a",
                "tbody tr td a",
                "tbody a"
        };

        for (String sel : selectors) {
            int count = page.locator(sel).count();
            System.out.println("[Search] Selector '" + sel + "' count: " + count);
            if (count > 0) {
                page.locator(sel).first().dispatchEvent("click");
                page.waitForTimeout(2000);
                System.out.println("[Search] Opened employee result using: " + sel);
                return this;
            }
        }

        throw new RuntimeException("[Search] No employee result link found in results table");
    }

    public EmployeeInboxPage openTakeActionMenu() {
        form.waitFor(takeActionBtn);
        form.clickDispatch(takeActionBtn);
        page.waitForTimeout(1000);
        System.out.println("[Search] Opened Take Action menu");
        return this;
    }

    private void clickMenuItemByText(String menuText) {
        page.locator(menuItem + ":has-text('" + menuText + "')")
                .first().dispatchEvent("click");
        page.waitForTimeout(2000);
        System.out.println("[Search] Clicked menu: " + menuText);
    }

    // ---------------------------------------------------------------
    // Test Case 1 — Edit Employee
    // ---------------------------------------------------------------

    public EmployeeInboxPage clickEditEmployee() {
        clickMenuItemByText("Edit Employee");
        return this;
    }

    public EmployeeInboxPage editEmployeeName() {
        form.waitFor(employeeName);
        String currentName = page.locator(employeeName).first().inputValue();
        String updatedName = currentName + " one";
        page.locator(employeeName).first().fill(updatedName);
        page.waitForTimeout(500);
        System.out.println("[Search] Updated employee name: " + currentName + " → " + updatedName);
        return this;
    }

    public EmployeeInboxPage fillRequiredEditFields() {
        // Employment Type — first custom dropdown (div.select / svg.cp)
        form.selectDropdown(0, 0);
        page.waitForTimeout(500);

        // Role — open the typeahead dropdown and inspect checkbox state.
        // The dropdown shows a checked checkbox when a role is already assigned.
        // DO NOT click a checked checkbox — that would deselect the role.
        // Only click if the checkbox is unchecked (role not yet assigned).
        page.locator("div.master input.cursorPointer").first().scrollIntoViewIfNeeded();
        page.waitForTimeout(500);
        try {
            page.locator("div.master input.cursorPointer").first()
                    .click(new Locator.ClickOptions().setForce(true));
        } catch (Exception e) {
            form.clickDispatch("div.master input.cursorPointer");
        }
        page.waitForTimeout(1500);

        if (page.locator("div.server input[type='checkbox'], .profile-dropdown--item").count() == 0) {
            page.locator("div.master input.cursorPointer").first().press("ArrowDown");
            page.waitForTimeout(1500);
        }

        if (page.locator("div.server input[type='checkbox']").count() > 0) {
            boolean isChecked = page.locator("div.server input[type='checkbox']").first().isChecked();
            if (isChecked) {
                System.out.println("[Search] Role already assigned (checkbox checked) — not changing");
            } else {
                System.out.println("[Search] Role not assigned — selecting first available role");
                page.locator("div.server input[type='checkbox']").first().dispatchEvent("click");
            }
        } else if (page.locator(".profile-dropdown--item").count() > 0) {
            // No checkbox visible — items are plain list items.
            // Skip to preserve any existing role selection already in form state.
            System.out.println("[Search] Role dropdown items found (no checkbox) — skipping to preserve existing");
        }

        // Close the dropdown: press Escape then click elsewhere to dismiss reliably
        page.waitForTimeout(500);
        page.keyboard().press("Escape");
        page.waitForTimeout(400);
        // Click on a safe neutral area (form heading) to fully dismiss the dropdown overlay
        try {
            page.locator("h1, h2, .form-heading").first()
                    .click(new Locator.ClickOptions().setForce(true));
        } catch (Exception e) {
            page.evaluate("document.body.click()");
        }
        page.waitForTimeout(400);
        System.out.println("[Search] Filled required edit fields (Employment Type + Role)");
        return this;
    }

    public EmployeeInboxPage saveEmployeeEdit() {
        form.waitFor(saveBtn);
        form.scrollTo(saveBtn);

        Locator btn = page.locator(saveBtn).first();

        // First click — enables button if disabled, or submits if already enabled
        btn.dispatchEvent("click");
        page.waitForTimeout(3000);

        // Second click — only if the button is still present AND not disabled.
        // When the form is already valid the first click submits immediately and the
        // page navigates away, so the button may no longer exist. Use a short
        // timeout to check the class; if the element is gone, skip the second click.
        try {
            String btnClass = btn.getAttribute("class",
                    new Locator.GetAttributeOptions().setTimeout(5000));
            if (btnClass != null && !btnClass.contains("disable")) {
                btn.dispatchEvent("click");
                page.waitForTimeout(3000);
            }
        } catch (Exception e) {
            // Button gone — first click already submitted the form successfully
            System.out.println("[Search] Save button no longer present — form submitted on first click");
        }

        // Confirmation popup — may appear after submit
        String[] confirmSelectors = {
                "button.selector-button-primary[type='submit']",
                "button.selector-button-primary"
        };
        for (String sel : confirmSelectors) {
            if (page.locator(sel).count() > 0) {
                page.locator(sel).first().dispatchEvent("click");
                System.out.println("[Search] Confirmed save popup: " + sel);
                page.waitForTimeout(3000);
                break;
            }
        }

        System.out.println("[Search] Save completed");
        return this;
    }

    // ---------------------------------------------------------------
    // Test Case 2 — Edit Campaign Assignment
    // ---------------------------------------------------------------

    public EmployeeInboxPage clickEditCampaignAssignment() {
        clickMenuItemByText("Edit Campaign Assignment");
        return this;
    }

    public EmployeeInboxPage selectTodayForCampaign() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // Use .last() — campaign date is always the last date input.
        // Form may have 1 input (campaign date only) or 2 inputs
        // (Date of Appointment + campaign date). .last() handles both.
        Locator dateInput = page.locator(campaignDateInput).last();
        dateInput.waitFor(new Locator.WaitForOptions().setTimeout(30000));
        dateInput.scrollIntoViewIfNeeded();
        dateInput.fill(today);
        dateInput.dispatchEvent("change");
        page.waitForTimeout(500);
        System.out.println("[Search] Campaign date set to: " + today);
        return this;
    }

    public EmployeeInboxPage saveCampaignEdit() {
        // Campaign form may not have div.action-bar-wrap — use the broader selector
        // and take the last submit button, same pattern as HRMSPage.submitForm()
        page.evaluate("window.scrollTo(0, document.body.scrollHeight)");
        page.waitForTimeout(1000);

        Locator allBtns = page.locator("button[type='submit']");
        int count = allBtns.count();
        System.out.println("[Search] Campaign save: submit buttons found: " + count);
        if (count == 0)
            return this;

        Locator btn = allBtns.nth(count - 1);
        btn.scrollIntoViewIfNeeded();

        String classBefore = btn.getAttribute("class");
        System.out.println("[Search] Campaign save: button class before = " + classBefore);

        // First click — enables button if disabled
        btn.dispatchEvent("click");
        page.waitForTimeout(3000);

        // Second click — null-safe: if class is null OR does not contain "disable",
        // click again
        String classAfter = btn.getAttribute("class");
        System.out.println("[Search] Campaign save: button class after first click = " + classAfter);
        if (classAfter == null || !classAfter.contains("disable")) {
            System.out.println("[Search] Campaign save: button enabled — clicking to submit");
            btn.dispatchEvent("click");
            page.waitForTimeout(3000);
        }

        // Confirmation popup
        String[] confirmSelectors = {
                "button.selector-button-primary[type='submit']",
                "button.selector-button-primary"
        };
        for (String sel : confirmSelectors) {
            if (page.locator(sel).count() > 0) {
                page.locator(sel).first().dispatchEvent("click");
                System.out.println("[Search] Campaign save confirmed with: " + sel);
                page.waitForTimeout(3000);
                break;
            }
        }

        System.out.println("[Search] Campaign save completed");
        return this;
    }

    // ---------------------------------------------------------------
    // Test Case 3 — Deactivate Employee
    // ---------------------------------------------------------------

    public EmployeeInboxPage clickDeactivateEmployee() {
        clickMenuItemByText("Deactivate Employee");
        return this;
    }

    public EmployeeInboxPage selectDeactivateReason() {
        // Click the SVG arrow to open the reason dropdown
        form.waitFor(reasonDropdownSvg);
        form.clickDispatch(reasonDropdownSvg);
        page.waitForTimeout(1500);

        // Select first option
        if (page.locator(".profile-dropdown--item").count() > 0) {
            page.locator(".profile-dropdown--item").first().dispatchEvent("click");
            System.out.println("[Search] Selected deactivate reason");
        }
        page.waitForTimeout(500);
        return this;
    }

    public EmployeeInboxPage confirmDeactivate() {
        form.waitFor(deactivateConfirm);
        form.clickDispatch(deactivateConfirm);
        page.waitForTimeout(3000);
        System.out.println("[Search] Confirmed deactivation");
        return this;
    }

    // ---------------------------------------------------------------
    // Shared — Success check and go back
    // ---------------------------------------------------------------

    public boolean isSuccessMessageVisible(String expectedText) {
        // Check primary success banner
        try {
            page.locator(successMessage)
                    .waitFor(new Locator.WaitForOptions().setTimeout(30000));
            String text = page.locator(successMessage).textContent();
            System.out.println("[Search] Success message: " + text);
            return text.contains(expectedText);
        } catch (Exception e) {
            System.out.println("[Search] Primary success banner not found: " + e.getMessage().split("\n")[0]);
        }

        // Fallback: check toast / snackbar notifications
        String[] toastSelectors = {
                ".toast-success",
                ".Toastify__toast",
                "div[role='alert']",
                ".digit-toast",
                "[class*='toast']",
                "[class*='success']"
        };
        for (String sel : toastSelectors) {
            try {
                if (page.locator(sel).count() > 0) {
                    String text = page.locator(sel).first().textContent();
                    System.out.println("[Search] Toast message (" + sel + "): " + text);
                    if (text.contains(expectedText))
                        return true;
                }
            } catch (Exception ex) {
                System.out.println("[Search] Toast check failed for " + sel + ": " + ex.getMessage().split("\n")[0]);
                break;
            }
        }

        System.out.println("[Search] No success indicator found for: " + expectedText);
        return false;
    }

    public void goBackToHome() {
        try {
            page.locator(goBackToHomeBtn).first()
                    .waitFor(new Locator.WaitForOptions().setTimeout(5000));
            page.locator(goBackToHomeBtn).first().dispatchEvent("click");
        } catch (Exception e) {
            // fallback — navigate directly
            page.navigate(page.url().split("/hrms")[0] + "/digit-ui/employee");
        }
        page.waitForTimeout(2000);
        System.out.println("[Search] Navigated back to home");
    }
}