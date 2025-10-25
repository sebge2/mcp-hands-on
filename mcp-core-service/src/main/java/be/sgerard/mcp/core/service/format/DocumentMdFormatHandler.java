package be.sgerard.mcp.core.service.format;

import be.sgerard.mcp.core.model.CourseEntity;
import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.ast.Paragraph;
import com.vladsch.flexmark.ast.Text;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.joining;

@Component
public class DocumentMdFormatHandler implements DocumentFormatHandler {

    public static final String SUMMARY_SECTION = "Summary";

    @Override
    public CourseEntity parse(String content) {
        final MutableDataSet options = new MutableDataSet();
        final Parser parser = Parser.builder(options).build();
        final Node document = parser.parse(content);

        return new CourseEntity()
                .setTitle(getTitle(document).orElseThrow(() -> new IllegalArgumentException("No title found.")))
                .setDescription(
                        this
                                .streamTextInsideHeading(
                                        findHeadingByText(document, SUMMARY_SECTION)
                                                .orElseThrow(() -> new IllegalArgumentException("No summary section found."))
                                )
                                .collect(joining("\n"))
                )
                .setContent(content);
    }

    private Optional<String> getTitle(Node document) {
        return streamChildren(document)
                .filter(Heading.class::isInstance)
                .map(Heading.class::cast)
                .findFirst()
                .map(this::getName);
    }

    @SuppressWarnings("SameParameterValue")
    private Optional<Heading> findHeadingByText(Node document, String headingText) {
        return streamChildren(document)
                .filter(Heading.class::isInstance)
                .map(Heading.class::cast)
                .filter(heading -> getName(heading).toLowerCase().contains(headingText.toLowerCase()))
                .findFirst();
    }

    private String getName(Heading heading) {
        return streamChildren(heading)
                .filter(Text.class::isInstance)
                .map(Text.class::cast)
                .map(text -> text.getChars().trim().unescape())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No title text found."));
    }

    private Stream<String> streamTextInsideHeading(Node node) {
        if (node instanceof Paragraph text) {
            return Stream.of(
                    streamChildren(text)
                            .flatMap(this::streamTextInsideHeading)
                            .collect(joining(""))
            );
        } else if (node instanceof Text text) {
            return Stream.of(text.getChars().toString());
        }

        return streamNext(node)
                .takeWhile(currentNode -> !(currentNode instanceof Heading))
                .flatMap(child -> streamChildren(child)
                        .flatMap(this::streamTextInsideHeading)
                );
    }

    private Stream<Node> streamChildren(Node node) {
        return StreamSupport.stream(node.getChildren().spliterator(), false);
    }

    private Stream<Node> streamNext(Node node) {
        return Stream.iterate(node, Objects::nonNull, Node::getNext).skip(1);
    }
}
