package com.example.controller;

import com.example.dto.MailParams;
import com.example.service.MailSenderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/mail")
@RestController
public class MailController {
    private final MailSenderService senderService;

    public MailController(MailSenderService senderService) {
        this.senderService = senderService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendActivationMail(@RequestBody MailParams mailParams){
        senderService.send(mailParams);
        return ResponseEntity.ok().build();
    }
}
