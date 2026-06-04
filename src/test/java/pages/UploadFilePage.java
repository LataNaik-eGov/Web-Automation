package pages;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.nio.file.Paths;

public class UploadFilePage {

    private Page page;

    // Upload file elements
    private Locator uploadDataButton;
    private Locator downloadTemplateButton;
    private Locator fileInputBody;
    private Locator submit;
    private Locator noFileToast;

    public UploadFilePage(Page page) {
        this.page = page;
        this.uploadDataButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Upload Data"));
        this.downloadTemplateButton = page.locator("#file-download-template");
        this.fileInputBody = page.locator("input[type='file']");
        this.submit = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit"));
        this.noFileToast = page.getByText("Please upload a file");
    }

    // --- Actions ---

    public void clickUploadData() {
        uploadDataButton.click();
    }

    public Download downloadTemplate() {
        Download download = page.waitForDownload(() -> {
            downloadTemplateButton.click();
        });
        return download;
    }

    public void uploadFile(String filePath) {
        fileInputBody.setInputFiles(Paths.get(filePath));
    }

    public void closePopupByClickingOutside() {
        page.mouse().click(50, 50);
    }

    public void clickSubmit() {
        submit.dispatchEvent("click");
    }

    public boolean isNoFileToastVisible() {
        noFileToast.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        return noFileToast.isVisible();
    }
}
