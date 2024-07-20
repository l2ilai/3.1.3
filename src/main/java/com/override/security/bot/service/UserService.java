package com.override.security.bot.service;

import com.override.security.bot.properties.OwnerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private OwnerProperties ownerProperties;

    public String getUserName(User user) {
        return (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());
    }

    public boolean isOwner(String userName) {
        return ownerProperties.getUserNamesTelegram().contains(userName);
    }

    public String getServerUserName(Update update) {
        String newCaption = update.getMessage().getCaption();
        String newServerUser = null;

        if (newCaption != null) {
            newServerUser = newCaption.toLowerCase().trim();
        }
        return newServerUser;
    }

}
