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
    private Locator dragAndDropLabel;
    private Locator fileInputBody;
    private Locator submit;
    private Locator noFileToast;
    private Locator uploadDataLabel;

    public UploadFilePage(Page page) {
        this.page = page;
        this.uploadDataButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Upload Data"));
        this.downloadTemplateButton = page.locator("#file-download-template");
        this.dragAndDropLabel = page.locator("label").filter(new Locator.FilterOptions().setHasText("Drag and drop your filled"));
        this.fileInputBody = page.locator("input[type='file']");
        this.submit = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit"));
        this.noFileToast = page.getByText("Please upload a file");
        this.uploadDataLabel = page.getByText("Upload Data").first();
    }

    // --- Actions ---

    public void clickUploadData() {
        uploadDataButton.click();
        page.waitForTimeout(1000);
    }

    public Download downloadTemplate() {
        Download download = page.waitForDownload(() -> {
            downloadTemplateButton.click();
        });
        page.waitForTimeout(1000);
        return download;
    }

    public void uploadFile(String filePath) {
        fileInputBody.setInputFiles(Paths.get(filePath));
        page.waitForTimeout(3000);
    }

    public void closePopupByClickingOutside() {
        page.mouse().click(50, 50);
        page.waitForTimeout(1000);
    }

    public void clickSubmit() {
        submit.dispatchEvent("click");
        page.waitForTimeout(2000);
    }

    public void clickUploadDataLabel() {
        uploadDataLabel.click();
        page.waitForTimeout(1000);
    }

    public boolean isNoFileToastVisible() {
        noFileToast.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        return noFileToast.isVisible();
    }
}
