package com.example.service;

import com.example.entity.AppDocument;
import com.example.entity.AppPhoto;
import com.example.service.enums.LinkType;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    AppDocument processDoc(Message telegramMessage);
    AppPhoto processPhoto(Message telegramMessage);
    String generateLink(Long docId, LinkType linkType);
}
