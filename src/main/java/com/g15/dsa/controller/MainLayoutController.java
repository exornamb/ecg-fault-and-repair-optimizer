package com.g15.dsa.controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class MainLayoutController {

    @FXML
    private StackPane contentPane;

    @FXML
    private Button dashboardButton;

    @FXML
    private Button faultButton;

    @FXML
    private Button dispatchButton;

    @FXML
    private Button analyticsButton;

    @FXML
    private Button databaseButton;

    @FXML
    private Button settingsButton;

    private Button currentActiveButton;

    @FXML
    public void initialize() {
        openDashboard();
    }

    @FXML
    public void openDashboard() {
        loadView("/fxml/views/dashboard.fxml", dashboardButton);
    }

    @FXML
    public void openFaultManagement() {
        loadView("/fxml/views/fault-management.fxml", faultButton);
    }

    @FXML
    public void openDispatch() {
        loadView("/fxml/views/dispatch.fxml", dispatchButton);
    }

    @FXML
    public void openAnalytics() {
        loadView("/fxml/views/analytics.fxml", analyticsButton);
    }

    @FXML
    public void openDatabase() {
        loadView("/fxml/views/database.fxml", databaseButton);
    }

    @FXML
    public void openSettings() {
        loadView("/fxml/views/settings.fxml", settingsButton);
    }

    @FXML
    public void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();

            if (contentPane != null && contentPane.getScene() != null) {
                Stage stage = (Stage) contentPane.getScene().getWindow();
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
                stage.setScene(scene);
                stage.setTitle("ECG Smart Dispatch — Sign In");
                stage.centerOnScreen();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadView(String fxmlPath, Button targetButton) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();

            if (contentPane != null) {
                contentPane.getChildren().clear();
                contentPane.getChildren().add(view);

                // Smooth fade-in transition
                FadeTransition ft = new FadeTransition(Duration.millis(250), view);
                ft.setFromValue(0.4);
                ft.setToValue(1.0);
                ft.play();
            }

            updateActiveButton(targetButton);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateActiveButton(Button button) {
        if (currentActiveButton != null) {
            currentActiveButton.getStyleClass().remove("menu-button-active");
            currentActiveButton.getStyleClass().remove("nav-button-active");
        }
        if (button != null) {
            if (!button.getStyleClass().contains("menu-button-active")) {
                button.getStyleClass().add("menu-button-active");
            }
            currentActiveButton = button;
        }
    }
}
