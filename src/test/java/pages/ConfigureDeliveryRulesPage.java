package pages;

import java.time.LocalDate;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class ConfigureDeliveryRulesPage {

    private Page page;

    // Delivery rules elements
    private Locator configureDeliveryButton;
    private Locator startDateInput;
    private Locator endDateInput;
    private Locator nextButton;
    private Locator submitButton;

    public ConfigureDeliveryRulesPage(Page page) {

        this.page = page;
        this.configureDeliveryButton = page.locator("#campaign-details-page-button-delivery-strategy");
        this.startDateInput = page.locator("input[placeholder='Start date']");
        this.endDateInput = page.locator("input[placeholder='End date']");
        this.nextButton = page.locator("#campaign-setup-campaign-standalone-setup-campaign-field-primary");
        this.submitButton = page.locator("#campaign-setup-campaign-standalone-setup-campaign-field-primary");
       
    }

    // --- Actions ---

    public void clickConfigureDelivery() {
        configureDeliveryButton.click();
        page.waitForTimeout(1000);
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

    public void clickNext() {
        nextButton.click();
        page.waitForTimeout(1000);
    }

    public void clickSubmit() {
        submitButton.click();
        page.waitForTimeout(1000);
    }
}
