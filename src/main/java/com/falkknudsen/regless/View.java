package com.falkknudsen.regless;

import javafx.event.Event;
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

    private final RichEditor regexEditor;
    private final RichEditor testStringEditor;

    public View(Stage stage, Model model) {
        this.stage = stage;
        this.model = model;

        HBox hRegexLabel = createLabelBox("Regular Expression:");
        regexEditor = createRegexEditor();
        HBox hRegexPane = createRegexEditorPane();
        VBox vRegexPane = new VBox(0.0, hRegexLabel, hRegexPane);

        HBox hMatchLabel = createLabelBox("Test String:");
        testStringEditor = createTestEditor();
        var vMatchPane = new VBox(0.0, hMatchLabel, testStringEditor);

        VBox vCenterContents = new VBox(DEFAULT_BOX_SPACING, vRegexPane, vMatchPane);
        vCenterContents.setAlignment(Pos.CENTER);
        vCenterContents.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                System.exit(0);
            }
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

    private RichEditor createTestEditor() {
        Editor highlighting = new Editor();
        highlighting.setMinHeight(UI_PADDING * 30);
        highlighting.setMinWidth(UI_PADDING * 50);

        TextArea text = new TextArea();
        text.setMinHeight(UI_PADDING * 30);
        text.setMinWidth(UI_PADDING * 50);
        text.setPromptText("Enter your test string here...");

        RichEditor matchEditor = new RichEditor(text, highlighting);
        matchEditor.setAlignment(Pos.CENTER);
        return matchEditor;
    }

    private RichEditor createRegexEditor() {
        Editor highlighting = new Editor();
        highlighting.setMinHeight(UI_PADDING * 10);

        TextArea text = new TextArea();
        text.setMinHeight(UI_PADDING * 10);
        text.setPromptText("Enter your regex here...");
        text.setOnKeyReleased(this::updateHighlighting);

        RichEditor regexEditor = new RichEditor(text, highlighting);
        regexEditor.setAlignment(Pos.CENTER);
        return regexEditor;
    }

    private HBox createRegexEditorPane() {
        Button regexButton = new Button("Match");
        regexButton.setMinWidth(UI_PADDING * 10);
        HBox hRegexPane = new HBox(DEFAULT_BOX_SPACING, regexEditor, regexButton);
        hRegexPane.setAlignment(Pos.CENTER);
        regexButton.setOnAction(this::updateHighlighting);

        return hRegexPane;
    }

    private void updateHighlighting(Event e) {
        UpdatePattern(regexEditor.getText());
        UpdateMatcher(testStringEditor.getText());
        testStringEditor.format(matcher);
    }

    private static HBox createLabelBox(String text) {
        Label matchLabel = new Label(text);
        matchLabel.setVisible(true);
        HBox hMatchLabel = new HBox(matchLabel);
        hMatchLabel.setAlignment(Pos.BOTTOM_LEFT);
        return hMatchLabel;
    }
}
