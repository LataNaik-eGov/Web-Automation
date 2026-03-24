package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

/**
 * Page Object for Login Page.
 *
 * Usage:
 *   LoginPage login = new LoginPage(page);
 *   HomePage home = login.login("username", "password");
 */
public class LoginPage extends BasePage {

    // Locators
    private final Locator usernameInput;
    private final Locator passwordInput;
    private final Locator tenantDropdown;
    private final Locator tenantOption;
    private final Locator privacyCheckbox;
    private final Locator loginButton;

    public LoginPage(Page page) {
        super(page);
        this.usernameInput = page.locator("input[name='username']");
        this.passwordInput = page.locator("input[name='password']");
        this.tenantDropdown = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Select an option"));
        this.tenantOption = page.locator(".main-option");
        this.privacyCheckbox = page.locator("#privacy-component-check");
        this.loginButton = page.locator("#formcomposer-submit-action");
    }

    /**
     * Login with credentials and return HomePage.
     *
     * @param username Username
     * @param password Password
     * @return HomePage object after successful login
     */
    public HomePage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        if (isTenantDropdownVisible()) {
            selectFirstTenant();
        }
        acceptPrivacyPolicy();
        clickLogin();
        waitForPageLoad();
        return new HomePage(page);
    }

    /**
     * Login with specific tenant selection.
     *
     * @param username   Username
     * @param password   Password
     * @param tenantName Name of tenant to select
     * @return HomePage object after successful login
     */
    public HomePage login(String username, String password, String tenantName) {
        enterUsername(username);
        enterPassword(password);
        if (isTenantDropdownVisible()) {
            selectTenant(tenantName);
        }
        acceptPrivacyPolicy();
        clickLogin();
        waitForPageLoad();
        return new HomePage(page);
    }

    // ==================== INDIVIDUAL ACTIONS ====================

    public void enterUsername(String username) {
        usernameInput.click();
        usernameInput.fill(username);
    }

    public void enterPassword(String password) {
        passwordInput.click();
        passwordInput.fill(password);
    }

    public void selectFirstTenant() {
        tenantDropdown.click();
        wait(1000);
        tenantOption.first().click();
    }

    public void selectTenant(String tenantName) {
        tenantDropdown.click();
        wait(1000);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(tenantName)).click();
    }

    public void acceptPrivacyPolicy() {
        privacyCheckbox.click();
    }

    public void clickLogin() {
        loginButton.click();
    }

    // ==================== VERIFICATION ====================

    public boolean isTenantDropdownVisible() {
        return tenantDropdown.isVisible();
    }

    public boolean isLoginPageDisplayed() {
        return loginButton.isVisible();
    }

    public String getErrorMessage() {
        return page.locator(".error-message").textContent();
    }
}
