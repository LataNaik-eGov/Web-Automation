package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import utils.ConfigReader;

import java.time.LocalDate;
import java.time.Month;
import java.util.Locale;
import java.util.Map;

public class DraftCampaignPage extends BasePage {

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
        super(page);
        this.campaignType = ConfigReader.get("CAMPAIGN_TYPE");
        if (campaignType == null || campaignType.isEmpty()) {
            throw new IllegalStateException("CAMPAIGN_TYPE is not configured. Set it as a GitHub variable (vars.CAMPAIGN_TYPE) for the target environment.");
        }
        this.campaignDisplayName = CAMPAIGN_DISPLAY_NAMES.getOrDefault(campaignType, campaignType);

        this.campaignTypeDropdown = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Select an option"));
        this.nextButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next"));
        this.submitButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit"));
        this.campaignName = page.locator("input[placeholder='CampaignName_Month_Year']");
        this.startDateInput = page.getByPlaceholder("Start date");
        this.endDateInput = page.getByPlaceholder("End date");
        this.campaignNameError = page.getByText("Please add valid campaign name as per the guidelines.");
        this.dateToastError = page.locator(".digit-toast-error, [class*='toast'][class*='error'], [role='alert']").first();
        this.currentMonthLabel = page.locator(".react-datepicker__current-month");
        this.nextMonthButton = page.locator(".react-datepicker__navigation--next");
    }

    // --- Actions ---

    public void clickCampaignTypeDropdown() {
        waitForVisible(campaignTypeDropdown);
       wait(3000);
        campaignTypeDropdown.click();
    }

    public void selectCampaignType() {
        Locator option = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(campaignDisplayName).setExact(true));
        waitForVisible(option);
       wait(3000);
        option.click();
    }

    public boolean isCampaignTypeVisible() {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(campaignDisplayName).setExact(true)).isVisible();
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

    public void clearAndEnterDynamicCampaignName() {
        waitForVisible(campaignName);
       wait(3000);
        campaignName.clear();
        String prefix = campaignType.replace(" ", "");
        if (prefix.length() > 21) prefix = prefix.substring(0, 21);
        String dynamicName = prefix + java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("HHmmssSSS"));
        campaignName.fill(dynamicName);
        campaignName.press("Tab");
    }

    public void enterCampaignName(String name) {
        waitForVisible(campaignName);
       wait(3000);
        campaignName.clear();
        campaignName.fill(name);
        campaignName.press("Tab");
    }

    public boolean isCampaignNameErrorVisible() {
        campaignNameError.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        boolean visible = campaignNameError.isVisible();
        wait(2000);
        return visible;
    }

    public boolean isDateToastErrorVisible() {
        dateToastError.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        boolean visible = dateToastError.isVisible();
        wait(2000);
        return visible;
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
        input.waitFor(new Locator.WaitForOptions().setTimeout(15000));
        input.click();
        currentMonthLabel.waitFor(new Locator.WaitForOptions().setTimeout(10000));

        String headerText = currentMonthLabel.innerText().trim();
        String[] parts = headerText.split(" ");
        int displayedMonth = Month.valueOf(parts[0].toUpperCase(Locale.ENGLISH)).getValue();
        int displayedYear = Integer.parseInt(parts[1]);

        int targetTotal = date.getYear() * 12 + date.getMonthValue();
        int displayedTotal = displayedYear * 12 + displayedMonth;

        for (int i = 0; i < targetTotal - displayedTotal; i++) {
            nextMonthButton.click();
        }

        dateCell(date).click();
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
