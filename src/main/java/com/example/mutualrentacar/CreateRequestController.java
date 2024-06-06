package com.example.mutualrentacar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javax.swing.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class CreateRequestController {

    @FXML
    private Label lblTitle, txtEngine, txtYear, txtFuel, lv1, lv2;

    @FXML
    private TextArea txtDescription;

    @FXML
    private DatePicker dtStart, dtEnd;

    @FXML
    private ToggleButton toggleAddPay;

    @FXML
    private TextField txtUserRec, txtOwnerRec, txtTitle;

    @FXML
    public SplitMenuButton splitMenu;

    @FXML
    private ImageView imgView, imgLiked;


    public static String HostCarId, clientCarId;
    public static boolean flag;
    Image imgSaved = new Image(this.getClass().getResourceAsStream("/Selected_Images/savedImg.png"));
    Image imgUnSaved = new Image(this.getClass().getResourceAsStream("/Selected_Images/unsavedImg.png"));

    @FXML
    private void imgLiked(){
        System.out.println("Liked? - " + flag);
        if(CreateRequestController.flag) {
            System.out.println("True");
            imgLiked.setImage(imgUnSaved);
            EstablishDBConnection.deleteLikedCar(HostCarId);
            flag=false;
        }else {
            System.out.println("False");
            imgLiked.setImage(imgSaved);
            EstablishDBConnection.addLikedCar(HostCarId);
            flag=true;
        }
    }

    public void setLiked(boolean flag){
        CreateRequestController.flag = flag;

        if(CreateRequestController.flag) {
            imgLiked.setImage(imgSaved);
        }else {
            imgLiked.setImage(imgUnSaved);
        }
    }

    public void setDates(LocalDate startDate, LocalDate endDate){
            this.dtStart.setValue(startDate);
            this.dtStart.setDisable(true);
            dtStart.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

            this.dtEnd.setValue(endDate);
            this.dtEnd.setDisable(true);
            dtEnd.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
    }

    public void CreateRequestController(Image img, String title, String engine , String year, String fuels){
                    imgView.setImage(img);
        System.out.println("Title: " + title);
                    lblTitle.setText(title);
                    txtEngine.setText("Двигател: " + engine);
                    txtYear.setText("Година: " + year);

                    txtFuel.setText("Гориво: " + fuels);

                    splitMenu.getItems().clear();
                    splitMenu.setStyle("-fx-alignment: center; -fx-font-size: 17px;");
                    splitMenu.getItems().addAll(EstablishDBConnection.findOwnerCars(splitMenu, HostCarId));

                    MenuItem menuItem = new MenuItem("Без размяна!");
                    menuItem.setOnAction(actionEvent -> {
                        splitMenu.setText("Без Размяна!");
                        CreateRequestController.clientCarId = "No Swap!";
                    });
                    splitMenu.getItems().add(menuItem);
    }

    @FXML
    private void goBack() throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("SearchPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 948, 954);
        Stage stage = new Stage();
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));

        SearchPageController searchPageController = fxmlLoader.getController();
        searchPageController.addDefaultContent();
        stage.show();

        ((Stage) txtDescription.getScene().getWindow()).close();
    }

    @FXML
    private void clear(){
        txtTitle.setText("");
        splitMenu.setText("Размени с");
        txtDescription.setText("");
        dtStart.setValue(LocalDate.now());
        dtEnd.setValue(LocalDate.now().plusDays(1));
        txtUserRec.setText("");
        txtOwnerRec.setText("");
    }

    @FXML
    private void createRequest() throws Exception {

        if(EstablishDBConnection.getItem("Requests", "Host_Car_Id").contains(HostCarId)) {
            ArrayList<String> idRequests = EstablishDBConnection.getItem("Requests", "_id");
            for (int j = 0; j < idRequests.size(); j++) {
                if (EstablishDBConnection.getItem("Requests", "Host_Car_Id").get(j).equals(HostCarId) == false)
                    continue;

                Date reqStart = EstablishDBConnection.findDateViaId("Requests", idRequests.get(j), "Start_Date");
                Date reqEnd = EstablishDBConnection.findDateViaId("Requests", idRequests.get(j), "End_Date");

                Date start = java.sql.Date.valueOf(dtStart.getValue());
                Date end = java.sql.Date.valueOf(dtEnd.getValue());

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                System.out.println(start + " - " + end + " - " + dateFormat.format(reqStart) + " - " + dateFormat.format(reqEnd));

                if (dateFormat.format(reqStart).equals(String.valueOf(dtEnd.getValue())) ||
                        dateFormat.format(reqEnd).equals(String.valueOf(dtStart.getValue())) ||
                        (start.before(reqEnd) && end.after(reqStart)) ||
                        (start.before(reqEnd) && end.after(reqEnd)) ||
                        (start.before(reqStart) && end.after(reqStart)) ||
                        (start.after(reqStart) && end.before(reqEnd))) {
                    JOptionPane.showMessageDialog(null,
                            "Избраните дати са заети!",
                            "Грешка! Заети дати!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }




                if (dtStart.getValue().isBefore(LocalDate.now()) || dtEnd.getValue().isBefore(LocalDate.now()) || dtStart.getValue().equals(dtEnd.getValue())
                        || dtStart.getValue().isAfter(dtEnd.getValue()))
                    JOptionPane.showMessageDialog(null, "Моля въведете валидни дати!", "Грешка!", JOptionPane.ERROR_MESSAGE);
                else if (splitMenu.getText().equals("Размени с"))
                    JOptionPane.showMessageDialog(null, "Моля изберете автомобил за размяна!", "Грешка!", JOptionPane.ERROR_MESSAGE);
                else {

                    int userRec, ownerRec;

                    if (txtUserRec.isVisible() == false) {
                        userRec = 0;
                        ownerRec = 0;
                    } else {
                        if (txtUserRec.getText().isEmpty())
                            userRec = 0;
                        else
                            userRec = Integer.parseInt(txtUserRec.getText());

                        if (txtOwnerRec.getText().isEmpty())
                            ownerRec = 0;
                        else
                            ownerRec = Integer.parseInt(txtOwnerRec.getText());
                    }

                    EstablishDBConnection.addRequest(
                            txtTitle.getText(),
                            dtStart.getValue(),
                            dtEnd.getValue(),
                            txtDescription.getText(),
                            EstablishDBConnection.loggedUsername,
                            clientCarId,
                            EstablishDBConnection.findViaId("Cars", HostCarId, "OwnerUsername"),
                            String.valueOf(HostCarId),
                            userRec,
                            ownerRec);

                    JOptionPane.showMessageDialog(null, "Запитването беше успешно изпратено!",
                            "Изпращане!", JOptionPane.INFORMATION_MESSAGE);

                    FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("SearchPage.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 948, 954);
                    Stage stage = new Stage();
                    stage.setTitle("Търси автомобил!");
                    stage.setScene(scene);
                    stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));

                    SearchPageController searchPageController = fxmlLoader.getController();
                    searchPageController.addDefaultContent();

                    stage.show();
                    ((Stage) txtDescription.getScene().getWindow()).close();
                }
    }

    @FXML
    private void toggleBtn(){
        System.err.println("\n\n\n***************\nToggle btn old status: " + toggleAddPay.isSelected());
        if(toggleAddPay.getText().equals("С доплащане")){
            toggleAddPay.setText("Без доплащане");
            toggleAddPay.setStyle("-fx-background-color: red; -fx-background-radius: 20;");
            txtUserRec.setVisible(false);
            lv1.setVisible(false);
            txtOwnerRec.setVisible(false);
            lv2.setVisible(false);
        }else {
            toggleAddPay.setText("С доплащане");
            toggleAddPay.setStyle("-fx-background-color:  #5afc03; -fx-background-radius: 20;");
            txtUserRec.setVisible(true);
            lv1.setVisible(true);
            txtOwnerRec.setVisible(true);
            lv2.setVisible(true);
        }
        System.err.println("\n\n\n***************\nToggle btn NEW status: " + toggleAddPay.isSelected());
    }

}
