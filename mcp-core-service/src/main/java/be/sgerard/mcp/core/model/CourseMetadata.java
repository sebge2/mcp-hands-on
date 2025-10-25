package be.sgerard.mcp.core.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CourseMetadata {

    private String id;

    private String title;

    private String description;

    public CourseMetadata(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
}
