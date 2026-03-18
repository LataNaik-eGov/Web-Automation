package pages;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.nio.file.Path;
import java.nio.file.Paths;

public class UploadFilePage {

    private Page page;

    // Upload file elements
    private Locator uploadDataButton;
    private Locator downloadTemplateButton;
    private Locator dragAndDropLabel;
    private Locator submit;

    public UploadFilePage(Page page) {
        this.page = page;
        this.uploadDataButton = page.locator("#campaign-details-page-button-unified-console-data-upload");
        this.downloadTemplateButton = page.locator("#file-download-template-popup");
        this.dragAndDropLabel = page.locator(".upload-drag-drop-container");
        this.submit = page.locator("#campaign-unified-upload-screen-formcomposer-form-primary-submit-btn");
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
        dragAndDropLabel.setInputFiles(Paths.get(filePath));
        page.waitForTimeout(3000);
        submit.click();
    }
}
