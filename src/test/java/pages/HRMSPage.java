package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class HRMSPage extends BasePage {

    // --- Create employee CSS locators ---
    private final String submitButton = "button[type='submit']";
    private final String confirmButton = "button.selector-button-primary[type='submit']";
    private final String confirmButtonAlt = "button.selector-button-primary";
    private final String confirmButtonAlt2 = "button[class*='selector-button-primary']";
    private final String successMessage = "div.emp-success-wrap header";
    private final String successEmpId = "div.emp-success-wrap p";

    // --- Employee inbox CSS locators ---
    private final String searchInput = "input[name='codes']";
    private final String searchBtn = "button.submit-bar-search[type='submit']";
    private final String takeActionBtn = "button.submit-bar[type='button']";
    private final String menuItem = "div.menu-wrap p";
    private final String employeeNameInput = "input[pattern*='1,50'][title*='Username']";
    private final String saveBtnSelector = "div.action-bar-wrap button[type='submit']";
    private final String campaignDateInput = "input.employee-card-input[type='date'][min]";
    private final String reasonDropdownSvg = "div.select svg.cp";
    private final String deactivateConfirm = "button.selector-button-primary[type='submit']";
    private final String goBackToHomeBtn = "div.emp-success-wrap button";

    // --- Aria-role locators (from recorded UI flow) ---
    private final Locator hierarchyTypeDropdown;
    private final Locator nextButton;
    private final Locator usernameInput;
    private final Locator passwordInput;
    private final Locator confirmPasswordInput;
    private final Locator nameInput;
    private final Locator mobileInput;
    private final Locator femaleRadio;
    private final Locator maleRadio;
    private final Locator dobInput;
    private final Locator emailInput;
    private final Locator addressInput;
    private final Locator doaInput;
    private final Locator selectAnOption;
    private final Locator roleSearchInput;

    public HRMSPage(Page page) {
        super(page);
        this.hierarchyTypeDropdown = page.locator("#hrms-create-standalone-hierarchytype-dropdown");
        this.nextButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next"));
        this.usernameInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Username"));
        this.passwordInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password").setExact(true));
        this.confirmPasswordInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Confirm Password"));
        this.nameInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Name").setExact(true));
        this.mobileInput = page.getByRole(AriaRole.SPINBUTTON, new Page.GetByRoleOptions().setName("Mobile Number"));
        this.femaleRadio = page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("Female"));
        this.maleRadio = page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("Male"));
        this.dobInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Date of Birth"));
        this.emailInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email"));
        this.addressInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Correspondence Address"));
        this.doaInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Date of Appointment"));
        this.selectAnOption = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Select an option"));
        this.roleSearchInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search options in"));
    }

    // ==================== STEP 1: HIERARCHY TYPE ====================

    public HRMSPage selectHierarchyType(String searchText, String optionName) {
        hierarchyTypeDropdown.click();
        hierarchyTypeDropdown.fill(searchText);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(optionName).setExact(true)).click();
        return this;
    }

    // ==================== STEP 2: NEXT ====================

    public HRMSPage clickNext() {
        nextButton.click();
        page.waitForLoadState();
        return this;
    }

    // ==================== STEP 3: LOGIN DETAILS ====================

    public HRMSPage fillLoginDetails(String empId, String pwd) {
        page.evaluate("window.scrollTo(0, 0)");
        usernameInput.waitFor(new Locator.WaitForOptions().setTimeout(10000));
        usernameInput.fill(empId);
        passwordInput.fill(pwd);
        confirmPasswordInput.fill(pwd);
        return this;
    }

    // ==================== STEP 4: PERSONAL DETAILS ====================

    public HRMSPage fillPersonalDetails(String name, String mobile, String gender,
            String dob, String emailId, String addr) {
        nameInput.waitFor();
        nameInput.fill(name);
        mobileInput.fill(mobile);

        if ("Female".equalsIgnoreCase(gender)) {
            femaleRadio.check();
        } else {
            maleRadio.check();
        }

        dobInput.fill(dob);
        emailInput.fill(emailId);
        addressInput.fill(addr);
        return this;
    }

    // ==================== STEP 5: EMPLOYMENT DETAILS ====================

    public HRMSPage fillEmploymentDetails(String empType, String doa,
            String department, String designation, String roleName, String jurisdiction) {

        // Employment Type — first "Select an option" dropdown
        selectAnOption.first().click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(empType)).click();

        // Date of Appointment
        doaInput.waitFor();
        doaInput.fill(doa);
        doaInput.dispatchEvent("change");

        // Department — second "Select an option" dropdown; options appear in #jk-dropdown-unique
        selectAnOption.nth(1).click();
        page.locator("#jk-dropdown-unique")
                .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName(department))
                .click();

        // Designation — third "Select an option" dropdown
        selectAnOption.nth(2).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(designation)).click();

        // Role — click to open the multiselect, search, then pick the option from the dropdown
        roleSearchInput.waitFor();
        roleSearchInput.click();
        roleSearchInput.fill(roleName);

        // Wait for filtered options to appear, then click the matching item
        Locator roleOption = page.locator(".profile-dropdown--item, li[role='option'], .dropdown-item")
                .filter(new Locator.FilterOptions().setHasText(roleName))
                .first();
        try {
            roleOption.waitFor(new Locator.WaitForOptions().setTimeout(5000));
            roleOption.dispatchEvent("click");
            System.out.println("[HRMS] Selected role via dropdown item: " + roleName);
        } catch (Exception e) {
            // Fallback: try aria checkbox
            System.out.println("[HRMS] Dropdown item not found, trying checkbox for: " + roleName);
            page.getByRole(AriaRole.CHECKBOX,
                    new Page.GetByRoleOptions().setName("Select option: " + roleName))
                    .dispatchEvent("click");
        }

        // Jurisdiction — expand the Country section and pick the jurisdiction option
        page.locator("div")
                .filter(new Locator.FilterOptions().setHasText(Pattern.compile("^Country$")))
                .nth(1)
                .click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(jurisdiction)).click();

        return this;
    }

    // ==================== STEP 6: SUBMIT ====================

    public void submitForm() {
        page.evaluate("window.scrollTo(0, document.body.scrollHeight)");
        page.locator(submitButton).first().waitFor(new Locator.WaitForOptions().setTimeout(15000));

        Locator submitBtns = page.locator(submitButton);
        int count = submitBtns.count();
        System.out.println("[HRMS] Submit buttons found: " + count);
        if (count == 0) return;

        Locator lastBtn = submitBtns.nth(count - 1);
        lastBtn.scrollIntoViewIfNeeded();

        // First click — enables the button if React marked it disabled
        lastBtn.dispatchEvent("click");
        try {
            page.waitForFunction(
                    "() => { const btns = document.querySelectorAll('button[type=\"submit\"]');"
                    + " const btn = btns[btns.length - 1];"
                    + " return btn && !btn.className.includes('disable'); }",
                    null,
                    new Page.WaitForFunctionOptions().setTimeout(10000));
        } catch (Exception ignored) {}

        // Second click — actual submission once button is enabled
        String btnClass = lastBtn.getAttribute("class");
        if (btnClass != null && !btnClass.contains("disable")) {
            System.out.println("[HRMS] Button enabled — submitting");
            lastBtn.dispatchEvent("click");
            page.locator(confirmButton + ", " + successMessage).first()
                    .waitFor(new Locator.WaitForOptions().setTimeout(15000));
        }

        // Confirmation popup
        String[] confirmSelectors = { confirmButton, confirmButtonAlt, confirmButtonAlt2 };
        for (String sel : confirmSelectors) {
            if (page.locator(sel).count() > 0) {
                page.locator(sel).first().dispatchEvent("click");
                System.out.println("[HRMS] Confirmed with: " + sel);
                break;
            }
        }
        page.locator(successMessage).first().waitFor(new Locator.WaitForOptions().setTimeout(60000));
    }

    // ==================== VERIFICATION ====================

    public boolean isEmployeeCreatedSuccessfully() {
        try {
            page.locator(successMessage).waitFor(new Locator.WaitForOptions().setTimeout(60000));
            String text = page.locator(successMessage).textContent();
            System.out.println("[HRMS] Success message: " + text);
            return text.contains("Employee Created Successfully");
        } catch (Exception e) {
            System.out.println("[HRMS] Success not found: " + e.getMessage().split("\n")[0]);
            return false;
        }
    }

    public String getEmployeeId() {
        try {
            page.locator(successEmpId).waitFor();
            return page.locator(successEmpId).textContent();
        } catch (Exception e) {
            return "Could not get Employee ID";
        }
    }

    // ==================== EMPLOYEE INBOX: SEARCH ====================

    public HRMSPage searchEmployee(String empId) {
        form.waitFor(searchInput);
        form.fill(searchInput, empId);
        page.waitForTimeout(500);
        form.clickDispatch(searchBtn);
        page.waitForTimeout(2000);
        System.out.println("[HRMS] Searched for employee: " + empId);
        return this;
    }

    public HRMSPage openEmployeeResult() {
        page.waitForTimeout(2000);
        String[] selectors = {
                "table tbody tr td:first-child a",
                "tbody tr td a",
                "tbody a"
        };
        for (String sel : selectors) {
            int count = page.locator(sel).count();
            System.out.println("[HRMS] Selector '" + sel + "' count: " + count);
            if (count > 0) {
                page.locator(sel).first().dispatchEvent("click");
                page.waitForTimeout(2000);
                System.out.println("[HRMS] Opened employee result using: " + sel);
                return this;
            }
        }
        throw new RuntimeException("[HRMS] No employee result link found in results table");
    }

    public HRMSPage openTakeActionMenu() {
        form.waitFor(takeActionBtn);
        form.clickDispatch(takeActionBtn);
        page.waitForTimeout(1000);
        System.out.println("[HRMS] Opened Take Action menu");
        return this;
    }

    private void clickMenuItemByText(String menuText) {
        page.locator(menuItem + ":has-text('" + menuText + "')")
                .first().dispatchEvent("click");
        page.waitForTimeout(2000);
        System.out.println("[HRMS] Clicked menu: " + menuText);
    }

    // ==================== EMPLOYEE INBOX: EDIT EMPLOYEE ====================

    public HRMSPage clickEditEmployee() {
        clickMenuItemByText("Edit Employee");
        return this;
    }

    public HRMSPage editEmployeeName() {
        form.waitFor(employeeNameInput);
        String currentName = page.locator(employeeNameInput).first().inputValue();
        String updatedName = currentName + " one";
        page.locator(employeeNameInput).first().fill(updatedName);
        page.waitForTimeout(500);
        System.out.println("[HRMS] Updated employee name: " + currentName + " → " + updatedName);
        return this;
    }

    public HRMSPage fillRequiredEditFields() {
        form.selectDropdown(0, 0);
        page.waitForTimeout(500);

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
                System.out.println("[HRMS] Role already assigned — not changing");
            } else {
                System.out.println("[HRMS] Role not assigned — selecting first available role");
                page.locator("div.server input[type='checkbox']").first().dispatchEvent("click");
            }
        } else if (page.locator(".profile-dropdown--item").count() > 0) {
            System.out.println("[HRMS] Role dropdown items found (no checkbox) — skipping to preserve existing");
        }

        page.waitForTimeout(500);
        page.keyboard().press("Escape");
        page.waitForTimeout(400);
        try {
            page.locator("h1, h2, .form-heading").first()
                    .click(new Locator.ClickOptions().setForce(true));
        } catch (Exception e) {
            page.evaluate("document.body.click()");
        }
        page.waitForTimeout(400);
        System.out.println("[HRMS] Filled required edit fields");
        return this;
    }

    public HRMSPage saveEmployeeEdit() {
        form.waitFor(saveBtnSelector);
        form.scrollTo(saveBtnSelector);

        Locator btn = page.locator(saveBtnSelector).first();
        btn.dispatchEvent("click");
        page.waitForTimeout(3000);

        try {
            String btnClass = btn.getAttribute("class",
                    new Locator.GetAttributeOptions().setTimeout(5000));
            if (btnClass != null && !btnClass.contains("disable")) {
                btn.dispatchEvent("click");
                page.waitForTimeout(3000);
            }
        } catch (Exception e) {
            System.out.println("[HRMS] Save button no longer present — form submitted on first click");
        }

        String[] confirmSelectors = {
                "button.selector-button-primary[type='submit']",
                "button.selector-button-primary"
        };
        for (String sel : confirmSelectors) {
            if (page.locator(sel).count() > 0) {
                page.locator(sel).first().dispatchEvent("click");
                System.out.println("[HRMS] Confirmed save popup: " + sel);
                page.waitForTimeout(3000);
                break;
            }
        }
        System.out.println("[HRMS] Save completed");
        return this;
    }

    // ==================== EMPLOYEE INBOX: DEACTIVATE ====================

    public HRMSPage clickDeactivateEmployee() {
        clickMenuItemByText("Deactivate Employee");
        return this;
    }

    public HRMSPage selectDeactivateReason() {
        form.waitFor(reasonDropdownSvg);
        form.clickDispatch(reasonDropdownSvg);
        page.waitForTimeout(1500);
        if (page.locator(".profile-dropdown--item").count() > 0) {
            page.locator(".profile-dropdown--item").first().dispatchEvent("click");
            System.out.println("[HRMS] Selected deactivate reason");
        }
        page.waitForTimeout(500);
        return this;
    }

    public HRMSPage confirmDeactivate() {
        form.waitFor(deactivateConfirm);
        form.clickDispatch(deactivateConfirm);
        page.waitForTimeout(3000);
        System.out.println("[HRMS] Confirmed deactivation");
        return this;
    }

    // ==================== EMPLOYEE INBOX: CAMPAIGN ASSIGNMENT ====================

    public HRMSPage clickEditCampaignAssignment() {
        clickMenuItemByText("Edit Campaign Assignment");
        return this;
    }

    public HRMSPage selectTodayForCampaign() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Locator dateInput = page.locator(campaignDateInput).last();
        dateInput.waitFor(new Locator.WaitForOptions().setTimeout(30000));
        dateInput.scrollIntoViewIfNeeded();
        dateInput.fill(today);
        dateInput.dispatchEvent("change");
        page.waitForTimeout(500);
        System.out.println("[HRMS] Campaign date set to: " + today);
        return this;
    }

    // ==================== EMPLOYEE INBOX: SUCCESS & NAVIGATION ====================

    public boolean isSuccessMessageVisible(String expectedText) {
        try {
            page.locator(successMessage)
                    .waitFor(new Locator.WaitForOptions().setTimeout(30000));
            String text = page.locator(successMessage).textContent();
            System.out.println("[HRMS] Success message: " + text);
            return text.contains(expectedText);
        } catch (Exception e) {
            System.out.println("[HRMS] Primary success banner not found: " + e.getMessage().split("\n")[0]);
        }

        String[] toastSelectors = {
                ".toast-success", ".Toastify__toast", "div[role='alert']",
                ".digit-toast", "[class*='toast']", "[class*='success']"
        };
        for (String sel : toastSelectors) {
            try {
                if (page.locator(sel).count() > 0) {
                    String text = page.locator(sel).first().textContent();
                    System.out.println("[HRMS] Toast message (" + sel + "): " + text);
                    if (text.contains(expectedText)) return true;
                }
            } catch (Exception ex) {
                System.out.println("[HRMS] Toast check failed for " + sel + ": " + ex.getMessage().split("\n")[0]);
                break;
            }
        }

        System.out.println("[HRMS] No success indicator found for: " + expectedText);
        return false;
    }

    public void goBackToHome() {
        try {
            page.locator(goBackToHomeBtn).first()
                    .waitFor(new Locator.WaitForOptions().setTimeout(5000));
            page.locator(goBackToHomeBtn).first().dispatchEvent("click");
        } catch (Exception e) {
            page.navigate(page.url().split("/hrms")[0] + "/digit-ui/employee");
        }
        page.waitForTimeout(2000);
        System.out.println("[HRMS] Navigated back to home");
    }
}
