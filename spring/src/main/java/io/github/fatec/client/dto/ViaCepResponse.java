package io.github.fatec.client.dto;

public record ViaCepResponse(
        String logradouro,
        String localidade,
        String uf
) {
}
