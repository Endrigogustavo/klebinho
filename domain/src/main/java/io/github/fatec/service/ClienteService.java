package io.github.fatec.service;

import io.github.fatec.client.EnderecoClient;
import io.github.fatec.entity.Cliente;

public class ClienteService {

    private static final System.Logger log = System.getLogger(ClienteService.class.getName());

    private final EnderecoClient enderecoClient;

    public ClienteService(EnderecoClient enderecoClient) {
        this.enderecoClient = enderecoClient;
    }

    public Cliente cadastrar(String nome, String cep) {
        log.log(System.Logger.Level.INFO, "Validando cliente cep={0}", cep);
        if (nome == null || nome.isBlank()) {
            log.log(System.Logger.Level.WARNING, "Nome invalido cep={0}", cep);
            throw new IllegalArgumentException("Nome obrigatório");
        }
        if (cep == null || !cep.matches("\\d{8}")) {
            log.log(System.Logger.Level.WARNING, "CEP invalido cep={0}", cep);
            throw new IllegalArgumentException("CEP deve ter 8 dígitos");
        }
        Cliente cliente = new Cliente(nome, cep, enderecoClient.buscarEndereco(cep));
        log.log(System.Logger.Level.INFO, "Cliente cadastrado cep={0} endereco={1}", cep, cliente.endereco());
        return cliente;
    }
}
