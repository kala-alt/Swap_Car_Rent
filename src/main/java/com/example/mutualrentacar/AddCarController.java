package com.example.mutualrentacar;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSUploadStream;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.Binary;
import org.bson.types.ObjectId;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AddCarController {

    @FXML
    private TextField txtBrand, txtModel, txtEngine;

    @FXML
    private TextArea txtDescription;

    @FXML
    private Button btnClear;

    @FXML
    private ToggleButton engine;

    @FXML
    private CheckBox gas, a95, diesel;

    @FXML
    private RadioButton hybrid;

    @FXML
    private ImageView imgFuel, imgGas, imgSetCar;

    @FXML
    private SplitMenuButton prodYear;

    private String fuels="";

    public void setProdYear(){
        int year = 1990, index=0;
        prodYear.getItems().clear();

        MenuItem arr[] = new MenuItem[LocalDate.now().getYear()-year];
        year++;
        while (year<=LocalDate.now().getYear())
        {
            arr[index] = new MenuItem(String.valueOf(year));
            year++;
            index++;
        }
        prodYear.getItems().addAll(arr);

        for(MenuItem item: arr) {
            item.setOnAction(e -> {
                prodYear.setText(item.getText());
                prodYear.setStyle("-fx-font-weight: bold; -fx-font-size: 22px; -fx-alignment: center;");
            });
        }
    }


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
        ((Stage) prodYear.getScene().getWindow()).close();
    }


    @FXML
    private void onClearAllBtn(){
            gas.setSelected(false);
            a95.setSelected(false);
            diesel.setSelected(false);
            gas.setDisable(false);
            a95.setDisable(false);
            diesel.setDisable(false);

            hybrid.setSelected(false);

            engine.setText("Към Електрически");
            engine.setStyle("-fx-background-color: green;");
            imgGas.setVisible(false);
            imgFuel.setImage(new Image("System_images/Fuel_image.jpg"));


            if(btnClear.isFocused()) {
                txtBrand.setText("");
                txtModel.setText("");
                txtEngine.setText("");
                txtDescription.setText("");
            }
    }

    @FXML
    private void onEngineButton(){
        onClearAllBtn();
        imgGas.setVisible(false);
        imgFuel.setImage(new Image("System_images/Fuel_image.jpg"));

        if(engine.isSelected()){
            engine.setText("Към ДВГ");
            engine.setStyle("-fx-background-color: red;");
            imgFuel.setImage(new Image("System_images/electric_car.png"));
            visibleBtnCheck(true);
        }else {
            engine.setText("Към Електрически");
            engine.setStyle("-fx-background-color: green;");
            imgFuel.setImage(new Image("System_images/Fuel_image.jpg"));
            visibleBtnCheck(false);
        }
    }

    private void visibleBtnCheck(boolean flag){
        if(engine.isSelected()){
            gas.setVisible(false);
            a95.setVisible(false);
            diesel.setVisible(false);
            hybrid.setVisible(true);
        }else{
            gas.setVisible(true);
            a95.setVisible(true);
            diesel.setVisible(true);
            hybrid.setVisible(false);
        }
    }

    @FXML
    private void GasImageVisible(){
        imgGas.setVisible(gas.isSelected());
    }

    @FXML
    private void lockDiesel(){
        GasImageVisible();
        diesel.setDisable(true);
        if(gas.isSelected() && !a95.isSelected()){
            imgFuel.setImage(new Image("System_images/avtogaz_image.jpg"));
            imgGas.setVisible(false);
        }else
            if(gas.isSelected() || a95.isSelected()) {
                imgFuel.setImage(new Image("System_images/a95-image.png"));
                diesel.setSelected(false);
                diesel.setDisable(true);
            }
            else
        if(a95.isSelected() && !gas.isSelected())
                    imgFuel.setImage(new Image("System_images/a95-image.png"));

                else {
                    imgFuel.setImage(new Image("System_images/Fuel_image.jpg"));
                    diesel.setDisable(false);
                }
    }

    @FXML
    private void lockGasA95(){
        GasImageVisible();
        if(diesel.isSelected()){
            imgFuel.setImage(new Image("System_images/diesel_image.jpg"));
            a95.setSelected(false);
            a95.setDisable(true);
            gas.setSelected(false);
            gas.setDisable(true);
        }else {
            imgFuel.setImage(new Image("System_images/Fuel_image.jpg"));
            a95.setDisable(false);
            gas.setDisable(false);
        }
    }

    @FXML
    private void setHybridAction(){
        if(hybrid.isSelected())
            imgFuel.setImage(new Image("System_images/hybrid-image.png"));
        else
            if(engine.getText().equals("Към ДВГ"))
                imgFuel.setImage(new Image("System_images/electric_car.png"));
            else
                imgFuel.setImage(new Image("System_images/Fuel_image.jpg"));
    }

    @FXML
    private void onPublishAction() throws IOException {
        if(fileSelected==null)
            JOptionPane.showMessageDialog(null, "Моля, качете снимка!", "Грешка!", JOptionPane.ERROR_MESSAGE);
        else
            if(txtBrand.getText().equals("") || txtModel.equals("") || txtEngine.getText().equals("") || checkIsSelectedFuels()==false || checkIsSelectedYear()==false)
                JOptionPane.showMessageDialog(null, "Моля, попълнете всички полета!", "Грешка!", JOptionPane.ERROR_MESSAGE);
            else
            {
                EstablishDBConnection.addCar(
                       EstablishDBConnection.loggedUsername,
                        imageBytes,txtBrand.getText(), txtModel.getText(),
                        Integer.parseInt(prodYear.getText()),
                        getFuels(),
                        txtEngine.getText(),
                        txtDescription.getText());

                JOptionPane.showMessageDialog(null, "Автомобилът беше успешно добавен!", "Успешно добавяне!", JOptionPane.INFORMATION_MESSAGE);

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

                ((Stage) txtBrand.getScene().getWindow()).close();
            }
    }

    private String getFuels(){
     if(engine.getText().equals("Към ДВГ")) {
         if (hybrid.isSelected())
             fuels = "Хибрид";
         else
             fuels = "Електрически";
     }else
            if (diesel.isSelected())
                fuels = "Дизел";
            else
                if(gas.isSelected() && a95.isSelected())
                    fuels = "Газ, Бензин";
                else
                    if(a95.isSelected())
                        fuels = "Бензин";
                    else
                        fuels = "Газ";
                    return fuels;
    }

    byte[] imageBytes;
    File fileSelected;
    @FXML
    private void openImage(){
        FileChooser.ExtensionFilter extension = new FileChooser.ExtensionFilter(".JPEG", "*.jpg");
        FileChooser.ExtensionFilter extension2 = new FileChooser.ExtensionFilter(".PNG", "*.png");
        FileChooser flChooser = new FileChooser();
        flChooser.getExtensionFilters().addAll(extension, extension2);
        flChooser.setTitle("Open your car images!");
        fileSelected = flChooser.showOpenDialog(new Stage());
        String filePath = fileSelected.getAbsolutePath();

        try {
            imageBytes = Files.readAllBytes(Paths.get(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        imgSetCar.setImage(new Image(new ByteArrayInputStream(imageBytes)));
    }

    private boolean checkIsSelectedYear(){
        if(prodYear.getText().equals("Година на производство")){
            JOptionPane.showMessageDialog(null,
                    "Моля изберете година\n на призводство!", "Грешка!", JOptionPane.ERROR_MESSAGE);
            return false;
        }else
            return true;
    }

    private boolean checkIsSelectedFuels(){
        if(a95.isSelected()==false && gas.isSelected()==false && diesel.isSelected() && hybrid.isSelected()){
        JOptionPane.showMessageDialog(null,
                "Моля изберете гориво!", "Грешка!", JOptionPane.ERROR_MESSAGE);
            return false;
        }else
            return true;
    }

}