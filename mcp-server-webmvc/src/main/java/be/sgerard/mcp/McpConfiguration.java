package be.sgerard.mcp;

import be.sgerard.mcp.core.service.CourseService;
import be.sgerard.mcp.core.service.mcp.CourseResourceMapper;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class McpConfiguration {

    private final CourseService service;
    private final CourseResourceMapper resourceMapper;

    @Bean
    ToolCallbackProvider courseTools() {
        return MethodToolCallbackProvider.builder()
                .toolObjects(service)
                .build();
    }

    @Bean
    List<McpServerFeatures.SyncResourceSpecification> myResources() {
        return service.getCourses().stream()
                .map(resourceMapper::map)
                .toList();
    }

    @Bean
    List<McpServerFeatures.SyncPromptSpecification> myPrompts() {
        var prompt = new McpSchema.Prompt("greeting", "A friendly greeting prompt",
                List.of(new McpSchema.PromptArgument("name", "The name to greet", true)));

        var promptSpecification = new McpServerFeatures.SyncPromptSpecification(prompt, (exchange, getPromptRequest) -> {
            String nameArgument = (String) getPromptRequest.arguments().get("name");
            if (nameArgument == null) {
                nameArgument = "friend";
            }
            var userMessage = new McpSchema.PromptMessage(McpSchema.Role.USER, new McpSchema.TextContent("Hello " + nameArgument + "! How can I assist you today?"));
            return new McpSchema.GetPromptResult("A personalized greeting message", List.of(userMessage));
        });

        return List.of(promptSpecification);
    }

}
