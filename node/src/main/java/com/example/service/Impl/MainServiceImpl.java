package com.example.service.Impl;

import com.example.dao.AppUserDAO;
import com.example.dao.RawDataDAO;
import com.example.entity.AppUser;
import com.example.entity.RawData;
import com.example.service.MainService;
import com.example.service.ProduceService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
public class MainServiceImpl implements MainService {
    private final RawDataDAO rawDataDAO;
    private final ProduceService produceService;
    private final AppUserDAO appUserDAO;

    public MainServiceImpl(RawDataDAO rawDataDAO, ProduceService produceService, AppUserDAO appUserDAO) {
        this.rawDataDAO = rawDataDAO;
        this.produceService = produceService;
        this.appUserDAO = appUserDAO;
    }

    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);
        var textMessage = update.getMessage();
        var telegramUser = textMessage.getFrom();
        var appUser = findOrSaveAppUser(telegramUser);

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

    private AppUser findOrSaveAppUser(User telegramUser){
        AppUser persistentAppUser = appUserDAO.findAppUserByTelegramUserId(telegramUser.getId());
        if (persistentAppUser==null){
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .userName(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    //TODO изменит значения после добавления регистрации
                    .isActive(true)
                    .build();
            return appUserDAO.save(transientAppUser);
        }
        return persistentAppUser;
    }
}
