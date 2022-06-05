package dao.custom.impl;

import dao.SQLUtil;
import dao.custom.ItemDAO;
import entity.Item;
import model.ItemDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {
    @Override
    public ArrayList<Item> loadAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.executeQuery("SELECT * FROM Item");
        ArrayList<Item> allItems = new ArrayList<>();
        while (rst.next()) {
            allItems.add(new Item(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getBigDecimal(4),
                    rst.getInt(5)
            ));
        }
        return allItems;
    }

    @Override
    public boolean add(Item entity) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("INSERT INTO Item (ItemCode, Description, PackSize, UnitPrice, QTYOnHand) VALUES (?,?,?,?,?)", entity.getItemCode(), entity.getDescription(),entity.getPackSize(), entity.getUnitPrice(), entity.getQtyOnHand());

    }

    @Override
    public boolean update(Item entity) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("UPDATE Item SET Description=?, PackSize=?, UnitPrice=?, QTYOnHand=? WHERE ItemCode=?", entity.getDescription(),entity.getPackSize(),entity.getUnitPrice(), entity.getQtyOnHand(),entity.getItemCode());

    }

    @Override
    public Item search(String code) throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.executeQuery("SELECT * FROM Item WHERE ItemCode=?", code);
        if (rst.next()) {
            return new Item(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getBigDecimal(4),
                    rst.getInt(5)
            );
        }
        return null;
    }

    @Override
    public boolean exist(String code) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeQuery("SELECT ItemCode FROM Item WHERE ItemCode=?", code).next();

    }

    @Override
    public boolean delete(String code) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("DELETE FROM Item WHERE ItemCode=?", code);
    }

    @Override
    public ArrayList<Item> searchItems(String enteredText) throws SQLException, ClassNotFoundException {
        ResultSet result = SQLUtil.executeQuery("SELECT * FROM Item WHERE ItemCode LIKE ? OR Description LIKE ? OR PackSize LIKE ? OR UnitPrice LIKE ? OR QTYOnHand LIKE ?  ", enteredText, enteredText, enteredText, enteredText, enteredText);
        ArrayList<Item> list = new ArrayList<>();

        while (result.next()) {
            list.add(new Item(
                    result.getString(1),
                    result.getString(2),
                    result.getString(3),
                    result.getBigDecimal(4),
                    result.getInt(5)

            ));
        }
        return list;
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException {
        ResultSet result = SQLUtil.executeQuery("SELECT ItemCode FROM item ORDER BY ItemCode DESC LIMIT 1");

        if (result.next()) {

            String numRun = result.getString("ItemCode");
            int col = numRun.length();

            String num1 = numRun.substring(0, 2);//first  (II)
            String num2 = numRun.substring(2, col);//last (1000)

            int n = Integer.parseInt(num2);
            n++;

            String num3 = Integer.toString(n);
            String fullnum = num1 + num3;
            return fullnum;

        } else {
            return "II1000";
        }
    }

    @Override
    public String loadCustomerCount() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public String loadItemCount() throws SQLException, ClassNotFoundException {
        ResultSet result2 = SQLUtil.executeQuery("SELECT COUNT(*)  FROM item");
        result2.next();
        String result =result2.getString(1);
        return result;
    }

    @Override
    public String loadOrderCount() throws SQLException, ClassNotFoundException {
        return null;
    }

}
