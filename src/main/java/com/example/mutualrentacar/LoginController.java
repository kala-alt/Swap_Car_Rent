package com.example.mutualrentacar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class LoginController {

    @FXML
    private TextField txtUser;

    @FXML
    private PasswordField txtPass;

    @FXML
    private void onLoginClick() throws IOException{
        if(txtUser.getText().equals("") || txtPass.getText().equals(""))
            JOptionPane.showMessageDialog(null, "Моля попълнете всички полета!", "Грешка!", JOptionPane.ERROR_MESSAGE);
        else {
            String pass = EstablishDBConnection.findResult("User", "Username",txtUser.getText(),"Password");
            EstablishDBConnection.loggedUsername = txtUser.getText();

           if(pass!= null && pass.equals(txtPass.getText())){
               FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
               Stage stage = new Stage();
               stage.setScene(new Scene(fxmlLoader.load(), 1012, 664));
               stage.show();
               stage.setTitle("Начало!");
               stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));
               ((Stage) txtUser.getScene().getWindow()).close();

           }
           else
               JOptionPane.showMessageDialog(null, "Невалидно потребителско име\n или парола!", "Грешка!", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    private void onClearClick(){
        txtUser.setText("");
        txtPass.setText("");
    }

    @FXML
    private void onNoAccount() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Register.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load(), 849, 603));
        stage.show();
        stage.setTitle("Регистрация!");
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));
        ((Stage) txtUser.getScene().getWindow()).close();
    }

    @FXML
    private void forgotPassword() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ForgotPassword.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load(), 849, 603));
        stage.show();
        stage.setTitle("Поднови паролата си!");
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));
        ((Stage) txtUser.getScene().getWindow()).close();
    }

}
