package dao.custom.impl;

import dao.SQLUtil;
import dao.custom.QueryDAO;
import model.CustomDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class QueryDAOImpl implements QueryDAO {
    @Override
    public ArrayList<CustomDTO> searchOrderByOrderID(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.executeQuery("SELECT order.OrderID,order.OrderDate,order.CustID,orderDetail.ItemCode,orderDetail.OrderQTY,orderDetail.UnitPrice FROM order inner join orderDetail on order.OrderID=orderDetail.OrderID WHERE order.OrderID=?;", id);
        ArrayList<CustomDTO> orderRecords = new ArrayList();
        while (rst.next()) {
            orderRecords.add(new CustomDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getInt(5),
                    rst.getBigDecimal(6)
                    ));
        }
        return orderRecords;
    }
}
