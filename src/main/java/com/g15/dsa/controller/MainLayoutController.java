package com.g15.dsa.controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;

public class MainLayoutController {

    @FXML
    private StackPane contentArea;

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnFaultManagement;

    @FXML
    private Button btnDispatch;

//    @FXML
//    private Button btnDefense;

    @FXML
    private Button btnSettings;

    private Button currentActiveButton;

    @FXML
    public void initialize() {
        showDashboard();
    }

    @FXML
    public void showDashboard() {
        loadView("/fxml/views/dashboard.fxml", btnDashboard);
    }

    @FXML
    public void showFaultManagement() {
        loadView("/fxml/views/fault-management.fxml", btnFaultManagement);
    }

    @FXML
    public void showDispatch() {
        loadView("/fxml/views/dispatch.fxml", btnDispatch);
    }

//    @FXML
//    public void showDefense() {
//        loadView("/fxml/views/defense.fxml", btnDefense);
//    }

    @FXML
    public void showSettings() {
        loadView("/fxml/views/settings.fxml", btnSettings);
    }

    private void loadView(String fxmlPath, Button targetButton) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();

            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);

            // Smooth fade-in transition
            FadeTransition ft = new FadeTransition(Duration.millis(250), view);
            ft.setFromValue(0.4);
            ft.setToValue(1.0);
            ft.play();

            updateActiveButton(targetButton);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateActiveButton(Button button) {
        if (currentActiveButton != null) {
            currentActiveButton.getStyleClass().remove("nav-button-active");
        }
        if (button != null) {
            if (!button.getStyleClass().contains("nav-button-active")) {
                button.getStyleClass().add("nav-button-active");
            }
            currentActiveButton = button;
        }
    }
}
