package com.falkknudsen.regless;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.util.Set;

public class View {
    private Stage stage;
    private Model model;

    public View(Stage stage, Model model) {
        this.stage = stage;
        this.model = model;

        HTMLEditor regexBox = NewHTMLEditor();
        HTMLEditor matchText = NewHTMLEditor();
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        //regexBox.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        //matchText.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        root.getChildren().addAll(regexBox, matchText);
        regexBox.setOnKeyReleased(event -> {
            System.out.println(regexBox.getHtmlText());
        });

        Scene scene = new Scene(root, 1280, 720);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    private static HTMLEditor NewHTMLEditor() {
        HTMLEditor result = new HTMLEditor();
        Set<Node> nodes = result.lookupAll(".tool-bar");
        for (Node node : nodes) {
            node.setVisible(false);
            node.setManaged(false);
        }
        return result;
    }
}
