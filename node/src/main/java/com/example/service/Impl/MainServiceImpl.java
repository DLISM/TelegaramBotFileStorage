package com.example.service.Impl;

import com.example.dao.RawDataDAO;
import com.example.entity.RawData;
import com.example.service.MainService;
import com.example.service.ProduceService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class MainServiceImpl implements MainService {
    private final RawDataDAO rawDataDAO;
    private final ProduceService produceService;

    public MainServiceImpl(RawDataDAO rawDataDAO, ProduceService produceService) {
        this.rawDataDAO = rawDataDAO;
        this.produceService = produceService;
    }

    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);

        var message = update.getMessage();
        var sendMessage=new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("Hello from node");

        produceService.produceAnswer(sendMessage);
    }

    private void saveRawData(Update update) {
        RawData rawData= RawData.builder()
                .event(update)
                .build();

        rawDataDAO.save(rawData);
    }
}
