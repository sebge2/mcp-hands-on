package be.sgerard.mcp;

import be.sgerard.mcp.core.service.CourseService;
import be.sgerard.mcp.core.service.mcp.CoursePromptProvider;
import be.sgerard.mcp.core.service.mcp.CourseResourceProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static io.modelcontextprotocol.server.McpServerFeatures.SyncPromptSpecification;
import static io.modelcontextprotocol.server.McpServerFeatures.SyncResourceSpecification;

@Configuration
@RequiredArgsConstructor
public class McpConfiguration {

    private final CourseService service;
    private final CourseResourceProvider resourceProvider;
    private final CoursePromptProvider promptProvider;

    @Bean
    ToolCallbackProvider courseTools() {
        return MethodToolCallbackProvider.builder()
                .toolObjects(service)
                .build();
    }

    @Bean
    List<SyncResourceSpecification> courseResources() {
        return resourceProvider.getResources();
    }

    @Bean
    List<SyncPromptSpecification> coursePrompt() {
        return List.of(promptProvider.getPrompt());
    }
}
