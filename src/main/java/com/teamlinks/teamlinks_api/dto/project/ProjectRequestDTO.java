package com.teamlinks.teamlinks_api.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProjectRequestDTO(
    @NotBlank(message = "Nome do projeto é obrigatório") String name,
    String description,
    @NotNull(message = "Cliente é obrigatório") Long clientId
) {}
