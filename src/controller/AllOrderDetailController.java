package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.AddWindowUi;
import util.CloseWindowUi;

import java.io.IOException;

public class AllOrderDetailController implements CloseWindowUi, AddWindowUi {
    public ComboBox cmbSelectTable;
    public TableView tblOrderDetails;
    public TableColumn colOrderId;
    public TableColumn colItemCode;
    public TableColumn colQty;
    public TableColumn colDiscount;
    public TableColumn colPrice;

   // private final OrderDetailBO orderDetailBO = (OrderDetailBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ORDER_Detail);


    /*public void initialize(){
        itemCode;
        private String description;
        private int qty;
        private BigDecimal unitPrice;
        private BigDecimal discount;
        private BigDecimal total;
        colOrderId.setCellValueFactory(new PropertyValueFactory("orderId"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory("date"));
        colCustId.setCellValueFactory(new PropertyValueFactory("customerId"));

        cmbSelectTable.getItems().add("Order Details Report");
        cmbSelectTable.getItems().add("Order Report");


        cmbSelectTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String s1 = String.valueOf(newValue);

            if(s1.equals("Order Report")){
                Stage stage = (Stage) orderDetailContext.getScene().getWindow();

                try {
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/.fxml"))));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        try {
            ArrayList<OrderDetailsDTO> allOrders = orderDetailBO.getAllOrderDetails();

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }}*/


    public void selectTabelOnAction(ActionEvent actionEvent) {
    }

    @Override
    public void loadUi(String location) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @Override
    public void CloseWindowUi(AnchorPane a) throws IOException {
        Stage stage= (Stage)a.getScene().getWindow();
        stage.close();
    }
}
