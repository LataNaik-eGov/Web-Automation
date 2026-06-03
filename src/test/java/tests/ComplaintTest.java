package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.ComplaintPage;

public class ComplaintTest extends BaseTest {

    private String createAndCaptureComplaint() {
        ComplaintPage complaint = nav.goToCreateComplaint();
        return complaint.fillForm("not working");
    }

    @Test(groups = {"payments-ui"})
    public void createComplaint() {
        ComplaintPage complaint = nav.goToCreateComplaint();
        String complaintNumber = complaint.fillForm("not working");
        Assert.assertNotNull(complaintNumber, "Complaint number should be visible after creation");
        Assert.assertTrue(complaintNumber.startsWith("PGR-"), "Complaint number should start with PGR-");
    }

    @Test(groups = {"payments-ui"})
    public void createComplaintWithJpeg() {
        ComplaintPage complaint = nav.goToCreateComplaint();
        String complaintNumber = complaint.fillFormWithFile("not working", "src/test/resources/complaint.jpeg");
        Assert.assertNotNull(complaintNumber, "Complaint number should be visible after creation");
        Assert.assertTrue(complaintNumber.startsWith("PGR-"), "Complaint number should start with PGR-");
    }

    @Test(groups = {"payments-ui"})
    public void createComplaintWithPdf() {
        ComplaintPage complaint = nav.goToCreateComplaint();
        String complaintNumber = complaint.fillFormWithFile("not working", "src/test/resources/complaint.pdf");
        Assert.assertNotNull(complaintNumber, "Complaint number should be visible after creation");
        Assert.assertTrue(complaintNumber.startsWith("PGR-"), "Complaint number should start with PGR-");
    }

    @Test(groups = {"payments-ui"})
    public void resolveComplaint() {
        String complaintNumber = createAndCaptureComplaint();

        // Navigate to search complaint
        ComplaintPage complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        complaint.searchAndResolve(complaintNumber, "Resolved");
    }

    @Test(groups = {"payments-ui"})
    public void rejectComplaint() {
        String complaintNumber = createAndCaptureComplaint();

        // Navigate to search complaint
        ComplaintPage complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        complaint.searchAndReject(complaintNumber, "rejected");
    }
}
