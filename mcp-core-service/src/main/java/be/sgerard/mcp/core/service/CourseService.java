package be.sgerard.mcp.core.service;

import be.sgerard.mcp.core.model.CourseEntity;
import be.sgerard.mcp.core.model.CourseMetadata;
import be.sgerard.mcp.core.repository.CourseRepository;
import be.sgerard.mcp.core.service.format.DocumentFormatHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static be.sgerard.mcp.core.utils.ResourceUtils.listFilesInResources;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseService {

    private final DocumentFormatHandler formatHandler;

    private final CourseRepository repository;

    @Tool(name = "get_all_courses", description = "Get all courses.")
    public List<String> findAllCourseTitles() {
        return repository.findAllMetadata().stream()
                .map(CourseMetadata::getTitle)
                .toList();
    }

    public List<CourseMetadata> findAllCourseMetadata() {
        return repository.findAllMetadata();
    }

    public Optional<CourseEntity> findById(String id) {
        return repository.findById(id);
    }

    @Tool(name = "get_course_by_title", description = "Get a course by its title.")
    public CourseEntity findByTitle(String title) {
        return repository.findByTitleIgnoreCase(title)
                .orElse(null);
    }

    @Tool(name = "create_course", description = "Create a new course.")
    public CourseEntity create(CourseEntity course) {
        return repository.save(course);
    }

    @Tool(name = "update_course_description", description = "Update course description.")
    public CourseEntity updateDescription(String title, String description) {
        final CourseEntity course = findByTitle(title);

        course.setDescription(description);

        return repository.save(course);
    }

    @Tool(name = "delete_course_by_title", description = "Delete course by its title.")
    public void deleteByTitle(String title) {
        repository.deleteByTitleIgnoreCase(title);
    }

    @PostConstruct
    void init() throws Exception {
        listFilesInResources("/courses", ".md")
                .stream()
                .map(this::initDocument)
                .peek(course -> log.info("Course [{}] added.", course.getTitle()))
                .forEach(repository::save);
    }

    private CourseEntity initDocument(Path file) {
        try {
            return formatHandler.parse(Files.readString(file));
        } catch (IOException e) {
            throw new IllegalStateException("Error while initializing course %s".formatted(file), e);
        }
    }
}