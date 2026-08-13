package controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SplashController {

    @FXML
    private ImageView logoImage;

    @FXML
    public void initialize() {

        logoImage.setImage(

                new Image(
                        getClass().getResourceAsStream(
                                "/images/ecg-logo.jpg"
                        )
                )
        );
    }
}