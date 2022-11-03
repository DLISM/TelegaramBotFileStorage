package com.example.service.Impl;

import com.example.service.ConsumerService;
import com.example.service.ProduceService;
import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.example.model.RabbitQueue.*;

@Service
@Log4j
public class ConsumerServiceImpl implements ConsumerService {

    private final ProduceService produceService;

    public ConsumerServiceImpl(ProduceService produceService) {
        this.produceService = produceService;
    }

    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    public void consumerTextMessageUpdate(Update update) {
        log.debug("NODE: Text message is received ");

        var message = update.getMessage();
        var sendMessage=new SendMessage();
        sendMessage.setChatId(message.getMessageId().toString());
        sendMessage.setText("Hello from node");

        produceService.produceAnswer(sendMessage);
    }

    @Override
    @RabbitListener(queues = DOC_MESSAGE_UPDATE)
    public void consumerDocMessageUpdate(Update update) {
        log.debug("NODE: Doc message is received ");
    }

    @Override
    @RabbitListener(queues = PHOTO_MESSAGE_UPDATE)
    public void consumerPhotoMessageUpdate(Update update) {
        log.debug("NODE: Photo message is received ");
    }
}
