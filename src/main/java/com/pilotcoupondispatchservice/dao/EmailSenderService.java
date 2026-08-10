package com.pilotcoupondispatchservice.dao;

import org.springframework.mail.SimpleMailMessage;

import java.util.Map;

public interface EmailSenderService {

    public void sendEmailNotification(SimpleMailMessage message);

    public void sendEmailNotificationAsync(SimpleMailMessage message);

    public void sendHtmlEmail(String from, String to, String subject, String fileName, Map<String, String> parameters);

    public void sendHtmlEmailAsync(String from, String to, String subject, String fileName, Map<String, String> parameters);

}
