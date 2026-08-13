package controller;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginController {

    @FXML
    private Button loginButton;


    @FXML
    public void handleLogin(ActionEvent event) {

        // Prevent double-click
        loginButton.setDisable(true);

        loginButton.setText("Signing in...");


        // =====================================================
        // FADE LOGIN SCREEN
        // =====================================================

        Node loginRoot =
                loginButton.getScene().getRoot();

        FadeTransition fadeOut =
                new FadeTransition(
                        Duration.millis(250),
                        loginRoot
                );

        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.65);


        fadeOut.setOnFinished(e -> {

            try {

                // =================================================
                // LOAD MAIN APPLICATION
                // =================================================

                FXMLLoader loader =
                        new FXMLLoader(
                                getClass().getResource(
                                        "/fxml/main-layout.fxml"
                                )
                        );

                Parent root =
                        loader.load();


                // =================================================
                // PREPARE DASHBOARD
                // =================================================

                root.setOpacity(0);


                Scene scene =
                        new Scene(
                                root,
                                1200,
                                750
                        );


                Stage stage =
                        (Stage) loginButton
                                .getScene()
                                .getWindow();


                stage.setScene(scene);

                stage.show();


                // =================================================
                // FADE DASHBOARD IN
                // =================================================

                FadeTransition fadeIn =
                        new FadeTransition(
                                Duration.millis(500),
                                root
                        );

                fadeIn.setFromValue(0);
                fadeIn.setToValue(1);


                fadeIn.play();


            } catch (Exception ex) {

                ex.printStackTrace();

                // Restore login if something fails

                loginButton.setDisable(false);

                loginButton.setText("Login");

                loginRoot.setOpacity(1);
            }
        });


        fadeOut.play();
    }
}