package com.example.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ConsumerService {
    void consumerTextMessageUpdate(Update update);
    void consumerDocMessageUpdate(Update update);
    void consumerPhotoMessageUpdate(Update update);
}
