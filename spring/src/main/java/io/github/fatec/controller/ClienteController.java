package io.github.fatec.controller;

import io.github.fatec.controller.adapter.ClienteControllerAdapter;
import io.github.fatec.controller.dto.request.ClienteRequest;
import io.github.fatec.controller.dto.response.ClienteResponse;
import io.github.fatec.service.ClienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    private static final Logger log = LoggerFactory.getLogger(ClienteController.class);

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ClienteResponse cadastrar(@RequestBody ClienteRequest request) {
        log.info("POST /cliente recebido cep={}", request.cep());
        ClienteResponse response = ClienteControllerAdapter.castResponse(clienteService.cadastrar(request.nome(), request.cep()));
        log.info("POST /cliente concluido cep={}", request.cep());
        return response;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> tratarErro(IllegalArgumentException e) {
        log.warn("Requisicao invalida: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
