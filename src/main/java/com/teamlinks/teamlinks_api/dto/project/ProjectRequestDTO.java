package com.teamlinks.teamlinks_api.dto.project;

import com.teamlinks.teamlinks_api.entity.ProjectStatus;

import jakarta.validation.constraints.NotBlank;

public record ProjectRequestDTO(
    @NotBlank(message = "Nome do projeto é obrigatório") String name,
    String description,
    ProjectStatus status
) {}
