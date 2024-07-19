package com.override.security.mapper;

import com.override.security.dto.ServerDTO;
import com.override.security.model.Server;

public class ServerMapper {
    public static ServerDTO entityToDTO(Server server) {
        return new ServerDTO(
                server.getId(),
                server.getName(),
                server.getIp(),
                server.getUsers()
        );
    }

    public static Server DTOToEntity(ServerDTO serverDTO) {
        return new Server(
                serverDTO.getId(),
                serverDTO.getName(),
                serverDTO.getIp(),
                serverDTO.getUsers()
        );

    }
}
