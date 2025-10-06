package com.falkknudsen.regless;

import javafx.scene.Node;
import javafx.scene.web.HTMLEditor;
import org.jsoup.Jsoup;
import org.jspecify.annotations.Nullable;

import java.util.Set;
import java.util.regex.Matcher;

public class Editor extends HTMLEditor {
    /// Cached string builder.
    private final StringBuilder sb = new StringBuilder(1000);
    /// Cached plaintext version of editor contents.
    private String text = "";

    /// The constructor removes the HTMLEditor's toolbars, and sets the contents
    /// to have the right style.
    public Editor() {
        Set<Node> nodes = this.lookupAll(".tool-bar");
        for (Node node : nodes) {
            node.setVisible(false);
            node.setManaged(false);
        }
        setHtmlText(Preamble + Postamble);
        //setStyle("-fx-background-color: transparent;");
    }

    /// Returns the contents of the editor stripped of all tags.
    public String GetText() {
        String html = getHtmlText();
        text = Jsoup.parse(html).text();
        return text;
    }

    /** Formats the editor's underlying HTML text so that the background of text
     that has been matched by a named subgroup is coloured. Different named
     subgroups have different colours. */
    public void Format(@Nullable Matcher matcher) {
        if (matcher == null || !matcher.find()) {
            return;
        }
        sb.delete(0, sb.length())
            .append(Preamble);
        var currentIdx = 0;
        do {
            var leftIdx = matcher.start();
            var rightIdx = matcher.end();
            if (leftIdx > currentIdx) {
                sb.append("<span>")
                    .append(text, currentIdx, leftIdx)
                    .append("</span>");
            }

            int group = GetGroup(matcher, leftIdx, rightIdx);
            System.out.println("Group nr: " + group);
            String colour = nonCaptureColour;
            if (group > 0) {
                colour = BackgroundColours[group % BackgroundColours.length];
            }
            sb.append("<span style=\"background-color:")
                .append(colour)
                .append(";\">")
                .append(text, leftIdx, rightIdx)
                .append("</span>");
            currentIdx = rightIdx;
        } while (matcher.find());

        if (currentIdx < text.length()) {
            sb.append("<span>")
                .append(text, currentIdx, text.length())
                .append("</span>");
        }
        sb.append("<span></span>")
            .append(Postamble);
        setHtmlText(sb.toString());
    }

    /// Prefix to the contents of the HTMLEditor
    private static final String Preamble =
        "<html dir=\"ltr\">" +
            "<head>" +
                "<style>" +
                    // Typeface set to one that is monospace.
                    "body {font-family:Consolas;}" +
                    // Still have display:block, for smoother newline behaviour
                    "p {margin-top:0;margin-bottom:0;}" +
                "</style>" +
            "</head>" +
            // Has to be set to true every time in order for editor to continue being editable.
            "<body contenteditable=\"true\">";

    /// Suffix to the contents of the HTMLEditor
    private static final String Postamble = "</body></html>";

    /// Colours used for the text background. Each matching group is assigned one, round-robin style,
    /// by a simple {@code BackgroundColours\[groupID % groups.length()]}
    public static final String[] BackgroundColours = {
            "#c4e8ac","#f6d7a6", "#c8c8ff", "#f2cfff", "#ffc5bf", "#c8e7d6"};

    /// Colour used for text background when something was matched, but not by a capturing group.
    private static final String nonCaptureColour = "#d5ebff";

    /** Returns the group number of the named capturing group that matched most
     recently.<br/>
     Will <b>not</b> fail fast if {@link Matcher#hasMatch matcher.hasMatch()} is false.<br/>
     Although the latter two parameters are unnecessary, they're needed outside
     anyway, so we may as well reduce the number of extraneous function calls.
     @param matcher The Matcher which found the match.
     @param start The value of matcher.start()
     @param end The value of matcher.end() */
    private static int GetGroup(Matcher matcher, int start, int end) {
        int groupCount = matcher.groupCount();
        for (int i = 1; i < groupCount; i++) {
            if (matcher.start(i) == start && matcher.end(i) == end) {
                return i;
            }
        }
        return -1;
    }

    /** Returns the group number of the named capturing group that matched most
     recently.<br/>
     Will <b>not</b> fail fast if {@link Matcher#hasMatch matcher.hasMatch()} is false.
     @param matcher The Matcher which found the match. */
    private static int GetGroup(Matcher matcher) {
        return GetGroup(matcher, matcher.start(), matcher.end());
    }
}
