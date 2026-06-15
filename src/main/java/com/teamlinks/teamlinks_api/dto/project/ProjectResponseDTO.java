package com.teamlinks.teamlinks_api.dto.project;

import com.teamlinks.teamlinks_api.entity.Project;
import com.teamlinks.teamlinks_api.entity.ProjectStatus;

import java.time.Instant;

public record ProjectResponseDTO(
    Long id,
    String name,
    String description,
    ProjectStatus status,
    int linkCount,
    Instant createdAt,
    Instant updatedAt
) {
    public static ProjectResponseDTO fromEntity(Project project) {
        return new ProjectResponseDTO(
            project.getId(),
            project.getName(),
            project.getDescription(),
            project.getStatus() != null ? project.getStatus() : ProjectStatus.INICIAR,
            project.getLinks() != null ? project.getLinks().size() : 0,
            project.getCreatedAt(),
            project.getUpdatedAt()
        );
    }
}
