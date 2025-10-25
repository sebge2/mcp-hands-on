package be.sgerard.mcp.core.repository;

import be.sgerard.mcp.core.model.CourseEntity;
import be.sgerard.mcp.core.model.CourseMetadata;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends CrudRepository<CourseEntity, String> {

    @Override
    @NonNull
    List<CourseEntity> findAll();

    @Query("SELECT new be.sgerard.mcp.core.model.CourseMetadata(c.id, c.title, c.description) FROM CourseEntity c")
    List<CourseMetadata> findAllMetadata();

    Optional<CourseEntity> findByTitleIgnoreCase(String title);

    @Modifying
    void deleteByTitleIgnoreCase(String title);
}
