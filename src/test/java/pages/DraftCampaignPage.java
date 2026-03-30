package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.ConfigReader;

import java.time.LocalDate;
import java.util.Map;

public class DraftCampaignPage {

    private Page page;

    private Locator campaignTypeDropdown;
    private Locator nextButton;
    private Locator campaignName;
    private Locator startDateInput;
    private Locator endDateInput;

    private final String campaignType;
    private final String campaignDisplayName;

    private static final Map<String, String> CAMPAIGN_DISPLAY_NAMES = Map.of(
            "BEDNET", "Bednet Distribution",
            "MR-DN", "MR-DN"
    );

    public DraftCampaignPage(Page page) {
        this.page = page;
        this.campaignType = ConfigReader.get("CAMPAIGN_TYPE");
        this.campaignDisplayName = CAMPAIGN_DISPLAY_NAMES.getOrDefault(campaignType, campaignType);

        this.campaignTypeDropdown = page.locator("#campaign-create-campaign-standalone-hcm_select_campaign_type-field");
        this.nextButton = page.locator("#campaign-create-campaign-formcomposer-setup-campaign-primary-submit-btn");
        this.campaignName = page.locator("input[name='CampaignName']");
        this.startDateInput = page.locator("input.digit-employeeCard-input").nth(0);
        this.endDateInput = page.locator("input.digit-employeeCard-input").nth(1);
    }

    // --- Actions ---

    public void clickCampaignTypeDropdown() {
        campaignTypeDropdown.click(new Locator.ClickOptions().setForce(true));
        page.waitForTimeout(1000);
    }

    public void selectCampaignType() {
        page.getByText(campaignDisplayName).click(new Locator.ClickOptions().setForce(true));
        page.waitForTimeout(1000);
    }

    public boolean isCampaignTypeVisible() {
        return page.getByText(campaignDisplayName).isVisible();
    }

    public void clickNext() {
        nextButton.click();
    }

    public void clearAndEnterDynamicCampaignName() {
        campaignName.clear();
        String dynamicName = campaignType + "Campaign" + java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        campaignName.fill(dynamicName);
    }

    public void fillStartDate() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        startDateInput.click();
        page.waitForTimeout(500);
        page.locator(".react-datepicker__day--0" + String.format("%02d", tomorrow.getDayOfMonth())
                + ":not(.react-datepicker__day--outside-month)").first().click();
        page.waitForTimeout(500);
    }

    public void fillEndDate() {
        LocalDate oneMonthLater = LocalDate.now().plusMonths(1);
        endDateInput.click();
        page.waitForTimeout(500);
        page.locator(".react-datepicker__navigation--next").click();
        page.waitForTimeout(500);
        page.locator(".react-datepicker__day--0" + String.format("%02d", oneMonthLater.getDayOfMonth())
                + ":not(.react-datepicker__day--outside-month)").first().click();
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
