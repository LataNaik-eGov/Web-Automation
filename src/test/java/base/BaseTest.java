package base;

import java.util.Arrays;

import io.qameta.allure.testng.AllureTestNg;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;

import pages.HomePage;
import pages.LoginPage;
import utils.ConfigReader;
import utils.FormHelper;
import utils.NavigationHelper;
import utils.ScreenshotHelper;
import utils.TestDataReader;

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
@Listeners(AllureTestNg.class)
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

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        TestDataReader.clearSession();
        playwright = Playwright.create();

        // Read config from .env file via ConfigReader (works for local and CI)
        String headlessStr = ConfigReader.get("HEADLESS");
        boolean headless = Boolean.parseBoolean(headlessStr != null ? headlessStr : "false");

        String browserChannel = ConfigReader.get("BROWSER");
        if (browserChannel == null) browserChannel = "chrome";

        // Display environment info
        System.out.println("==========================================");
        System.out.println("  TEST EXECUTION ENVIRONMENT (BaseTest)");
        System.out.println("==========================================");
        System.out.println("BASE_URL: " + ConfigReader.get("BASE_URL"));
        System.out.println("USERNAME: " + ConfigReader.get("USERNAME"));
        System.out.println("BROWSER: " + browserChannel);
        System.out.println("HEADLESS: " + headless);
        System.out.println("==========================================");

        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(headless)
                .setArgs(Arrays.asList("--disable-dev-shm-usage", "--no-sandbox", "--start-maximized"));
        if (browserChannel != null && !browserChannel.equalsIgnoreCase("chromium")) {
            launchOptions.setChannel(browserChannel);
        }
        browser = playwright.chromium().launch(launchOptions);

        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions();
        if (headless) {
            // --start-maximized has no effect in headless mode, so the viewport
            // would otherwise default to a small size that collapses the header
            // and overlaps form fields.
            contextOptions.setViewportSize(1920, 1080);
        } else {
            contextOptions.setViewportSize(null);
        }
        context = browser.newContext(contextOptions);
        page = context.newPage();
        // 30s was too tight for this app: the campaign setup chain drives eight
        // mobile-app module screens, each a save-and-return round trip, and demo
        // latency intermittently pushed individual steps past the ceiling. That
        // produced repeated failures on locators proven correct by passing runs
        // (STOCKREPORTS, PERMISSIONHANDLER, the delivery Submit, and login itself),
        // none of them assertion failures. Every wait that actually gates a result
        // is explicit elsewhere, so this only affects how long a step may take.
        page.setDefaultTimeout(60000);

        // Initialize helpers
        nav = new NavigationHelper(page);
        form = new FormHelper(page);
        screenshot = new ScreenshotHelper(page);

        // Navigate to app and login
        String baseUrl = ConfigReader.get("BASE_URL");
        String username = ConfigReader.get("USERNAME");
        String password = ConfigReader.get("PASSWORD");

        if (baseUrl == null || username == null || password == null) {
            throw new RuntimeException("Missing required config in .env file: BASE_URL, USERNAME, or PASSWORD not set");
        }

        page.navigate(baseUrl, new Page.NavigateOptions().setTimeout(120000));
        page.waitForLoadState(LoadState.NETWORKIDLE);
        LoginPage loginPage = new LoginPage(page);
        homePage = loginPage.login(username, password);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE && page != null && !page.isClosed()) {
            try {
                String testName = result.getMethod().getMethodName();
                String className = result.getTestClass().getRealClass().getSimpleName();
                screenshot.captureOnFailure(className, testName);
            } catch (Exception e) {
                System.err.println("Failed to capture failure screenshot: " + e.getMessage());
            }
        }

        try { if (context != null) context.close(); } catch (Exception ignored) {}
        try { if (browser != null) browser.close(); } catch (Exception ignored) {}
        try { if (playwright != null) playwright.close(); } catch (Exception ignored) {}
    }
}
