package com.override.security.dto;



import com.override.security.model.Role;
import com.override.security.model.Server;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String password;
    private Set<Role> roles;
    private Set<Server> servers;
}
