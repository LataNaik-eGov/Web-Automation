package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import utils.ConfigReader;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;

public class DraftCampaignPage {

    private Page page;

    private Locator campaignTypeDropdown;
    private Locator nextButton;
    private Locator submitButton;
    private Locator campaignName;
    private Locator startDateInput;
    private Locator endDateInput;

    // Date picker elements
    private Locator currentMonthLabel;
    private Locator nextMonthButton;

    private final String campaignType;
    private final String campaignDisplayName;

    private static final Map<String, String> CAMPAIGN_DISPLAY_NAMES = Map.of(
            "BEDNET", "Bednet Distribution",
            "MR-DN", "Seasonal Malaria Chemoprevention (SMC)"
    );

    public DraftCampaignPage(Page page) {
        this.page = page;
        this.campaignType = ConfigReader.get("CAMPAIGN_TYPE");
        this.campaignDisplayName = CAMPAIGN_DISPLAY_NAMES.getOrDefault(campaignType, campaignType);

        this.campaignTypeDropdown = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Select an option"));
        this.nextButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next"));
        this.submitButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit"));
        this.campaignName = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("CampaignName_Month_Year"));
        this.startDateInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Start date"));
        this.endDateInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("End date"));
        this.currentMonthLabel = page.locator(".react-datepicker__current-month");
        this.nextMonthButton = page.locator(".react-datepicker__navigation--next");
    }

    // --- Actions ---

    public void clickCampaignTypeDropdown() {
        campaignTypeDropdown.click();
        page.waitForTimeout(1000);
    }

    public void selectCampaignType() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(campaignDisplayName).setExact(true)).click();
        page.waitForTimeout(1000);
    }

    public boolean isCampaignTypeVisible() {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(campaignDisplayName).setExact(true)).isVisible();
    }

    public void clickNext() {
        nextButton.click();
    }

    public void clickSubmit() {
        submitButton.click();
    }

    public void clearAndEnterDynamicCampaignName() {
        campaignName.clear();
        String dynamicName = campaignType.replace(" ", "") + java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddss"));
        campaignName.fill(dynamicName);
        campaignName.press("Enter");
    }

    public void fillStartDate() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        selectDate(startDateInput, startDate);
    }

    public void fillEndDate() {
        // MR-DN needs 3 cycles × 1 week each with 1-week gaps = 36 days from tomorrow
        LocalDate endDate = "MR-DN".equals(campaignType)
                ? LocalDate.now().plusWeeks(6)
                : LocalDate.now().plusMonths(1);
        selectDate(endDateInput, endDate);
    }

    private Locator dateCell(LocalDate date) {
        return page.locator(".react-datepicker__day:not(.react-datepicker__day--outside-month)")
                   .getByText(String.valueOf(date.getDayOfMonth()), new Locator.GetByTextOptions().setExact(true));
    }

    private void selectDate(Locator input, LocalDate date) {
        input.click();
        page.waitForTimeout(500);

        String headerText = currentMonthLabel.innerText().trim();
        String[] parts = headerText.split(" ");
        int displayedMonth = Month.valueOf(parts[0].toUpperCase(Locale.ENGLISH)).getValue();
        int displayedYear = Integer.parseInt(parts[1]);

        int targetTotal = date.getYear() * 12 + date.getMonthValue();
        int displayedTotal = displayedYear * 12 + displayedMonth;

        for (int i = 0; i < targetTotal - displayedTotal; i++) {
            nextMonthButton.click();
            page.waitForTimeout(300);
        }

        dateCell(date).click();
        page.waitForTimeout(500);
    }

    private String getGridCellName(LocalDate date) {
        String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        String month = date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
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

    public void fillStartAndEndDates() {
        fillStartDate();
        fillEndDate();
    }

    public String getCampaignDisplayName() {
        return campaignDisplayName;
    }

    public String getStartDateValue() {
        String value = startDateInput.inputValue();
        if (value.isEmpty()) {
            value = (String) startDateInput.evaluate("el => el.value");
        }
        return value;
    }

    public String getEndDateValue() {
        String value = endDateInput.inputValue();
        if (value.isEmpty()) {
            value = (String) endDateInput.evaluate("el => el.value");
        }
        return value;
    }
}
