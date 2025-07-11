package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

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
    Label offsetLabel = new Label("Offset:");
    Label resultLabel = new Label("----");

    HBox offsetRow = new HBox(5, offsetLabel, resultLabel);
    offsetRow.setAlignment(Pos.CENTER_LEFT);

    // Calculate button
    Button calculateBtn = new Button("Calculate");

    // Clear button
    Button clearBtn = new Button("Clear");

    // Copy button
    Button copyBtn = new Button("Copy");

    // Horizontal row for buttons and offset output
    HBox buttonRow = new HBox(20,
        new HBox(10, calculateBtn, clearBtn, copyBtn),  // 👈 Add copyBtn here
        offsetRow
    );
    buttonRow.setAlignment(Pos.CENTER);

    // Create image
    ImageView imageView = new ImageView();
    imageView.setFitWidth(300);          // Adjust as needed
    imageView.setPreserveRatio(true);    // Keep aspect ratio
    imageView.setSmooth(false);

    // Show the default image when the app loads
    String initialImage = "KeywayBottom.PNG";
    Image image = new Image(getClass().getResource("/images/" + initialImage).toExternalForm());
    imageView.setImage(image);

    // Calculate button press
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

        resultLabel.setText(String.format("%.4f", result));
        } catch (Exception ex) {
            ex.printStackTrace(); // helpful while debugging
            resultLabel.setText("Error");
        }
    });

    // Clear button press
    clearBtn.setOnAction(e -> {
        inputA.clear();
        inputB.clear();
        inputC.clear();
        resultLabel.setText("----");
    });

    // Copy button press
    copyBtn.setOnAction(e -> {
    String resultText = resultLabel.getText();
        if (!resultText.equals("----") && !resultText.equals("Error")) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(resultText);
            clipboard.setContent(content);
        }
    });

    // When dropdown changes, update input labels/visibility
    methodSelector.setOnAction(e -> {
        String method = methodSelector.getValue();

        // Set input prompts and input visibility...
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

        // Update the image based on the selected method
        String imageFile = switch (method.charAt(0)) {
            case '1' -> "KeywayBottom.PNG";
            case '2' -> "KeywayFlat.PNG";
            case '3' -> "KeywayTop.PNG";
            default -> null;
        };

        if (imageFile != null) {
            imageView.setImage(new Image(getClass().getResource("/images/" + imageFile).toExternalForm()));
        }

        // Keep or clear other UI as needed
        resultLabel.setText("----");
    });

    VBox layout = new VBox(15,
        methodSelector,
        inputA,
        inputB,
        inputC,
        buttonRow,
        imageView
    );
    layout.setAlignment(Pos.TOP_CENTER);
    layout.setPadding(new Insets(20));

    Scene scene = new Scene(layout, 320, 475);
    stage.setTitle("Keyway Offset Calculator");
    stage.setScene(scene);
    stage.show();
}

    public static void main(String[] args) {
        launch();
    }

}
