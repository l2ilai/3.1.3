package com.override.security.bot;

import com.override.security.bot.contants.ServersCommand;
import com.override.security.bot.contants.StartCommand;
import com.override.security.bot.properties.BotProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class Bot extends TelegramLongPollingCommandBot {

    @Autowired
    private BotProperties botProperties;

    public Bot () {
        super();
        register(new StartCommand("start", "Старт"));
//        register(new ServersCommand("servers", "Сложение"));
    }

    @Override
    public String getBotUsername() {
        return botProperties.getName();
    }

    @Override
    public String getBotToken() {
        return botProperties.getToken();
    }

    @Override
    public void processNonCommandUpdate(Update update) {

    }

    private void sendMessage(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.setText("Hi");
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }
}
