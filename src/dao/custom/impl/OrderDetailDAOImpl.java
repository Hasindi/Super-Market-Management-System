package dao.custom.impl;

import dao.SQLUtil;
import dao.custom.OrderDetailDAO;
import entity.OrderDetails;
import model.OrderDetailsDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailDAOImpl implements OrderDetailDAO {
    @Override
    public ArrayList<OrderDetails> loadAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean add(OrderDetails entity) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("INSERT INTO `OrderDetail` (OrderID, ItemCode, UnitPrice, OrderQTY) VALUES (?,?,?,?)", entity.getOrderID(), entity.getItemCode(),entity.getUnitPrice(), entity.getOrderQTY());

    }

    @Override
    public boolean update(OrderDetails entity) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public OrderDetails search(String s) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean exist(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public String loadCustomerCount() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public String loadItemCount() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public String loadOrderCount() throws SQLException, ClassNotFoundException {
        return null;
    }
}
