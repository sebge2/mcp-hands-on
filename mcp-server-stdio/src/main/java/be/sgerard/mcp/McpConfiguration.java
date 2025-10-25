package be.sgerard.mcp;

import be.sgerard.mcp.core.service.CourseService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpConfiguration {

    @Bean
    ToolCallbackProvider courseTools(CourseService service) {
        return MethodToolCallbackProvider.builder().toolObjects(service).build();
    }

}
