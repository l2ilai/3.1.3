package com.override.security.bot;

import com.override.security.bot.commands.ServiceCommand;
import com.override.security.bot.nonCommand.KeyFile;
import com.override.security.bot.properties.BotProperties;
import com.override.security.bot.service.KeyFileService;
import com.override.security.bot.service.ServerService;
import com.override.security.bot.service.UserService;
import com.override.security.service.ServerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class Bot extends TelegramLongPollingCommandBot {

    @Autowired
    private BotProperties botProperties;

    @Autowired
    private KeyFile keyFile;

    @Autowired
    private ServerService serverService;

    @Autowired
    private UserService userService;

    public Bot(List<ServiceCommand> allCommands) {
        super();
        allCommands.forEach(this::register);
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

        Long chatId = update.getMessage().getChatId();
        String userName = update.getMessage().getFrom().getUserName();
        if (userService.isOwner(userName)) {
            if (update.hasMessage() && update.getMessage().hasDocument()) {
                String message = keyFile.ExecuteLoadKeyFile(update, getBotToken());
                sendMessage(chatId, message);
                try {
                    execute(serverService.getServersInlineKeyboard(chatId, userName));
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            } 
//            else if (update.hasMessage() && update.getMessage().hasText()) {
//                String msgText = update.getMessage().getText();
//                String resCommand = serverService.execCommand(msgText);
//                sendMessage(chatId, resCommand);
//            } 
        } else {
            sendMessage(chatId, "Нет прав для выполнения команды!");
        }
    }


    private void sendMessage(long chatId, String msg) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(msg);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(long chatId, String msg, InlineKeyboardMarkup markupInline) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(msg);
        message.setReplyMarkup(markupInline);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
