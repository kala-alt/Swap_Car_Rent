package com.example.mutualrentacar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

public class RegisterController {

    @FXML
    private TextField txtUser, txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnRegister, btnClear;

    @FXML
    private void onRegisterClick() throws IOException{
        if(txtUser.getText().equals("") || txtPassword.getText().equals(""))
            JOptionPane.showMessageDialog(null, "Въведените данни са невалидни!", "Грешка!",
                    JOptionPane.ERROR_MESSAGE);
        else {
            try {
                EstablishDBConnection.insertUser(txtEmail.getText(), txtUser.getText(), txtPassword.getText());
            JOptionPane.showMessageDialog(null, "Вие се регистрирахте успешно!", "Поздравления!", JOptionPane.INFORMATION_MESSAGE);

                FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("Login.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 584, 702);
                Stage stage = new Stage();
                stage.setTitle("Вход!");
                stage.setScene(scene

            );
                stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));
            stage.show();

            }catch (Exception ee){
                return;
        }
            ((Stage) txtUser.getScene().getWindow()).close();
        }
    }

    @FXML
    private void onClearClick(){
        txtUser.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
    }

    @FXML
    private void haveAccount() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load(), 584, 702));
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/System_Images/logo.png"))));
        stage.setTitle("Вход!");
        stage.show();
        ((Stage) txtEmail.getScene().getWindow()).close();
    }
}
