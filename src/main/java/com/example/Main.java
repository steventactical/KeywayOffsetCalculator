package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        // Basic UI just so the window still opens
        Label label = new Label("Check the terminal for method test results.");
        Scene scene = new Scene(label, 400, 200);
        stage.setTitle("Keyway Offset Calculator");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
