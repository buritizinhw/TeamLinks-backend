package com.teamlinks.teamlinks_api.dto.client;

import jakarta.validation.constraints.NotBlank;

public record ClientRequestDTO(
    @NotBlank(message = "Nome do cliente é obrigatório") String name
) {}
