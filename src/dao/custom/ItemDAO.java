package dao.custom;

import dao.CrudDAO;
import dao.SuperDAO;
import entity.Item;
import model.CustomerDTO;
import model.ItemDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface ItemDAO extends CrudDAO<Item,String> {
    ArrayList<Item> searchItems(String enteredText) throws SQLException, ClassNotFoundException;
}
