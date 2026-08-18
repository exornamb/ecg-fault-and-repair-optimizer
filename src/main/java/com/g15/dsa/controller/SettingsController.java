package com.g15.dsa.controller;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class SettingsController {

    // =========================
    // SYSTEM CONFIGURATION
    // =========================

    @FXML
    private TextField systemNameField;

    @FXML
    private ComboBox<String> regionBox;

    @FXML
    private ComboBox<String> responseTargetBox;


    // =========================
    // NOTIFICATIONS
    // =========================

    @FXML
    private CheckBox highPriorityAlerts;

    @FXML
    private CheckBox dispatchNotifications;

    @FXML
    private CheckBox resolutionNotifications;


    // =========================
    // DEFAULT VALUES
    // =========================

    private static final String DEFAULT_SYSTEM_NAME =
            "ECG Smart Dispatch System";

    private static final String DEFAULT_REGION =
            "Greater Accra";

    private static final String DEFAULT_RESPONSE_TARGET =
            "20 minutes";


    // =========================
    // INITIALIZE
    // =========================

    @FXML
    public void initialize() {

        setupRegions();

        setupResponseTargets();

        loadDefaultSettings();

        playEntranceAnimations();
    }


    // =========================
    // REGION OPTIONS
    // =========================

    private void setupRegions() {

        regionBox.getItems().addAll(
                "Greater Accra",
                "Ashanti",
                "Eastern",
                "Central",
                "Western",
                "Volta"
        );
    }


    // =========================
    // RESPONSE TARGET OPTIONS
    // =========================

    private void setupResponseTargets() {

        responseTargetBox.getItems().addAll(
                "10 minutes",
                "15 minutes",
                "20 minutes",
                "30 minutes",
                "45 minutes",
                "60 minutes"
        );
    }


    // =========================
    // DEFAULT SETTINGS
    // =========================

    private void loadDefaultSettings() {

        systemNameField.setText(
                DEFAULT_SYSTEM_NAME
        );

        regionBox.setValue(
                DEFAULT_REGION
        );

        responseTargetBox.setValue(
                DEFAULT_RESPONSE_TARGET
        );

        highPriorityAlerts.setSelected(true);

        dispatchNotifications.setSelected(true);

        resolutionNotifications.setSelected(true);
    }


    // =========================
    // PAGE ENTRANCE ANIMATION
    // =========================

    private void playEntranceAnimations() {

        animateNode(
                systemNameField,
                80
        );

        animateNode(
                regionBox,
                160
        );

        animateNode(
                responseTargetBox,
                240
        );

        animateNode(
                highPriorityAlerts,
                330
        );

        animateNode(
                dispatchNotifications,
                400
        );

        animateNode(
                resolutionNotifications,
                470
        );
    }


    // =========================
    // FADE + SLIDE ANIMATION
    // =========================

    private void animateNode(
            Node node,
            int delay) {

        if (node == null) {
            return;
        }

        node.setOpacity(0);

        node.setTranslateY(15);

        javafx.animation.PauseTransition pause =
                new javafx.animation.PauseTransition(
                        Duration.millis(delay)
                );

        pause.setOnFinished(event -> {

            FadeTransition fade =
                    new FadeTransition(
                            Duration.millis(400),
                            node
                    );

            fade.setFromValue(0);

            fade.setToValue(1);

            TranslateTransition slide =
                    new TranslateTransition(
                            Duration.millis(400),
                            node
                    );

            slide.setFromY(15);

            slide.setToY(0);

            ParallelTransition animation =
                    new ParallelTransition(
                            fade,
                            slide
                    );

            animation.play();
        });

        pause.play();
    }


    // =========================
    // SAVE SETTINGS
    // =========================

    @FXML
    private void saveSettings(ActionEvent event) {

        String systemName =
                systemNameField.getText();

        String region =
                regionBox.getValue();

        String responseTarget =
                responseTargetBox.getValue();


        // =========================
        // VALIDATION
        // =========================

        if (systemName == null
                || systemName.trim().isEmpty()) {

            showWarning(
                    "Invalid System Name",
                    "Please enter a system name."
            );

            return;
        }


        if (region == null) {

            showWarning(
                    "Region Required",
                    "Please select an operational region."
            );

            return;
        }


        if (responseTarget == null) {

            showWarning(
                    "Response Target Required",
                    "Please select a response target."
            );

            return;
        }


        // =========================
        // SETTINGS SAVED
        // =========================

        showInformation(
                "Settings Saved",
                "System settings have been saved successfully."
        );

        playSuccessAnimation();
    }


    // =========================
    // RESET SETTINGS
    // =========================

    @FXML
    private void resetSettings(ActionEvent event) {

        loadDefaultSettings();

        showInformation(
                "Settings Reset",
                "Settings have been restored to their default values."
        );

        playSuccessAnimation();
    }


    // =========================
    // SUCCESS EFFECT
    // =========================

    private void playSuccessAnimation() {

        Node[] nodes = {

                systemNameField,

                regionBox,

                responseTargetBox,

                highPriorityAlerts,

                dispatchNotifications,

                resolutionNotifications
        };


        for (Node node : nodes) {

            if (node == null) {
                continue;
            }


            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(180),
                            node
                    );

            scale.setFromX(1.0);

            scale.setFromY(1.0);

            scale.setToX(1.02);

            scale.setToY(1.02);


            ScaleTransition back =
                    new ScaleTransition(
                            Duration.millis(180),
                            node
                    );

            back.setFromX(1.02);

            back.setFromY(1.02);

            back.setToX(1.0);

            back.setToY(1.0);


            scale.setOnFinished(
                    event -> back.play()
            );

            scale.play();
        }
    }


    // =========================
    // WARNING
    // =========================

    private void showWarning(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.WARNING
                );

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }


    // =========================
    // INFORMATION
    // =========================

    private void showInformation(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }
}
