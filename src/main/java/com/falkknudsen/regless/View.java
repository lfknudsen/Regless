package com.falkknudsen.regless;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static com.falkknudsen.regless.ResourceLoader.loadAppIcon;
import static com.falkknudsen.regless.ResourceLoader.loadStylesheet;

public class View {
    private Stage stage;
    private Model model;

    public final static boolean VERBOSE = false;

    private Pattern pattern;
    private Matcher matcher;

    public final static double UI_PADDING = 9.0; // Used as Double sometimes, therefore not float
    public final static float DEFAULT_BOX_SPACING = 10.0f;

    // This default works well for 1080p screens.
    // TODO: Find more scalable (heh) solution.
    public final static double WINDOW_WIDTH = 775;
    public final static double WINDOW_HEIGHT = 417;

    public View(Stage stage, Model model) {
        this.stage = stage;
        this.model = model;

        TextArea textArea = new TextArea();
        textArea.setMinHeight(UI_PADDING * 30);

        Label regexLabel = new Label("Regular Expression:");
        regexLabel.setVisible(true);
        HBox hRegexLabel = new HBox(regexLabel);
        hRegexLabel.setAlignment(Pos.BOTTOM_LEFT);
        Label matchLabel = new Label("Test String:");
        matchLabel.setVisible(true);
        HBox hMatchLabel = new HBox(matchLabel);
        hMatchLabel.setAlignment(Pos.BOTTOM_LEFT);

        Editor matchText = new Editor();
        matchText.setMinHeight(UI_PADDING * 30);
        matchText.setMinWidth(UI_PADDING * 50);

        Editor regexText = new Editor();
        regexText.setMinHeight(UI_PADDING * 10);
        Button regexButton = new Button("Match");
        regexButton.setMinWidth(UI_PADDING * 10);

        RichEditor matchPane = new RichEditor(textArea, matchText);
        matchPane.setAlignment(Pos.CENTER);
        VBox vMatchPane = new VBox(0.0, hMatchLabel, matchPane);

        HBox hRegexPane = new HBox(DEFAULT_BOX_SPACING, regexText, regexButton);
        hRegexPane.setAlignment(Pos.CENTER);
        VBox vRegexPane = new VBox(0.0, hRegexLabel, hRegexPane);

        VBox vCenterContents = new VBox(DEFAULT_BOX_SPACING, vRegexPane, vMatchPane);
        vCenterContents.setAlignment(Pos.CENTER);
        vCenterContents.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                System.exit(0);
            }
        });
        regexText.setOnKeyReleased(_ -> {
            UpdatePattern(regexText.getText());
            UpdateMatcher(matchPane.getText());
            matchPane.format(matcher);
        });
        regexButton.setOnAction(e -> {
            UpdatePattern(regexText.getText());
            UpdateMatcher(matchPane.getText());
            matchPane.format(matcher);
        });

        AnchorPane.setTopAnchor(vCenterContents, UI_PADDING);
        AnchorPane.setBottomAnchor(vCenterContents, UI_PADDING);
        AnchorPane.setLeftAnchor(vCenterContents, UI_PADDING);
        AnchorPane.setRightAnchor(vCenterContents, UI_PADDING);
        AnchorPane root = new AnchorPane(vCenterContents);
        root.setId("root");
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        if (scene.getStylesheets().isEmpty()) {
            String stylesheet = loadStylesheet();
            if (stylesheet != null) {
                scene.getStylesheets().add(stylesheet);
            }
        }

        var iconPath = loadAppIcon();
        if (iconPath != null) {
            stage.getIcons().add(new Image(iconPath));
        }
        stage.setTitle("Regless");
        stage.setScene(scene);
        stage.show();
    }

    @NullMarked
    private void UpdatePattern(String regex) {
        //regex = "(?:" + regex + ")";
        try {
            pattern = Pattern.compile(regex);
            if (VERBOSE) System.out.println("Valid regular expression: " + regex);
        } catch (PatternSyntaxException e) {
            if (VERBOSE) System.err.println("Invalid regular expression: " + regex);
            pattern = Pattern.compile("");//("(?:)");
        }
    }

    @NullUnmarked
    private void UpdateMatcher(String match) {
        if (pattern == null) {
            matcher = null;
        } else {
            matcher = pattern.matcher(match);
        }
    }
}
