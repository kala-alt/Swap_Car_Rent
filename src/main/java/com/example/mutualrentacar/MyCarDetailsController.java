package com.example.mutualrentacar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;


public class MyCarDetailsController {

    @FXML
    public TextArea txtDescription;

    @FXML
    public Label txtEngine, txtFuel, txtYear;

    @FXML
    public ImageView carImage;


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

        ((Stage) txtYear.getScene().getWindow()).close();
    }

    @FXML
    private void Home() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load(), 1012, 664));
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));
        stage.show();
        stage.setTitle("Начало!");
        ((Stage) txtYear.getScene().getWindow()).close();
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

        ((Stage) txtYear.getScene().getWindow()).close();
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

        ((Stage) txtYear.getScene().getWindow()).close();
    }

    @FXML
    private void deleteCar() throws IOException {
        System.out.println("Car id to be deleted: " + CreateRequestController.HostCarId);

        //TODO Delete Approved requests (it contains all needed deletes) with this car
        EstablishDBConnection.deleteAppReq(CreateRequestController.HostCarId);
//        EstablishDBConnection.delete("Cars", "_id", CreateRequestController.HostCarId);



//        EstablishDBConnection.delete("Requests", "Host_Car_Id", CreateRequestController.HostCarId);
//        EstablishDBConnection.deleteLikedCar(CreateRequestController.HostCarId);



        JOptionPane.showMessageDialog(null, "Автомобила беше успешно изтрит!", "Изтрит запис!", JOptionPane.INFORMATION_MESSAGE);

        FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("MyAccount.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1205, 564);
        Stage stage = new Stage();
        stage.setTitle("Моят акаунт!");
        stage.setScene(scene);
        stage.getIcons()
                .add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));

        MyAccountController myAccountController = fxmlLoader.getController();
        myAccountController.setData();
        stage.setResizable(false);
        stage.show();
        ((Stage) txtYear.getScene().getWindow()).close();
    }

}
