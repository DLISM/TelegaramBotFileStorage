package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebHookController {
    private final UpdateProcessor updateProcessor;

    public WebHookController(UpdateProcessor updateProcessor) {
        this.updateProcessor = updateProcessor;
    }

    @PostMapping("/callback/update")
    public ResponseEntity<?> onUpdateReceived(@RequestBody Update update){
        updateProcessor.processUpdate(update);
        return ResponseEntity.ok().build();
    }
}
