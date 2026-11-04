package io.github.fatec.controller.dto.response;

public record ClienteResponse(
        String nome,
        String cep,
        String endereco
) {
}
