package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.FormHelper;

/**
 * Base class for all Page Objects.
 * Provides common functionality like navigation, waits, and form handling.
 *
 * All page classes should extend this:
 *   public class LoginPage extends BasePage { ... }
 */
public abstract class BasePage {

    protected final Page page;
    protected final FormHelper form;

    // Common header/navigation locators (shared across pages)
    private final String homeButton = ".digit-topbar-home";
    private final String backButton = ".digit-back-btn";

    public BasePage(Page page) {
        this.page = page;
        this.form = new FormHelper(page);
    }

    // ==================== NAVIGATION ====================

    /**
     * Navigate to a URL path (appends to base URL).
     *
     * @param path URL path (e.g., "/employee/pgr/create-complaint")
     */
    protected void navigateTo(String path) {
        String currentUrl = page.url();
        String baseUrl = currentUrl.split("/employee")[0];
        page.navigate(baseUrl + path);
    }

    /**
     * Navigate to full URL.
     *
     * @param url Complete URL
     */
    protected void navigateToUrl(String url) {
        page.navigate(url);
    }

    /**
     * Go back to previous page.
     */
    public void goBack() {
        page.goBack();
    }

    /**
     * Click the back button (if available on page).
     */
    public void clickBackButton() {
        if (page.locator(backButton).isVisible()) {
            page.locator(backButton).click();
        } else {
            goBack();
        }
    }

    /**
     * Navigate to home page.
     *
     * @return HomePage object
     */
    public HomePage goToHome() {
        if (page.locator(homeButton).isVisible()) {
            page.locator(homeButton).click();
        } else {
            goBack();
        }
        return new HomePage(page);
    }

    // ==================== WAIT HELPERS ====================

    /**
     * Wait for page to finish loading.
     */
    protected void waitForPageLoad() {
        page.waitForLoadState();
    }

    /**
     * Wait for an element to be visible.
     *
     * @param selector CSS selector
     */
    protected void waitForVisible(String selector) {
        page.locator(selector).waitFor();
    }

    /**
     * Wait for an element to be visible.
     *
     * @param locator Playwright Locator
     */
    protected void waitForVisible(Locator locator) {
        locator.waitFor();
    }

    /**
     * Wait for element to disappear (e.g., loading spinner).
     *
     * @param selector CSS selector
     */
    protected void waitForHidden(String selector) {
        page.locator(selector).waitFor(new Locator.WaitForOptions().setState(
                com.microsoft.playwright.options.WaitForSelectorState.HIDDEN));
    }

    /**
     * Wait specified milliseconds.
     *
     * @param ms Milliseconds to wait
     */
    protected void wait(int ms) {
        page.waitForTimeout(ms);
    }

    /**
     * Wait for the DIGIT popup overlay to disappear before interacting with elements beneath it.
     */
    protected void waitForOverlayToHide() {
        try {
            page.locator(".digit-popup-overlay")
                    .waitFor(new Locator.WaitForOptions()
                            .setState(com.microsoft.playwright.options.WaitForSelectorState.HIDDEN)
                            .setTimeout(10000));
        } catch (Exception ignored) {}
    }

    // ==================== VERIFICATION ====================

    /**
     * Check if current page URL contains expected text.
     *
     * @param urlPart Expected text in URL
     * @return true if URL contains the text
     */
    public boolean isOnPage(String urlPart) {
        return page.url().contains(urlPart);
    }

    /**
     * Get current page URL.
     *
     * @return Current URL
     */
    public String getCurrentUrl() {
        return page.url();
    }

    /**
     * Get page title.
     *
     * @return Page title
     */
    public String getPageTitle() {
        return page.title();
    }

    /**
     * Check if element is visible on page.
     *
     * @param selector CSS selector
     * @return true if visible
     */
    public boolean isVisible(String selector) {
        return page.locator(selector).isVisible();
    }
}
