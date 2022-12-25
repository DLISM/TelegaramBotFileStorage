package com.example.service;

import com.example.dto.MailParams;

public interface MailSenderService {
    void send(MailParams mailParams);
}
