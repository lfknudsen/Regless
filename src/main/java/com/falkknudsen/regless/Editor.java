package com.falkknudsen.regless;

import javafx.scene.Node;
import javafx.scene.web.HTMLEditor;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.jspecify.annotations.Nullable;

import java.util.Set;
import java.util.regex.Matcher;

import static com.falkknudsen.regless.View.VERBOSE;

/** An {@linkplain HTMLEditor} with a few extensions to parse and format its contents.<br/>
 Constants related to HTML formatting are located at the bottom of the file.<br/>
 Uses the {@linkplain org.jsoup.Jsoup} library for simple HTML parsing.
 @see HTMLEditor
 @author Louis Falk Knudsen */
public class Editor extends HTMLEditor {
    /// Cached string builder for formatting text as HTML.
    private final StringBuilder _sb = new StringBuilder(1000);
    /// Cached HTML parser.
    private final Parser _parser = new Parser(new HtmlTreeBuilder());

    /** The constructor removes the HTMLEditor's toolbars, and sets the contents to have the right
    style. */
    public Editor() {
        Set<Node> nodes = this.lookupAll(".tool-bar");
        for (Node node : nodes) {
            node.setVisible(false);
            node.setManaged(false);
        }
        setHtmlText(EmptyHTMLBody);
    }

    /// Returns the contents of the editor stripped of all tags.
    public String getText() {
        return _parser.parseInput(getHtmlText(), "").text();
    }

    /**
     Formats the editor's underlying HTML text so that the background of text that has been matched
     by a named subgroup is coloured. Different named subgroups have different colours.
     */
    public void format(@Nullable Matcher matcher, char[] text) {
        if (matcher == null
            || !matcher.find()
            || text.length == 0
            // this is cached, so it's just a few function calls:
            || matcher.pattern().toString().isEmpty()) {
            setHtmlText(EmptyHTMLBody);
            return;
        }

        _sb.setLength(0);
        _sb.append(Preamble);
        var currentIdx = 0;
        do {
            var leftIdx = matcher.start();
            var rightIdx = matcher.end();

            // Characters before match:
            if (leftIdx > currentIdx) {
                _sb.append(SPAN_START);
                for (int i = currentIdx; i < leftIdx; i++) {
                    _sb.append(replace(text[i]));
                }
                _sb.append(SPAN_END);
            }

            // Characters for match:
            int group = getGroup(matcher, leftIdx, rightIdx);
            if (VERBOSE) System.out.println("Group nr: " + group);

            if (group > 0) {
                _sb.append(ColourSpans[group % ColourSpans.length]);
            } else {
                _sb.append(nonCaptureColourSpan);
            }
            for (int i = leftIdx; i < rightIdx; i++) {
                _sb.append(replace(text[i]));
            }
            _sb.append(SPAN_END);
            currentIdx = rightIdx;
        } while (matcher.find());

        // Characters after match:
        if (currentIdx < text.length) {
            _sb.append(SPAN_START);
            for (int i = currentIdx; i < text.length; i++) {
                _sb.append(replace(text[i]));
            }
            _sb.append(SPAN_END);
        }
        _sb.append(Postamble);
        setHtmlText(_sb.toString());
        if (VERBOSE) System.out.println("After: \n" + _sb);
    }

    /// Return the whitespace HTML that will replace the given character.
    private static String replace(char c) {
        return switch (c) {
            case '\t' -> "&nbsp;&nbsp;&nbsp;&nbsp;";
            case '\n' -> "<br/>";
            default -> "&nbsp;";
        };
    }

    /**
     Returns the group number of the named capturing group that matched most recently.<br/> Will
     <b>not</b> fail fast if {@link Matcher#hasMatch matcher.hasMatch()} is false.<br/> Although the
     latter two parameters are unnecessary, they're needed outside anyway, so we may as well reduce
     the number of extraneous function calls.
     @param matcher
     The Matcher which found the match.
     @param start
     The value of matcher.start()
     @param end
     The value of matcher.end()
     */
    private static int getGroup(Matcher matcher, int start, int end) {
        int groupCount = matcher.groupCount();
        for (int i = 1; i <= groupCount; i++) {
            if (matcher.start(i) == start && matcher.end(i) == end) {
                return i;
            }
        }
        return -1;
    }

    /**
     Returns the group number of the named capturing group that matched most recently.<br/> Will
     <b>not</b> fail fast if {@link Matcher#hasMatch matcher.hasMatch()} is false.
     @param matcher
     The Matcher which found the match.
     */
    private static int getGroup(Matcher matcher) {
        return getGroup(matcher, matcher.start(), matcher.end());
    }

    //##########################################################################
    // String literals/constants
    //##########################################################################

    private static final String SPAN_START = "<span>";
    private static final String SPAN_END = "</span>";
    private static final String SPAN_WITH_COLOUR_START = "<span style=\"background-color:";
    private static final String SPAN_WITH_COLOUR_END = ";\">";

    /// Prefix to the contents of the HTMLEditor
    private static final String Preamble =
            "<html dir=\"ltr\">" +
            "<head>" +
            "<style>" +
            // Typeface set to one that is monospace.
            // This line height matches text-area's at this font size.
            "body{font-family:Consolas;font-size:16px;font-scale:1;line-height:21px;}" +
            // Erasing the three rules which define a default <p>
            "p{display:inline;margin-top:0;margin-bottom:0;}" +
            "</style>" +
            "</head>" +
            // Has to be set to true every time in order for editor to continue being editable.
            "<body contenteditable=\"true\">";

    /// Suffix to the contents of the HTMLEditor
    private static final String Postamble = "</body></html>";

    private static final String EmptyHTMLBody = Preamble + Postamble;

    /// Colours used for the text background. Each matching group is assigned one, round-robin
    /// style, by a simple {@code BackgroundColours\[groupID % groups.length()]}
    private static final String[] BackgroundColours = {
            "#c4e8ac", "#f6d7a6", "#c8c8ff", "#f2cfff", "#ffc5bf", "#c8e7d6" };

    /// Colour used for text background when something was matched, but not by a capturing group.
    private static final String nonCaptureColour = "#d5ebff";
    private static final String defaultBackground = "#ffffff";

    private static final String[] ColourSpans = new String[BackgroundColours.length];
    private static final String nonCaptureColourSpan =
            SPAN_WITH_COLOUR_START + nonCaptureColour + SPAN_WITH_COLOUR_END;

    static {
        for (int i = 0; i < BackgroundColours.length; i++) {
            ColourSpans[i] = SPAN_WITH_COLOUR_START + BackgroundColours[i] + SPAN_WITH_COLOUR_END;
        }
    }
}
