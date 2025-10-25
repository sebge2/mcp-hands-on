package be.sgerard.mcp.core.service.format;

import be.sgerard.mcp.core.model.CourseEntity;

public interface DocumentFormatHandler {

    CourseEntity parse(String content);

}
