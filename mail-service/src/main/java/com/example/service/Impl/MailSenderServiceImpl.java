package com.example.service.Impl;

import com.example.dto.MailParams;
import com.example.service.MailSenderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderServiceImpl implements MailSenderService {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String emailFrom;
    @Value("${service.activation.uri}")
    private String activationServiceUrl;

    public MailSenderServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void send(MailParams mailParams) {
        var subject = "Активация учетной записи";
        var messageBody = getActivationMainBody(mailParams.getId());
        var emailTo = mailParams.getEmailTo();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(emailFrom);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(messageBody);

        javaMailSender.send(mailMessage);
    }

    private String getActivationMainBody(String id) {
        var msg = String.format(" Для завершения регистрации" +
                " перейдите по ссылке %s", activationServiceUrl);
        return msg.replace("{id}", id);
    }
}
