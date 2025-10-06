package com.falkknudsen.regless;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;

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

        Editor matchText = new Editor();
        Editor highlight = new Editor();
        highlight.setBackground(
                new Background(
                new BackgroundFill(
                        new Color(1, 0, 0, 1), null, null)));
        var matchPane = new StackPane();
        matchPane.setAlignment(Pos.CENTER);
        matchPane.getChildren().addAll(matchText);//, highlight);

        Editor regexText = new Editor();
        Button regexButton = new Button("Match");
        regexButton.setOnAction(e -> {
            UpdatePattern(regexText.GetText());
            UpdateMatcher(matchText.GetText());
            matchText.Format(matcher);
        });

        HBox regexPane = new HBox();
        regexPane.setAlignment(Pos.CENTER);
        regexPane.getChildren().addAll(regexText, regexButton);

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(regexPane, matchPane);
        root.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                System.exit(0);
            }
        });
        regexText.setOnKeyReleased(_ -> {
            UpdatePattern(regexText.GetText());
            UpdateMatcher(matchText.GetText());
            matchText.Format(matcher);
        });

        Scene scene = new Scene(root, 1280, 720);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    @NullMarked
    private void UpdatePattern(String regex) {
        regex = "(?:" + regex + ")";
        try {
            pattern = Pattern.compile(regex);
            System.out.println("Valid regular expression: " + regex);
        } catch (PatternSyntaxException e) {
            System.err.println("Invalid regular expression: " + regex);
            pattern = Pattern.compile("(?:)");
        }
    }

    @NullUnmarked
    private void UpdateMatcher(String match) {
        if (pattern == null) {
            matcher = null;
            return;
        }
        matcher = pattern.matcher(match);
    }
}
