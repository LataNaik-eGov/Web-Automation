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

    @Test(groups = {"payments-ui", "Complaint", "sanity"})
    public void createComplaint() {
        ComplaintPage complaint = nav.goToCreateComplaint();
        String complaintNumber = complaint.fillForm("not working");
        Assert.assertNotNull(complaintNumber, "Complaint number should be visible after creation");
        Assert.assertTrue(complaintNumber.startsWith("PGR-"), "Complaint number should start with PGR-");
        complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        Assert.assertTrue(complaint.isComplaintFound(complaintNumber), "Complaint " + complaintNumber + " should appear in search results");
    }

    @Test(groups = {"payments-ui", "Complaint", "regression"})
    public void createComplaintWithJpeg() {
        ComplaintPage complaint = nav.goToCreateComplaint();
        String complaintNumber = complaint.fillFormWithFile("not working", "src/test/resources/complaint.jpeg");
        Assert.assertNotNull(complaintNumber, "Complaint number should be visible after creation");
        Assert.assertTrue(complaintNumber.startsWith("PGR-"), "Complaint number should start with PGR-");
        complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        Assert.assertTrue(complaint.isComplaintFound(complaintNumber), "Complaint " + complaintNumber + " should appear in search results");
    }

    @Test(groups = {"payments-ui", "Complaint", "regression"})
    public void createComplaintWithPdf() {
        ComplaintPage complaint = nav.goToCreateComplaint();
        String complaintNumber = complaint.fillFormWithFile("not working", "src/test/resources/complaint.pdf");
        Assert.assertNotNull(complaintNumber, "Complaint number should be visible after creation");
        Assert.assertTrue(complaintNumber.startsWith("PGR-"), "Complaint number should start with PGR-");
        complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        Assert.assertTrue(complaint.isComplaintFound(complaintNumber), "Complaint " + complaintNumber + " should appear in search results");
    }

    @Test(groups = {"payments-ui", "Complaint", "sanity"})
    public void resolveComplaint() {
        String complaintNumber = createAndCaptureComplaint();
        ComplaintPage complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        complaint.searchAndResolve(complaintNumber, "Resolved");
        Assert.assertTrue(complaint.isStatusVisible("Resolved"), "Status should be updated to Resolved");
    }

    @Test(groups = {"payments-ui", "Complaint", "regression"})
    public void resolveComplaintWithJpeg() {
        ComplaintPage complaint = nav.goToCreateComplaint();
        String complaintNumber = complaint.fillFormWithFile("not working", "src/test/resources/complaint.jpeg");
        complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        complaint.searchAndResolve(complaintNumber, "Resolved with image", "src/test/resources/complaint.jpeg");
        Assert.assertTrue(complaint.isStatusVisible("Resolved"), "Status should be updated to Resolved");
    }

    @Test(groups = {"payments-ui", "Complaint", "regression"})
    public void resolveComplaintWithPdf() {
        ComplaintPage complaint = nav.goToCreateComplaint();
        String complaintNumber = complaint.fillFormWithFile("not working", "src/test/resources/complaint.pdf");
        complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        complaint.searchAndResolve(complaintNumber, "Resolved with pdf", "src/test/resources/complaint.pdf");
        Assert.assertTrue(complaint.isStatusVisible("Resolved"), "Status should be updated to Resolved");
    }

    @Test(groups = {"payments-ui", "Complaint", "sanity"})
    public void assignComplaint() {
        String complaintNumber = createAndCaptureComplaint();
        ComplaintPage complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        complaint.searchAndAssign(complaintNumber, "Assigned");
        Assert.assertTrue(complaint.isStatusVisible("Assigned"), "Status should be updated to Assigned");
    }

    @Test(groups = {"payments-ui", "Complaint", "regression"})
    public void assignComplaintWithJpeg() {
        ComplaintPage complaint = nav.goToCreateComplaint();
        String complaintNumber = complaint.fillFormWithFile("not working", "src/test/resources/complaint.jpeg");
        complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        complaint.searchAndAssign(complaintNumber, "Assigned with image");
        Assert.assertTrue(complaint.isStatusVisible("Assigned"), "Status should be updated to Assigned");
    }

    @Test(groups = {"payments-ui", "Complaint", "regression"})
    public void assignComplaintWithPdf() {
        ComplaintPage complaint = nav.goToCreateComplaint();
        String complaintNumber = complaint.fillFormWithFile("not working", "src/test/resources/complaint.pdf");
        complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        complaint.searchAndAssign(complaintNumber, "Assigned with pdf");
        Assert.assertTrue(complaint.isStatusVisible("Assigned"), "Status should be updated to Assigned");
    }

    @Test(groups = {"payments-ui", "Complaint", "sanity"})
    public void createAssignAndResolveComplaint() {
        String complaintNumber = createAndCaptureComplaint();
        ComplaintPage complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        complaint.searchAndAssign(complaintNumber, "Assigned");
        Assert.assertTrue(complaint.isStatusVisible("Assigned"), "Status should be updated to Assigned");
        complaint.resolve("Resolved", null);
        Assert.assertTrue(complaint.isStatusVisible("Resolved"), "Status should be updated to Resolved");
    }

    @Test(groups = {"payments-ui", "Complaint", "regression"})
    public void createAssignAndResolveComplaintWithJpeg() {
        ComplaintPage complaint = nav.goToCreateComplaint();
        String complaintNumber = complaint.fillFormWithFile("not working", "src/test/resources/complaint.jpeg");
        complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        complaint.searchAndAssign(complaintNumber, "Assigned", "src/test/resources/complaint.jpeg");
        Assert.assertTrue(complaint.isStatusVisible("Assigned"), "Status should be updated to Assigned");
        complaint.resolve("Resolved", "src/test/resources/complaint.jpeg");
        Assert.assertTrue(complaint.isStatusVisible("Resolved"), "Status should be updated to Resolved");
    }

    @Test(groups = {"payments-ui", "Complaint", "regression"})
    public void createAssignAndResolveComplaintWithPdf() {
        ComplaintPage complaint = nav.goToCreateComplaint();
        String complaintNumber = complaint.fillFormWithFile("not working", "src/test/resources/complaint.pdf");
        complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        complaint.searchAndAssign(complaintNumber, "Assigned", "src/test/resources/complaint.pdf");
        Assert.assertTrue(complaint.isStatusVisible("Assigned"), "Status should be updated to Assigned");
        complaint.resolve("Resolved", "src/test/resources/complaint.pdf");
        Assert.assertTrue(complaint.isStatusVisible("Resolved"), "Status should be updated to Resolved");
    }

    @Test(groups = {"payments-ui", "Complaint", "sanity"})
    public void createAssignAndRejectComplaint() {
        String complaintNumber = createAndCaptureComplaint();
        ComplaintPage complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        complaint.searchAndAssign(complaintNumber, "Assigned");
        Assert.assertTrue(complaint.isStatusVisible("Assigned"), "Status should be updated to Assigned");
        complaint.reject("rejected", null);
        Assert.assertTrue(complaint.isStatusVisible("Rejected"), "Status should be updated to Rejected");
    }

    @Test(groups = {"payments-ui", "Complaint", "regression"})
    public void createAssignAndRejectComplaintWithJpeg() {
        ComplaintPage complaint = nav.goToCreateComplaint();
        String complaintNumber = complaint.fillFormWithFile("not working", "src/test/resources/complaint.jpeg");
        complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        complaint.searchAndAssign(complaintNumber, "Assigned", "src/test/resources/complaint.jpeg");
        Assert.assertTrue(complaint.isStatusVisible("Assigned"), "Status should be updated to Assigned");
        complaint.reject("rejected", "src/test/resources/complaint.jpeg");
        Assert.assertTrue(complaint.isStatusVisible("Rejected"), "Status should be updated to Rejected");
    }

    @Test(groups = {"payments-ui", "Complaint", "regression"})
    public void createAssignAndRejectComplaintWithPdf() {
        ComplaintPage complaint = nav.goToCreateComplaint();
        String complaintNumber = complaint.fillFormWithFile("not working", "src/test/resources/complaint.pdf");
        complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        complaint.searchAndAssign(complaintNumber, "Assigned", "src/test/resources/complaint.pdf");
        Assert.assertTrue(complaint.isStatusVisible("Assigned"), "Status should be updated to Assigned");
        complaint.reject("rejected", "src/test/resources/complaint.pdf");
        Assert.assertTrue(complaint.isStatusVisible("Rejected"), "Status should be updated to Rejected");
    }

    @Test(groups = {"payments-ui", "Complaint", "sanity"})
    public void rejectComplaint() {
        String complaintNumber = createAndCaptureComplaint();
        ComplaintPage complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        complaint.searchAndReject(complaintNumber, "rejected");
        Assert.assertTrue(complaint.isStatusVisible("Rejected"), "Status should be updated to Rejected");
    }

    @Test(groups = {"payments-ui", "Complaint", "regression"})
    public void rejectComplaintWithJpeg() {
        ComplaintPage complaint = nav.goToCreateComplaint();
        String complaintNumber = complaint.fillFormWithFile("not working", "src/test/resources/complaint.jpeg");
        complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        complaint.searchAndReject(complaintNumber, "rejected with image", "src/test/resources/complaint.jpeg");
        Assert.assertTrue(complaint.isStatusVisible("Rejected"), "Status should be updated to Rejected");
    }

    @Test(groups = {"payments-ui", "Complaint", "regression"})
    public void rejectComplaintWithPdf() {
        ComplaintPage complaint = nav.goToCreateComplaint();
        String complaintNumber = complaint.fillFormWithFile("not working", "src/test/resources/complaint.pdf");
        complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        complaint.searchAndReject(complaintNumber, "rejected with pdf", "src/test/resources/complaint.pdf");
        Assert.assertTrue(complaint.isStatusVisible("Rejected"), "Status should be updated to Rejected");
    }
}
