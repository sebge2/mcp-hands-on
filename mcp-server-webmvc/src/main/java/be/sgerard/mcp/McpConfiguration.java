package be.sgerard.mcp;

import be.sgerard.mcp.core.service.CourseService;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class McpConfiguration {

    @Bean
    ToolCallbackProvider courseTools(CourseService service) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(service)
                .build();
    }

    @Bean
    List<McpServerFeatures.SyncResourceSpecification> myResources() {
        var systemInfoResource = new McpSchema.Resource(
                "system://info",
                "System Information",
                "Provides system configuration and runtime information",
                "text/plain",
                null
        );

        var systemInfoSpec = new McpServerFeatures.SyncResourceSpecification(
                systemInfoResource,
                (exchange, request) -> {
                    String systemInfo = """
                System: %s
                Java Version: %s
                Memory: %d MB
                """.formatted(
                            System.getProperty("os.name"),
                            System.getProperty("java.version"),
                            Runtime.getRuntime().totalMemory() / (1024 * 1024)
                    );

                    return new McpSchema.ReadResourceResult(
                            List.of(new McpSchema.TextResourceContents(
                                    request.uri(),
                                    "text/plain",
                                    systemInfo
                            ))
                    );
                }
        );

        return List.of(systemInfoSpec);
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
