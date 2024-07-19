package com.override.security.dto;

import com.override.security.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerDTO {
    private Long id;
    private String name;
    private String ip;
    private Set<User> users;
}
