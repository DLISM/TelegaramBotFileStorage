package com.example.service.impl;

import com.example.controller.UpdateProcessor;
import com.example.service.AnswerConsumer;
import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.example.model.RabbitQueue.ANSWER_MESSAGE;

@Service
@Log4j
public class AnswerConsumerImpl implements AnswerConsumer {

    private final UpdateProcessor updateProcessor;

    public AnswerConsumerImpl(UpdateProcessor updateProcessor) {
        this.updateProcessor = updateProcessor;
    }

    @Override
    @RabbitListener(queues = ANSWER_MESSAGE)
    public void consumer(SendMessage sendMessage) {
        updateProcessor.setView(sendMessage);
    }
}
