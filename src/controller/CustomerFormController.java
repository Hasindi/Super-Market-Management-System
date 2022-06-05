package controller;

import bo.BOFactory;
import bo.custom.CustomerBO;
import bo.custom.impl.CustomerBOImpl;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.CustomerDTO;
import util.ValidationUtil;
import view.TM.CustomerTM;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class CustomerFormController {
    public TableView<CustomerTM> tblAllCustomer;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colTitle;
    public TableColumn colCity;
    public TableColumn colProvince;
    public TableColumn colPostCode;
    public TextField txtSearchId;
    public JFXButton btnUpdate;
    public JFXButton btnDelete;
    public JFXButton btnAdd;
    public JFXTextField txtAddress;
    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXTextField txtProvince;
    public JFXTextField txtCity;
    public JFXTextField txtTitle;
    public JFXTextField txtPostCode;

    //private final CustomerBO customerBO = new CustomerBOImpl();
    private final CustomerBO customerBO = (CustomerBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOMER);

    LinkedHashMap<JFXTextField, Pattern> map = new LinkedHashMap<>();

    public void initialize() throws ClassNotFoundException, SQLException {
        btnAdd.setDisable(true);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

        colId.setCellValueFactory(new PropertyValueFactory("CustID"));
        colTitle.setCellValueFactory(new PropertyValueFactory("CustTitle"));
        colName.setCellValueFactory(new PropertyValueFactory("CustName"));
        colAddress.setCellValueFactory(new PropertyValueFactory("CustAddress"));
        colCity.setCellValueFactory(new PropertyValueFactory("City"));
        colProvince.setCellValueFactory(new PropertyValueFactory("Province"));
        colPostCode.setCellValueFactory(new PropertyValueFactory("PostCode"));


        try {
            loadAllCustomers();
            generateNewCustomerID();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        tblAllCustomer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setData(newValue);
            }
        });

        Pattern TitlePattern = Pattern.compile("^[A-z .]{3,}$");
        Pattern NamePattern = Pattern.compile("^[A-z]{3,20}$");
        Pattern AddressPattern = Pattern.compile("^[A-z0-9 ,/]{4,20}$");
        Pattern CityPattern = Pattern.compile("^[A-z0-9 ,/]{4,20}$");
        Pattern ProvincePattern = Pattern.compile("^[A-z0-9 ,/]{4,20}$");
        Pattern postalCodePattern = Pattern.compile("^[A-z0-9 ,/]{4,20}$");


        map.put(txtTitle, TitlePattern);
        map.put(txtName, NamePattern);
        map.put(txtAddress, AddressPattern);
        map.put(txtCity, CityPattern);
        map.put(txtProvince, ProvincePattern);
        map.put(txtPostCode, postalCodePattern);
    }

    private void setData(CustomerTM c) {
        txtId.setText(c.getCustID());
        txtTitle.setText(c.getCustTitle());
        txtName.setText(c.getCustName());
        txtAddress.setText(c.getCustAddress());
        txtCity.setText(c.getCity());
        txtProvince.setText(c.getProvince());
        txtPostCode.setText(c.getPostCode());

        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
        btnAdd.setDisable(true);
    }

    public void generateNewCustomerID() {
        try {
            CustomerBO customerBO = new CustomerBOImpl();
            String id = customerBO.generateNewCustomerID();
            txtId.setText(id);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAllCustomers()  {
        try {
            ArrayList<CustomerDTO> allCustomers = customerBO.loadAllCustomers();
            for (CustomerDTO customer : allCustomers) {
                tblAllCustomer.getItems().add(new CustomerTM(
                        customer.getCustID(), customer.getCustTitle(), customer.getCustName(), customer.getCustAddress(), customer.getCity(), customer.getProvince(), customer.getPostCode()));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void updateCustomerOnAction(ActionEvent actionEvent) {
        String CustID = txtId.getText();
        String CustTitle = txtTitle.getText();
        String CustName = txtName.getText();
        String CustAddress = txtAddress.getText();
        String City = txtCity.getText();
        String Province = txtProvince.getText();
        String PostCode = txtPostCode.getText();

        try {
            if (!existCustomer(CustID)) {
                new Alert(Alert.AlertType.WARNING, "Try Again...!").show();            }

            customerBO.updateCustomer(new CustomerDTO(
                    CustID, CustTitle, CustName, CustAddress, City, Province, PostCode
            ));

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        CustomerTM selectedCustomer = tblAllCustomer.getSelectionModel().getSelectedItem();
        selectedCustomer.setCustTitle(CustTitle);
        selectedCustomer.setCustName(CustName);
        selectedCustomer.setCustAddress(CustAddress);
        selectedCustomer.setCity(City);
        selectedCustomer.setProvince(Province);
        selectedCustomer.setPostCode(PostCode);
        tblAllCustomer.refresh();

        clearText();

    }

    boolean existCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerBO.customerExist(id);
    }

    public void deleteCustomerOnAction(ActionEvent actionEvent) {
        String id = tblAllCustomer.getSelectionModel().getSelectedItem().getCustID();
        try {
            if (!existCustomer(id)) {
                new Alert(Alert.AlertType.CONFIRMATION, "There is no such Customer associated with the id " + id).show();
            }

            customerBO.deleteCustomer(id);
            tblAllCustomer.getItems().remove(tblAllCustomer.getSelectionModel().getSelectedItem());
            tblAllCustomer.getSelectionModel().clearSelection();

        } catch (SQLException  | ClassNotFoundException e) {
           // new Alert(Alert.AlertType.WARNING, "Try Again...!").show();
            e.printStackTrace();
        }
    }

    public void addCustomerOnAction(ActionEvent actionEvent) {
        String CustID = txtId.getText();
        String CustTitle = txtTitle.getText();
        String CustName = txtName.getText();
        String CustAddress = txtAddress.getText();
        String City = txtCity.getText();
        String Province = txtProvince.getText();
        String PostCode = txtPostCode.getText();

        try {
            if (existCustomer(CustID)) {
                new Alert(Alert.AlertType.ERROR, CustID + " Already exists").show();
            }

            customerBO.addCustomer(new CustomerDTO(CustID, CustTitle, CustName, CustAddress, City, Province, PostCode));
            tblAllCustomer.getItems().add(new CustomerTM(CustID, CustTitle, CustName, CustAddress, City, Province, PostCode));

        } catch (SQLException |ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to save the Customer " + e.getMessage()).show();
            e.printStackTrace();
        }

        try {
            CustomerTM selectedCustomer = tblAllCustomer.getSelectionModel().getSelectedItem();
            selectedCustomer.setCustTitle(CustTitle);
            selectedCustomer.setCustName(CustName);
            selectedCustomer.setCustAddress(CustAddress);
            selectedCustomer.setCity(City);
            selectedCustomer.setProvince(Province);
            selectedCustomer.setPostCode(PostCode);
            tblAllCustomer.refresh();
        }catch (Exception e){}

        clearText();

    }

    private void clearText() {
        txtTitle.clear();
        txtName.clear();
        txtAddress.clear();
        txtCity.clear();
        txtProvince.clear();
        txtPostCode.clear();
        generateNewCustomerID();
        tblAllCustomer.refresh();
    }

    public void clearOnAction(ActionEvent actionEvent) {
        clearText();
    }

    public void TextFieldsReleased(KeyEvent keyEvent) {
        ValidationUtil.validate(map, btnAdd);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response = ValidationUtil.validate(map, btnAdd);

            if (response instanceof JFXTextField) {
                JFXTextField textField = (JFXTextField) response;
                textField.requestFocus();
            } else if (response instanceof Boolean) {

            }
        }
    }

    public void SearchOnKeyReleased(KeyEvent keyEvent) throws SQLException, ClassNotFoundException {

        /*String search = "%" + txtSearchId.getText() + "%";

        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            ArrayList<CustomerDTO> customerDTO = customerBO.searchCustomers(search);
            ObservableList<CustomerTM> obCustomerTM = FXCollections.observableArrayList();

            for (CustomerDTO cusDTO : customerDTO) {
                obCustomerTM.add(new CustomerTM(
                        cusDTO.getCustID(),
                        cusDTO.getCustTitle(),
                        cusDTO.getCustName(),
                        cusDTO.getCustAddress(),
                        cusDTO.getCity(),
                        cusDTO.getProvince(),
                        cusDTO.getPostCode()));
            }
            tblAllCustomer.getItems().clear();
            tblAllCustomer.getItems().addAll(obCustomerTM);
            tblAllCustomer.refresh();
        }*/
    }

}
