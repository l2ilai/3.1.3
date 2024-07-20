package com.override.security.bot.nonCommand;

import com.override.security.bot.service.KeyFileService;
import com.override.security.bot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class KeyFile {

    @Value("${file.path}")
    private String pathDownload;

    @Autowired
    private KeyFileService keyFileService;
    @Autowired
    private UserService userService;


    public String ExecuteLoadKeyFile(Update update, String botToken) {
        Document document = update.getMessage().getDocument();
        String newServerUser = userService.getServerUserName(update);
        String docId = update.getMessage().getDocument().getFileId();
        String docName = document.getFileName();
        String typeDoc = docName.substring(docName.lastIndexOf("."));

        if (typeDoc.equals(".pub")) {
            if (newServerUser != null) {
                keyFileService.uploadFile(docName, docId, pathDownload, botToken);
                return  "Файл " + docName + " Загружен!";
                //TODO  перенести в новый класс
//                serverService.execCommand("./script " + newServerUser);
//                sendMessage(chat_id, "Пользователь " + newServerUser + " создан!");

            } else return "Нет подписи файла!";
        } else {
            return  "Файл должен иметь расширение *.pub!";
        }
    }
}
