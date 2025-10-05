package com.falkknudsen.regless;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class View {
    private Stage stage;
    private Model model;

    private Pattern pattern;
    private Matcher matcher;

    public View(Stage stage, Model model) {
        this.stage = stage;
        this.model = model;

        Editor regexText = new Editor();
        Editor matchText = new Editor();
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(regexText, matchText);
        regexText.setOnKeyReleased(_ -> UpdatePattern(regexText.GetText()));
        matchText.setOnKeyReleased(_ -> UpdateMatcher(matchText.GetText()));

        Scene scene = new Scene(root, 1280, 720);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    private void UpdatePattern(String regex) {
        try {
            pattern = Pattern.compile(regex);
        } catch (PatternSyntaxException e) {
            pattern = null;
        }
    }

    private void UpdateMatcher(String match) {
        matcher = pattern.matcher(match);
        if (matcher.find()) {
            System.out.println("Match found!!");
        }
    }
}
