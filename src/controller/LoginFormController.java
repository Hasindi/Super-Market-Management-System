package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;


public class LoginFormController {
    public JFXPasswordField pwdPassword;
    public JFXButton btnLogIn;
    public JFXTextField txtUserName;
    public AnchorPane loginContext;

    public void signInOnAction(ActionEvent actionEvent) throws IOException {
        if(txtUserName.getText().equals("admin")& pwdPassword.getText().equals("1234")){
            Stage stage=(Stage) loginContext.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/AdminDashBoard.fxml"))));
            stage.centerOnScreen();
        }else{
            new Alert(Alert.AlertType.WARNING,"Invalid Login...!").show();
        }
    }

    public void backOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage=(Stage) loginContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/SignUpForm.fxml"))));
        stage.centerOnScreen();
    }

    public void TextFieldsReleased(KeyEvent keyEvent) {
    }
}
