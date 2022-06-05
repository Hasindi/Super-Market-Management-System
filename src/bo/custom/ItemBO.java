package bo.custom;

import bo.SuperBO;
import model.ItemDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ItemBO extends SuperBO {
    ArrayList<ItemDTO> loadAllItems() throws SQLException, ClassNotFoundException;

    boolean deleteItem(String code) throws SQLException, ClassNotFoundException;

    boolean addItem(ItemDTO dto) throws SQLException, ClassNotFoundException;

    boolean updateItem(ItemDTO dto) throws SQLException, ClassNotFoundException;

    boolean itemExist(String code) throws SQLException, ClassNotFoundException;

    String generateNewItemID() throws SQLException, ClassNotFoundException;

    String loadItemCount() throws SQLException, ClassNotFoundException;

   ArrayList<ItemDTO> searchItems(String enteredText) throws SQLException, ClassNotFoundException;
}
