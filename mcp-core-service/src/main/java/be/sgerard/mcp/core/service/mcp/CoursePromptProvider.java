package be.sgerard.mcp.core.service.mcp;

import be.sgerard.mcp.core.model.CourseEntity;
import be.sgerard.mcp.core.service.CourseService;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static io.modelcontextprotocol.server.McpServerFeatures.SyncPromptSpecification;
import static io.modelcontextprotocol.spec.McpSchema.*;

@Component
@RequiredArgsConstructor
public class CoursePromptProvider {

    private final CourseService service;

    public SyncPromptSpecification getPrompt() {
        var prompt = new Prompt(
                "Summarize a lesson",
                "Summarize a lesson.",
                List.of(new PromptArgument("title", "The lesson title.", true))
        );

        return new SyncPromptSpecification(prompt, this::prompt);
    }

    private GetPromptResult prompt(McpSyncServerExchange exchange, GetPromptRequest request) {
        final String title = (String) request.arguments().get("title");

        final String content = Optional.ofNullable(service.findByTitle(title))
                .map(CourseEntity::getContent)
                .orElse(null);

        final PromptMessage userMessage = new PromptMessage(Role.USER, new TextContent("Please summarize in 10 lines the following summary. If there are pros and cons please add them. " + content));
        return new GetPromptResult("Lesson summary.", List.of(userMessage));
    }
}
