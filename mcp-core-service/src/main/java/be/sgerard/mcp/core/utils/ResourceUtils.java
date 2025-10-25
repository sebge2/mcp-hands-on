package be.sgerard.mcp.core.utils;

import lombok.experimental.UtilityClass;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.Collections.emptyList;

@UtilityClass
public class ResourceUtils {

    public static List<Path> listFilesInResources(String basePath, String fileExtension) throws Exception {
        final URL resource = ResourceUtils.class.getResource(basePath);

        if (resource == null) {
            return emptyList();
        }

        try (var filesPaths = Files.walk(Paths.get(resource.toURI()))) {
            return filesPaths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(fileExtension))
                    .toList();
        }
    }

}