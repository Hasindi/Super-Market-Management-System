package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SignUpFormController {
    public AnchorPane SignUpContext;

    public void AdminOnAction(ActionEvent actionEvent) throws IOException {
        SignUpContext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/LoginForm.fxml"));
        SignUpContext.getChildren().add(parent);
    }

    public void CashierOnAction(ActionEvent actionEvent) throws IOException {
        SignUpContext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/LoginFormCashier.fxml"));
        SignUpContext.getChildren().add(parent);
    }
}
