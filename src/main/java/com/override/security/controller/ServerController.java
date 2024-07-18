package com.override.security.controller;

import com.override.security.dto.ServerDTO;
import com.override.security.mapper.ServerMapper;
import com.override.security.model.Server;
import com.override.security.service.ServerServiceImpl;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/server-panel")
public class ServerController {

    @Autowired
    private ServerServiceImpl serverServiceImpl;

    @GetMapping
    public String getServers(Model model) {
        model.addAttribute("servers", serverServiceImpl.findAllServers());
        return "admin-panel";
    }

    @GetMapping("/server/{id}")
    @ResponseBody
    public ResponseEntity<Server> getServerById(@PathVariable Long id) {
        Server server = serverServiceImpl.findServer(id);
        if (server == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(server);
    }

    @GetMapping("/allServers")
    @ResponseBody
    public List<ServerDTO> getAllServers() {
        return serverServiceImpl.findAllServers().stream().map(ServerMapper::entityToDTO).toList();
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> createServer(@RequestBody @Valid ServerDTO serverDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        Server server = ServerMapper.DTOToEntity(serverDTO);
        serverServiceImpl.saveServer(server);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/server/{id}")
    public ResponseEntity<HttpStatus> updateServer(@RequestBody @Valid ServerDTO serverDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        serverServiceImpl.updateServer(ServerMapper.DTOToEntity(serverDTO));

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/server/{id}")
    public ResponseEntity<HttpStatus> deleteServer(@PathVariable Long id) {
        serverServiceImpl.deleteServer(id);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }

    @SneakyThrows
    @ResponseBody
    @GetMapping("/bash")
    public String execCommand(@RequestParam String cmd) {
        return serverServiceImpl.execCommandViaWeb(cmd);
    }
}
