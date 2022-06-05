package controller;

import bo.BOFactory;
import bo.custom.ItemBO;
import bo.custom.impl.ItemBOImpl;
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
import model.ItemDTO;
import util.ValidationUtil;
import view.TM.ItemTM;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ItemFormController {
    public TableView<ItemTM> tblAllItem;
    public TableColumn colItemCode;
    public TableColumn colDescription;
    public TableColumn colQTYOnHand;
    public TableColumn colUnitPrice;
    public TableColumn colPackSize;
    public TextField txtSearchId;
    public JFXButton btnUpdate;
    public JFXButton btnDelete;
    public JFXTextField txtDescription;
    public JFXButton btnAdd;
    public JFXTextField txtItemCode;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtQtyOnHand;
    public TextField txtSearchName;
    public JFXTextField txtPackSize;

    //private final ItemBO itemBO = new ItemBOImpl();
    private ItemBO itemBO = (ItemBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ITEM);

    LinkedHashMap<JFXTextField, Pattern> map = new LinkedHashMap<>();

    public void initialize() throws ClassNotFoundException, SQLException {

        btnAdd.setDisable(true);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

        colItemCode.setCellValueFactory(new PropertyValueFactory("ItemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory("Description"));
        colPackSize.setCellValueFactory(new PropertyValueFactory("PackSize"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory("UnitPrice"));
        colQTYOnHand.setCellValueFactory(new PropertyValueFactory("QtyOnHand"));


        try{
            loadAllItems();
            generateNewItemID();
        }catch(NullPointerException e){
            e.printStackTrace();
        }


        tblAllItem.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null){
                setData(newValue);
            }
        });

        Pattern descriptionPattern = Pattern.compile("^[A-z]{3,10} $");
        Pattern packSizePattern = Pattern.compile("^[A-z0-9/ ]{5,30}$");
        Pattern unitPricePattern = Pattern.compile("^[0-9]{1,}(.00)$");
        Pattern qtyPattern = Pattern.compile("^[0-9]{1,}$");

        map.put(txtDescription,descriptionPattern);
        map.put(txtPackSize,packSizePattern);
        map.put(txtUnitPrice,unitPricePattern);
        map.put(txtQtyOnHand,qtyPattern);
    }

    private void setData(ItemTM i) {
        txtItemCode.setText(i.getItemCode());
        txtDescription.setText(i.getDescription());
        txtPackSize.setText(i.getPackSize());
        txtUnitPrice.setText(String.valueOf((i.getUnitPrice())));
        txtQtyOnHand.setText(String.valueOf(i.getQtyOnHand()));

        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
        btnAdd.setDisable(true);
    }

    public void generateNewItemID(){
        try {
            ItemBO itemBO = new ItemBOImpl();
            String id = itemBO.generateNewItemID();
            txtItemCode.setText(id);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAllItems()  {
        tblAllItem.getItems().clear();
        try {
            ArrayList<ItemDTO> allItems = itemBO.loadAllItems();
            for (ItemDTO item : allItems) {
                tblAllItem.getItems().add(new ItemTM(
                        item.getItemCode(), item.getDescription(), item.getPackSize(), item.getUnitPrice(), item.getQtyOnHand()));
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void clearOnAction(ActionEvent actionEvent) {
        clearText();
    }

    public void TextFieldsReleased(KeyEvent keyEvent) {
        ValidationUtil.validate(map,btnAdd);

        if (keyEvent.getCode()== KeyCode.ENTER){
            Object response = ValidationUtil.validate(map,btnAdd);

            if (response instanceof JFXTextField){
                JFXTextField textField = (JFXTextField) response;
                textField.requestFocus();
            }else if (response instanceof Boolean){

            }
        }
    }

    public void addItemOnAction(ActionEvent actionEvent) {
        String ItemCode = txtItemCode.getText();
        String Description = txtDescription.getText();
        String PackSize = txtPackSize.getText();
        BigDecimal UnitPrice = new BigDecimal(txtUnitPrice.getText()).setScale(2);
        int QtyOnHand = Integer.parseInt(txtQtyOnHand.getText());

        try {
            if (existItem(ItemCode)) {
                new Alert(Alert.AlertType.ERROR, ItemCode + " Already exists").show();
            }

            itemBO.addItem(new ItemDTO(ItemCode, Description, PackSize, UnitPrice, QtyOnHand));
            tblAllItem.getItems().add(new ItemTM(ItemCode, Description, PackSize, UnitPrice, QtyOnHand));

        } catch (SQLException | ClassNotFoundException e) {
           // new Alert(Alert.AlertType.ERROR, "Failed to save the Item " + e.getMessage()).show();
        }
        try {
            ItemTM selectedItem = tblAllItem.getSelectionModel().getSelectedItem();
            selectedItem.setDescription(Description);
            selectedItem.setPackSize(PackSize);
            selectedItem.setUnitPrice(UnitPrice);
            selectedItem.setQtyOnHand(QtyOnHand);
            tblAllItem.refresh();
        } catch (Exception e) {
        }

        clearText();
    }

    private void clearText() {
        txtDescription.clear();
        txtPackSize.clear();
        txtQtyOnHand.clear();
        txtUnitPrice.clear();
        generateNewItemID();
        tblAllItem.refresh();
    }

    public void deleteItemOnAction(ActionEvent actionEvent) {
        String code = tblAllItem.getSelectionModel().getSelectedItem().getItemCode();

        try {

            if (!existItem(code)) {
                new Alert(Alert.AlertType.CONFIRMATION, "There is no such Item associated with the code " + code).show();
            }

            itemBO.deleteItem(code);
            tblAllItem.getItems().remove(tblAllItem.getSelectionModel().getSelectedItem());
            tblAllItem.getSelectionModel().clearSelection();

        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.WARNING, "Try Again...!").show();
            e.printStackTrace();
        }
    }

    boolean existItem(String code) throws SQLException, ClassNotFoundException {
        return itemBO.itemExist(code);
    }

    public void updateItemOnAction(ActionEvent actionEvent)  {
        String ItemCode = txtItemCode.getText();
        String Description = txtDescription.getText();
        String PackSize = txtPackSize.getText();
        BigDecimal UnitPrice = new BigDecimal(txtUnitPrice.getText()).setScale(2);
        int QtyOnHand = Integer.parseInt(txtQtyOnHand.getText());

        try {
            if (!existItem(ItemCode)) {
                new Alert(Alert.AlertType.WARNING, "Try Again...!").show();            }

            itemBO.updateItem(new ItemDTO(
                    ItemCode, Description, PackSize, UnitPrice, QtyOnHand
            ));

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            ItemTM selectedItem = tblAllItem.getSelectionModel().getSelectedItem();
            selectedItem.setDescription(Description);
            selectedItem.setPackSize(PackSize);
            selectedItem.setUnitPrice(UnitPrice);
            selectedItem.setQtyOnHand(QtyOnHand);
            tblAllItem.refresh();
        }catch (Exception e){}

        clearText();
    }

    public void SearchOnKeyReleased(KeyEvent keyEvent) throws SQLException, ClassNotFoundException {
        /*String search = "%" + txtSearchId.getText() + "%";

        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            ArrayList<ItemDTO> itemDTO = itemBO.searchItems(search);
            ObservableList<ItemTM> obItemTM = FXCollections.observableArrayList();

            for (ItemDTO iDTO : itemDTO) {
                obItemTM.add(new ItemTM(iDTO.getItemCode(),
                        iDTO.getDescription(),
                        iDTO.getPackSize(),
                        iDTO.getUnitPrice(),
                        iDTO.getQtyOnHand()
                       ));
            }
            tblAllItem.getItems().clear();
            tblAllItem.getItems().addAll(obItemTM);
            tblAllItem.refresh();
        }*/
    }
}
