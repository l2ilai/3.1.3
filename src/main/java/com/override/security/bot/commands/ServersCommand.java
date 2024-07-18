package com.override.security.bot.commands;

import com.override.security.service.ServerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.stream.Collectors;

import static com.override.security.bot.enums.Commands.SERVERS;

@Component
public class ServersCommand  extends ServiceCommand {

    @Autowired
    private ServerServiceImpl serverService;

    public ServersCommand() {
        super(SERVERS.getAlias(), SERVERS.getDescription());
    }
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), this.getUserName(user),
                "Список доступных серверов:\n" +
                        serverService.findAllServers().stream()
                                .map(server -> "\uD83D\uDC49" + " " + server.getName() +
                                        " " + server.getIp())
                                .collect(Collectors.joining("\n")));
    }
}
