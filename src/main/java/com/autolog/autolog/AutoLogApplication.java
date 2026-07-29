package com.autolog.autolog;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AutoLogApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                AutoLogApplication.class.getResource("layouts/mobileProfile.fxml")
        );

        Scene scene = new Scene(loader.load());
        stage.setTitle("AutoLog");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}