package com.teamlinks.teamlinks_api.service.client;

import com.teamlinks.teamlinks_api.dto.client.ClientRequestDTO;
import com.teamlinks.teamlinks_api.dto.client.ClientResponseDTO;
import com.teamlinks.teamlinks_api.entity.Client;
import com.teamlinks.teamlinks_api.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    @Transactional
    public ClientResponseDTO create(ClientRequestDTO dto) {
        if (clientRepository.existsByName(dto.name())) {
            throw new IllegalArgumentException("Cliente com nome '" + dto.name() + "' já existe.");
        }

        var client = new Client();
        client.setName(dto.name());

        return ClientResponseDTO.fromEntity(clientRepository.save(client));
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponseDTO findById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente com ID " + id + " não encontrado."));
        return ClientResponseDTO.fromEntity(client);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClientResponseDTO> findAll(Pageable pageable) {
        return clientRepository.findAll(pageable)
                .map(ClientResponseDTO::fromEntity);
    }

    @Override
    @Transactional
    public ClientResponseDTO update(Long id, ClientRequestDTO dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente com ID " + id + " não encontrado."));

        if (!client.getName().equals(dto.name()) && clientRepository.existsByName(dto.name())) {
            throw new IllegalArgumentException("Cliente com nome '" + dto.name() + "' já existe.");
        }

        client.setName(dto.name());

        return ClientResponseDTO.fromEntity(clientRepository.save(client));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente com ID " + id + " não encontrado."));

        if (client.getProjects() != null && !client.getProjects().isEmpty()) {
            throw new IllegalArgumentException("Não é possível excluir cliente com projetos associados.");
        }

        clientRepository.delete(client);
    }
}
