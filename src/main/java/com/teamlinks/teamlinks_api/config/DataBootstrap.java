package com.teamlinks.teamlinks_api.config;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.teamlinks.teamlinks_api.entity.Link;
import com.teamlinks.teamlinks_api.entity.Project;
import com.teamlinks.teamlinks_api.entity.ProjectStatus;
import com.teamlinks.teamlinks_api.entity.Tag;
import com.teamlinks.teamlinks_api.repository.LinkRepository;
import com.teamlinks.teamlinks_api.repository.ProjectRepository;
import com.teamlinks.teamlinks_api.repository.TagRepository;

import com.teamlinks.teamlinks_api.util.ShortCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;

@Component
@Order(0)
@RequiredArgsConstructor
public class DataBootstrap implements CommandLineRunner {

    private final TagRepository tagRepository;
    private final ProjectRepository projectRepository;
    private final LinkRepository linkRepository;
    private final ShortCodeGenerator shortCodeGenerator;

    @Override
    public void run(String... args) {
        if (tagRepository.count() > 0) {
            return;
        }

        List<Tag> tags = createTags();
        Map<String, Tag> tagMap = tags.stream()
                .collect(Collectors.toMap(Tag::getName, t -> t));

        Project teamLinks = createProject("TeamLinks", "Sistema de gerenciamento de links para projetos", ProjectStatus.EM_ANDAMENTO);
        Project ecommerce = createProject("E-commerce API", "API REST para plataforma de e-commerce", ProjectStatus.INICIAR);

        createLink("GitHub - TeamLinks",
                "https://github.com/teamlinks/teamlinks",
                "Repositório principal do projeto",
                teamLinks,
                Set.of(tagMap.get("repositorio"), tagMap.get("backend")));

        createLink("Documentação Spring Boot",
                "https://docs.spring.io/spring-boot/reference/",
                "Referência oficial do Spring Boot",
                teamLinks,
                Set.of(tagMap.get("documentacao"), tagMap.get("backend")));

        createLink("Swagger UI",
                "http://localhost:8080/swagger-ui.html",
                "Documentação interativa da API",
                teamLinks,
                Set.of(tagMap.get("documentacao"), tagMap.get("backend")));

        createLink("Repositório GitLab",
                "https://gitlab.com/ecommerce/api",
                "Repositório principal do e-commerce",
                ecommerce,
                Set.of(tagMap.get("repositorio"), tagMap.get("backend")));

        createLink("Figma - Protótipo",
                "https://figma.com/file/ecommerce-prototype",
                "Protótipo de alta fidelidade do e-commerce",
                ecommerce,
                Set.of(tagMap.get("design"), tagMap.get("frontend")));
    }

    private List<Tag> createTags() {
        List<String> names = List.of(
                "backend", "frontend", "repositorio", "design",
                "ia", "comunicacao", "testes", "deploy", "documentacao"
        );

        return names.stream()
                .map(name -> {
                    Tag tag = new Tag();
                    tag.setName(name);
                    return tagRepository.save(tag);
                })
                .toList();
    }

    private Project createProject(String name, String description, ProjectStatus status) {
        Project project = new Project();
        project.setName(name);
        project.setDescription(description);
        project.setStatus(status);
        return projectRepository.save(project);
    }

    private void createLink(String name, String url, String description, Project project, Set<Tag> tags) {
        Link link = new Link();
        link.setName(name);
        link.setUrl(url);
        link.setDescription(description);
        link.setProject(project);
        link.setTags(tags);
        link.setShortCode(shortCodeGenerator.nextUnique());
        linkRepository.save(link);
    }
}
