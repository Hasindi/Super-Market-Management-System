package controller;

import bo.BOFactory;
import bo.custom.PlaceOrderBO;
import bo.custom.impl.PlaceOrderBOImpl;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.CustomerDTO;
import model.ItemDTO;
import model.OrderDTO;
import model.OrderDetailsDTO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import view.TM.OrderDetailTM;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TakeOrderFormController {
    public JFXTextField txtOrderId;
    public JFXTextField txtOrderDate;
    public TextField txtCustomerName;
    public TextField txtPostCode;
    public ComboBox<String> cmbCustomerId;
    public TextField txtLastCustomerId;
    public ComboBox<String> cmbItemCode;
    public TextField txtDesc;
    public TextField txtQtyOnHand;
    public TextField txtUnitPrice;
    public TextField txtDiscount;
    public TextField txtQty;
    public Button btnAddToCart;
    public TextField txtTotal;
    public AnchorPane OrderFormContext;
    public TableView<OrderDetailTM> tblItemDetails;
    public TableColumn colItemCode;
    public TableColumn colDesc;
    public TableColumn colqty;
    public TableColumn colUnitPrice;
    public TableColumn colTotalCost;
    public TableColumn colAction;
    public TableColumn colDiscount;
    public Button btnPlaceOrder;

    //private final PlaceOrderBO placeOrderBO = new PlaceOrderBOImpl();
    PlaceOrderBO placeOrderBO = (PlaceOrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PLACE_ORDER);

    private String OrderID;

    public void initialize() {
        tblItemDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        tblItemDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblItemDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tblItemDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tblItemDetails.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("discount"));
        tblItemDetails.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("total"));


        TableColumn<OrderDetailTM, Button> lastCol = (TableColumn<OrderDetailTM, Button>) tblItemDetails.getColumns().get(6);
        lastCol.setCellValueFactory(param -> {
            Button btnDelete = new Button("Delete");
            btnDelete.setOnAction(event -> {
                tblItemDetails.getItems().remove(param.getValue());
                tblItemDetails.getSelectionModel().clearSelection();

                calculateTotal();
                enableOrDisablePlaceOrderButton();

            });
            return new ReadOnlyObjectWrapper<>(btnDelete);
        });

        setAction();

        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            enableOrDisablePlaceOrderButton();


            //Customer
            if (newValue != null) {

                try {
                    Connection connection = DBConnection.getInstance().getConnection();

                    try {

                        if (!existCustomer(newValue + "")) {
                            new Alert(Alert.AlertType.ERROR, "There is no such Customer associated with the ID " + newValue + "").show();
                        }

                        CustomerDTO search = placeOrderBO.searchCustomer(newValue + "");
                        txtCustomerName.setText(search.getCustName());
                        txtPostCode.setText(search.getPostCode());

                    } catch (SQLException e) {
                        new Alert(Alert.AlertType.ERROR, "Failed to find the customer.Try Again...! ").show();
                    }

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                txtCustomerName.clear();
                txtPostCode.clear();
            }
        });


        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newItemCode) -> {
            txtQty.setEditable(newItemCode != null);
            btnAddToCart.setDisable(newItemCode == null);


            //Item
            if (newItemCode != null) {

                try {
                    if (!existItem(newItemCode + "")) {
                    }

                    ItemDTO item = placeOrderBO.searchItem(newItemCode + "");
                    txtDesc.setText(item.getDescription());
                    txtUnitPrice.setText(item.getUnitPrice().setScale(2).toString());
                    Optional<OrderDetailTM> optOrderDetail = tblItemDetails.getItems().stream().filter(detail -> detail.getItemCode().equals(newItemCode)).findFirst();
                    txtQtyOnHand.setText((optOrderDetail.isPresent() ? item.getQtyOnHand() - optOrderDetail.get().getQty() : item.getQtyOnHand()) + "");

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            } else {
                clearItems();
            }
        });

        tblItemDetails.getSelectionModel().selectedItemProperty().addListener((
                observable, oldValue, selectedOrderDetail) -> {

            if (selectedOrderDetail != null) {
                cmbItemCode.setDisable(true);
                cmbItemCode.setValue(selectedOrderDetail.getItemCode());
                btnAddToCart.setText("Update Cart           ");
                txtQtyOnHand.setText(Integer.parseInt(txtQtyOnHand.getText()) + selectedOrderDetail.getQty() + "");
                txtQty.setText(selectedOrderDetail.getQty() + "");
            } else {
                btnAddToCart.setText("Add to Cart     ");
                cmbItemCode.setDisable(false);
                cmbItemCode.getSelectionModel().clearSelection();
                txtQty.clear();
            }

        });

        generateNewOrderID();
        setCustomerIds();
        setItemCode();
        loadDate();
        LastCId();
    }

    private void setAction() {
        OrderID = generateNewOrderID();
        txtOrderId.setText(OrderID);
        txtOrderDate.setText(LocalDate.now().toString());
        btnPlaceOrder.setDisable(true);
        txtCustomerName.setFocusTraversable(false);
        txtCustomerName.setEditable(false);
        txtPostCode.setFocusTraversable(false);
        txtPostCode.setEditable(false);
        txtDesc.setFocusTraversable(false);
        txtDesc.setEditable(false);
        txtUnitPrice.setFocusTraversable(false);
        txtUnitPrice.setEditable(false);
        txtQtyOnHand.setFocusTraversable(false);
        txtQtyOnHand.setEditable(false);
        txtQty.setOnAction(event -> btnAddToCart.fire());
        txtQty.setEditable(false);
        btnAddToCart.setDisable(true);
    }

    private void clearItems() {
        txtDesc.clear();
        txtQty.clear();
        txtQtyOnHand.clear();
        txtUnitPrice.clear();
        txtDiscount.clear();
    }

    private void loadDate() {
        txtOrderDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    public String generateNewOrderID() {
        try {
            return placeOrderBO.generateNewOrderID();

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to generate a new order id").show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "OID-001";
    }

    public void LastCId() {
        try {
            PlaceOrderBO placeOrderBO = new PlaceOrderBOImpl();
            ResultSet result = placeOrderBO.LastCId();

            if (result.next()) {
                String numRun = result.getString("CustID");
                txtLastCustomerId.setText(numRun);
            } else {
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void setItemCode() {
        try {
            ArrayList<ItemDTO> all = placeOrderBO.loadAllItems();
            for (ItemDTO dto : all) {
                cmbItemCode.getItems().add(dto.getItemCode());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setCustomerIds() {
        try {
            ArrayList<CustomerDTO> all = placeOrderBO.loadAllCustomers();
            for (CustomerDTO customerDTO : all) {
                cmbCustomerId.getItems().add(customerDTO.getCustID());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void backCustomerFormOnAction(ActionEvent actionEvent) throws IOException {
        OrderFormContext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/CustomerForm.fxml"));
        OrderFormContext.getChildren().add(parent);
    }

    public void AddToCartOnAction(ActionEvent actionEvent) {
        if (!txtQty.getText().matches("\\d+") || Integer.parseInt(txtQty.getText()) <= 0 ||
                Integer.parseInt(txtQty.getText()) > Integer.parseInt(txtQtyOnHand.getText())) {
            new Alert(Alert.AlertType.ERROR, "Invalid qty").show();
            txtQty.requestFocus();
            txtQty.selectAll();
            return;
        }


        String itemCode = cmbItemCode.getSelectionModel().getSelectedItem();
        String description = txtDesc.getText();
        BigDecimal unitPrice = new BigDecimal(txtUnitPrice.getText()).setScale(2);
        int qty = Integer.parseInt(txtQty.getText());
        BigDecimal oldTotal = unitPrice.multiply(new BigDecimal(qty)).setScale(2);
        BigDecimal tempDiscount = new BigDecimal(txtDiscount.getText()).setScale(2);
        BigDecimal discount = oldTotal.multiply(tempDiscount).divide(BigDecimal.valueOf(100));
        BigDecimal total = oldTotal.subtract(discount);

        boolean exists = tblItemDetails.getItems().stream().anyMatch(detail -> detail.getItemCode().equals(itemCode));

        if (exists) {
            OrderDetailTM orderDetailTM = tblItemDetails.getItems().stream().filter(detail -> detail.getItemCode().equals(itemCode)).findFirst().get();

            if (btnAddToCart.getText().equalsIgnoreCase("Update Cart")) {
                orderDetailTM.setQty(qty);
                orderDetailTM.setTotal(total);
                tblItemDetails.getSelectionModel().clearSelection();
            } else {
                orderDetailTM.setQty((qty+orderDetailTM.getQty())-orderDetailTM.getQty());
                total = new BigDecimal(orderDetailTM.getQty()).multiply(unitPrice).setScale(2);
                orderDetailTM.setTotal(total);
            }
            tblItemDetails.refresh();
        } else {
            tblItemDetails.getItems().add(new OrderDetailTM(itemCode, description, qty, unitPrice, discount, total));
        };

        cmbItemCode.getSelectionModel().clearSelection();
        cmbItemCode.requestFocus();
        calculateTotal();
        enableOrDisablePlaceOrderButton();
    }

    private void enableOrDisablePlaceOrderButton() {
        btnPlaceOrder.setDisable(!(cmbCustomerId.getSelectionModel().getSelectedItem() != null && !tblItemDetails.getItems().isEmpty()));
    }

    private void calculateTotal() {
        BigDecimal total = new BigDecimal(0);
        for (OrderDetailTM detail : tblItemDetails.getItems()) {
            total = total.add(detail.getTotal());
        }
        txtTotal.setText(String.valueOf(total));
    }

    private boolean existItem(String code) throws SQLException, ClassNotFoundException {
        return placeOrderBO.checkItemIsAvailable(code);
    }

    boolean existCustomer(String id) throws SQLException, ClassNotFoundException {
        return placeOrderBO.checkCustomerIsAvailable(id);
    }

    public void PlaceOrderOnAction(ActionEvent actionEvent) throws SQLException {
        boolean b = saveOrder(OrderID, LocalDate.now(), cmbCustomerId.getValue(),
                tblItemDetails.getItems().stream().map(tm -> new OrderDetailsDTO(OrderID, tm.getItemCode(),
                        tm.getQty(), tm.getUnitPrice())).collect(Collectors.toList()));
        if (b) {
            new Alert(Alert.AlertType.INFORMATION, "Order Placed Successfully").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Order has not been Placed Successfully").show();
        }

        OrderID = generateNewOrderID();
        txtOrderId.setText(OrderID);
        cmbCustomerId.getSelectionModel().clearSelection();
        cmbItemCode.getSelectionModel().clearSelection();
        tblItemDetails.getItems().clear();
        txtQty.clear();
        calculateTotal();
    }

    public boolean saveOrder(String OrderID, LocalDate orderDate, String customerID, List<OrderDetailsDTO> orderDetails) {
        try {
            return placeOrderBO.placeOrder(new OrderDTO(OrderID, orderDate, customerID, orderDetails));

        } catch (SQLException throwables) {
            throwables.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ItemDTO findItem(String code) {
        try {
            return placeOrderBO.searchItem(code);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find the Item " + code, e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void printOnAction(ActionEvent actionEvent) {
        String description = txtDesc.getText();
        int qty = Integer.parseInt(txtQty.getText());
        BigDecimal unitPrice = new BigDecimal(txtUnitPrice.getText()).setScale(2);
        BigDecimal discount= new BigDecimal(txtDiscount.getText()).setScale(2);
        BigDecimal total = new BigDecimal(txtTotal.getText()).setScale(2);
        String OrderId = txtOrderId.getText();

        HashMap paramMap = new HashMap();

        paramMap.put("OrderId", OrderId);
        paramMap.put("description", description);
        paramMap.put("unitPrice", unitPrice);
        paramMap.put("discount", discount);
        paramMap.put("qty", qty);
        paramMap.put("total", total);


        try {
            JasperReport compileReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/view/report/Payment.jasper"));
            ObservableList<OrderDetailTM> items = tblItemDetails.getItems();
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, paramMap, new JRBeanArrayDataSource(tblItemDetails.getItems().toArray()));
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }
}

