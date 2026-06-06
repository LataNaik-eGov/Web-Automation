package pages;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import com.microsoft.playwright.options.WaitForSelectorState;

import java.nio.file.Paths;

public class UploadFilePage extends BasePage {

    // Upload file elements
    private Locator uploadDataButton;
    private Locator downloadTemplateButton;
    private Locator submit;
    private Locator noFileToast;
    private Locator cancelIcon;

    public UploadFilePage(Page page) {
        super(page);
        this.uploadDataButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Upload Data"));
        this.downloadTemplateButton = page.locator("#file-download-template");
        this.submit = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit"));
        this.noFileToast = page.locator("[class*='digit-toast'], [role='alert'], .Toastify__toast")
                .filter(new Locator.FilterOptions().setHasText("Please upload a file"));
        this.cancelIcon = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Cancel"));
    }

    // --- Actions ---

    public void clickUploadData() {
        uploadDataButton.click();
        wait(1000);
    }

    public Download downloadTemplate() {
        Download download = page.waitForDownload(() -> {
            downloadTemplateButton.click();
        });
        return download;
    }

    public void uploadFile(String filePath) {
        wait(2000);
        page.waitForFileChooser(() -> {
            page.getByText("Browse in my files").click();
        }).setFiles(Paths.get(filePath));
        wait(5000);
    }

    public void closePopup() {
        cancelIcon.click();
    page.locator(".digit-popup-overlay").waitFor(
                new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

    public void clickSubmit() {
        submit.click();
    }

    public boolean isNoFileToastVisible() {
        noFileToast.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        boolean visible = noFileToast.isVisible();
        wait(3000);
        return visible;
    }
}
