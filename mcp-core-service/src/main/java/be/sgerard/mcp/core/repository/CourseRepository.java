package be.sgerard.mcp.core.repository;

import be.sgerard.mcp.core.model.CourseEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends CrudRepository<CourseEntity, String> {

    @Override
    @NonNull
    List<CourseEntity> findAll();

    Optional<CourseEntity> findByTitleIgnoreCase(String title);

    @Modifying
    void deleteByTitleIgnoreCase(String title);
}
