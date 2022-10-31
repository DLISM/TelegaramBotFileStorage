package com.example.controller;

import com.example.service.UpdateProducer;
import com.example.utils.MessageUtils;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.example.model.RabbitQueue.*;

@Component
@Log4j
public class UpdateController {
    private TelegramBot telegramBot;
    private MessageUtils messageUtils;
    private UpdateProducer updateProducer;

    public UpdateController(MessageUtils messageUtils, UpdateProducer updateProducer) {
        this.messageUtils = messageUtils;
        this.updateProducer=updateProducer;
    }

    public void registerBot(TelegramBot telegramBot){
        this.telegramBot=telegramBot;
    }

    public void processUpdate(Update update){
        if(update==null){
            log.error("Received error is null");
            return;
        }

        if(update.getMessage()!=null){
            distributeMessageByType(update);
        }else {
            log.error("Received unsupported message "+update);
        }
    }

    private void distributeMessageByType(Update update) {
        var message = update.getMessage();

        if(message.getText()!=null){
            processTextMessage(update);
        }else if(message.getDocument()!=null){
            processDocMessage(update);
        }else if(message.getPhoto()!=null){
            processPhoto(update);
        }else {
            setUnsupportedMessageTypeView(update);
        }
    }

    private void setUnsupportedMessageTypeView(Update update) {
        var sendMessage =messageUtils.generateSendMessageWithText(update,
                "Неизветсный тип сообщения");

        setView(sendMessage);
    }

    private void setView(SendMessage sendMessage) {
        telegramBot.sendMessage(sendMessage);
    }

    private void setFileRecivedView(Update update) {
        var sendMessage =messageUtils.generateSendMessageWithText(update,
                "Файл получен! обрабоативается...");
        setView(sendMessage);
    }

    private void processPhoto(Update update) {
        updateProducer.produce(PHOTO_MESSAGE_UPDATE, update);
        setFileRecivedView(update);
    }

    private void processDocMessage(Update update) {
        updateProducer.produce(DOC_MESSAGE_UPDATE, update);
        setFileRecivedView(update);
    }

    private void processTextMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
    }

}
