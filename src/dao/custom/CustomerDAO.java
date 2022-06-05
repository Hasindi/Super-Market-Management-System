package dao.custom;

import dao.CrudDAO;
import entity.Customer;
import model.CustomerDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface CustomerDAO extends CrudDAO<Customer,String> {
    ArrayList<Customer> searchCustomers(String enteredText) throws SQLException, ClassNotFoundException;
    ResultSet lastCustomerId() throws SQLException, ClassNotFoundException;
}
