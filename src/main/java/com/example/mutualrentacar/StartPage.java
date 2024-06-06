package com.example.mutualrentacar;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.lang.Exception;


import java.util.Objects;

public class StartPage extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 584, 702);
        stage.setTitle("Вход!");
        stage.setScene(scene);

        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));

        stage.show();

    }



    public static void main(String[] args) {
        launch();
    }
}