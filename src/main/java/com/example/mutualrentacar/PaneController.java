package com.example.mutualrentacar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;



public class PaneController {

//    private static String CarDescription;

    public static boolean flagCarDetails = false;

    @FXML
    public Label lblTitle, lblCarDescription;


    @FXML
    public ImageView imgView;

    @FXML
    private void onClick() throws IOException {

        System.out.println("Засечено кликлане!");

//        SetDatesController.carId = lblCarDescription.getId();
//        SetDatesController.img = imgView.getImage();

        MyAccountController.flag = false;
        CreateRequestController.HostCarId = lblCarDescription.getId();
//        System.out.println(CreateRequestController.HostCarId);
        ArrayList<String> arrResult = EstablishDBConnection.findViaIdArr(lblCarDescription.getId(), true);
        System.out.println("Count: " + arrResult.size());

        if (flagCarDetails==false) {

            FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("CreateRequest.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 984, 810);
            Stage stage = new Stage();
            stage.setTitle("Създай запитване!");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));
            stage.show();
            ((Stage) lblTitle.getScene().getWindow()).close();

//            System.out.println("Host car id: " + CreateRequestController.HostCarId);

            ByteArrayInputStream bis = new ByteArrayInputStream(
                    EstablishDBConnection.getImages("Cars", CreateRequestController.HostCarId, "imageData").getFirst().getData());

            CreateRequestController createRequestController = fxmlLoader.getController();
//            System.out.println(EstablishDBConnection.checkLikedCar(CreateRequestController.HostCarId));
            createRequestController.setLiked(EstablishDBConnection.checkLikedCar(CreateRequestController.HostCarId));

            createRequestController.CreateRequestController(
                    new Image(bis),
                    arrResult.get(4) + " " +
                         arrResult.get(5),
                    arrResult.getFirst(),
                    arrResult.get(1),
                    arrResult.get(2)
            );



        }else {

            FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("MyCarDetails.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1087, 637);
            Stage stage = new Stage();
            stage.setTitle("Преглед на моят автомобил!");
            stage.setScene(scene);
            stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));

            stage.show();
            ((Stage) lblTitle.getScene().getWindow()).close();

            ByteArrayInputStream bis = new ByteArrayInputStream(
                    EstablishDBConnection.getImages("Cars", CreateRequestController.HostCarId, "imageData").getFirst().getData());

            MyCarDetailsController myCarDetailsController = fxmlLoader.getController();
//            System.out.println("ImageData: " + EstablishDBConnection.getImages("Cars", CreateRequestController.HostCarId, "imageData").getFirst());



            myCarDetailsController.carImage.setImage(new Image(bis));

            myCarDetailsController.txtEngine.setText(arrResult.getFirst());
            myCarDetailsController.txtYear.setText(arrResult.get(1));
            myCarDetailsController.txtFuel.setText(arrResult.get(2));
            myCarDetailsController.txtDescription.setText(arrResult.get(3));


        }
    }
}