package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import utils.ConfigReader;

import java.time.LocalDate;
import java.time.Month;
import java.util.Locale;
import java.util.Map;

public class DraftCampaignPage {

    private Page page;

    private Locator campaignTypeDropdown;
    private Locator nextButton;
    private Locator submitButton;
    private Locator campaignName;
    private Locator campaignNameError;
    private Locator startDateInput;
    private Locator endDateInput;
    private Locator dateToastError;

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
        this.campaignName = page.locator("input[placeholder='CampaignName_Month_Year']");
        this.startDateInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Start date"));
        this.endDateInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("End date"));
        this.campaignNameError = page.getByText("Please add valid campaign name as per the guidelines.");
        this.dateToastError = page.locator(".digit-toast-error, [class*='toast'][class*='error'], [role='alert']").first();
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
        String prefix = campaignType.replace(" ", "");
        if (prefix.length() > 22) prefix = prefix.substring(0, 22);
        String dynamicName = prefix + java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("HHssSSS"));
        campaignName.fill(dynamicName);
        campaignName.press("Enter");
    }

    public void enterCampaignName(String name) {
        campaignName.clear();
        campaignName.fill(name);
        campaignName.press("Tab");
        page.waitForTimeout(500);
    }

    public boolean isCampaignNameErrorVisible() {
        campaignNameError.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        return campaignNameError.isVisible();
    }

    public boolean isDateToastErrorVisible() {
        dateToastError.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        return dateToastError.isVisible();
    }

    public String getCampaignNameErrorText() {
        return campaignNameError.textContent();
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
        input.waitFor(new Locator.WaitForOptions().setTimeout(45000));
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
