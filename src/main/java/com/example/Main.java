package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
public void start(Stage stage) {
    // Method selection dropdown
    ComboBox<String> methodSelector = new ComboBox<>();
    methodSelector.getItems().addAll(
        "1. Bottom of diameter to bottom of keyway",
        "2. Flat across top to bottom of keyway",
        "3. Top of theoretical diameter to bottom of keyway"
    );
    methodSelector.setValue("1. Bottom of diameter to bottom of keyway");

    // Input fields
    TextField inputA = new TextField();
    inputA.setPromptText("Diameter (A)");

    TextField inputB = new TextField();
    inputB.setPromptText("Distance or Depth (B)");

    TextField inputC = new TextField(); // only used for method 2
    inputC.setPromptText("Enter C");
    inputC.setVisible(false); // only shown for method 2

    // Output label
    Label resultLabel = new Label("Offset will appear here.");

    // Calculate button
    Button calculateBtn = new Button("Calculate");

    calculateBtn.setOnAction(e -> {
    try {
        String method = methodSelector.getValue();

        double a = ExpressionEvaluator.evaluate(inputA.getText());
        double b = ExpressionEvaluator.evaluate(inputB.getText());

        System.out.println("A = " + inputA.getText() + " → " + a);
        System.out.println("B = " + inputB.getText() + " → " + b);

        double result = 0;

        if (method.startsWith("1")) {
            result = KeywayCalculator.fromBottomOfDiameter(a, b);
        } else if (method.startsWith("2")) {
            double c = ExpressionEvaluator.evaluate(inputC.getText());
            System.out.println("C = " + inputC.getText() + " → " + c);
            result = KeywayCalculator.fromTopFlat(a, b, c);
        } else if (method.startsWith("3")) {
            result = KeywayCalculator.fromTopOfDiameter(a, b);
        }

        resultLabel.setText("Offset = " + result);
    } catch (Exception ex) {
        ex.printStackTrace(); // helpful while debugging
        resultLabel.setText("Error: Invalid input.");
    }
});

    // When dropdown changes, update input labels/visibility
    methodSelector.setOnAction(e -> {
        String method = methodSelector.getValue();
        if (method.startsWith("2")) {
            inputA.setPromptText("Keyway Depth (A)");
            inputB.setPromptText("Keyway Width (B)");
            inputC.setPromptText("Diameter (C)");
            inputC.setVisible(true);
        } else {
            inputA.setPromptText("Diameter (A)");
            inputB.setPromptText("Distance or Depth (B)");
            inputC.setVisible(false);
        }
    });

    VBox layout = new VBox(10, methodSelector, inputA, inputB, inputC, calculateBtn, resultLabel);
    layout.setPadding(new Insets(20));

    Scene scene = new Scene(layout, 400, 300);
    stage.setTitle("Keyway Offset Calculator");
    stage.setScene(scene);
    stage.show();
}

    public static void main(String[] args) {
        launch();
    }
}
