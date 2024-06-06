package com.example.mutualrentacar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class RequestReceivedController {

    @FXML
    private ScrollPane scrollPane;

    public void showRequests (ArrayList<String> arrId) throws IOException {
            AnchorPane contentPane = new AnchorPane();
            Pane[] arr = new Pane[arrId.size()];
            for (int i = 0; i < arrId.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("paneReceived.fxml"));
                Pane pane = fxmlLoader.load();

                if (i != 0)
                    pane.setLayoutY(arr[i - 1].getLayoutY() + arr[i - 1].getPrefHeight());
                PaneReceivedController paneReceivedControllerController = fxmlLoader.getController();
                paneReceivedControllerController.setRequestId(arrId.get(i));
                paneReceivedControllerController.st = ((Stage) scrollPane.getScene().getWindow());
                paneReceivedControllerController.lblTitle
                        .setText(EstablishDBConnection.findViaId("Requests", arrId.get(i), "Title"));
                paneReceivedControllerController
                        .lblDescription.setText(EstablishDBConnection.findViaId("Requests", arrId.get(i), "Description"));
                    ByteArrayInputStream bis = new ByteArrayInputStream(
                            EstablishDBConnection.getImages(
                                    "Cars",
                                    EstablishDBConnection.findViaId("Requests", arrId.get(i), "Host_Car_Id"),
                                    "imageData").get(0).getData()
                    );
                Image image = new Image(bis);
                paneReceivedControllerController.imgView.setImage(image);
                arr[i] = pane;
            }
            contentPane.getChildren().addAll(arr);
            scrollPane.setContent(contentPane);
        }


    @FXML
    private void goBack() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("MyAccount.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1205, 564);
        Stage stage = new Stage();
        stage.setTitle("Моят акаунт!");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));

        MyAccountController myAccountController = fxmlLoader.getController();
        myAccountController.setData();
        stage.setResizable(false);
        stage.show();
        ((Stage) scrollPane.getScene().getWindow()).close();
    }



    }
