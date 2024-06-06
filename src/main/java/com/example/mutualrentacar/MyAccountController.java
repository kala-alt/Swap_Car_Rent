package com.example.mutualrentacar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MyAccountController {

    @FXML
    private Label lblUser, lblEmail;

    public static boolean flag=false;

    void setClickFlag(){
        flag=true;
        System.out.println(flag);
    }

    void setData(){
        PaneController.flagCarDetails = true;
            lblUser.setText("Потребител: " +EstablishDBConnection.findResult("User", "Username", EstablishDBConnection.loggedUsername, "Username"));
            lblEmail.setText("Имейл: " + EstablishDBConnection.findResult("User", "Username", EstablishDBConnection.loggedUsername, "Email"));
    }

    @FXML
    private void savedCars()throws Exception{
        PaneController.flagCarDetails = false;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SavedCars.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load(), 884, 639));
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));

        stage.setTitle("Запазени автомобили!");

        SavedCarsController savedCarsController = fxmlLoader.getController();
        savedCarsController.showData(EstablishDBConnection.findSavedIdCars());
        setClickFlag();

        stage.show();
        ((Stage) lblUser.getScene().getWindow()).close();
    }

    @FXML
    private void myCars()throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyCars.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load(), 884, 639));
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));

        stage.setTitle("Моите автомобили!");

        MyCarController myCarController = fxmlLoader.getController();
        myCarController.myCars();
        setClickFlag();

        stage.show();
        ((Stage) lblUser.getScene().getWindow()).close();
    }

    @FXML
    private void start() throws Exception {
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
        ((Stage) lblUser.getScene().getWindow()).close();
    }

    @FXML
    private void signout() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 584, 702);
        Stage stage = new Stage();
        stage.setTitle("Вход!");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));
        stage.show();

        ((Stage) lblUser.getScene().getWindow()).close();
    }

    @FXML
    private void myRequests() throws IOException {

        ArrayList<String> arr = EstablishDBConnection.findId("Host_Username","Requests", "_id");
        System.err.println("\n\n\narr num of records: " + arr.size());
        arr.removeAll(EstablishDBConnection.findIdArr("ApprovedRequests", "ReqId"));
        System.err.println("\n\n\narr num of records: " + arr.size());

        if(EstablishDBConnection.getNumOfRecords("Requests") == 0 || arr.isEmpty())
            JOptionPane.showMessageDialog(null, "Нямате заявки към този момент!", "Грешка!", JOptionPane.ERROR_MESSAGE);
        else {
            FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("RequestReceived.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 942, 682);
            Stage stage = new Stage();
            stage.setTitle("Получени заявки!");
            stage.setScene(scene);
            stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));

            RequestReceivedController req = fxmlLoader.getController();
            req.showRequests(arr);

            stage.show();
            ((Stage) lblUser.getScene().getWindow()).close();
        }
    }


    @FXML
    private void addCar() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("AddCar.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 842, 926);
        Stage stage = new Stage();
        stage.setTitle("Добави автомобил!");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));

        AddCarController addCarController = fxmlLoader.getController();
        addCarController.setProdYear();

        stage.show();
        ((Stage) lblUser.getScene().getWindow()).close();
    }



    @FXML
    private void myApprovedRequests() throws IOException {
        if(EstablishDBConnection.getNumOfRecords("ApprovedRequests") == 0)
            JOptionPane.showMessageDialog(null, "Нямате одобрени заявки към този момент!", "Грешка!", JOptionPane.ERROR_MESSAGE);

        else {
            FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("ApprovedRequests.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 891, 686);
            Stage stage = new Stage();
            stage.setTitle("Одобрени заявки!");
            stage.setScene(scene);
            stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));

            ApprovedRequestsController app = fxmlLoader.getController();
            app.showApprovedRequests();
            stage.show();

            ((Stage) lblUser.getScene().getWindow()).close();
        }
    }
}
