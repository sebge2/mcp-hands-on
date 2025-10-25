package be.sgerard.mcp.core.service.mcp;

import be.sgerard.mcp.core.model.CourseEntity;
import be.sgerard.mcp.core.model.CourseMetadata;
import be.sgerard.mcp.core.service.CourseService;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.modelcontextprotocol.server.McpServerFeatures.SyncResourceSpecification;
import static io.modelcontextprotocol.spec.McpSchema.*;

@Component
@RequiredArgsConstructor
public class CourseResourceProvider {

    public static final String PREFIX_URI = "courses://";

    private final CourseService service;

    public List<SyncResourceSpecification> getResources() {
        return service.findAllCourseMetadata().stream()
                .map(this::map)
                .toList();
    }

    private SyncResourceSpecification map(CourseMetadata course) {
        var resource = new McpSchema.Resource(
                PREFIX_URI + course.getId(),
                course.getTitle(),
                course.getDescription(),
                "text/plain",
                null
        );

        return new SyncResourceSpecification(resource, this::getResource);
    }

    private ReadResourceResult getResource(McpSyncServerExchange exchange, ReadResourceRequest request) {
        final String content = service
                .findById(request.uri().substring(PREFIX_URI.length()))
                .map(CourseEntity::getContent)
                .orElse(null);

        return new ReadResourceResult(
                List.of(new TextResourceContents(
                        request.uri(),
                        "text/plain",
                        content
                ))
        );
    }
}
