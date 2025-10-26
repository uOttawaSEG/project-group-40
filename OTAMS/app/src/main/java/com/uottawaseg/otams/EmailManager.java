package com.uottawaseg.otams;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

// not sure where u want to keep this manager so i kept it outside all the big folders.
// if u decide to move it, dont forget to change the package

// i also modified AdminClientInfo to make it so that when admin clicks the accept or deny button,
// it sends the email immediately

// possible reasons to why it doesnt work:
// it cant read the email from student/tutor
// u need to sign in to gmail on the emulator phone (im grasping at straws fr)


/**
 * Handles sending emails to registrants when their requests are accepted or denied.
 */
public class EmailManager {

    private final Context _context;

    public EmailManager(Context context) {
        this._context = context;
    }


    /**
     * Sends an email notifying the registrant that their request has been accepted.
     *
     * @param recipientEmail the email of the registrant
     * @param registrantName the name of the registrant
     */
    public void sendAcceptanceEmail(String recipientEmail, String registrantName) {
        String subject = "Request Accepted";
        String message = "Dear " + registrantName + ",\n\n" +
                "Your registration request has been accepted.\n\n" +
                "Welcome to OTAMS!\n\n" +
                "Good luck,\n" +
                "The Administration";

        _sendEmail(recipientEmail, subject, message);
    }


    /**
     * Sends an email notifying the registrant that their request has been denied.
     *
     * @param recipientEmail the email of the applicant
     * @param registrantName the name of the applicant
     */
    public void sendRejectionEmail(String recipientEmail, String registrantName) {
        String subject = "Request Denied";
        String message = "Dear " + registrantName + ",\n\n" +
                "Your registration request has been denied.\n" +
                "If you have any questions, please contact our support line at (420) 696-6767.\n\n" +
                "Sincerely,\n" +
                "The Administration";

        _sendEmail(recipientEmail, subject, message);
    }


    /**
     * Creates and starts an email intent.
     *
     * @param recipientEmail recipient email address
     * @param subject subject of the email
     * @param body body of the email
     */
    private void _sendEmail(String recipientEmail, String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + recipientEmail));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);

        // Check if there is an email app to handle this intent
        if (intent.resolveActivity(_context.getPackageManager()) != null) {
            _context.startActivity(intent);
        }
    }

}
