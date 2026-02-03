package com.floodapp.flood_impact.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    // --- NEW: Verification Email ---
    public void sendVerificationEmail(String toEmail, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("praneethsbliyanage@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Flood Tracker - Verify Your Account");
        message.setText("Hello,\n\n" +
                "Thank you for registering with Infrastructure Command.\n" +
                "Please click the link below to verify your email address:\n\n" +
                "https://floodimpact.online/?token=" + token + "\n\n" +
                "If you did not create an account, please ignore this email.\n\n" +
                "Best regards,\nFlood Tracker Team");

        mailSender.send(message);
        System.out.println("Verification email sent to " + toEmail);
    }

    public void sendResetEmail(String toEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("praneethsbliyanage@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Flood Tracker - Password Reset Request");
        message.setText("Hello,\n\n" +
                "We received a request to reset your password for your Flood Damage Tracker account.\n\n" +
                "Please click the link below to reset your password:\n" +
                "https://floodimpact.online/reset-password?email=" + toEmail + "\n\n" +
                "If you did not request this, please ignore this email.\n\n" +
                "Best regards,\nFlood Tracker Team");

        mailSender.send(message);
        System.out.println("Reset mail sent to " + toEmail);
    }
}