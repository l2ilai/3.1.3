package com.override.security.mapper;

import com.override.security.dto.ServerDTO;
import com.override.security.model.Server;

public class ServerMapper {
    public static ServerDTO entityToDTO(Server server) {
        ServerDTO serverDTO = new ServerDTO();
        serverDTO.setId(server.getId());
        serverDTO.setName(server.getName());
        serverDTO.setIp(server.getIp());
        return serverDTO;

    }

    public static Server DTOToEntity(ServerDTO serverDTO) {
        Server server = new Server();
        server.setId(serverDTO.getId());
        server.setName(serverDTO.getName());
        server.setIp(serverDTO.getIp());
        return server;
    }
}
