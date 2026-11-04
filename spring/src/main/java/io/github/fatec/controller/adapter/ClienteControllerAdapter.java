package io.github.fatec.controller.adapter;

import io.github.fatec.controller.dto.response.ClienteResponse;
import io.github.fatec.entity.Cliente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClienteControllerAdapter {

    private static final Logger log = LoggerFactory.getLogger(ClienteControllerAdapter.class);

    private ClienteControllerAdapter() {
    }

    public static ClienteResponse castResponse(Cliente cliente) {
        log.info("Convertendo cliente para resposta cep={}", cliente.cep());
        return new ClienteResponse(cliente.nome(), cliente.cep(), cliente.endereco());
    }
}
