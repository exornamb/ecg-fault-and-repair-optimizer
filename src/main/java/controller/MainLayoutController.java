package controller;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class MainLayoutController {

    @FXML
    private javafx.scene.control.Button dashboardButton;

    @FXML
    private javafx.scene.control.Button faultButton;

    @FXML
    private javafx.scene.control.Button dispatchButton;

    @FXML
    private javafx.scene.control.Button analyticsButton;

    @FXML
    private javafx.scene.control.Button databaseButton;

    @FXML
    private javafx.scene.control.Button settingsButton;

    @FXML
    private StackPane contentPane;

    @FXML
    public void initialize() {
        setActiveButton(dashboardButton);

        loadPage("/fxml/views/dashboard.fxml");
    }


    // =========================================================
    // PAGE LOADING + ANIMATION
    // =========================================================

    private void loadPage(String fxml) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(fxml)
                    );

            Node newPage = loader.load();

            // Start slightly lower and invisible
            newPage.setOpacity(0);
            newPage.setTranslateY(12);

            // Remove current page
            contentPane.getChildren().clear();

            // Add new page
            contentPane.getChildren().add(newPage);

            // Fade in
            FadeTransition fade =
                    new FadeTransition(
                            Duration.millis(280),
                            newPage
                    );

            fade.setFromValue(0);
            fade.setToValue(1);

            // Slide upward slightly
            TranslateTransition slide =
                    new TranslateTransition(
                            Duration.millis(320),
                            newPage
                    );

            slide.setFromY(12);
            slide.setToY(0);

            // Run both together
            ParallelTransition transition =
                    new ParallelTransition(
                            fade,
                            slide
                    );

            transition.play();

        } catch (Exception e) {

            System.out.println(
                    "FAILED TO LOAD: " + fxml
            );

            e.printStackTrace();
        }
    }


    // =========================================================
    // NAVIGATION
    // =========================================================

    @FXML
    private void openDashboard() {

        setActiveButton(dashboardButton);

        animateButton(dashboardButton);

        loadPage("/fxml/views/dashboard.fxml");
    }


    @FXML
    private void openFaultManagement() {

        setActiveButton(faultButton);

        animateButton(faultButton);

        loadPage("/fxml/views/fault-management.fxml");
    }

    @FXML
    private void openDispatch() {

        setActiveButton(dispatchButton);

        animateButton(dispatchButton);

        loadPage("/fxml/views/dispatch.fxml");
    }

    @FXML
    private void openAnalytics() {

        setActiveButton(analyticsButton);

        animateButton(analyticsButton);

        loadPage("/fxml/views/analytics.fxml");
    }

    @FXML
    private void openDatabase() {

        setActiveButton(databaseButton);

        animateButton(databaseButton);

        loadPage("/fxml/views/database.fxml");
    }

    @FXML
    private void openSettings() {

        setActiveButton(settingsButton);

        animateButton(settingsButton);

        loadPage("/fxml/views/settings.fxml");
    }


    // =========================================================
    // LOGOUT
    // =========================================================

    @FXML
    private void logout() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/fxml/login.fxml"
                            )
                    );

            Node loginPage =
                    loader.load();

            Scene scene =
                    new Scene(
                            (javafx.scene.Parent) loginPage,
                            1200,
                            750
                    );

            Stage stage =
                    (Stage) contentPane
                            .getScene()
                            .getWindow();

            stage.setScene(scene);

            stage.centerOnScreen();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void animateButton(Node node) {

        if (node == null) {
            return;
        }

        ScaleTransition press =
                new ScaleTransition(
                        Duration.millis(120),
                        node
                );

        press.setFromX(1.0);
        press.setFromY(1.0);

        press.setToX(0.97);
        press.setToY(0.97);

        ScaleTransition release =
                new ScaleTransition(
                        Duration.millis(160),
                        node
                );

        release.setFromX(0.97);
        release.setFromY(0.97);

        release.setToX(1.0);
        release.setToY(1.0);

        press.setOnFinished(
                event -> release.play()
        );

        press.play();
    }

    private void setActiveButton(Button activeButton) {

        Button[] buttons = {
                dashboardButton,
                faultButton,
                dispatchButton,
                analyticsButton,
                databaseButton,
                settingsButton
        };

        for (Button button : buttons) {

            if (button == null) {
                continue;
            }

            button.getStyleClass().remove("active-menu-button");
        }

        if (activeButton != null) {

            if (!activeButton.getStyleClass()
                    .contains("active-menu-button")) {

                activeButton.getStyleClass()
                        .add("active-menu-button");
            }
        }
    }
}