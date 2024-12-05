package com.dev.projecttesting.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class OtpService {
    @Autowired
    private JavaMailSender mailSender;

    private static final int OTP_EXPIRATION_TIME = 2 * 60 * 1000;

    public String generateOtp()
    {
        return String.valueOf(new Random().nextInt(99999) + 200000);
    }

    public void sendOtp(String email, String otp) {
        String subject = "Your OTP for Account Registration";
        String body = "Hello,\n\n" +
                "Thank you for registering with us. To complete your registration process, please use the OTP below.\n\n" +
                "Your OTP: " + otp + "\n\n" +
                "Please note that the OTP is valid for 2 minutes, so make sure to use it within this time frame.\n\n" +
                "If you did not request this OTP, please ignore this message. For any issues, feel free to contact our support team.\n\n" +
                "Best regards,\nThe Support Team,\n" +
                "IndiaChronicles";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendEmail(String to, String subject, String body)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("testing63001@gmail.com");
        mailSender.send(message);
    }

    public void sendEmails(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);  // 'true' for multipart

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);  // 'true' indicates that the body is HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email");
        }
    }

    public boolean isOtpValid(String otp, Date otpTimestamp)
    {
        long currentTime = System.currentTimeMillis();
        return (currentTime - otpTimestamp.getTime()) <= OTP_EXPIRATION_TIME;
    }
}
