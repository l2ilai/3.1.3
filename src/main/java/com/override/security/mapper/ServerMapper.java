package com.override.security.mapper;

import com.override.security.dto.ServerDTO;
import com.override.security.model.Server;

public class ServerMapper {
    public static ServerDTO entityToDTO(Server server) {
        return new ServerDTO(server.getId(),
                server.getName(),
                server.getIp());

    }

    public static Server DTOToEntity(ServerDTO serverDTO) {
        Server server = new Server();
        server.setId(serverDTO.getId());
        server.setName(serverDTO.getName());
        server.setIp(serverDTO.getIp());
        return server;
    }
}
