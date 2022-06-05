package dao.custom;

import dao.CrudDAO;
import dao.SuperDAO;
import entity.OrderDetails;
import model.CustomerDTO;
import model.OrderDetailsDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface OrderDetailDAO extends CrudDAO<OrderDetails,String> {
}
