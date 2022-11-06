package com.example.service.Impl;

import com.example.dao.AppUserDAO;
import com.example.dao.RawDataDAO;
import com.example.entity.AppUser;
import com.example.entity.RawData;
import com.example.service.MainService;
import com.example.service.ProduceService;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static com.example.entity.enums.UserState.BASIC_STATE;
import static com.example.entity.enums.UserState.WAIT_FOR_EMAIL_STATE;
import static com.example.service.enums.ServiceCommand.*;

@Service
@Log4j
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
        var appUser = findOrSaveAppUser(update);
        var userState = appUser.getState();
        var text = update.getMessage().getText();
        var output = "";

        if (CANCEL.equals(text)) {
            output = cancelProcess(appUser);
        } else if (BASIC_STATE.equals(userState)) {
            output = processServiceCommand(appUser, text);
        } else if (WAIT_FOR_EMAIL_STATE.equals(userState)) {
            //TODO добавить обработку емейла
        } else {
            log.error("Unknown user state: " + userState);
            output = "Неизвестная ошибка! Введите /cancel и попробуйте снова!";
        }

        var chatId = update.getMessage().getChatId();
        sendAnswer(output, chatId);


    }

    @Override
    public void processDocMessage(Update update) {
        saveRawData(update);
        var appUser = findOrSaveAppUser(update);
        var chatId = update.getMessage().getChatId();
        if(isNotAllowToSendContent(chatId, appUser)){
            return;
        }
        //TODO добавить сохранения документа
        var answer = "Документ успешно загружен! Ссылка для скачивания http://test.ru";
        sendAnswer(answer, chatId);
    }

    private boolean isNotAllowToSendContent(Long chatId, AppUser appUser) {
        var userState = appUser.getState();
        if(!appUser.getIsActive()){
             var error = "Зарегистрируйтес или активируйте свою учетную запись";
             sendAnswer(error, chatId);
             return true;
        }else if(!BASIC_STATE.equals(userState)){
            var error = "Отмените текущую команду с помощью /cancel";
            sendAnswer(error, chatId);
            return true;
        }
        return false;
    }

    @Override
    public void processPhotoMessage(Update update) {
        saveRawData(update);
        var appUser = findOrSaveAppUser(update);
        var chatId = update.getMessage().getChatId();
        if(isNotAllowToSendContent(chatId, appUser)){
            return;
        }
        //TODO добавить сохранения документа
        var answer = "Фото успешно загружен! Ссылка для скачивания http://test.ru";
        sendAnswer(answer, chatId);
    }

    private void sendAnswer(String output, Long chatId) {
        var sendMessage=new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        produceService.produceAnswer(sendMessage);
    }

    private String processServiceCommand(AppUser appUser, String cmd) {
        if(REGISTRATION.equals(cmd)){
            //TODO добавить регистрацию
            return "Временно не доступно";
        }else if(HELP.equals(cmd)){
            return help();
        }else if (START.equals(cmd)){
            return "Чтобы посмотреть список доступных команд вводите /help";
        }else {
            return "Неизвестная команда. Вводите команду /help";
        }
    }

    private String help() {
        return "Список доступных комманд: \n"+
                "/cancel - отмена текущей команды \n"
                +"/registration - регистрация пользователя";
    }

    private String cancelProcess(AppUser appUser) {
        appUser.setState(BASIC_STATE);
        appUserDAO.save(appUser);

        return "Команда отменена";
    }

    private void saveRawData(Update update) {
        RawData rawData= RawData.builder()
                .event(update)
                .build();

        rawDataDAO.save(rawData);
    }

    private AppUser findOrSaveAppUser(Update update){
        User telegramUser = update.getMessage().getFrom();
        AppUser persistentAppUser = appUserDAO.findAppUserByTelegramUserId(telegramUser.getId());
        if (persistentAppUser == null) {
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .userName(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    //TODO изменить значение по умолчанию после добавления регистрации
                    .isActive(true)
                    .state(BASIC_STATE)
                    .build();
            return appUserDAO.save(transientAppUser);
        }
        return persistentAppUser;
    }
}
