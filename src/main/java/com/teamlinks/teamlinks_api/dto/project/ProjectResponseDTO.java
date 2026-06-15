package com.teamlinks.teamlinks_api.dto.project;

import com.teamlinks.teamlinks_api.entity.Project;

import java.time.Instant;

public record ProjectResponseDTO(
    Long id,
    String name,
    String description,
    Long clientId,
    String clientName,
    int linkCount,
    Instant createdAt,
    Instant updatedAt
) {
    public static ProjectResponseDTO fromEntity(Project project) {
        return new ProjectResponseDTO(
            project.getId(),
            project.getName(),
            project.getDescription(),
            project.getClient().getId(),
            project.getClient().getName(),
            project.getLinks() != null ? project.getLinks().size() : 0,
            project.getCreatedAt(),
            project.getUpdatedAt()
        );
    }
}
