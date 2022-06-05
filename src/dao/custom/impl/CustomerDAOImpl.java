package dao.custom.impl;

import dao.SQLUtil;
import dao.custom.CustomerDAO;
import entity.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public ArrayList<Customer> loadAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.executeQuery("SELECT * FROM customer");
        ArrayList<Customer> allCustomers = new ArrayList<>();
        while (rst.next()) {
            allCustomers.add(new Customer(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)
            ));
        }
        return allCustomers;
    }

    @Override
    public boolean add(Customer entity) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("INSERT INTO Customer (CustID,CustTitle,CustName,CustAddress,City,Province,PostCode) VALUES (?,?,?,?,?,?,?)",
                entity.getCustID(), entity.getCustTitle(), entity.getCustName(), entity.getCustAddress(),entity.getCity(),entity.getProvince(),entity.getPostCode());

    }

    @Override
    public boolean update(Customer entity) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("UPDATE Customer SET CustTitle=?, CustName=?, CustAddress=?, City=?, Province=?, PostCode=? WHERE CustID=?",
                entity.getCustTitle(), entity.getCustName(), entity.getCustAddress(),entity.getCity(),entity.getProvince(),entity.getPostCode(),entity.getCustID());

    }

    @Override
    public Customer search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.executeQuery("SELECT * FROM customer WHERE CustID=?", id);
        if (rst.next()) {
            return new Customer(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)
            );
        }
        return null;
    }

    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeQuery("SELECT CustID FROM customer WHERE CustID=?", id).next();
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("DELETE FROM customer WHERE CustID=?", id);
    }

    @Override
    public ArrayList<Customer> searchCustomers(String enteredText) throws SQLException, ClassNotFoundException {
        ResultSet result = SQLUtil.executeQuery("SELECT * FROM Customer WHERE CustID LIKE ? OR CustTitle LIKE ? OR CustName LIKE ? OR CustAddress LIKE ? OR City LIKE ? OR Province LIKE ? OR PostCode LIKE ? ", enteredText, enteredText, enteredText, enteredText, enteredText, enteredText, enteredText);
        ArrayList<Customer> list = new ArrayList<>();

        while (result.next()) {
            list.add(new Customer(
                    result.getString(1),
                    result.getString(2),
                    result.getString(3),
                    result.getString(4),
                    result.getString(5),
                    result.getString(6),
                    result.getString(7)

            ));
        }
        return list;

    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException {
        /*ResultSet result = SQLUtil.executeQuery("SELECT CustID FROM Customer ORDER BY CustID DESC LIMIT 1");

        if (result.next()) {

            String numRun = result.getString("CustID");
            int col = numRun.length();

            String num1 = numRun.substring(0, 2);//first  (CI)
            String num2 = numRun.substring(2, col);//last (1000)

            int n = Integer.parseInt(num2);
            n++;

            String num3 = Integer.toString(n);
            String fullnum = num1 + num3;
            return fullnum;

        } else {
            return "CI1000";
        }*/
        ResultSet rst = SQLUtil.executeQuery("SELECT custID FROM SuperMarket.Customer ORDER BY custID DESC LIMIT 1");
        if (rst.next()) {
            String id = rst.getString("custID");
            int newCustomerId = Integer.parseInt(id.replace("C00-", "")) + 1;
            return String.format("C00-%03d", newCustomerId);
        } else {
            return "C00-001";
        }
    }

    public ResultSet lastCustomerId() throws SQLException, ClassNotFoundException {
       return SQLUtil.executeQuery("SELECT CustID FROM customer ORDER BY CustID DESC LIMIT 1");
    }

    @Override
    public String loadCustomerCount() throws SQLException, ClassNotFoundException {
        ResultSet result1 = SQLUtil.executeQuery("SELECT COUNT(*)  FROM customer");
        result1.next();
        String result = result1.getString(1);
        return result;
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
