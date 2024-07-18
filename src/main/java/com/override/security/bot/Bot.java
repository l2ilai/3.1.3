package com.override.security.bot;

import com.override.security.bot.service.KeyFileService;
import com.override.security.bot.commands.ServiceCommand;
import com.override.security.bot.properties.BotProperties;
import com.override.security.service.ServerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.List;

@Component
public class Bot extends TelegramLongPollingCommandBot {

    @Value("${owner.names}")
    private String ownerName;

    @Autowired
    private BotProperties botProperties;

    @Autowired
    private KeyFileService keyFileService;

    @Autowired
    private ServerServiceImpl serverService;

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
        boolean isDownload = false;
        if (update.hasMessage() && update.getMessage().hasDocument()) {


            String name = update.getMessage().getFrom().getUserName();

            Document document = update.getMessage().getDocument();
            String newCaption = update.getMessage().getCaption();
            String newServerUser = null;;
            if (newCaption!= null) {
                newServerUser = newCaption.toLowerCase().trim();
            }
            String docId = update.getMessage().getDocument().getFileId();
            String docName = document.getFileName();
            String typeDoc = docName.substring(docName.lastIndexOf("."));
            if (name.equals(ownerName)) {
                if (typeDoc.equals(".pub")) {
                    if (newServerUser != null) {
                        try {
                            keyFileService.uploadFile(docName, docId, pathDownload, getBotToken());
                            sendMessage(chat_id, serverService.execCommandViaWeb("touch 321"));


                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else System.out.println("Нет подписи файла!");
                } else {
                    System.out.println("Файл не pub");
                }
            } else {
                System.out.println("Нет прав для загрузки файла!");
            }
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            String msgText = update.getMessage().getText();
            sendMessage(chat_id, msgText);
        }

//        if (isDownload) {
//            serverService.execCommand("ls");
//        }
    }




    private void sendMessage(long chatId, String msg) {
        SendMessage message =new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(msg);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
