package base;

import java.util.Arrays;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import pages.HomePage;
import pages.LoginPage;
import utils.ConfigReader;
import utils.FormHelper;
import utils.NavigationHelper;
import utils.ScreenshotHelper;

/**
 * Base class for all test classes.
 * Handles browser setup, login, and teardown.
 *
 * Usage:
 *   public class MyTest extends BaseTest {
 *
 *       @Test
 *       public void myTest() {
 *           // Use nav for navigation
 *           ComplaintPage complaint = nav.goToCreateComplaint();
 *
 *           // Use form for form interactions
 *           form.enterText("#field", "value");
 *
 *           // Use homePage directly
 *           homePage.goToCreateComplaint();
 *       }
 *   }
 */
public class BaseTest {

    // Core Playwright objects
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    // Helpers available in all tests
    protected NavigationHelper nav;
    protected FormHelper form;
    protected ScreenshotHelper screenshot;

    // Pre-initialized page objects
    protected HomePage homePage;

    @BeforeMethod
    public void setup() {
        playwright = Playwright.create();

        // Get config with fallback: system property -> env variable -> .env file
        String headlessStr = getConfig("HEADLESS", "false");
        boolean headless = Boolean.parseBoolean(headlessStr);

        String browserChannel = getConfig("BROWSER", "chrome");

        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(headless)
                .setChannel(browserChannel)
                .setArgs(Arrays.asList("--disable-dev-shm-usage", "--no-sandbox", "--start-maximized")));

        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
        page = context.newPage();
        page.setDefaultTimeout(60000);

        // Initialize helpers
        nav = new NavigationHelper(page);
        form = new FormHelper(page);
        screenshot = new ScreenshotHelper(page);

        // Navigate to app and login
        String baseUrl = getConfig("BASE_URL", null);
        String username = getConfig("USERNAME", null);
        String password = getConfig("PASSWORD", null);

        if (baseUrl == null || username == null || password == null) {
            throw new RuntimeException("Missing required config: BASE_URL, USERNAME, or PASSWORD not set");
        }

        page.navigate(baseUrl, new Page.NavigateOptions().setTimeout(60000));
        LoginPage loginPage = new LoginPage(page);
        homePage = loginPage.login(username, password);
    }

    private String getConfig(String key, String defaultValue) {
        // Check system property first
        String value = System.getProperty(key);
        if (value != null && !value.isEmpty()) return value;

        // Check environment variable
        value = System.getenv(key);
        if (value != null && !value.isEmpty()) return value;

        // Fall back to .env file via ConfigReader
        value = ConfigReader.get(key);
        if (value != null && !value.isEmpty()) return value;

        return defaultValue;
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.FAILURE && page != null) {
                String testName = result.getMethod().getMethodName();
                String className = result.getTestClass().getRealClass().getSimpleName();
                screenshot.captureOnFailure(className, testName);
            }
        } catch (Exception e) {
            // Ignore screenshot failures during teardown
        }

        try { if (context != null) context.close(); } catch (Exception ignored) {}
        try { if (browser != null) browser.close(); } catch (Exception ignored) {}
        try { if (playwright != null) playwright.close(); } catch (Exception ignored) {}
    }
}
