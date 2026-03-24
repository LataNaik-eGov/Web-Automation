package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * Page Object for Home Page (Dashboard after login).
 *
 * Usage:
 *   HomePage home = new HomePage(page);
 *
 *   // Navigate to other pages (returns page object)
 *   ComplaintPage complaint = home.goToCreateComplaint();
 *
 *   // Or just navigate without getting page object
 *   home.navigateToCreateComplaint();
 */
public class HomePage extends BasePage {

    // Locators
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

    /**
     * Go to Create Complaint page.
     *
     * @return ComplaintPage object
     */
    public ComplaintPage goToCreateComplaint() {
        createComplaintBtn.click();
        waitForPageLoad();
        return new ComplaintPage(page);
    }

    /**
     * Go to Search Complaint page.
     * (Add SearchComplaintPage class when needed)
     */
    public ComplaintPage goToSearchComplaint() {
        searchComplaintBtn.click();
        waitForPageLoad();
        return new ComplaintPage(page);
    }

    /**
     * Go to Create User page.
     * (Add CreateUserPage class when needed)
     */
    public void goToCreateUser() {
        createUserBtn.click();
        waitForPageLoad();
    }

    /**
     * Go to Search User page.
     * (Add SearchUserPage class when needed)
     */
    public void goToSearchUser() {
        searchUserBtn.click();
        waitForPageLoad();
    }

    // ==================== SIMPLE NAVIGATION (No return) ====================

    public void navigateToCreateComplaint() {
        createComplaintBtn.click();
    }

    public void navigateToSearchComplaint() {
        searchComplaintBtn.click();
    }

    public void navigateToCreateUser() {
        createUserBtn.click();
    }

    public void navigateToSearchUser() {
        searchUserBtn.click();
    }

    // ==================== VERIFICATION ====================

    /**
     * Check if home page is displayed.
     *
     * @return true if home element is visible
     */
    public boolean isHomeDisplayed() {
        waitForVisible(homeElement);
        return homeElement.isVisible();
    }

    /**
     * Backward compatibility method.
     */
    public boolean isHomeVisible() {
        return isHomeDisplayed();
    }

    /**
     * Check if Create Complaint button is visible.
     */
    public boolean isCreateComplaintVisible() {
        waitForVisible(createComplaintBtn);
        return createComplaintBtn.isVisible();
    }

    /**
     * Check if Search Complaint button is visible.
     */
    public boolean isSearchComplaintVisible() {
        waitForVisible(searchComplaintBtn);
        return searchComplaintBtn.isVisible();
    }
}
