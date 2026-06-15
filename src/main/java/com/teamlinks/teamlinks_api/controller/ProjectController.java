package com.teamlinks.teamlinks_api.controller;

import com.teamlinks.teamlinks_api.dto.link.LinkResponseDTO;
import com.teamlinks.teamlinks_api.dto.project.ProjectRequestDTO;
import com.teamlinks.teamlinks_api.dto.project.ProjectResponseDTO;
import com.teamlinks.teamlinks_api.service.link.LinkService;
import com.teamlinks.teamlinks_api.service.project.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final LinkService linkService;

    @PostMapping
    public ResponseEntity<ProjectResponseDTO> create(@Valid @RequestBody ProjectRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.create(dto));
    }

    @GetMapping
    public ResponseEntity<Page<ProjectResponseDTO>> findAll(@ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(projectService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequestDTO dto) {
        return ResponseEntity.ok(projectService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{projectId}/links")
    public ResponseEntity<Page<LinkResponseDTO>> findLinksByProject(
            @PathVariable Long projectId,
            @ParameterObject @PageableDefault(size = 10, sort = "clickCount", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(linkService.findByProjectId(projectId, pageable));
    }
}
