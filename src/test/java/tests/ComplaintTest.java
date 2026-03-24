package tests;

import java.time.LocalDate;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.ComplaintPage;

public class ComplaintTest extends BaseTest {

    private String createAndCaptureComplaint() {
        String today = LocalDate.now().toString();
        ComplaintPage complaint = nav.goToCreateComplaint();
        return complaint.fillForm(today, "not working");
    }

    @Test
    public void createComplaint() {
        String complaintNumber = createAndCaptureComplaint();
        Assert.assertNotNull(complaintNumber, "Complaint number should be visible after creation");
        Assert.assertTrue(complaintNumber.startsWith("PGR-"), "Complaint number should start with PGR-");
    }

    @Test
    public void resolveComplaint() {
        String complaintNumber = createAndCaptureComplaint();

        // Navigate to search complaint
        ComplaintPage complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        complaint.searchAndResolve(complaintNumber, "Resolved");
    }

    @Test
    public void rejectComplaint() {
        String complaintNumber = createAndCaptureComplaint();

        // Navigate to search complaint
        ComplaintPage complaint = nav.complaintPage();
        complaint.clickBackToComplaintsInbox();
        complaint.searchAndReject(complaintNumber, "rejected");
    }
}
