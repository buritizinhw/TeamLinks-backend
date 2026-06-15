package com.teamlinks.teamlinks_api.dto.link;

import com.teamlinks.teamlinks_api.entity.Link;
import com.teamlinks.teamlinks_api.entity.Tag;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

public record LinkResponseDTO(
    Long id,
    String shortCode,
    String shortUrl,
    String url,
    String name,
    String description,
    long clickCount,
    Long projectId,
    String projectName,
    Set<String> tagNames,
    Instant createdAt,
    Instant updatedAt
) {
    public static LinkResponseDTO fromEntity(Link link, String shortUrl) {
        return new LinkResponseDTO(
            link.getId(),
            link.getShortCode(),
            shortUrl,
            link.getUrl(),
            link.getName(),
            link.getDescription(),
            link.getClickCount(),
            link.getProject().getId(),
            link.getProject().getName(),
            link.getTags().stream()
                    .map(Tag::getName)
                    .collect(Collectors.toSet()),
            link.getCreatedAt(),
            link.getUpdatedAt()
        );
    }
}
