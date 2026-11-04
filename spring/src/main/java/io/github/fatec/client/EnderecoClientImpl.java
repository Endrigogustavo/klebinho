package io.github.fatec.client;

import io.github.fatec.client.dto.BrasilApiResponse;
import io.github.fatec.client.dto.ViaCepResponse;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class EnderecoClientImpl implements EnderecoClient {

    private static final Logger log = LoggerFactory.getLogger(EnderecoClientImpl.class);

    private final RestClient restClient;
    private final RestClient fallbackClient;

    public EnderecoClientImpl(@Value("${viacep.url}") String url, @Value("${brasilapi.url}") String fallbackUrl) {
        this.restClient = RestClient.create(url);
        this.fallbackClient = RestClient.create(fallbackUrl);
    }

    @Override
    @Retry(name = "viacep", fallbackMethod = "fallback")
    public String buscarEndereco(String cep) {
        log.info("Consultando api=ViaCEP cep={}", cep);
        ViaCepResponse response;
        try {
            response = restClient.get()
                    .uri("/ws/{cep}/json/", cep)
                    .retrieve()
                    .body(ViaCepResponse.class);
        } catch (RestClientException e) {
            log.warn("Tentativa falhou api=ViaCEP cep={} erro={}", cep, e.getMessage());
            throw e;
        }
        if (response.logradouro() == null) {
            log.warn("CEP nao encontrado api=ViaCEP cep={}", cep);
            return "Endereço não encontrado";
        }
        log.info("Endereco encontrado api=ViaCEP cep={}", cep);
        return response.logradouro() + ", " + response.localidade() + " - " + response.uf();
    }

    public String fallback(String cep, Exception e) {
        log.error("Fallback acionado api=BrasilAPI cep={} erro={}", cep, e.getMessage());
        try {
            BrasilApiResponse response = fallbackClient.get()
                    .uri("/api/cep/v1/{cep}", cep)
                    .retrieve()
                    .body(BrasilApiResponse.class);
            log.info("Endereco encontrado api=BrasilAPI cep={}", cep);
            return response.street() + ", " + response.city() + " - " + response.state();
        } catch (RestClientException ex) {
            log.error("Fallback falhou api=BrasilAPI cep={} erro={}", cep, ex.getMessage());
            return "Endereço indisponível";
        }
    }
}
