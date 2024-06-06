package com.example.mutualrentacar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

public class ForgotPasswordController {

    @FXML
    private TextField txtUser;

    @FXML
    private PasswordField txtPassword, txtRepeatPassword;

    @FXML
    private void clear(){
        txtUser.setText("");
        txtPassword.setText("");
        txtRepeatPassword.setText("");
    }

    @FXML
    private void goBack() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 584, 702);
        Stage stage = new Stage();
        stage.setTitle("Вход!");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));
        stage.show();

        ((Stage) txtUser.getScene().getWindow()).close();
    }

    @FXML
    private void changePassword() throws IOException{
        if(txtUser.getText().equals("") || txtPassword.getText().equals("") || txtRepeatPassword.getText().equals(""))
            JOptionPane.showMessageDialog(null, "Моля попълнете всички полета!", "Грешка!", JOptionPane.ERROR_MESSAGE);
        else
            if(txtPassword.getText().equals(txtRepeatPassword.getText())==false)
                JOptionPane.showMessageDialog(null, "Паролите не съвпадат!", "Грешка!", JOptionPane.ERROR_MESSAGE);
            else{
                try {
                    EstablishDBConnection.editData("User", "Username", txtUser.getText(), "Password", txtPassword.getText());
                }catch (Exception e){
                    JOptionPane.showMessageDialog(null, "Моля, въведете валидно\n потребителско име!", "Грешка!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                goBack();
            }
    }
}
