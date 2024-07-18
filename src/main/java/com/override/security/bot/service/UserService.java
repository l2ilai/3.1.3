package com.override.security.bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
@Service
public class UserService {
    public String getUserName(User user) {
        return (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());
    }
}
