package com.teamlinks.teamlinks_api.service.project;

import com.teamlinks.teamlinks_api.dto.project.ProjectRequestDTO;
import com.teamlinks.teamlinks_api.dto.project.ProjectResponseDTO;
import com.teamlinks.teamlinks_api.entity.Project;
import com.teamlinks.teamlinks_api.entity.ProjectStatus;
import com.teamlinks.teamlinks_api.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    @Transactional
    public ProjectResponseDTO create(ProjectRequestDTO dto) {
        if (projectRepository.existsByName(dto.name())) {
            throw new IllegalArgumentException("Projeto com nome '" + dto.name() + "' já existe.");
        }

        var project = new Project();
        project.setName(dto.name());
        project.setDescription(dto.description());
        project.setStatus(dto.status() != null ? dto.status() : ProjectStatus.INICIAR);

        return ProjectResponseDTO.fromEntity(projectRepository.save(project));
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponseDTO findById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Projeto com ID " + id + " não encontrado."));
        return ProjectResponseDTO.fromEntity(project);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponseDTO> findAll(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(ProjectResponseDTO::fromEntity);
    }

    @Override
    @Transactional
    public ProjectResponseDTO update(Long id, ProjectRequestDTO dto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Projeto com ID " + id + " não encontrado."));

        if (!project.getName().equals(dto.name()) && projectRepository.existsByName(dto.name())) {
            throw new IllegalArgumentException("Projeto com nome '" + dto.name() + "' já existe.");
        }

        project.setName(dto.name());
        project.setDescription(dto.description());
        if (dto.status() != null) {
            project.setStatus(dto.status());
        }

        return ProjectResponseDTO.fromEntity(projectRepository.save(project));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new EntityNotFoundException("Projeto com ID " + id + " não encontrado.");
        }
        projectRepository.deleteById(id);
    }
}
