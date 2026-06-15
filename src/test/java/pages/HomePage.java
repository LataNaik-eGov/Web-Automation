package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class HomePage extends BasePage {

    private final Locator homeElement;
    private final Locator createComplaintBtn;
    private final Locator searchComplaintBtn;
    private final Locator createUserBtn;
    private final Locator searchUserBtn;

    public HomePage(Page page) {
        super(page);
        this.homeElement = page.locator(".digit-topbar-ulb");
        this.createComplaintBtn = page.locator("h2.digit-button-label:has-text('Create Complaint')");
        this.searchComplaintBtn = page.locator("h2.digit-button-label:has-text('Search Complaint')");
        this.createUserBtn = page.locator("h2.digit-button-label:has-text('Create User')");
        this.searchUserBtn = page.locator("h2.digit-button-label:has-text('Search User')");
    }

    // ==================== NAVIGATION (Returns Page Object) ====================

    public ComplaintPage goToCreateComplaint() {
        createComplaintBtn.click();
        waitForPageLoad();
        return new ComplaintPage(page);
    }

    public ComplaintPage goToSearchComplaint() {
        searchComplaintBtn.click();
        waitForPageLoad();
        return new ComplaintPage(page);
    }

    public HRMSPage goToCreateUser() {
        createUserBtn.click();
        waitForPageLoad();
        return new HRMSPage(page);
    }

    public void goToSearchUser() {
        searchUserBtn.click();
        waitForPageLoad();
    }

    // ==================== SIMPLE NAVIGATION (No return) ====================

    public void navigateToCreateComplaint() {
        createComplaintBtn.click();
    }

    public void navigateToCreateUser() {
        createUserBtn.click();
    }

    // ==================== VERIFICATION ====================

    public boolean isHomeDisplayed() {
        waitForVisible(homeElement);
        return homeElement.isVisible();
    }

    public boolean isCreateComplaintVisible() {
        waitForVisible(createComplaintBtn);
        return createComplaintBtn.isVisible();
    }

    public boolean isSearchComplaintVisible() {
        waitForVisible(searchComplaintBtn);
        return searchComplaintBtn.isVisible();
    }
}
