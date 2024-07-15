package com.override.security.bot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Commands {
    START("start", "Начать использование бота"),
    HELP("help", "Помощь"),
    SERVERS("servers", "Показать список доступных серверов");

    private String alias;
    private String description;

}
