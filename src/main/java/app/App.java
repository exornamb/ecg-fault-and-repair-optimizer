package app;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // ==========================================
        // APPLICATION WINDOW
        // ==========================================

        stage.setTitle("ECG Smart Dispatch System");

        Image icon = new Image(
                getClass().getResourceAsStream(
                        "/images/ecg-logo.jpg"
                )
        );

        stage.getIcons().add(icon);

        // Keep the window at one consistent size
        stage.setWidth(1200);
        stage.setHeight(750);

        stage.setMinWidth(1000);
        stage.setMinHeight(650);


        // ==========================================
        // SPLASH SCREEN
        // ==========================================

        FXMLLoader splashLoader =
                new FXMLLoader(
                        getClass().getResource(
                                "/fxml/views/Splash.fxml"
                        )
                );

        Scene splashScene =
                new Scene(
                        splashLoader.load(),
                        1200,
                        750
                );

        stage.setScene(splashScene);

        stage.centerOnScreen();

        stage.show();


        // ==========================================
        // SPLASH DELAY
        // ==========================================

        PauseTransition pause =
                new PauseTransition(
                        Duration.seconds(2.5)
                );


        pause.setOnFinished(event -> {

            try {

                FXMLLoader loginLoader =
                        new FXMLLoader(
                                getClass().getResource(
                                        "/fxml/login.fxml"
                                )
                        );

                Scene loginScene =
                        new Scene(
                                loginLoader.load(),
                                1200,
                                750
                        );


                // ==================================
                // LOGIN FADE-IN
                // ==================================

                loginScene.getRoot().setOpacity(0);


                stage.setScene(loginScene);

                stage.centerOnScreen();


                FadeTransition fade =
                        new FadeTransition(
                                Duration.millis(450),
                                loginScene.getRoot()
                        );

                fade.setFromValue(0);

                fade.setToValue(1);

                fade.play();


            } catch (Exception e) {

                e.printStackTrace();

            }

        });


        pause.play();
    }


    public static void main(String[] args) {

        launch(args);

    }
}