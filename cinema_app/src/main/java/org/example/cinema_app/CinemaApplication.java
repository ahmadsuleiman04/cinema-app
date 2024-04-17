package org.example.cinema_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class CinemaApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CinemaApplication.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 300, 250);
        stage.setTitle("Vono Cinema");
        stage.setScene(scene);
        stage.setMaxHeight(250);
        stage.setMaxWidth(300);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}