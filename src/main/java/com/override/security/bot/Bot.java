package com.override.security.bot;

import com.override.security.bot.contants.ServiceCommand;
import com.override.security.bot.properties.BotProperties;
import com.override.security.service.ServerServiceImpl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

@Component
public class Bot extends TelegramLongPollingCommandBot {

    @Value("${owner.names}")
    private String ownerName;

    @Autowired
    private BotProperties botProperties;

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
        boolean isDownload = false;
        Long chat_id = update.getMessage().getChatId();
        if (update.hasMessage() && update.getMessage().hasDocument()) {


            String name = update.getMessage().getFrom().getUserName();

            Document document = update.getMessage().getDocument();
            String newCaption = update.getMessage().getCaption();
            String newServerUser;
            if (newCaption!= null) {
                newServerUser = newCaption.toLowerCase().trim();
            } else {
                newServerUser = null;
            }
            String docId = update.getMessage().getDocument().getFileId();
            String docName = document.getFileName();
            Integer docSize = document.getFileSize();
            System.out.println(newServerUser);
            String typeDoc = docName.substring(docName.lastIndexOf("."));
            if (name.equals(ownerName)) {
                if (typeDoc.equals(".pub")) {
                    if (docSize < 5000) {
                        if (newServerUser != null) {
                            try {
                                uploadFile(docName, docId, getBotToken());
//                                isDownload = true;
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } else System.out.println("Нет подписи файла!");
                    } else {
                        System.out.println("Размер файла не должен превышать 5 КБайт!");
                    }
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
//            serverService.execCommand("ls -la");
//        }
    }


    public void uploadFile(String file_name, String file_id, String token) throws IOException {
        URL url = new URL("https://api.telegram.org/bot" + token + "/getFile?file_id=" + file_id);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String res = in.readLine();
        JSONObject jresult = new JSONObject(res);
        JSONObject path = jresult.getJSONObject("result");
        String file_path = path.getString("file_path");
        URL downoload = new URL("https://api.telegram.org/file/bot" + token + "/" + file_path);
        FileOutputStream fos = new FileOutputStream(pathDownload + file_name);
        System.out.println("Start upload");
        ReadableByteChannel rbc = Channels.newChannel(downoload.openStream());
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
        System.out.println("Uploaded!");
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
