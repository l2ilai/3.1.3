package com.override.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "ssh-server")
public class ServerProperties {
    private String ip;
    private int port;
    private String user;
    private String pathToPrivateKey;
}
