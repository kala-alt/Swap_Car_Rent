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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class PaneReceivedController {

    @FXML
    public Label lblTitle , lblDescription;

    @FXML
    public ImageView imgView;

    private String requestId;

    public String page = "";
    public Stage st;
    public void setRequestId(String requestId){
        this.requestId = requestId;
    }

    public String getRequestId(){
        return requestId;
    }

    @FXML
    private void onClick() throws IOException {

        String carId = EstablishDBConnection.findViaId("Requests", requestId, "Client_Car_Id");

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("ReqRecDetails.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1278, 834);
        Stage stage = new Stage();
        stage.setTitle("Подробности за офертата!");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));
        ReqRecDetailsController reqRecDetailsController = fxmlLoader.getController();


        if (carId.equals("No Swap!")){
            reqRecDetailsController.lblNoSwapCar.setVisible(true);
            reqRecDetailsController.btnSwapCar.setVisible(false);
    }else{
        reqRecDetailsController.lblNoSwapCar.setVisible(false);
        reqRecDetailsController.btnSwapCar.setVisible(true);
    }

        reqRecDetailsController.setData(
                imgView.getImage(),
                requestId,
                EstablishDBConnection.findViaId("Requests", requestId, "Date"),
                formatter.format(EstablishDBConnection.findDateViaId("Requests", requestId, "Start_Date")),
                formatter.format(EstablishDBConnection.findDateViaId("Requests", requestId, "End_Date")),
                EstablishDBConnection.findViaId("Requests", requestId, "Description"),
                EstablishDBConnection.findViaId("Requests", requestId, "Host_Receive"),
                EstablishDBConnection.findViaId("Requests", requestId, "Client_Receive"));
        stage.show();

        if(page.equals("одобрени")) {
            reqRecDetailsController.accept.setVisible(false);
            reqRecDetailsController.cancel.setVisible(false);
        }

        ((Stage) lblTitle.getScene().getWindow()).close();
        st.close();
    }

}
