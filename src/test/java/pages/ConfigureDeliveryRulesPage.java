package pages;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
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
    private Locator datePickerButton;
    private Locator nextButton;
    private Locator submitButton;

    public ConfigureDeliveryRulesPage(Page page) {

        this.page = page;
        this.campaignType = ConfigReader.get("CAMPAIGN_TYPE");
        this.configureDeliveryButton = page.locator("#campaign-details-page-button-delivery-strategy");
        this.datePickerButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Open date picker"));
        this.nextButton = page.locator("#campaign-setup-campaign-standalone-setup-campaign-field-primary");
        this.submitButton = page.locator("#campaign-setup-campaign-standalone-setup-campaign-field-primary");

    }

    // --- Actions ---

    public void clickConfigureDelivery() {
        configureDeliveryButton.click();
        page.waitForTimeout(1000);
    }

    private String getGridCellName(LocalDate date) {
        String dayOfWeek = date.getDayOfWeek().getDisplayName(java.time.format.TextStyle.FULL, Locale.ENGLISH);
        String month = date.getMonth().getDisplayName(java.time.format.TextStyle.FULL, Locale.ENGLISH);
        int day = date.getDayOfMonth();
        String ordinal;
        if (day >= 11 && day <= 13) {
            ordinal = day + "th";
        } else {
            switch (day % 10) {
                case 1: ordinal = day + "st"; break;
                case 2: ordinal = day + "nd"; break;
                case 3: ordinal = day + "rd"; break;
                default: ordinal = day + "th"; break;
            }
        }
        return "Choose " + dayOfWeek + ", " + month + " " + ordinal + ",";
    }

    private void selectDate(int pickerIndex, LocalDate date) {
        datePickerButton.nth(pickerIndex).click();
        page.waitForTimeout(500);

        // Navigate months if needed
        String headerText = page.locator(".react-datepicker__current-month").innerText().trim();
        String[] parts = headerText.split(" ");
        String displayedMonthStr = parts[0];
        int displayedYear = Integer.parseInt(parts[1]);
        int displayedMonth = Month.valueOf(displayedMonthStr.toUpperCase(Locale.ENGLISH)).getValue();

        int targetMonthTotal = date.getYear() * 12 + date.getMonthValue();
        int displayedMonthTotal = displayedYear * 12 + displayedMonth;
        int monthDiff = targetMonthTotal - displayedMonthTotal;

        for (int i = 0; i < monthDiff; i++) {
            page.locator(".react-datepicker__navigation--next").click();
            page.waitForTimeout(300);
        }

        page.getByRole(AriaRole.GRIDCELL, new Page.GetByRoleOptions().setName(getGridCellName(date))).click();
        page.waitForTimeout(500);
    }

    public void fillStartDate() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        selectDate(0, tomorrow);
    }

    public void fillEndDate() {
        LocalDate oneMonthLater = LocalDate.now().plusMonths(1);
        selectDate(1, oneMonthLater);
    }

    public void fillMRDNDates() {
        LocalDate cycleStart = LocalDate.now().plusDays(1);

        for (int i = 0; i < 3; i++) {
            LocalDate cycleEnd = cycleStart.plusWeeks(1);

            selectDate(i * 2, cycleStart);
            selectDate(i * 2 + 1, cycleEnd);

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

    public void clickNext() {
        nextButton.click();
        page.waitForTimeout(1000);
    }

    public void clickSubmit() {
        submitButton.click();
        page.waitForTimeout(1000);
    }
}
