package com.example.mutualrentacar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ApprovedRequestsController {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button btnMyReq, btnRecReq;

    @FXML
    private Label lblNoResults;

    @FXML
    private Label lblTitle;

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
        ((Stage) lblTitle.getScene().getWindow()).close();
    }


    public void showApprovedRequests () throws IOException {
        //Ids from the two tables are always different and cannt have a match, we have to use new method

        lblTitle.setText("Преглед на одобрените от мен заявки");
        btnRecReq.setStyle("-fx-background-color:  #24aae3");
        btnMyReq.setStyle("-fx-background-color:   #9a9b9c");

        ArrayList<String> arrId = EstablishDBConnection.findIdArr("ApprovedRequests", "ReqId");

        AnchorPane contentPane = new AnchorPane();

        for(int j=0;j<arrId.size();j++)
            if (EstablishDBConnection.findViaId("Requests", arrId.get(j), "Host_Username")
                    .equals(EstablishDBConnection.loggedUsername)==false)
                arrId.remove(j);


        if(arrId.isEmpty())
            scrollPane.setContent(lblNoResults);
        else {
            Pane[] arr = new Pane[arrId.size()];

            for (int i = 0; i < arrId.size(); i++) {

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("paneReceived.fxml"));
                Pane pane = fxmlLoader.load();

                if (i != 0)
                    pane.setLayoutY(arr[i - 1].getLayoutY() + arr[i - 1].getPrefHeight());

                PaneReceivedController paneReceivedControllerController = fxmlLoader.getController();
                paneReceivedControllerController.setRequestId(arrId.get(i));
                paneReceivedControllerController.page = "одобрени";
                paneReceivedControllerController.st = ((Stage) scrollPane.getScene().getWindow());

                paneReceivedControllerController.lblTitle.setText(EstablishDBConnection.findViaId("Requests", arrId.get(i), "Title"));

                paneReceivedControllerController.lblDescription.setText(EstablishDBConnection.findViaId("Requests", arrId.get(i), "Description"));


                ByteArrayInputStream bis = new ByteArrayInputStream(EstablishDBConnection.getImages("Cars",
                        EstablishDBConnection.findViaId("Requests", arrId.get(i), "Host_Car_Id")
                        , "imageData").getFirst().getData());
                Image image = new Image(bis);
                paneReceivedControllerController.imgView.setImage(image);

                arr[i] = pane;
            }

            contentPane.getChildren().clear();
            contentPane.getChildren().addAll(arr);
            scrollPane.setContent(contentPane);
        }
    }

    @FXML
    public void loadMyRequest() throws IOException {
        //Logged user is client

        lblTitle.setText("Преглед на моите заявки");

        btnMyReq.setStyle("-fx-background-color:  #24aae3");
        btnRecReq.setStyle("-fx-background-color:   #9a9b9c");

        ArrayList<String> arrId = EstablishDBConnection.findIdArr("ApprovedRequests", "ReqId");

        AnchorPane contentPane = new AnchorPane();

        for(int j=0;j<arrId.size();j++)
            if (EstablishDBConnection.findViaId("Requests", arrId.get(j), "Client_Username")
                    .equals(EstablishDBConnection.loggedUsername)==false)
                arrId.remove(j);

        if(arrId.isEmpty())
            scrollPane.setContent(lblNoResults);
        else {
            Pane[] arr = new Pane[arrId.size()];

            for (int i = 0; i < arrId.size(); i++) {

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("paneReceived.fxml"));
                Pane pane = fxmlLoader.load();

                if (i != 0)
                    pane.setLayoutY(arr[i - 1].getLayoutY() + arr[i - 1].getPrefHeight());

                PaneReceivedController paneReceivedControllerController = fxmlLoader.getController();
                paneReceivedControllerController.setRequestId(arrId.get(i));
                paneReceivedControllerController.page = "одобрени";
                paneReceivedControllerController.st = ((Stage) scrollPane.getScene().getWindow());

                paneReceivedControllerController.lblTitle.setText(EstablishDBConnection.findViaId("Requests", arrId.get(i), "Title"));

                paneReceivedControllerController.lblDescription.setText(EstablishDBConnection.findViaId("Requests", arrId.get(i), "Description"));


                ByteArrayInputStream bis = new ByteArrayInputStream(EstablishDBConnection.getImages("Cars",
                        EstablishDBConnection.findViaId("Requests", arrId.get(i), "Host_Car_Id")
                        , "imageData").getFirst().getData());
                Image image = new Image(bis);
                paneReceivedControllerController.imgView.setImage(image);

                arr[i] = pane;
            }

            contentPane.getChildren().clear();
            contentPane.getChildren().addAll(arr);
            scrollPane.setContent(contentPane);
        }
    }
}
