package com.falkknudsen.regless;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Model m = new Model();
        View v = new View(stage, m);
        Controller c = new Controller(v, m);
    }
}
