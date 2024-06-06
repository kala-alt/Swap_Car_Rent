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

public class MyCarController {

    @FXML
    private ScrollPane scroll;

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
        ((Stage) scroll.getScene().getWindow()).close();
    }

    @FXML
    public void myCars() throws IOException {
        AnchorPane contentPane = new AnchorPane();

        ArrayList<String> ids;
        ids = EstablishDBConnection.findId("OwnerUsername", "Cars", "_id");

        ArrayList<Pane> arr = new ArrayList<>();
        int size = ids.size();
        System.out.println("Cars: "  + size);

        if(size==0)
            lblNoCars.setVisible(true);
        else
            lblNoCars.setVisible(false);

        ArrayList<String> arrResults;

        for (int i = 0; i < ids.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(MyCarController.class.getResource("pane.fxml"));
            Pane pane = fxmlLoader.load();

            if (i != 0)
                pane.setLayoutY(arr.getLast().getLayoutY() + arr.getLast().getPrefHeight());

            PaneController paneController = fxmlLoader.getController();

            arrResults = EstablishDBConnection.findViaIdArr(ids.get(i), false);

            paneController.lblTitle
                    .setText(arrResults.get(1)
                            + " " + arrResults.get(2));

            paneController
                    .lblCarDescription.setText(arrResults.get(0));
            paneController.lblCarDescription.setId(ids.get(i));

            ByteArrayInputStream bis = new ByteArrayInputStream(EstablishDBConnection.getImages("Cars", ids.get(i), "imageData")
                    .getFirst().getData());

            Image image = new Image(bis);
            paneController.imgView.setImage(image);

            arr.add(pane);
        }
        contentPane.getChildren().addAll(arr);
        scroll.setContent(contentPane);
    }
}
