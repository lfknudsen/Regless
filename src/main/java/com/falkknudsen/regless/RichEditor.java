package com.falkknudsen.regless;

import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;

import java.util.regex.Matcher;

public class RichEditor extends StackPane {
    TextArea front;
    Editor back;

    StringBuilder sb = new StringBuilder(1000);

    public RichEditor(TextArea front, Editor back) {
        this.front = front;
        this.back = back;
        getChildren().addAll(back, front);
        sb.repeat(' ', 1000);
    }

    public String getText() {
        return front.getText();
    }

    public void format(Matcher matcher) {
        back.format(matcher, front.getText());
    }
}
