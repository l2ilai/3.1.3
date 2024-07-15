package com.override.security.bot.contants;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static com.override.security.bot.enums.Commands.START;

@Component
public class StartCommand extends ServiceCommand {

    public StartCommand() {
        super(START.getAlias(), START.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                "Ку! Я могу добавить публичный SSH \uD83D\uDD11 на любой доступный сервер." +
                        " Узнать доступные сервера /servers. Получить помощь /help");
    }
}
