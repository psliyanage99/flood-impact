package com.floodapp.flood_impact.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    // --- UPDATED: Throws exception instead of catching it ---
    public void sendVerificationEmail(String toEmail, String token) throws MessagingException {
        String verifyUrl = "https://floodimpact.online/?token=" + token;

        String htmlContent = "<html>"
                + "<body style='font-family: Arial, sans-serif;'>"
                + "<div style='background-color: #f4f4f4; padding: 20px;'>"
                + "  <div style='background-color: #ffffff; padding: 30px; border-radius: 10px; max-width: 500px; margin: 0 auto;'>"
                + "    <h2 style='color: #333;'>Verify Your Account</h2>"
                + "    <p>Thank you for registering with <strong>Infrastructure Command</strong>.</p>"
                + "    <p>Please click the button below to verify your email address:</p>"
                + "    <br>"
                + "    <a href='" + verifyUrl + "' style='background-color: #2563EB; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px; font-weight: bold; display: inline-block;'>Verify My Email</a>"
                + "    <br><br>"
                + "    <p style='font-size: 12px; color: #777;'>If the button doesn't work, copy and paste this link into your browser:</p>"
                + "    <p style='font-size: 12px; color: #555; word-break: break-all;'>" + verifyUrl + "</p>"
                + "  </div>"
                + "</div>"
                + "</body>"
                + "</html>";

        // No try-catch here! Let the error propagate to the Controller.
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("praneethsbliyanage@gmail.com");
        helper.setTo(toEmail);
        helper.setSubject("Action Required: Verify your Flood Tracker Account");
        helper.setText(htmlContent, true);

        mailSender.send(message);
        System.out.println("HTML Verification email sent to " + toEmail);
    }

    public void sendResetEmail(String toEmail) throws MessagingException {
        String resetUrl = "https://floodimpact.online/reset-password?email=" + toEmail;
        String htmlContent = "<html><body>"
                + "<h3>Password Reset Request</h3>"
                + "<p>Click the link below to reset your password:</p>"
                + "<a href='" + resetUrl + "'>Reset Password</a>"
                + "</body></html>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("praneethsbliyanage@gmail.com");
        helper.setTo(toEmail);
        helper.setSubject("Flood Tracker - Password Reset");
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}