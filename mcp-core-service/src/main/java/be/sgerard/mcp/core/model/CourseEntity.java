package be.sgerard.mcp.core.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@Accessors(chain = true)
@Entity
public class CourseEntity {

    @Id
    @UuidGenerator
    @GeneratedValue
    private String id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String content;

}
