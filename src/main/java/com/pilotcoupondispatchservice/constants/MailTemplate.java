package com.pilotcoupondispatchservice.constants;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class MailTemplate {

    public MimeMessage getHtmlMail(
            String from,
            String to,
            String subject,
            String fileName,
            Map<String, String> model,
            JavaMailSender sender
    ) {
        MimeMessage message = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, false, StandardCharsets.UTF_8.name());
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(model == null ? "" : String.join("\n", model.values()), true);
            return message;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to build html mail", e);
        }
    }
}
