//package com.pilotcoupondispatchservice.dao;
//
//import com.pilotcoupondispatchservice.constants.MailTemplate;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.mail.MailException;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//import jakarta.mail.internet.MimeMessage;
//import java.util.Map;
//
////
//@Slf4j
//@Service
//@AllArgsConstructor
//public class EmailSenderServiceImpl implements EmailSenderService {
//
//    private final JavaMailSender javaMailSender;
//    private final MailTemplate mailTemplate;
//
//    @Override
//    public void sendEmailNotification(SimpleMailMessage message) {
//
//        try {
//            javaMailSender.send(message);
//            log.info("Email notification send {}", message);
//        } catch (MailException e) {
//            log.error("Failed to send mail: {}", e.getMessage());
//        }
//    }
//
//    @Async("customExecutorPool")
//    @Override
//    public void sendEmailNotificationAsync(SimpleMailMessage message) {
//        sendEmailNotification(message);
//    }
//
//    @Override
//    public void sendHtmlEmail(String from, String to, String subject, String fileName, Map<String, String> parameters) {
//
//        MimeMessage htmlMail = mailTemplate.getHtmlMail(
//            from,
//            to,
//            subject,
//            fileName,
//            parameters,
//            javaMailSender
//        );
//
//        try {
//            javaMailSender.send(htmlMail);
//            log.info("Email notification send.");
//        } catch (MailException e) {
//            log.error("Failed to send mail: {}", e.getMessage());
//        }
//
//    }
//
//    @Async("customExecutorPool")
//    @Override
//    public void sendHtmlEmailAsync(String from, String to, String subject, String fileName, Map<String, String> parameters) {
//        sendHtmlEmail(from, to, subject, fileName, parameters);
//    }
//
//}
