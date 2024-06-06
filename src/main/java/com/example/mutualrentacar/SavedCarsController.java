package com.example.mutualrentacar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class SavedCarsController {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label lblNoCars;

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

    @FXML
    public void showData(ArrayList<String> arrId) throws IOException {
        AnchorPane contentPane = new AnchorPane();
        ArrayList<Pane> arr = new ArrayList<>();

        ArrayList<String> arrResults;

        if(arrId.isEmpty())
            lblNoCars.setVisible(true);
        else
            lblNoCars.setVisible(false);

        for (int i = 0; i < arrId.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("pane.fxml"));
            Pane pane = fxmlLoader.load();

            if (i != 0)
                pane.setLayoutY(arr.getLast().getLayoutY() + arr.getLast().getPrefHeight());

            PaneController paneController = fxmlLoader.getController();

            arrResults = EstablishDBConnection.findViaIdArr(arrId.get(i), true);

            paneController.lblTitle
                    .setText(arrResults.get(4)
                            + " " + arrResults.get(5));

            paneController
                    .lblCarDescription.setText(arrResults.get(3));
            paneController.lblCarDescription.setId(arrId.get(i));

ByteArrayInputStream bis = new ByteArrayInputStream(EstablishDBConnection.getImages("Cars", arrId.get(i), "imageData").get(0).getData());

            Image image = new Image(bis);
            paneController.imgView.setImage(image);

            arr.add(pane);
        }

        contentPane.getChildren().addAll(arr);
        scrollPane.setContent(contentPane);
    }
    }

