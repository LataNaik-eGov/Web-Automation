package pages;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import com.microsoft.playwright.options.WaitForSelectorState;

import java.nio.file.Path;
import java.nio.file.Paths;

import utils.MicroplanTemplateFiller;

public class UploadFilePage extends BasePage {

    // Upload file elements
    private Locator uploadDataButton;
    private Locator downloadTemplateButton;
    private Locator fileInput;
    private Locator submit;
    private Locator noFileToast;
    private Locator cancelIcon;
    private Locator validationCard;
    private Locator validationCardMessage;
    private Locator validationErrorCard;
    private Locator fileRejectedToast;

    public UploadFilePage(Page page) {
        super(page);
        this.uploadDataButton = page.locator("#campaign-details-page-button-unified-console-data-upload")
                .or(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Upload Data")))
                .first();
        this.downloadTemplateButton = page.locator("#file-download-template");
        this.fileInput = page.locator("input#file[type='file']");
        this.submit = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit"));
        // Submit with nothing uploaded: there is no file row for a card to sit under,
        this.noFileToast = page.locator(
                        "[class*='digit-toast'], [role='alert'], .Toastify__toast, .validation-alert-card")
                .filter(new Locator.FilterOptions().setHasText("Please upload a file"));
        this.cancelIcon = page.locator("button[aria-label='Cancel']");

        this.validationCard = page.locator(".validation-alert-card").first();
        this.validationCardMessage = this.validationCard.locator(".digit-infobanner-header").first();
        // A rejected upload renders the same card without the success state class,
        // so failure is detected structurally rather than by message text.
        this.validationErrorCard = page.locator(".validation-alert-card:not(.success)").first();
        // A wrong file *type* is rejected client-side before any server validation
        // and reports through a toast only — no result card is rendered for it.
        // Verified on hcm-demo 2026-09-09: div.digit-toast-success.digit-error,
        // "Please upload valid excel file as per template only."
        this.fileRejectedToast = page.locator("[class*='digit-toast'][class*='digit-error']").first();
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
        fileInput.setInputFiles(Paths.get(filePath));
        // Kept short on purpose: a rejection toast is transient, and a longer sleep
        // here outlived it. Waiting for validation to finish is the job of
        // waitForUploadSuccess()/isValidationErrorCardVisible(), not of this method.
        wait(1500);
    }

    public void closePopup() {
        cancelIcon.click();
        wait(500);
        page.locator(".digit-popup-overlay").waitFor(
                new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

    public void clickSubmit() {
        submit.click();
    }

    /** Where the downloaded and filled templates are kept for post-run inspection. */
    private static final Path TEMPLATE_DIR = Paths.get("target", "templates");

    /**
     * Downloads the template this campaign generated, fills it, and uploads it.
     *
     * The template must be round-tripped rather than taken from test resources:
     * it is generated per campaign, carrying that campaign's own boundaries in
     * "Boundary List" and a campaign uuid in a hidden sheet. A committed file
     * built for another hierarchy fails validation, which — combined with an
     * unbounded wait on the success toast — used to hang the test rather than
     * fail it.
     *
     * @return the filled workbook, kept under target/templates for inspection
     */
    public Path downloadFillAndUploadTemplate() {
        Download download = downloadTemplate();
        String name = download.suggestedFilename();
        Path raw = TEMPLATE_DIR.resolve(name);
        download.saveAs(raw);
        System.out.println("[Upload] Downloaded template -> " + raw);

        Path filled = TEMPLATE_DIR.resolve("filled-" + name);
        MicroplanTemplateFiller.fill(raw, filled);

        uploadFile(filled.toAbsolutePath().toString());
        return filled;
    }

    /**
     * Waits for the validation result card below the uploaded file, and fails if
     * that card is not the success one.
     *
     * Waiting for whichever card appears (rather than for the success card alone)
     * means a rejected upload reports the card's own message instead of timing
     * out with nothing to go on.
     *
     * Bounded deliberately: an unbounded wait turns a failed upload into a hung
     * suite instead of a reported failure.
     */
    public void waitForUploadSuccess() {
        waitForUploadSuccess(600000);
    }

    public void waitForUploadSuccess(double timeoutMs) {
        validationCard.waitFor(new Locator.WaitForOptions().setTimeout(timeoutMs));
        String classes = validationCard.getAttribute("class");
        if (classes == null || !classes.contains("success")) {
            throw new AssertionError("Upload validation did not succeed. The result card said: \""
                    + getValidationMessage() + "\" (card classes: " + classes + ")");
        }
        System.out.println("[Upload] " + getValidationMessage());
    }

    /** Message shown on the validation result card. */
    public String getValidationMessage() {
        return validationCardMessage.innerText().trim();
    }

    public boolean isNoFileToastVisible() {
        noFileToast.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        boolean visible = noFileToast.isVisible();
        wait(3000);
        return visible;
    }

    /**
     * Whether the app rejected the file outright with an error toast.
     *
     * This is the wrong-file-type surface: the rejection happens client-side, so
     * no result card is ever rendered and asserting on the card would simply time
     * out. Content and structure problems report through the card instead — see
     * {@link #isValidationErrorCardVisible()}.
     */
    public boolean isFileRejectedToastVisible() {
        fileRejectedToast.waitFor(new Locator.WaitForOptions().setTimeout(30000));
        boolean visible = fileRejectedToast.isVisible();
        if (visible) {
            System.out.println("[Upload] file rejected: " + fileRejectedToast.innerText().trim());
        }
        return visible;
    }

    /**
     * Whether the validation result card reports a failure.
     *
     * A rejected upload reports through the same card as a successful one, below
     * the uploaded file, distinguished only by the absence of the success state
     * class — so this must not be asserted via toast text.
     *
     * The wait is longer than a toast's would be because a file that is
     * structurally valid but has bad content is only rejected after the server
     * validates it.
     */
    public boolean isValidationErrorCardVisible() {
        validationErrorCard.waitFor(new Locator.WaitForOptions().setTimeout(120000));
        boolean visible = validationErrorCard.isVisible();
        if (visible) {
            System.out.println("[Upload] validation error card: " + getValidationMessage());
        }
        return visible;
    }
}
