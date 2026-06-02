package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import java.util.regex.Pattern;

public class HRMSPage extends BasePage {

    // --- Legacy CSS locators ---
    private final String submitButton = "button[type='submit']";
    private final String confirmButton = "button.selector-button-primary[type='submit']";
    private final String confirmButtonAlt = "button.selector-button-primary";
    private final String confirmButtonAlt2 = "button[class*='selector-button-primary']";
    private final String successMessage = "div.emp-success-wrap header";
    private final String successEmpId = "div.emp-success-wrap p";

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
}
