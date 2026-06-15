package pages;

import java.time.LocalDate;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import utils.TestDataReader;

public class ConfigureDeliveryRulesPage extends BasePage {

    private String campaignType;

    // Delivery rules elements
    private Locator configureDeliveryButton;
    private Locator startDateTextbox;
    private Locator endDateTextbox;
    private Locator nextButton;
    private Locator submitButton;
    private Locator cycleDateToast;
    private Locator deliveryErrorToast;

    // Date picker elements
    private Locator currentMonthLabel;
    private Locator nextMonthButton;

    public ConfigureDeliveryRulesPage(Page page) {
        super(page);
        this.campaignType = TestDataReader.getSessionValue("CAMPAIGN_TYPE");
        this.configureDeliveryButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Start Planning Deliveries"));
        this.startDateTextbox = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Start date"));
        this.endDateTextbox = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("End date"));
        this.nextButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next"));
        this.submitButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit"));
        this.currentMonthLabel = page.locator(".react-datepicker__current-month");
        this.nextMonthButton = page.locator(".react-datepicker__navigation--next");
        this.cycleDateToast = page.getByText("Please fill the cycle dates to move ahead.");
        this.deliveryErrorToast = page.locator(".digit-toast-error, [class*='toast'][class*='error'], [role='alert']").first();

    }

    // --- Actions ---

    public void clickConfigureDelivery() {
        waitForVisible(configureDeliveryButton);
        configureDeliveryButton.click();
    }

    private Locator dateCell(LocalDate date) {
        return page.locator(".react-datepicker__day:not(.react-datepicker__day--outside-month)")
                   .getByText(String.valueOf(date.getDayOfMonth()), new Locator.GetByTextOptions().setExact(true));
    }

    private void selectDate(Locator textbox, LocalDate date) {
        waitForVisible(textbox);
        textbox.click();
        navigateToMonth(currentMonthLabel, nextMonthButton, date);
        dateCell(date).click();
    }

    public void fillStartDate() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        selectDate(startDateTextbox.first(), tomorrow);
    }

    public void fillEndDate() {
        LocalDate oneMonthLater = LocalDate.now().plusMonths(1);
        selectDate(endDateTextbox, oneMonthLater);
    }

    public void fillMRDNDates() {
        LocalDate cycleStart = LocalDate.now().plusDays(1);

        for (int i = 0; i < 3; i++) {
            LocalDate cycleEnd = cycleStart.plusWeeks(1);

            selectDate(startDateTextbox.nth(i), cycleStart);
            selectDate(endDateTextbox.nth(i), cycleEnd);

            cycleStart = cycleEnd.plusWeeks(1);
        }
    }

    public void fillDates() {
        if ("MR-DN".equals(campaignType)) {
            fillMRDNDates();
        } else {
            fillStartDate();
            fillEndDate();
        }
    }

    public boolean isConfigureDeliveryButtonVisible() {
        configureDeliveryButton.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        return configureDeliveryButton.isVisible();
    }

    public boolean isCycleDateToastVisible() {
        return waitAndCheckVisible(cycleDateToast, 5000);
    }

    public boolean isDeliveryErrorToastVisible() {
        return waitAndCheckVisible(deliveryErrorToast, 5000);
    }

    public void fillNthTextbox(int index, String value) {
        Locator textbox = page.getByRole(AriaRole.TEXTBOX).nth(index);
        waitForVisible(textbox);
        // dispatchEvent bypasses pointer-interception from overlapping DIGIT UI elements
        textbox.dispatchEvent("click");
        textbox.fill(value, new Locator.FillOptions().setForce(true));
    }

    public void clickNext() {
        waitForVisible(nextButton);
        nextButton.click();
    }

    public void clickSubmit() {
        waitForVisible(submitButton);
        submitButton.click();
    }

    public void removeResource(String resourceName) {
        Locator removeBtn = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Remove " + resourceName));
        waitForVisible(removeBtn);
        removeBtn.click();
    }
}
