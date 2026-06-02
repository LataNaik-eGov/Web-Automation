package pages;

import java.time.LocalDate;
import java.time.Month;
import java.util.Locale;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import utils.ConfigReader;

public class ConfigureDeliveryRulesPage {

    private Page page;
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

        this.page = page;
        this.campaignType = ConfigReader.get("CAMPAIGN_TYPE");
        this.configureDeliveryButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Start Planning Deliveries"));
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
        configureDeliveryButton.click();
        page.waitForTimeout(1000);
    }

    private Locator dateCell(LocalDate date) {
        return page.locator(".react-datepicker__day:not(.react-datepicker__day--outside-month)")
                   .getByText(String.valueOf(date.getDayOfMonth()), new Locator.GetByTextOptions().setExact(true));
    }

    private void selectDate(Locator textbox, LocalDate date) {
        textbox.click();
        page.waitForTimeout(500);

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
            page.waitForTimeout(300);
        }

        dateCell(date).click();
        page.waitForTimeout(500);
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

    public boolean isCycleDateToastVisible() {
        cycleDateToast.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        return cycleDateToast.isVisible();
    }

    public void clickNext() {
        nextButton.click();
        page.waitForTimeout(1000);
    }

    public void clickSubmit() {
        submitButton.click();
        page.waitForTimeout(1000);
    }
}
