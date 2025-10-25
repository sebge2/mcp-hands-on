package be.sgerard.mcp.core.service.mcp;

import be.sgerard.mcp.core.model.CourseEntity;
import be.sgerard.mcp.core.service.CourseService;
import io.modelcontextprotocol.server.McpServerFeatures.SyncResourceSpecification;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CourseResourceMapper {

    public static final String PREFIX_URI = "courses://";

    private final CourseService service;

    public SyncResourceSpecification map(CourseEntity course) {
        var resource = new McpSchema.Resource(
                PREFIX_URI + course.getId(),
                course.getTitle(),
                course.getDescription(),
                "text/plain",
                null
        );

        return new SyncResourceSpecification(resource, this::getResource);

    }

    private McpSchema.ReadResourceResult getResource(McpSyncServerExchange exchange, McpSchema.ReadResourceRequest request) {
        final String content = service
                .findById(request.uri().substring(PREFIX_URI.length()))
                .map(CourseEntity::getContent)
                .orElse(null);

        return new McpSchema.ReadResourceResult(
                List.of(new McpSchema.TextResourceContents(
                        request.uri(),
                        "text/plain",
                        content
                ))
        );
    }
}
