package com.falkknudsen.regless;

import javafx.scene.Node;
import javafx.scene.web.HTMLEditor;
import org.jsoup.Jsoup;

import java.util.Set;

public class Editor extends HTMLEditor {
    public Editor() {
        Set<Node> nodes = this.lookupAll(".tool-bar");
        for (Node node : nodes) {
            node.setVisible(false);
            node.setManaged(false);
        }
    }

    public String GetText() {
        return Jsoup.parse(this.getHtmlText()).text();
    }
}
