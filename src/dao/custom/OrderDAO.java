package dao.custom;

import dao.CrudDAO;
import dao.SuperDAO;
import entity.Order;
import model.OrderDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface OrderDAO  extends CrudDAO<Order,String> {

}
