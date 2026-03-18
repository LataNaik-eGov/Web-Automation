package pages;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CreateChecklist {

    private Page page;

    // Create checklist
    private Locator createChecklist;
    private Locator configureList;
    private Locator configureChecklist;
    private Locator confirmChecklist;

     // Create campaign

    public CreateChecklistPage(Page page) {
        this.page = page;
        this.createChecklist = page.locator("#campaign-details-page-button-checklist");
        this.configureList = page.locator(".digit-button-secondary.medium");
        this.configureChecklist = page.locator("#campaign-checklist-create-standalone-form-field-primary");
        this.confirmChecklist = page.locator(".digit-button-primary.large");
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
        page.waitForTimeout(1000);
        submit.click();
    }
}
