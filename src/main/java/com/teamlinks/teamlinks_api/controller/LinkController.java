package com.teamlinks.teamlinks_api.controller;

import com.teamlinks.teamlinks_api.dto.link.LinkRequestDTO;
import com.teamlinks.teamlinks_api.dto.link.LinkResponseDTO;
import com.teamlinks.teamlinks_api.service.link.LinkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/links")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    @PostMapping("/project/{projectId}")
    public ResponseEntity<LinkResponseDTO> create(
            @PathVariable Long projectId,
            @Valid @RequestBody LinkRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(linkService.create(projectId, dto));
    }

    @GetMapping("/ref/{shortCode}")
    public ResponseEntity<LinkResponseDTO> findByShortCode(@PathVariable String shortCode) {
        return ResponseEntity.ok(linkService.findByShortCode(shortCode));
    }

    @GetMapping("/ref/{shortCode}/redirect")
    public ResponseEntity<LinkResponseDTO> redirect(@PathVariable String shortCode) {
        return ResponseEntity.ok(linkService.resolveForRedirect(shortCode));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LinkResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(linkService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LinkResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody LinkRequestDTO dto) {
        return ResponseEntity.ok(linkService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        linkService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
