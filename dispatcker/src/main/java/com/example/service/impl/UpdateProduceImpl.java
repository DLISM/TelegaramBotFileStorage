package com.example.service.impl;

import com.example.service.UpdateProducer;
import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Log4j
public class UpdateProduceImpl implements UpdateProducer {

    private final RabbitTemplate rabbitTemplate;

    public UpdateProduceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produce(String rabbitQueue, Update update) {
        log.debug(update.getMessage().getText());

        rabbitTemplate.convertAndSend(rabbitQueue, update);
    }
}
