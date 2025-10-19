package be.sgerard.mcp.service;

import be.sgerard.mcp.model.Course;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    private final List<Course> courses = new ArrayList<>();

    @Tool(name = "get_all_courses", description = "Get all courses.")
    public List<String> getCourses() {
        return courses.stream()
                .map(Course::getTitle)
                .toList();
    }

    @Tool(name = "get_course_by_title", description = "Get a course by its title.")
    public Course getCourseByTitle(String title) {
        return courses.stream()
                .filter(course -> course.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }

    @Tool(name = "create_course", description = "Create a new course.")
    public Course createCourse(Course course) {
        courses.add(course);
        return course;
    }

    @Tool(name = "update_course_description", description = "Update course description.")
    public Course updateDescription(String title, String description) {
        final Course course = getCourseByTitle(title);

        if (course == null) {
            return null;
        }

        course.setDescription(description);

        return course;
    }

    @Tool(name = "delete_course_by_title", description = "Delete course by its title.")
    public void deleteCourseByTitle(String title) {
        final Course course = getCourseByTitle(title);

        if (course == null) {
            return;
        }

        courses.remove(course);
    }

    @PostConstruct
    void init() {
        courses.add(new Course("MCP Hands-On", "Create your own MCP server!"));
    }
}
