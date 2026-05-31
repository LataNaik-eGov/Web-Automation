package base;

import java.util.Arrays;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

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

public class BaseTest {

    // Suite-level: launched once for the entire test run
    private static Playwright playwright;
    private static Browser browser;

    // Test-level: fresh context + page per test for isolation
    protected BrowserContext context;
    protected Page page;

    // Helpers available in all tests
    protected NavigationHelper nav;
    protected FormHelper form;
    protected ScreenshotHelper screenshot;

    // Pre-initialized page objects
    protected HomePage homePage;

    @BeforeSuite(alwaysRun = true)
    public static void launchBrowser() {
        playwright = Playwright.create();

        String headlessStr = ConfigReader.get("HEADLESS");
        boolean headless = Boolean.parseBoolean(headlessStr != null ? headlessStr : "false");

        String browserChannel = ConfigReader.get("BROWSER");
        if (browserChannel == null) browserChannel = "chrome";

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
        if (!browserChannel.equalsIgnoreCase("chromium")) {
            launchOptions.setChannel(browserChannel);
        }
        browser = playwright.chromium().launch(launchOptions);
    }

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
        page = context.newPage();
        page.setDefaultTimeout(60000);

        nav = new NavigationHelper(page);
        form = new FormHelper(page);
        screenshot = new ScreenshotHelper(page);

        String baseUrl = ConfigReader.get("BASE_URL");
        String username = ConfigReader.get("USERNAME");
        String password = ConfigReader.get("PASSWORD");

        if (baseUrl == null || username == null || password == null) {
            throw new RuntimeException("Missing required config in .env file: BASE_URL, USERNAME, or PASSWORD not set");
        }

        page.navigate(baseUrl, new Page.NavigateOptions().setTimeout(60000));
        LoginPage loginPage = new LoginPage(page);
        homePage = loginPage.login(username, password);
    }

    @AfterMethod(alwaysRun = true)
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
    }

    @AfterSuite(alwaysRun = true)
    public static void closeBrowser() {
        try { if (browser != null) browser.close(); } catch (Exception ignored) {}
        try { if (playwright != null) playwright.close(); } catch (Exception ignored) {}
    }
}
