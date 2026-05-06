package utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitUntilState;

import pages.*;

/**
 * NavigationHelper - Central place for all page navigation.
 *
 * Usage:
 *   NavigationHelper nav = new NavigationHelper(page);
 *
 *   // Direct navigation
 *   nav.goToHome();
 *   nav.goToCreateComplaint();
 *
 *   // Get page objects
 *   HomePage home = nav.homePage();
 *   ComplaintPage complaint = nav.complaintPage();
 */
public class NavigationHelper {

    private final Page page;

    public NavigationHelper(Page page) {
        this.page = page;
    }

    // ==================== QUICK NAVIGATION ====================

    /**
     * Navigate to Home page and return HomePage object.
     */
    public HomePage goToHome() {
        // Click home button if visible, otherwise use browser back
        if (page.locator(".digit-topbar-home").isVisible()) {
            page.locator(".digit-topbar-home").click();
        }
        return new HomePage(page);
    }

    public HRMSPage goToCreateEmployee() {
        if (page.locator(".digit-topbar-home").isVisible()) {
            page.locator(".digit-topbar-home").click();
        }
        return new HRMSPage(page);
    }

    /**
     * Navigate to Create Complaint page.
     */
    public ComplaintPage goToCreateComplaint() {
        HomePage home = goToHome();
        home.navigateToCreateComplaint();
        return new ComplaintPage(page);
    }

    /**
     * Navigate to Search Complaint page.
     */
    public ComplaintPage goToSearchComplaint() {
        HomePage home = goToHome();
        return home.goToSearchComplaint();
    }

    /**
     * Navigate to Create User page.
     */
    public void goToCreateUser() {
        HomePage home = goToHome();
        home.navigateToCreateUser();
    }

    /**
     * Navigate to Search User page.
     */
    /** Navigate to Search Employee (HRMS inbox). */
    public EmployeeInboxPage goToSearchEmployee() {
        // Navigate via the sidebar from a fully-loaded home page (React Router).
        // Direct URL navigation re-initialises the React app from scratch and the
        // HRMS inbox API call intermittently fails before auth context is ready.
        // We retry up to 3 times: each attempt reloads home, clicks the sidebar
        // link, then waits up to 15s for the search form (input[name='codes']).
        // A failed attempt resets the page so the next attempt starts clean.
        String[] sidebarSelectors = {
                "a[href*='hrms/inbox']",
                "span.text.removeHeight:has-text('Search Employee')",
                "a:has-text('Search Employee')",
                "span.text:has-text('Search Employee')"
        };

        for (int attempt = 1; attempt <= 3; attempt++) {
            System.out.println("[Nav] goToSearchEmployee attempt " + attempt);

            page.locator("span.text.removeHeight:has-text('Complaints')").first()
                    .waitFor(new Locator.WaitForOptions().setTimeout(30000));
            page.waitForTimeout(2000);

            for (String sel : sidebarSelectors) {
                if (page.locator(sel).count() > 0) {
                    page.locator(sel).first().dispatchEvent("click");
                    System.out.println("[Nav] Clicked sidebar link: " + sel);
                    break;
                }
            }

            // Confirm the search form loaded by waiting for its input
            try {
                page.locator("input[name='codes']").first()
                        .waitFor(new Locator.WaitForOptions().setTimeout(15000));
                System.out.println("[Nav] Search form loaded on attempt " + attempt);
                return new EmployeeInboxPage(page);
            } catch (Exception e) {
                System.out.println("[Nav] Search form not loaded on attempt " + attempt + " — retrying");
            }
        }

        System.out.println("[Nav] All sidebar attempts exhausted — returning page as-is");
        return new EmployeeInboxPage(page);
    }
    // ==================== PAGE OBJECT GETTERS ====================
    // Use these when you're already on the page

    /**
     * Get LoginPage object (use when on login page).
     */
    public LoginPage loginPage() {
        return new LoginPage(page);
    }

    /**
     * Get HomePage object (use when on home page).
     */
    public HomePage homePage() {
        return new HomePage(page);
    }

    /**
     * Get ComplaintPage object (use when on complaint page).
     */
    public ComplaintPage complaintPage() {
        return new ComplaintPage(page);
    }

    public HRMSPage hrmsPage() {
        return new HRMSPage(page);
    }

    /** Get SearchEmployeePage object (use when on HRMS inbox). */
    public EmployeeInboxPage searchEmployeePage() {
        return new EmployeeInboxPage(page);
    }
    // ==================== UTILITY ====================

    /**
     * Go back to previous page.
     */
    public void goBack() {
        page.goBack(new Page.GoBackOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    /**
     * Refresh current page.
     */
     public void refresh() {
        page.reload(new Page.ReloadOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    /**
     * Navigate to any URL.
     *
     * @param url Full URL to navigate to
     */
    public void navigateTo(String url) {
        page.navigate(url, new Page.NavigateOptions()
                .setTimeout(60000)
                .setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    /**
     * Get current page URL.
     */
    public String getCurrentUrl() {
        return page.url();
    }

    /**
     * Wait for page to load completely.
     */
    public void waitForPageLoad() {
        page.waitForLoadState();
    }
}
