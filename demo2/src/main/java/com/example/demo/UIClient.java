package com.example.demo;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UIClient extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(UIClient.class.getResource("client-ui.fxml"));
        Scene scene = new Scene((Parent) fxmlLoader.load(), 600, 400);
        stage.setTitle("GB Chat!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void onHelloButtonClick(ActionEvent actionEvent) {
    }
}