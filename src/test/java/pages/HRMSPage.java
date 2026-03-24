package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.FormHelper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HRMSPage extends BasePage {

    private final FormHelper form;

    // --- Login Details ---
    private final String employeeId = "input[title='Please provide a valid and unique Username']";
    private final String employeeIdWait = "input[title*='Username']";
    private final String password = "input[type='password']";

    // --- Personal Details ---
    private final String employeeName = "input[required][pattern*='1,50']";
    private final String mobileNumber = "input[autocomplete='off'][type='text']";
    private final String genderMale = "input.radio-btn[type='radio']";
    private final String dateOfBirth = "input[type='date'][max*='2008']";
    private final String email = "input[type='email']";
    private final String address = "input[pattern*='1,300']";

    // --- Employment Details ---
    private final String dateOfAppointment = "input[type='date'][max*='2026']";
    private final String roleInput = "div.master input.cursorPointer";
    private final String roleOptions = "div.server input[type='checkbox'], .profile-dropdown--item";
    private final String roleCheckbox = "div.server input[type='checkbox']";
    private final String profileDropdownItem = ".profile-dropdown--item";

    // --- Submit ---
    private final String submitButton = "button[type='submit']";
    private final String confirmButton = "button.selector-button-primary[type='submit']";
    private final String confirmButtonAlt = "button.selector-button-primary";
    private final String confirmButtonAlt2 = "button[class*='selector-button-primary']";

    // --- Success ---
    private final String successMessage = "div.emp-success-wrap header";
    private final String successEmpId = "div.emp-success-wrap p";

    public HRMSPage(Page page) {
        super(page);
        this.form = new FormHelper(page);
    }

    public HRMSPage fillLoginDetails(String empId, String pwd) {
        page.evaluate("window.scrollTo(0, 0)");
        // Wait for the employee ID field to be visible after scroll
        page.locator(employeeIdWait).first()
                .waitFor(new Locator.WaitForOptions().setTimeout(10000));

        form.fillForce(employeeId, empId);
        form.fillForceNth(password, 0, pwd);
        form.fillForceNth(password, 1, pwd);
        return this;
    }

    public HRMSPage fillPersonalDetails(String name, String mobile,
            String dob, String emailId, String addr) {

        form.waitFor(employeeName);
        form.fill(employeeName, name);

        page.locator(mobileNumber).nth(2).waitFor();
        page.locator(mobileNumber).nth(2).fill(mobile);

        form.waitFor(genderMale);
        form.scrollTo(genderMale);
        form.clickDispatch(genderMale);

        form.waitFor(dateOfBirth);
        form.fill(dateOfBirth, dob);

        form.waitFor(email);
        form.fill(email, emailId);

        form.waitFor(address);
        form.fill(address, addr);
        return this;
    }

    public HRMSPage fillEmployeeDetails() {
        // Employment Type — first dropdown, first option
        form.selectDropdown(0, 0);

        // Date of Appointment — Playwright fill triggers React events; JS-only set does not
        String today = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Locator dateInput = page.locator(dateOfAppointment);
        dateInput.scrollIntoViewIfNeeded();
        dateInput.fill(today);
        dateInput.dispatchEvent("change");

        // Role — scroll and click to open typeahead
        page.locator(roleInput).first().scrollIntoViewIfNeeded();
        try {
            page.locator(roleInput).first()
                    .click(new Locator.ClickOptions().setForce(true));
        } catch (Exception e) {
            form.clickDispatch(roleInput);
        }

        // Wait for typeahead options to appear
        try {
            page.locator(roleOptions).first()
                    .waitFor(new Locator.WaitForOptions().setTimeout(3000));
        } catch (Exception ignored) {
            // Options didn't appear — try ArrowDown
        }

        // ArrowDown is required to open this typeahead dropdown — click alone is not enough
        if (page.locator(roleOptions).count() == 0) {
            page.locator(roleInput).first().press("ArrowDown");
            // Wait for options to appear after ArrowDown
            page.locator(roleOptions).first()
                    .waitFor(new Locator.WaitForOptions().setTimeout(5000));
        }

        // Select role option
        if (page.locator(roleCheckbox).count() > 0) {
            page.locator(roleCheckbox).first().dispatchEvent("click");
        } else if (page.locator(profileDropdownItem).count() > 0) {
            page.locator(profileDropdownItem).first().dispatchEvent("click");
        }
        page.keyboard().press("Escape");
        return this;
    }

    public void submitForm() {
        page.evaluate("window.scrollTo(0, document.body.scrollHeight)");
        // Wait for the submit button to be present
        page.locator(submitButton).first()
                .waitFor(new Locator.WaitForOptions().setTimeout(15000));

        Locator submitBtns = page.locator(submitButton);
        int count = submitBtns.count();
        System.out.println("[HRMS] Submit buttons found: " + count);
        if (count == 0)
            return;

        Locator lastBtn = submitBtns.nth(count - 1);
        lastBtn.scrollIntoViewIfNeeded();

        // First click — if button was disabled, this enables it (React state update)
        lastBtn.dispatchEvent("click");
        // Wait for React to update button class (disabled → enabled)
        try {
            page.waitForFunction(
                    "() => { const btns = document.querySelectorAll('button[type=\"submit\"]');"
                    + " const btn = btns[btns.length - 1];"
                    + " return btn && !btn.className.includes('disable'); }",
                    null,
                    new Page.WaitForFunctionOptions().setTimeout(10000));
        } catch (Exception ignored) { /* button may already be enabled or stay disabled */ }

        // Second click — if button is now enabled, this is the actual submission
        String btnClass = lastBtn.getAttribute("class");
        if (btnClass != null && !btnClass.contains("disable")) {
            System.out.println("[HRMS] Button enabled after first click — clicking again to submit");
            lastBtn.dispatchEvent("click");
            // Wait for confirmation popup or success page to appear
            page.locator(confirmButton + ", " + successMessage).first()
                    .waitFor(new Locator.WaitForOptions().setTimeout(15000));
        }

        // Confirm popup — appears after actual submission
        String[] confirmSelectors = { confirmButton, confirmButtonAlt, confirmButtonAlt2 };
        for (String sel : confirmSelectors) {
            if (page.locator(sel).count() > 0) {
                page.locator(sel).first().dispatchEvent("click");
                System.out.println("[HRMS] Confirmed with: " + sel);
                break;
            }
        }
        // Wait for success message to confirm the employee was created
        page.locator(successMessage).first()
                .waitFor(new Locator.WaitForOptions().setTimeout(60000));
    }

    public boolean isEmployeeCreatedSuccessfully() {
        try {
            page.locator(successMessage)
                    .waitFor(new Locator.WaitForOptions().setTimeout(60000));
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