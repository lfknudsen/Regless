package com.falkknudsen.regless;

import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;

import java.util.regex.Matcher;

/** A wrapper and StackPane for a TextArea and HTMLEditor.
 The TextArea is laid out in front of the HTMLEditor. */
public class RichEditor extends StackPane {
    TextArea front;
    Editor back;

    public RichEditor(TextArea front, Editor back) {
        this.front = front;
        this.back = back;
        getChildren().addAll(back, front);
    }

    public String getText() {
        return front.getText();
    }

    public void format(Matcher matcher) {
        back.format(matcher, front.getText().toCharArray());
    }
}
