package com.override.security.service;

import com.override.security.model.Server;
import com.override.security.properties.ServerProperties;
import com.override.security.repository.ServerRepository;
import lombok.SneakyThrows;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.userauth.keyprovider.KeyProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static net.schmizz.sshj.SSHClient.DEFAULT_PORT;


@Service
public class ServerServiceImpl {
    @Autowired
    private ServerRepository serverRepository;

    public List<Server> findAllServers() {
        return serverRepository.findAll();
    }

    public Server findServer(Long id) {
        Optional<Server> serverFromDb = serverRepository.findById(id);
        return serverFromDb.orElseThrow();
    }

    public boolean saveServer(Server server) {
        Optional<Server> optionalServer = serverRepository.findByName(server.getName());
        if (optionalServer.isPresent()) {
            return false;
        }
        serverRepository.save(server);
        return true;
    }

    @Transactional
    public void updateServer(Server updatedServer) {
        Server newServer = findServer(updatedServer.getId());
        newServer.setName(updatedServer.getName());
        newServer.setIp(updatedServer.getIp());
        serverRepository.save(newServer);
    }

    @Transactional
    public void deleteServer(Long id) {
        if (serverRepository.findById(id).isPresent()) {
            serverRepository.deleteById(id);
        }
    }
}
