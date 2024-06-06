package com.example.mutualrentacar;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class HomePageController {

    @FXML
    private Label lblContinue;

    @FXML
    public void goToSearchPage() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("SearchPage.fxml"));

//        FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("RequestReceived.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 948, 824);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Търси автомобили!");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));

//        RequestReceivedController req = fxmlLoader.getController();
//        req.showRequests();

        SearchPageController searchPageController = fxmlLoader.getController();
        searchPageController.addDefaultContent();

        stage.show();

        ((Stage) lblContinue.getScene().getWindow()).close();
    }
}