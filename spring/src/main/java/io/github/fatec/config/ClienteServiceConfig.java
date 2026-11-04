package io.github.fatec.config;

import io.github.fatec.client.EnderecoClient;
import io.github.fatec.service.ClienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClienteServiceConfig {

    private static final Logger log = LoggerFactory.getLogger(ClienteServiceConfig.class);

    @Bean
    public ClienteService clienteService(EnderecoClient enderecoClient) {
        log.info("Criando ClienteService");
        return new ClienteService(enderecoClient);
    }
}
