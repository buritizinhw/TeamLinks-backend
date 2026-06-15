package com.teamlinks.teamlinks_api.controller;

import com.teamlinks.teamlinks_api.dto.client.ClientRequestDTO;
import com.teamlinks.teamlinks_api.dto.client.ClientResponseDTO;
import com.teamlinks.teamlinks_api.service.client.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientResponseDTO> create(@Valid @RequestBody ClientRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.create(dto));
    }

    @GetMapping
    public ResponseEntity<Page<ClientResponseDTO>> findAll(@ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(clientService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ClientRequestDTO dto) {
        return ResponseEntity.ok(clientService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
