package com.example.mutualrentacar;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.swing.*;

public class CarDetailsController {


    @FXML
    private ImageView imgView;

    @FXML
    private javafx.scene.control.Label txtEngine, txtYear, txtFuel;

    @FXML
    private TextField txtTitle;

    public void setDetails(Image image, String model, String brand, String engine, int year, String fuels){
        imgView.setImage(image);
        txtTitle.setText(model + " " + brand);
        txtEngine.setText("Двигател: " + engine);
        txtYear.setText("Произведена: " + year);
        txtFuel.setText("Гориво: " + fuels);
    }

    @FXML
    private void goBack(){
        ((Stage) imgView.getScene().getWindow()).close();
    }

}
