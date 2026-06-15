package com.teamlinks.teamlinks_api.service.link;

import com.teamlinks.teamlinks_api.dto.link.LinkRequestDTO;
import com.teamlinks.teamlinks_api.dto.link.LinkResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LinkService {

    LinkResponseDTO create(Long projectId, LinkRequestDTO dto);

    LinkResponseDTO findById(Long id);

    LinkResponseDTO findByShortCode(String shortCode);

    LinkResponseDTO resolveForRedirect(String shortCode);

    Page<LinkResponseDTO> findByProjectId(Long projectId, Pageable pageable);

    LinkResponseDTO update(Long id, LinkRequestDTO dto);

    void delete(Long id);
}
