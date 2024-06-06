package com.example.mutualrentacar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ReqRecDetailsController {

    @FXML
    private Label lblRecDate, lblRentPeriod, lblUserRec, lblOwnerRec;

    @FXML
    private TextArea txtDescription;

    @FXML
    public Label accept, cancel, lblNoSwapCar;

    @FXML
    private ImageView imgView;

    private String reqId;

    @FXML
    public Button btnSwapCar;


    //TODO Test this snippet of code
    @FXML
    private void showSwapCar() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("CarDetails.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 984, 468);
        Stage stage = new Stage();
        stage.setTitle("Детайли за предложения автомобил!");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));

        String clientCarId = EstablishDBConnection.findViaId("Requests", reqId, "Client_Car_Id");

        ByteArrayInputStream bis = new ByteArrayInputStream(
                EstablishDBConnection.getImages(
                        "Cars",
                        clientCarId,
                        "imageData").getFirst().getData());

        CarDetailsController carDetailsController = fxmlLoader.getController();
        ArrayList<String> arr = EstablishDBConnection.findCarViaId("Cars", clientCarId);

        carDetailsController.setDetails(
                new Image(bis),
                arr.get(0),
                arr.get(1),
                arr.get(2),
                Integer.parseInt(arr.get(3)),
                arr.get(4)
                );
        stage.show();
    }

    @FXML
    private void acceptOffer() throws Exception {
        EstablishDBConnection.addApprovedReq(reqId);

        ArrayList<String> arr = EstablishDBConnection.findId("Host_Username","Requests", "_id");
        arr.removeAll(EstablishDBConnection.findIdArr("ApprovedRequests", "ReqId"));

        if(EstablishDBConnection.getNumOfRecords("Requests") == 0 || arr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Нямате неразгледани заявки към този момент!", "Грешка!", JOptionPane.ERROR_MESSAGE);

            FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("SearchPage.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 948, 954);
            Stage stage = new Stage();
            stage.setTitle("Търси автомобили!");
            stage.setScene(scene);
            stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));

            SearchPageController searchPageController = fxmlLoader.getController();
            searchPageController.addDefaultContent();

            stage.show();
            ((Stage) txtDescription.getScene().getWindow()).close();

        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("RequestReceived.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 942, 682);
            Stage stage = new Stage();
            stage.setTitle("Получени заявки!");
            stage.setScene(scene);
            stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));

            RequestReceivedController req = fxmlLoader.getController();
            req.showRequests(arr);
            stage.show();
            ((Stage) txtDescription.getScene().getWindow()).close();
        }
    }

    public void setData(Image img, String recId, String recDate, String rentStartDate, String rentEndDate, String description, String userRec, String ownerRec){
        this.imgView.setImage(img);
        this.reqId = recId;
        lblRecDate.setText("Офертата е получена на: " + recDate);
        lblRentPeriod.setText("Период на наема: " + rentStartDate + " - " + rentEndDate);
        txtDescription.setText(description);
        lblUserRec.setText("Ще получа: " + userRec + " лв");
        lblOwnerRec.setText("Ще платя: " + ownerRec + " лв");
    }


    @FXML
    private void goToMyAccount() throws IOException {
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

        ((Stage) txtDescription.getScene().getWindow()).close();
    }

    @FXML
    private void Home() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load(), 1012, 664));
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));
        stage.show();
        stage.setTitle("Начало!");
        ((Stage) txtDescription.getScene().getWindow()).close();
    }

    @FXML
    private void goToSearchPage() throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("SearchPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 948, 954);
        Stage stage = new Stage();
        stage.setTitle("Намери подходящия автомобил!");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));
        SearchPageController searchPageController = fxmlLoader.getController();
        searchPageController.addDefaultContent();
        stage.show();

        ((Stage) txtDescription.getScene().getWindow()).close();
    }

    @FXML
    private void Exit() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 584, 702);
        Stage stage = new Stage();
        stage.setTitle("Вход!");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));
        stage.show();

        ((Stage) txtDescription.getScene().getWindow()).close();
    }

    @FXML
    private void cancel() throws Exception {

        //Delete request
        EstablishDBConnection.delete("Requests", "_id", reqId);

        FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("SearchPage.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 948, 954);
        Stage stage = new Stage();
        stage.setTitle("Търси автомобили!");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));

        SearchPageController searchPageController = fxmlLoader.getController();
        searchPageController.addDefaultContent();

        stage.show();


        ((Stage) txtDescription.getScene().getWindow()).close();
    }



}
