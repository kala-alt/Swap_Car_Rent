package com.example.mutualrentacar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.bson.types.Binary;


import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class SearchPageController{
    @FXML
    private TextField txtSearchBar;
    @FXML
    private ScrollPane scrollResult;
    @FXML
    public DatePicker dtStart, dtEnd;
    @FXML
    private Label lblNotFound, lblNoCars;
    public static boolean flag = false, flag1=false, flag2=false;
    ArrayList<String> model
            = EstablishDBConnection.getItem("Cars", "Model");
    ArrayList<String> brand
            = EstablishDBConnection.getItem("Cars", "Brand");
    ArrayList<String> ids
            = EstablishDBConnection.getItem("Cars", "_id");
    ArrayList<String> descr
            = EstablishDBConnection.getItem("Cars", "Description");
    ArrayList<Binary> imgs = EstablishDBConnection.getImages("Cars", null, "imageData");
    ArrayList<String> ownUsername = EstablishDBConnection.getItem("Cars", "OwnerUsername");

    @FXML
    private void onSearchClick() throws IOException {
        flag = false;
        flag1=false;
        flag2=false;
    int size = EstablishDBConnection.getNumOfRecords("Cars");

    if(size==0){
        lblNotFound.setVisible(true);
        JOptionPane.showMessageDialog(null, "Няма качени автомобили\nв базата данни!", "Грешка!", JOptionPane.ERROR_MESSAGE);
        return;
    }
        ArrayList<String> blackList = new ArrayList<>();
        if((dtStart.getValue() == null && dtEnd.getValue() !=null) || (dtStart.getValue() != null && dtEnd.getValue()==null)){
            JOptionPane.showMessageDialog(null, "Моля въведете и двете дати!", "Грешка!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(dtStart.getValue() != null)
            flag1=true;
        if(dtEnd.getValue() != null)
            flag2=true;
        if(flag1==false && flag2==false)
            flag=true;

        if(flag1 && flag2) {
            flag=true;
            if (dtStart.getValue().equals(LocalDate.now()) || dtStart.getValue().isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(null, "Невалидни дати!", "Грешка!", JOptionPane.ERROR_MESSAGE);
                return;
            }


            if (dtEnd.getValue().equals(LocalDate.now()) || dtEnd.getValue().isBefore(LocalDate.now()) | dtStart.getValue().equals(dtEnd.getValue())
                    || dtStart.getValue().isAfter(dtEnd.getValue())) {
                JOptionPane.showMessageDialog(null, "Невалидни дати!", "Грешка!", JOptionPane.ERROR_MESSAGE);
                return;
            }


    //Search via dates
    ArrayList<String> idRequests = EstablishDBConnection.getItem("Requests", "_id");
    try {
        Date start = java.sql.Date.valueOf(dtStart.getValue());
        Date end = java.sql.Date.valueOf(dtEnd.getValue());

        for (String id : idRequests) {
                Date reqStart = EstablishDBConnection.findDateViaId("Requests", id, "Start_Date");
                Date reqEnd = EstablishDBConnection.findDateViaId("Requests", id, "End_Date");
            String formattedReqStart = new SimpleDateFormat("yyyy-MM-dd").format(reqStart);
            String formattedReqEnd = new SimpleDateFormat("yyyy-MM-dd").format(reqEnd);
            if( formattedReqStart.equals(String.valueOf(dtEnd.getValue())) ||
                    formattedReqEnd.equals(String.valueOf(dtStart.getValue())) ||
                    (start.before(reqEnd) && end.after(reqStart)) ||
                    (start.before(reqEnd) && end.after(reqEnd)) ||
                    (start.before(reqStart) && end.after(reqStart)) ||
                    (start.after(reqStart) && end.before(reqEnd)))
                {
                    blackList.add(EstablishDBConnection.findViaId("Requests", id, "Host_Car_Id"));
                }
            }
        System.out.println("BlackList items: " + blackList);
        } catch (Exception e) {
            System.err.println("There aren`t any dates!");
        }
    }else System.out.println("No need to check dates!");

// Now 'blackList' contains the IDs of requests with overlapping date ranges (without common days)

            //Search via text
            lblNotFound.setVisible(false);

            AnchorPane contentPane = new AnchorPane();
            ArrayList<Pane> arr = new ArrayList<>();

            for (int i = 0; i < size; i++)
                if (blackList.contains(ids.get(i)) == false
                        && ((brand.get(i).contains(txtSearchBar.getText())
                        || model.get(i).contains(txtSearchBar.getText()))
                        && (EstablishDBConnection.loggedUsername.equals(
                                ownUsername.get(i)) == false))) {

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("pane.fxml"));
                    Pane pane = fxmlLoader.load();

                    if (arr.isEmpty() == false)
                        pane.setLayoutY(arr.getLast().getLayoutY() + arr.getLast().getPrefHeight());

                    PaneController paneController = fxmlLoader.getController();
                    paneController.lblTitle.setText(brand.get(i) +
                            " " + model.get(i));

                    paneController.lblCarDescription.setId(EstablishDBConnection.findIdArr("Cars", "_id").get(i));
                    paneController.lblCarDescription.setText(EstablishDBConnection.findIdArr("Cars", "Description").get(i));
                    arr.add(pane);

                    //Its working properly
                    ByteArrayInputStream bis = new ByteArrayInputStream(imgs.get(i).getData());
                    Image image = new Image(bis);
                    paneController.imgView.setImage(image);
                }

            if (arr.isEmpty())
                lblNotFound.setVisible(true);
            else
                lblNotFound.setVisible(false);

            contentPane.getChildren().addAll(arr);
            scrollResult.setContent(contentPane);
    }



//    @FXML
//    private void NoSelectedDates() throws IOException {
//        if(dtStart.getValue()==null){
//            flag=false;
//            FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("setDates.fxml"));
//            Scene scene = new Scene(fxmlLoader.load(), 800, 366);
//            Stage stage = new Stage();
//            stage.setTitle("Изберете дати!");
//            stage.setScene(scene);
//            stage.setResizable(false);
//            stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));
//            stage.show();
//            ((Stage) lblNotFound.getScene().getWindow()).close();
//        }
//    }




    @FXML
    private void onExitClick(){
        ((Stage) txtSearchBar.getScene().getWindow()).close();
    }

    protected void addDefaultContent() throws Exception {

        PaneController.flagCarDetails = false;
        AnchorPane contentPane = new AnchorPane();
        int owner=0, size = EstablishDBConnection.getNumOfRecords("Cars");
        for(int j=0; j<size; j++)
            if(EstablishDBConnection.loggedUsername.equals(ownUsername.get(j)))
                owner++;
        if((size - owner)==0){
            System.err.println("Няма добавени коли в базата данни!");
            lblNoCars.setVisible(true);
            return;
        }else
            lblNoCars.setVisible(false);
        ArrayList<Pane> arr = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (EstablishDBConnection.loggedUsername.equals(ownUsername.get(i)))
                continue;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("pane.fxml"));
            Pane pane = fxmlLoader.load();
            if (arr.isEmpty()==false)
                pane.setLayoutY(arr.getLast().getLayoutY() + arr.getLast().getPrefHeight());

            PaneController paneController = fxmlLoader.getController();
            paneController.lblTitle.setText(brand.get(i) + " " + model.get(i));
            paneController.lblCarDescription.setId(ids.get(i));
            paneController.lblCarDescription.setText(descr.get(i));
            arr.add(pane);
            ByteArrayInputStream bis = new ByteArrayInputStream(imgs.get(i).getData());
            Image image = new Image(bis);
            paneController.imgView.setImage(image);
        }
        contentPane.getChildren().addAll(arr);
        scrollResult.setContent(contentPane);
    }

    @FXML
    public void goToMyAccount() throws IOException {
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
        ((Stage) scrollResult.getScene().getWindow()).close();
    }

    @FXML
    private void requests() throws IOException {
        ArrayList<String> arr = EstablishDBConnection.findId("Host_Username","Requests", "_id");
        arr.removeAll(EstablishDBConnection.findIdArr("ApprovedRequests", "ReqId"));

        if(EstablishDBConnection.getNumOfRecords("Requests") == 0 || arr.isEmpty())
            JOptionPane.showMessageDialog(null, "Нямате заявки към този момент!",
                    "Грешка!", JOptionPane.ERROR_MESSAGE);
        else {
            FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("RequestReceived.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 942, 682);
            Stage stage = new Stage();
            stage.setTitle("Получени заявки!");
            stage.setScene(scene);
            stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass()
                    .getResourceAsStream("/System_Images/logo.png"))));

            RequestReceivedController req = fxmlLoader.getController();
            req.showRequests(arr);

            stage.show();
            ((Stage) txtSearchBar.getScene().getWindow()).close();
        }
    }
}