package com.override.security.bot;

import com.override.security.bot.commands.ServiceCommand;
import com.override.security.bot.properties.BotProperties;
import com.override.security.bot.service.KeyFileService;
import com.override.security.bot.service.UserService;
import com.override.security.service.ServerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class Bot extends TelegramLongPollingCommandBot {

    @Autowired
    private BotProperties botProperties;

    @Autowired
    private KeyFileService keyFileService;

    @Autowired
    private ServerServiceImpl serverService;

    @Autowired
    private UserService userService;

    @Value("${file.path}")
    private String pathDownload;

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
        Long chat_id = update.getMessage().getChatId();
        String name = update.getMessage().getFrom().getUserName();

        if (update.hasMessage() && update.getMessage().hasDocument()) {

            Document document = update.getMessage().getDocument();
            String newCaption = update.getMessage().getCaption();
            String newServerUser = null;

            if (newCaption != null) {
                newServerUser = newCaption.toLowerCase().trim();
            }
            String docId = update.getMessage().getDocument().getFileId();
            String docName = document.getFileName();
            String typeDoc = docName.substring(docName.lastIndexOf("."));
            if (userService.isOwner(name)) {
                if (typeDoc.equals(".pub")) {
                    if (newServerUser != null) {
                        keyFileService.uploadFile(docName, docId, pathDownload, getBotToken());
                        sendMessage(chat_id, "Файл " + docName + " Загружен!");
                        serverService.execCommand("./script " + newServerUser);
                        sendMessage(chat_id, "Пользователь " + newServerUser + " создан!");

                    } else sendMessage(chat_id, "Нет подписи файла!");
                } else {
                    sendMessage(chat_id, "Файл должен иметь расширение *.pub!");
                }
            } else {
                sendMessage(chat_id, "Нет прав для загрузки файла!");
            }
        } else if (update.hasMessage() && update.getMessage().hasText() && userService.isOwner(name)) {
            String msgText = update.getMessage().getText();
            String resCommand = serverService.execCommand(msgText);
            sendMessage(chat_id, resCommand);

        } else {
            sendMessage(chat_id, "Нет прав для выполнения команды!");
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

}
