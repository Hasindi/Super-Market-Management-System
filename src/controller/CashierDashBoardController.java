package controller;

import bo.custom.CustomerBO;
import bo.custom.PlaceOrderBO;
import bo.custom.impl.CustomerBOImpl;
import bo.custom.impl.PlaceOrderBOImpl;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.AddWindowUi;
import util.CloseWindowUi;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public class CashierDashBoardController implements AddWindowUi, CloseWindowUi {
    public AnchorPane loadFormContext;
    public Label lblCustomerCount;
    public Label lblOrderCount;
    public Label lblDay;
    public Label lblDate;
    public Label lblTime;
    public Button btnDashboard;
    public Button btnCustomer;
    public Button btnPlaceOrder;
    public AnchorPane CashierContext;
    public Button btnManageOrder;

    public void initialize() {
        DateTime();

        try {
            loadAllDashLabels();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAllDashLabels() throws SQLException, ClassNotFoundException {
        try {
            CustomerBO customerBO = new CustomerBOImpl();
            String id1 = customerBO.loadCustomerCount();
            lblCustomerCount.setText(id1);

            PlaceOrderBO orderBO = new PlaceOrderBOImpl();
            String id3 = orderBO.loadOrderCount();
            lblOrderCount.setText(id3);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void DateTime() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            LocalDate currentDate = LocalDate.now();

            lblDay.setText(LocalDate.now().getDayOfWeek().toString());
            lblDate.setText(currentDate.getYear()+"-"+ currentDate.getMonthValue()
                    +"-"+ currentDate.getDayOfMonth());

            lblTime.setText(currentTime.getHour() + ":" + currentTime.getMinute() + ":"+ currentTime.getSecond());
        }),
                new KeyFrame(Duration.seconds(1))
        );

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public void AdminInfoOnAction(ActionEvent actionEvent) {

    }

    public void DashboardOnAction(ActionEvent actionEvent) throws IOException {
        CloseWindowUi(CashierContext);
        Parent parent = FXMLLoader.load(getClass().getResource("../view/CashierDashBoard.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    public void CustomerOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("CustomerForm");
    }

    public void PlaceOrderOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("TakeOrderForm");
    }

    public void ManageOrderOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("AllOrderDetail");
    }

    public void LogoutOnAction(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are You Sure...?", ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.get().equals(ButtonType.YES)) {
            Stage stage = (Stage) CashierContext.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/LoginForm.fxml"))));
            stage.centerOnScreen();
        }
    }

    @Override
    public void loadUi(String location) throws IOException {
        loadFormContext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"));
        loadFormContext.getChildren().add(parent);
    }

    @Override
    public void CloseWindowUi(AnchorPane a) throws IOException {
        Stage stage= (Stage)a.getScene().getWindow();
        stage.close();
    }

}
