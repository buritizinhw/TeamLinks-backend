package com.teamlinks.teamlinks_api.service.client;

import com.teamlinks.teamlinks_api.dto.client.ClientRequestDTO;
import com.teamlinks.teamlinks_api.dto.client.ClientResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientService {

    ClientResponseDTO create(ClientRequestDTO dto);

    ClientResponseDTO findById(Long id);

    Page<ClientResponseDTO> findAll(Pageable pageable);

    ClientResponseDTO update(Long id, ClientRequestDTO dto);

    void delete(Long id);
}
