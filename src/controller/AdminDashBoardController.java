package controller;

import bo.BOFactory;
import bo.custom.CustomerBO;
import bo.custom.ItemBO;
import bo.custom.PlaceOrderBO;
import bo.custom.impl.CustomerBOImpl;
import bo.custom.impl.ItemBOImpl;
import bo.custom.impl.PlaceOrderBOImpl;
import dao.SQLUtil;
import entity.ItemQTYRates;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdminDashBoardController implements AddWindowUi, CloseWindowUi {
    public AnchorPane loadFormContext;
    public Label lblTotalIncome;
    public Label lblDay;
    public Label lblDate;
    public Label lblTime;
    public Button btnDashboard;
    public Button btnItem;
    public Label lblItemCount;
    public AnchorPane AdminDashBoardContext;
    public Label lblReportCount;
    public Button btnReport;
    public Label lblCustomerCount;
    public Button btnCustomer;
    public BarChart barChartItem;

    public void initialize() {

        DateTime();

        try {
            loadAllDashLabels();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        calculateIncome();
    }

    private void loadAllDashLabels() throws SQLException, ClassNotFoundException {
        try {
            CustomerBO customerBO = new CustomerBOImpl();
            String id1 = customerBO.loadCustomerCount();
            lblCustomerCount.setText(id1);

            ItemBO itemBO = new ItemBOImpl();
            String id2 = itemBO.loadItemCount();
            lblItemCount.setText(id2);

            PlaceOrderBO orderBO = new PlaceOrderBOImpl();
            String id3 = orderBO.loadOrderCount();
            lblReportCount.setText(id3);

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
        CloseWindowUi(AdminDashBoardContext);
        Parent parent = FXMLLoader.load(getClass().getResource("../view/AdminDashboard.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    public void CustomerOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("CustomerForm");
    }

    public void ItemOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("ItemForm");
    }

    public void ReportOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("AllOrderDetail");
    }

    public void LogoutOnAction(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are You Sure...?", ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.get().equals(ButtonType.YES)) {
            Stage stage = (Stage) AdminDashBoardContext.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/LoginForm.fxml"))));
            stage.centerOnScreen();
        }
    }

    public void  calculateIncome(){
        /*try{
            ResultSet result = SQLUtil.executeUpdate("SELECT SUM(Price) FROM `OrderDetail`");
            result.next();
            String s = String.valueOf(result.getDouble(1));
            lblTotalIncome.setText(s);
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }*/
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
