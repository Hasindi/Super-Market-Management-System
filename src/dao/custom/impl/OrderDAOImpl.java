package dao.custom.impl;

import dao.SQLUtil;
import dao.custom.OrderDAO;
import entity.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDAOImpl implements OrderDAO {
    @Override
    public ArrayList<Order> loadAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean add(Order entity) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("INSERT INTO `Order` (OrderID, OrderDate, CustID) VALUES (?,?,?)", entity.getOrderId(), entity.getDate(), entity.getCustomerID());

    }

    @Override
    public boolean update(Order entity) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public Order search(String id) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeQuery("SELECT OrderID FROM `Order` WHERE OrderID=?", id).next();

    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException {
        /*ResultSet result = SQLUtil.executeQuery("SELECT Order.OrderID FROM `Order` ORDER BY OrderID DESC LIMIT 1;");

        if (result.next()) {

            String numRun = result.getString("OrderID");
            int col = numRun.length();

            String num1 = numRun.substring(0, 2);//first  (OI)
            String num2 = numRun.substring(2, col);//last (1000)

            int n = Integer.parseInt(num2);
            n++;

            String num3 = Integer.toString(n);
            String fullnum = num1 + num3;
            return fullnum;

        } else {
            return "OI1000";
        }*/
        ResultSet rst = SQLUtil.executeQuery("SELECT `Order`.OrderID FROM `Order` ORDER BY OrderID DESC LIMIT 1;");
        return rst.next() ? String.format("OID-%03d", (Integer.parseInt(rst.getString("OrderID").replace("OID-", "")) + 1)) : "OID-001";
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
        ResultSet result3 = SQLUtil.executeQuery("SELECT COUNT(*)  FROM `Order`");
        result3.next();
        String result = result3.getString(1);
        return result;
    }
}
