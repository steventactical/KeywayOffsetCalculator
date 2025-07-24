package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        ComboBox<String> methodSelector = new ComboBox<>();
        methodSelector.getItems().addAll(
            "1. Bottom of diameter to bottom of keyway",
            "2. Flat across top to bottom of keyway",
            "3. Top of theoretical diameter to bottom of keyway"
        );
        methodSelector.setValue("1. Bottom of diameter to bottom of keyway");

        List<String> calculationLog = new ArrayList<>();

        TextField inputA = new TextField();
        inputA.setPromptText("Diameter (A)");

        TextField inputB = new TextField();
        inputB.setPromptText("Distance or Depth (B)");

        TextField inputC = new TextField();
        inputC.setPromptText("Enter C");
        inputC.setVisible(false);

        Label offsetLabel = new Label("Offset:");
        offsetLabel.getStyleClass().add("offset-label");

        Label resultLabel = new Label("      ");
        resultLabel.setPrefWidth(50);
        resultLabel.setStyle("-fx-alignment: center-right;");
        resultLabel.getStyleClass().add("offset-value");

        HBox offsetRow = new HBox(5, offsetLabel, resultLabel);
        offsetRow.setAlignment(Pos.CENTER_LEFT);

        Button calculateBtn = new Button("Calculate");
        Button clearBtn = new Button("Clear");
        Button copyBtn = new Button("Copy");

        HBox buttonRow = new HBox(5,
            new HBox(5, calculateBtn, clearBtn, copyBtn),
            offsetRow
        );
        buttonRow.setAlignment(Pos.CENTER);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(320);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(false);

        Map<String, Image> imageCache = new HashMap<>();
        imageCache.put("1", new Image(getClass().getResource("/images/KeywayBottom.PNG").toExternalForm()));
        imageCache.put("2", new Image(getClass().getResource("/images/KeywayFlat.PNG").toExternalForm()));
        imageCache.put("3", new Image(getClass().getResource("/images/KeywayTop.PNG").toExternalForm()));

        imageView.setImage(imageCache.get("1"));

        TextArea logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefHeight(100);
        logArea.setWrapText(true);
        logArea.setPromptText("Calculation log...");
        logArea.setVisible(true);

        calculateBtn.setOnAction(e -> {
            try {
                String method = methodSelector.getValue();
                String methodNumber = method.substring(0, 2); // Extracts "1.", "2.", or "3."

                double a = ExpressionEvaluator.evaluate(inputA.getText());
                double b = ExpressionEvaluator.evaluate(inputB.getText());

                double result = 0;
                String logEntry;

                if (method.startsWith("1")) {
                    result = KeywayCalculator.fromBottomOfDiameter(a, b);
                    logEntry = String.format("%s -> A: %.4f, B: %.4f, Offset: %.4f", methodNumber, a, b, result);
                } else if (method.startsWith("2")) {
                    double c = ExpressionEvaluator.evaluate(inputC.getText());
                    result = KeywayCalculator.fromTopFlat(a, b, c);
                    logEntry = String.format("%s -> A: %.4f, B: %.4f, C: %.4f, Offset: %.4f", methodNumber, a, b, c, result);
                } else {
                    result = KeywayCalculator.fromTopOfDiameter(a, b);
                    logEntry = String.format("%s -> A: %.4f, B: %.4f, Offset: %.4f", methodNumber, a, b, result);
                }

                resultLabel.setText(String.format("%.4f", result));
                calculationLog.add(logEntry);
                logArea.clear();
                calculationLog.forEach(entry -> logArea.appendText(entry + "\n"));
            } catch (Exception ex) {
                ex.printStackTrace();
                resultLabel.setText("Error");
            }
        });

        clearBtn.setOnAction(e -> {
            inputA.clear();
            inputB.clear();
            inputC.clear();
            resultLabel.setText("      ");
        });

        copyBtn.setOnAction(e -> {
            String resultText = resultLabel.getText();
            if (!resultText.equals("      ") && !resultText.equals("Error")) {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(resultText);
                clipboard.setContent(content);
            }
        });

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

            String key = String.valueOf(method.charAt(0));
            imageView.setImage(imageCache.getOrDefault(key, null));
            resultLabel.setText("      ");
        });

        VBox layout = new VBox(15,
            methodSelector,
            inputA,
            inputB,
            inputC,
            buttonRow,
            imageView,
            logArea
        );
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 370, 550);
        scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
        stage.setTitle("Keyway Offset Calculator");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
} 
