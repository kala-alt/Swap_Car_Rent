package com.example.mutualrentacar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class SetDatesController {
    @FXML
    private Button btnContinue, goBack;

    @FXML
    private DatePicker dtStart, dtEnd;

    public static String carId;
    public static Image img;

    @FXML
    private void submit() throws IOException {
        ArrayList<String> hostCarId = EstablishDBConnection.getItem("Requests", "Host_Car_Id");
        if(hostCarId.contains(carId)) {
            ArrayList<String> idRequests = EstablishDBConnection.getItem("Requests", "_id");
            for (int j = 0; j < idRequests.size(); j++) {
                if (hostCarId.get(j).equals(carId) == false)
                    continue;

                Date reqStart = EstablishDBConnection.findDateViaId("Requests", idRequests.get(j), "Start_Date");
                Date reqEnd = EstablishDBConnection.findDateViaId("Requests", idRequests.get(j), "End_Date");

                Date start = java.sql.Date.valueOf(dtStart.getValue());
                Date end = java.sql.Date.valueOf(dtEnd.getValue());

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String formattedReqStart = dateFormat.format(reqStart);
                String formattedReqEnd = dateFormat.format(reqEnd);
                System.out.println(start + " - " + end + " - " + formattedReqStart + " - " + formattedReqEnd);

                if (formattedReqStart.equals(String.valueOf(dtEnd.getValue())) ||
                        formattedReqEnd.equals(String.valueOf(dtStart.getValue())) ||
                        (start.before(reqEnd) && end.after(reqStart)) ||
                        (start.before(reqEnd) && end.after(reqEnd)) ||
                        (start.before(reqStart) && end.after(reqStart)) ||
                        (start.after(reqStart) && end.before(reqEnd))) {
                    JOptionPane.showMessageDialog(null, "Избраните дати са заети!", "Грешка! Заети дати!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }



        if (dtStart.getValue() == null || dtEnd.getValue() == null || dtStart.getValue().isAfter(dtEnd.getValue())
                || dtStart.getValue().isBefore(LocalDate.now()))
            JOptionPane.showMessageDialog(null, "Невалидни данни!\nМоля опитайте отново!",
                    "Грешка!", JOptionPane.ERROR_MESSAGE);
        else {
            CreateRequestController.HostCarId = carId;
            FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("CreateRequest.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 984, 810);
            Stage stage = new Stage();
            stage.setTitle("Създай запитване!");
            stage.setScene(scene);
            stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));

            CreateRequestController createRequestController = fxmlLoader.getController();

            //Search if car is liked
            createRequestController.setDates(dtStart.getValue(), dtEnd.getValue());
            createRequestController.setLiked(EstablishDBConnection.checkLikedCar(carId));

            createRequestController.CreateRequestController(
                    img,
                    EstablishDBConnection.findViaId("Cars",
                            carId, "Brand") + " " +
                            EstablishDBConnection.findViaId("Cars",
                                    carId, "Model"),
                    EstablishDBConnection.findViaId("Cars",
                            carId, "Engine"),
                    EstablishDBConnection.findViaId("Cars",
                            carId, "Year"),
                    EstablishDBConnection.findViaId(
                            "Cars",
                            carId,
                            "Fuels")
            );
            stage.show();
            ((Stage) dtStart.getScene().getWindow()).close();
        }
    }

    @FXML
    private void goBack() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("SearchPage.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 948, 954);
        Stage stage = new Stage();
        stage.setTitle("Търси автомобили!");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));

        SearchPageController searchPageController = fxmlLoader.getController();
        searchPageController.addDefaultContent();

        stage.show();

        ((Stage) dtStart.getScene().getWindow()).close();
    }
}
