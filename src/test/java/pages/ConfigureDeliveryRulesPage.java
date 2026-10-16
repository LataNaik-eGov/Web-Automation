package pages;

import java.time.LocalDate;
import java.time.Month;
import java.util.Locale;

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

    // Date picker elements
    private Locator currentMonthLabel;
    private Locator nextMonthButton;

    public ConfigureDeliveryRulesPage(Page page) {
        super(page);
        this.campaignType = TestDataReader.getSessionValue("CAMPAIGN_TYPE");
        this.configureDeliveryButton = page.locator(
                "#campaign-details-page-button-delivery-strategy, "
                        + "#campaign-details-page-button-edit-delivery-strategy").first();
        this.startDateTextbox = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Start date"));
        this.endDateTextbox = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("End date"));
        this.nextButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next"));
        this.submitButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit"));
        this.currentMonthLabel = page.locator(".react-datepicker__current-month");
        this.nextMonthButton = page.locator(".react-datepicker__navigation--next");
        this.cycleDateToast = page.getByText("Please fill the cycle dates to move ahead.");
  }

    // --- Actions ---

    public void clickConfigureDelivery() {
        waitForVisible(configureDeliveryButton);
       wait(3000);
        configureDeliveryButton.click();
    }

    private Locator dateCell(LocalDate date) {
        return page.locator(".react-datepicker__day:not(.react-datepicker__day--outside-month)")
                   .getByText(String.valueOf(date.getDayOfMonth()), new Locator.GetByTextOptions().setExact(true));
    }

    private void selectDate(Locator textbox, LocalDate date) {

        waitForVisible(textbox);
       wait(3000);
        textbox.click();

        // Navigate months if needed
        String headerText = currentMonthLabel.innerText().trim();
        String[] parts = headerText.split(" ");
        String displayedMonthStr = parts[0];
        int displayedYear = Integer.parseInt(parts[1]);
        int displayedMonth = Month.valueOf(displayedMonthStr.toUpperCase(Locale.ENGLISH)).getValue();

        int targetMonthTotal = date.getYear() * 12 + date.getMonthValue();
        int displayedMonthTotal = displayedYear * 12 + displayedMonth;
        int monthDiff = targetMonthTotal - displayedMonthTotal;

        for (int i = 0; i < monthDiff; i++) {
            nextMonthButton.click();
        }

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

    /**
     * Whether the screen currently shown is the cycle-date screen.
     * campaign type (e.g. BEDNET) goes straight from the cycles / deliveries
     * screen to the delivery-conditions screen with no cycle-date screen in
     * between, so callers walking the flow must not assume it is there.
     */
    public boolean hasCycleDateStep() {
        try {
            startDateTextbox.first().waitFor(new Locator.WaitForOptions().setTimeout(8000));
            return startDateTextbox.first().isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Fills the cycle dates and advances, but only when the cycle-date screen is
     * actually part of this campaign type's flow. Returns true if it advanced.
     */
    public boolean fillDatesAndNextIfPresent() {
        if (!hasCycleDateStep()) {
            System.out.println("[DeliveryRules] No cycle-date step in this flow — skipping");
            return false;
        }
        fillDates();
        clickNext();
        return true;
    }


    public boolean isConfigureDeliveryButtonVisible() {
        configureDeliveryButton.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        return configureDeliveryButton.isVisible();
    }

    public boolean isCycleDateToastVisible() {
        wait(3000);
        cycleDateToast.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        return cycleDateToast.isVisible();
    }

    /**
     * Value input of the Nth delivery condition, 0-based.
     *
     * Each condition renders as a .attribute-container holding an attribute
     * dropdown, an operator dropdown and this value input. Scoping to the
     * container is what makes the index mean the same thing for every campaign
     * type — a page-wide getByRole(TEXTBOX).nth(n) does not, because the number
     * and order of conditions differs per type, so index 3 was an attribute
     * dropdown for BEDNET rather than a value field.
     */
    private Locator conditionValue(int index) {
        return page.locator(".attribute-container .digit-employeeCard-input").nth(index);
    }

    /**
     * Types into a delivery condition's value field and returns what the field kept.
     *
     * Typing rather than filling is deliberate: the field refuses non-numeric
     * characters per keystroke (so "abc" is kept as ""), and strips a leading
     * minus. Verified on hcm-demo 2026-09-09.
     */
    public String typeConditionValueAndGetValue(int index, String value) {
        Locator input = conditionValue(index);
        waitForVisible(input);
        wait(1000);
        input.click();
        input.fill("");
        if (!value.isEmpty()) {
            input.type(value);
        }
        wait(1000);
        return input.inputValue();
    }

    /**
     * Whether the delivery-conditions step is still displayed.
     *
     * An unacceptable condition value is refused silently — Next simply does not
     * advance, with no toast, card or inline message — so "was it rejected?" can
     * only be answered by whether this step is still on screen. The summary step
     * that follows renders no condition rows.
     */
    public boolean isOnDeliveryConditionsStep() {
        wait(2000);
        return page.locator(".attribute-container").count() > 0;
    }

    public void clickNext() {
        waitForVisible(nextButton);
       wait(3000);
        nextButton.click();
    }

    public void clickSubmit() {
        waitForVisible(submitButton);
       wait(3000);
        submitButton.click();
    }

    public void removeResource(String resourceName) {
        Locator removeBtn = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Remove " + resourceName));
        waitForVisible(removeBtn);
        removeBtn.click();
    }
}
