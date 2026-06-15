package com.teamlinks.teamlinks_api.service.link;

import com.teamlinks.teamlinks_api.config.ShortenerProperties;
import com.teamlinks.teamlinks_api.dto.link.LinkRequestDTO;
import com.teamlinks.teamlinks_api.dto.link.LinkResponseDTO;
import com.teamlinks.teamlinks_api.entity.Link;
import com.teamlinks.teamlinks_api.entity.Project;
import com.teamlinks.teamlinks_api.entity.Tag;
import com.teamlinks.teamlinks_api.repository.LinkRepository;
import com.teamlinks.teamlinks_api.repository.ProjectRepository;
import com.teamlinks.teamlinks_api.repository.TagRepository;
import com.teamlinks.teamlinks_api.util.ShortCodeGenerator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;
    private final ProjectRepository projectRepository;
    private final TagRepository tagRepository;
    private final ShortenerProperties shortenerProperties;
    private final ShortCodeGenerator shortCodeGenerator;

    @Override
    @Transactional
    public LinkResponseDTO create(Long projectId, LinkRequestDTO dto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Projeto com ID " + projectId + " não encontrado."));

        Link link = new Link();
        link.setUrl(dto.url());
        link.setName(dto.name());
        link.setDescription(dto.description());
        link.setProject(project);
        link.setTags(resolveTags(dto.tagNames()));
        link.setShortCode(shortCodeGenerator.nextUnique());

        return toDto(linkRepository.save(link));
    }

    @Override
    @Transactional(readOnly = true)
    public LinkResponseDTO findById(Long id) {
        Link link = linkRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Link com ID " + id + " não encontrado."));
        return toDto(link);
    }

    @Override
    @Transactional(readOnly = true)
    public LinkResponseDTO findByShortCode(String shortCode) {
        Link link = linkRepository.findByShortCode(shortCode.trim())
                .orElseThrow(() -> new EntityNotFoundException("Link com código '" + shortCode + "' não encontrado."));
        return toDto(link);
    }

    @Override
    @Transactional
    public LinkResponseDTO resolveForRedirect(String shortCode) {
        Link link = linkRepository.findByShortCode(shortCode.trim())
                .orElseThrow(() -> new EntityNotFoundException("Link com código '" + shortCode + "' não encontrado."));
        link.setClickCount(link.getClickCount() + 1);
        return toDto(linkRepository.save(link));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LinkResponseDTO> findByProjectId(Long projectId, Pageable pageable) {
        if (!projectRepository.existsById(projectId)) {
            throw new EntityNotFoundException("Projeto com ID " + projectId + " não encontrado.");
        }
        return linkRepository.findAllByProjectId(projectId, pageable)
                .map(this::toDto);
    }

    @Override
    @Transactional
    public LinkResponseDTO update(Long id, LinkRequestDTO dto) {
        Link link = linkRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Link com ID " + id + " não encontrado."));

        link.setUrl(dto.url());
        link.setName(dto.name());
        link.setDescription(dto.description());
        link.setTags(resolveTags(dto.tagNames()));

        return toDto(linkRepository.save(link));
    }

    private LinkResponseDTO toDto(Link link) {
        String base = shortenerProperties.getPublicBaseUrl().trim();
        while (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        String code = link.getShortCode();
        if (code == null || code.isBlank()) {
            throw new IllegalStateException("Link sem shortCode; aguarde o backfill ou recrie a base.");
        }
        String shortUrl = base + "/r/" + code;
        return LinkResponseDTO.fromEntity(link, shortUrl);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!linkRepository.existsById(id)) {
            throw new EntityNotFoundException("Link com ID " + id + " não encontrado.");
        }
        linkRepository.deleteById(id);
    }

    private Set<Tag> resolveTags(Set<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return new HashSet<>();
        }

        Set<Tag> tags = new HashSet<>();
        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseThrow(() -> new EntityNotFoundException("Tag '" + tagName + "' não encontrada. Crie a tag primeiro."));
            tags.add(tag);
        }
        return tags;
    }
}
