package com.override.security.bot.service;

import com.override.security.model.Server;
import com.override.security.properties.ServerProperties;
import com.override.security.service.ServerServiceImpl;
import lombok.SneakyThrows;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.userauth.keyprovider.KeyProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.schmizz.sshj.SSHClient.DEFAULT_PORT;

@Service
public class ServerService {

    @Autowired
    private ServerProperties serverProperties;

    @Autowired
    private ServerServiceImpl serverServiceImpl;

    public Session authToServer(String serverIP, String pathToPrivateKey, String serverUserName, SSHClient sshConnect) throws IOException {
        File privateKey = new File(pathToPrivateKey);
        KeyProvider keys = sshConnect.loadKeys(privateKey.getPath());
        sshConnect.addHostKeyVerifier(new PromiscuousVerifier());
        sshConnect.connect(serverIP, DEFAULT_PORT);
        sshConnect.authPublickey(serverUserName, keys);
        return sshConnect.startSession();
    }

    @SneakyThrows
    public String execCommand(String command) {
        SSHClient sshConnect = new SSHClient();
        Session session = authToServer(serverProperties.getIp(), serverProperties.getPathToPrivateKey(), serverProperties.getUser(), sshConnect);
        Session.Command cmd = session.exec(command);
        String ret = IOUtils.readFully(cmd.getInputStream()).toString();
        System.out.println("========stdout==========\n" + ret + "============+=");
        session.close();
        sshConnect.close();
        return ret;
    }

    public SendMessage getServersInlineKeyboard(Long chatId, String userName) {

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        serverServiceImpl.findAllServers().forEach( x -> {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(x.getName());
            inlineKeyboardButton.setCallbackData("Сервер " + x.getName() + " c IP: " + x.getIp() + " выбран для пользователя " + userName);
            rowInline.add(inlineKeyboardButton);
            rowsInline.add(rowInline);
        });

        markupInline.setKeyboard(rowsInline);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Servers: ");
        message.setReplyMarkup(markupInline);

        return message;

    }






}
