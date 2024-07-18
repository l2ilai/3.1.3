package com.override.security.bot.commands;

import com.override.security.service.ServerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static com.override.security.bot.enums.Commands.HELP;

@Component
public class HelpCommand extends ServiceCommand {

    public HelpCommand() {
        super(HELP.getAlias(), HELP.getDescription());
    }

    @Autowired
    private ServerServiceImpl serverService;

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), this.getUserName(user),
                "\uD83E\uDD21");

        serverService.execCommand("touch 777");
    }
}
