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
    private Locator fileInputBody;
    private Locator submit;

    public UploadFilePage(Page page) {
        this.page = page;
        this.uploadDataButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Upload Data"));
        this.downloadTemplateButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Do you want to download")).getByLabel("Download Template");
        this.dragAndDropLabel = page.locator("label").filter(new Locator.FilterOptions().setHasText("Drag and drop your filled"));
        this.fileInputBody = page.locator("input[type='file']");
        this.submit = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit"));
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
        dragAndDropLabel.click();
        fileInputBody.setInputFiles(Paths.get(filePath));
        page.waitForTimeout(3000);
        submit.click();
    }
}
