package com.override.security.bot.contants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Command {
    START("start", "Начать использование бота"),
    SERVERS("servers", "Показать список доступных серверов");

    private String alias;
    private String description;

}
