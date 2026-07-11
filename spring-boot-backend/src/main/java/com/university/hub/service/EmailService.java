package com.university.hub.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${app.mail.from:noreply@universityhub.edu}")
    private String fromEmail;

    /**
     * Sends a real HTML e-mail for account activation (6-digit OTP).
     */
    public void sendVerificationEmail(String toEmail, String username, String verificationCode) {
        String subject = "Verify Your University Hub Account - Code: " + verificationCode;
        String htmlContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e2e8f0; border-radius: 12px; background-color: #ffffff;'>"
                + "<div style='text-align: center; border-bottom: 2px solid #3b82f6; padding-bottom: 15px; margin-bottom: 20px;'>"
                + "  <h1 style='color: #1e3a8a; margin: 0; font-size: 24px;'>University Hub</h1>"
                + "  <p style='color: #64748b; margin: 5px 0 0 0; font-size: 14px;'>Unified Academic Portal</p>"
                + "</div>"
                + "<p style='font-size: 16px; color: #334155;'>Hello <strong>" + username + "</strong>,</p>"
                + "<p style='font-size: 14px; color: #475569; line-height: 1.5;'>Thank you for register-boarding with us. Please use the following 6-digit verification code to activate your portal credentials:</p>"
                + "<div style='text-align: center; margin: 30px 0;'>"
                + "  <span style='display: inline-block; font-family: monospace; font-size: 32px; font-weight: bold; letter-spacing: 6px; color: #1d4ed8; background-color: #eff6ff; padding: 12px 24px; border: 2px dashed #bfdbfe; border-radius: 8px;'>" + verificationCode + "</span>"
                + "</div>"
                + "<p style='font-size: 12px; color: #ef4444; font-weight: bold; text-align: center;'>This code is active for 15 minutes. Do not share this credential.</p>"
                + "<hr style='border: 0; border-top: 1px solid #e2e8f0; margin: 25px 0;'>"
                + "<p style='font-size: 11px; color: #94a3b8; text-align: center;'>If you did not sign up for this portal account, please ignore this email.</p>"
                + "</div>";

        sendHtmlEmail(toEmail, subject, htmlContent);
    }

    /**
     * Sends a real HTML e-mail for password recovery (6-digit OTP).
     */
    public void sendPasswordResetEmail(String toEmail, String username, String resetCode) {
        String subject = "Reset Your Password - Code: " + resetCode;
        String htmlContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e2e8f0; border-radius: 12px; background-color: #ffffff;'>"
                + "<div style='text-align: center; border-bottom: 2px solid #ef4444; padding-bottom: 15px; margin-bottom: 20px;'>"
                + "  <h1 style='color: #7f1d1d; margin: 0; font-size: 24px;'>University Hub</h1>"
                + "  <p style='color: #64748b; margin: 5px 0 0 0; font-size: 14px;'>Account Recovery desk</p>"
                + "</div>"
                + "<p style='font-size: 16px; color: #334155;'>Dear <strong>" + username + "</strong>,</p>"
                + "<p style='font-size: 14px; color: #475569; line-height: 1.5;'>We received a password reset query for your University Hub credentials. Input the 6-digit recovery code to set a new password:</p>"
                + "<div style='text-align: center; margin: 30px 0;'>"
                + "  <span style='display: inline-block; font-family: monospace; font-size: 32px; font-weight: bold; letter-spacing: 6px; color: #b91c1c; background-color: #fef2f2; padding: 12px 24px; border: 2px dashed #fecaca; border-radius: 8px;'>" + resetCode + "</span>"
                + "</div>"
                + "<p style='font-size: 12px; color: #e11d48; font-weight: bold; text-align: center;'>If you did not request this, please secure your credentials immediately.</p>"
                + "<hr style='border: 0; border-top: 1px solid #e2e8f0; margin: 25px 0;'>"
                + "<p style='font-size: 11px; color: #94a3b8; text-align: center;'>University Registry Security Division</p>"
                + "</div>";

        sendHtmlEmail(toEmail, subject, htmlContent);
    }

    private void sendHtmlEmail(String toEmail, String subject, String htmlBody) {
        if (mailSender == null) {
            System.out.println("\n=========================================");
            System.out.println("[SMTP NOT CONFIGURED] Fallback Mock Alert:");
            System.out.println("TO: " + toEmail);
            System.out.println("SUBJECT: " + subject);
            System.out.println("HTML CONTENT:\n" + htmlBody);
            System.out.println("=========================================\n");
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // True marks it as HTML content
            
            mailSender.send(message);
            System.out.println("[MAILER] Successfully sent HTML e-mail to: " + toEmail);
        } catch (MessagingException e) {
            System.err.println("[MAILER ERROR] Failed to dispatch MIME message: " + e.getMessage());
            throw new RuntimeException("E-mail delivery error: " + e.getMessage(), e);
        }
    }
}
