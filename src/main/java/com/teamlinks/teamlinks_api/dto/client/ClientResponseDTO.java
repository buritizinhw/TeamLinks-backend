package com.teamlinks.teamlinks_api.dto.client;

import com.teamlinks.teamlinks_api.entity.Client;

import java.time.Instant;

public record ClientResponseDTO(
    Long id,
    String name,
    int projectCount,
    Instant createdAt,
    Instant updatedAt
) {
    public static ClientResponseDTO fromEntity(Client client) {
        return new ClientResponseDTO(
            client.getId(),
            client.getName(),
            client.getProjects() != null ? client.getProjects().size() : 0,
            client.getCreatedAt(),
            client.getUpdatedAt()
        );
    }
}
