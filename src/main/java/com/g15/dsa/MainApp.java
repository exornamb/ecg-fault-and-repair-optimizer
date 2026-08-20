package com.g15.dsa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main-layout.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            // Size the window to 85% of the screen instead of a fixed 1280x800,
            // so it looks right on both small laptops and large monitors.
            javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
            double width = screenBounds.getWidth() * 0.85;
            double height = screenBounds.getHeight() * 0.85;

            primaryStage.setTitle("ECG Dumsor Response & Repair Optimizer v2.0 - Group 15");
            primaryStage.setScene(scene);
            primaryStage.setWidth(width);
            primaryStage.setHeight(height);
            primaryStage.setMinWidth(1000);
            primaryStage.setMinHeight(650);
            primaryStage.centerOnScreen();

            // Make sure the whole app actually exits when the window is closed,
            // instead of the process hanging around in the background.
            primaryStage.setOnCloseRequest(event -> {
                javafx.application.Platform.exit();
                System.exit(0);
            });

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
