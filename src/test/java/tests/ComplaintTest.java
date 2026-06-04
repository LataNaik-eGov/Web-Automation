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
        complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        Assert.assertTrue(complaint.isComplaintFound(complaintNumber), "Complaint " + complaintNumber + " should appear in search results");
    }

    @Test(groups = {"payments-ui"})
    public void createComplaintWithJpeg() {
        ComplaintPage complaint = nav.goToCreateComplaint();
        String complaintNumber = complaint.fillFormWithFile("not working", "src/test/resources/complaint.jpeg");
        Assert.assertNotNull(complaintNumber, "Complaint number should be visible after creation");
        Assert.assertTrue(complaintNumber.startsWith("PGR-"), "Complaint number should start with PGR-");
        complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        Assert.assertTrue(complaint.isComplaintFound(complaintNumber), "Complaint " + complaintNumber + " should appear in search results");
    }

    @Test(groups = {"payments-ui"})
    public void createComplaintWithPdf() {
        ComplaintPage complaint = nav.goToCreateComplaint();
        String complaintNumber = complaint.fillFormWithFile("not working", "src/test/resources/complaint.pdf");
        Assert.assertNotNull(complaintNumber, "Complaint number should be visible after creation");
        Assert.assertTrue(complaintNumber.startsWith("PGR-"), "Complaint number should start with PGR-");
        complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        Assert.assertTrue(complaint.isComplaintFound(complaintNumber), "Complaint " + complaintNumber + " should appear in search results");
    }

    @Test(groups = {"payments-ui"})
    public void resolveComplaint() {
        String complaintNumber = createAndCaptureComplaint();
        ComplaintPage complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        complaint.searchAndResolve(complaintNumber, "Resolved");
    }

    @Test(groups = {"payments-ui"})
    public void resolveComplaintWithJpeg() {
        ComplaintPage complaint = nav.goToCreateComplaint();
        String complaintNumber = complaint.fillFormWithFile("not working", "src/test/resources/complaint.jpeg");
        complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        complaint.searchAndResolve(complaintNumber, "Resolved with image", "src/test/resources/complaint.jpeg");
    }

    @Test(groups = {"payments-ui"})
    public void resolveComplaintWithPdf() {
        ComplaintPage complaint = nav.goToCreateComplaint();
        String complaintNumber = complaint.fillFormWithFile("not working", "src/test/resources/complaint.pdf");
        complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        complaint.searchAndResolve(complaintNumber, "Resolved with pdf", "src/test/resources/complaint.pdf");
    }

    @Test(groups = {"payments-ui"})
    public void assignComplaint() {
        String complaintNumber = createAndCaptureComplaint();
        ComplaintPage complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        complaint.searchAndAssign(complaintNumber, "Assigned");
    }

    @Test(groups = {"payments-ui"})
    public void assignComplaintWithJpeg() {
        ComplaintPage complaint = nav.goToCreateComplaint();
        String complaintNumber = complaint.fillFormWithFile("not working", "src/test/resources/complaint.jpeg");
        complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        complaint.searchAndAssign(complaintNumber, "Assigned with image");
    }

    @Test(groups = {"payments-ui"})
    public void assignComplaintWithPdf() {
        ComplaintPage complaint = nav.goToCreateComplaint();
        String complaintNumber = complaint.fillFormWithFile("not working", "src/test/resources/complaint.pdf");
        complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        complaint.searchAndAssign(complaintNumber, "Assigned with pdf");
    }

    @Test(groups = {"payments-ui"})
    public void rejectComplaint() {
        String complaintNumber = createAndCaptureComplaint();
        ComplaintPage complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        complaint.searchAndReject(complaintNumber, "rejected");
    }

    @Test(groups = {"payments-ui"})
    public void rejectComplaintWithJpeg() {
        ComplaintPage complaint = nav.goToCreateComplaint();
        String complaintNumber = complaint.fillFormWithFile("not working", "src/test/resources/complaint.jpeg");
        complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        complaint.searchAndReject(complaintNumber, "rejected with image", "src/test/resources/complaint.jpeg");
    }

    @Test(groups = {"payments-ui"})
    public void rejectComplaintWithPdf() {
        ComplaintPage complaint = nav.goToCreateComplaint();
        String complaintNumber = complaint.fillFormWithFile("not working", "src/test/resources/complaint.pdf");
        complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        complaint.searchAndReject(complaintNumber, "rejected with pdf", "src/test/resources/complaint.pdf");
    }
}
