package bo.custom.impl;

import bo.custom.PlaceOrderBO;
import dao.DAOFactory;
import dao.custom.*;
import db.DBConnection;
import entity.Customer;
import entity.Item;
import entity.Order;
import entity.OrderDetails;
import javafx.scene.control.Alert;
import model.CustomerDTO;
import model.ItemDTO;
import model.OrderDTO;
import model.OrderDetailsDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlaceOrderBOImpl implements PlaceOrderBO {

    private final CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);
    private final ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);
    private final OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    private final OrderDetailDAO orderDetailDAO = (OrderDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDERDETAIL);
    private final QueryDAO queryDAO = (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERYDAO);


    @Override
    public boolean placeOrder(OrderDTO dto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();

        if (orderDAO.exist(dto.getOrderId())) {

        }
        connection.setAutoCommit(false);

        boolean isOrderSaved = orderDAO.add(new Order(dto.getOrderId(), dto.getOrderDate(), dto.getCustomerId()));

        if (!isOrderSaved) {
            connection.rollback();
            connection.setAutoCommit(true);
            return false;
        }

        for (OrderDetailsDTO detailsDTO : dto.getOrderDetails()) {

            boolean save1 = orderDetailDAO.add(new OrderDetails(
                    detailsDTO.getOrderId(), detailsDTO.getItemCode(), detailsDTO.getQty(), detailsDTO.getUnitPrice()));

            if (!save1) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }


            ItemDTO item = searchItem(detailsDTO.getItemCode());
            item.setQtyOnHand(item.getQtyOnHand() - detailsDTO.getQty());


            boolean update = itemDAO.update(new Item(
                    item.getItemCode(), item.getDescription(),item.getPackSize(),item.getUnitPrice(), item.getQtyOnHand() ));

            if (!update) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }
        }
        connection.commit();
        connection.setAutoCommit(true);
        return true;
    }

    @Override
    public CustomerDTO searchCustomer(String id) throws SQLException, ClassNotFoundException {
        Customer entity = customerDAO.search(id);
        return new CustomerDTO(entity.getCustID(), entity.getCustTitle(), entity.getCustName(), entity.getCustAddress(),entity.getCity(),entity.getProvince(),entity.getPostCode());
    }

    @Override
    public ItemDTO searchItem(String code) throws SQLException, ClassNotFoundException {
        Item entity = itemDAO.search(code);
        return new ItemDTO(
                entity.getItemCode(),entity.getDescription(),entity.getPackSize(), entity.getUnitPrice(), entity.getQtyOnHand());
    }

    @Override
    public boolean checkItemIsAvailable(String code) throws SQLException, ClassNotFoundException {
        return itemDAO.exist(code);
    }

    @Override
    public boolean checkCustomerIsAvailable(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.exist(id);
    }

    @Override
    public String generateNewOrderID() throws SQLException, ClassNotFoundException {
        return orderDAO.generateNewID();
    }

    @Override
    public ArrayList<CustomerDTO> loadAllCustomers() throws SQLException, ClassNotFoundException {
        ArrayList<Customer> all = customerDAO.loadAll();
        ArrayList<CustomerDTO> allCustomers = new ArrayList<>();
        for (Customer entity : all) {
            allCustomers.add(new CustomerDTO(
                    entity.getCustID(), entity.getCustTitle(), entity.getCustName(),entity.getCustAddress(),entity.getCity(),entity.getProvince(),entity.getPostCode()));
        }
        return allCustomers;
    }

    @Override
    public ArrayList<ItemDTO> loadAllItems() throws SQLException, ClassNotFoundException {
        ArrayList<Item> all = itemDAO.loadAll();
        ArrayList<ItemDTO> allItems = new ArrayList<>();
        for (Item entity : all) {
            allItems.add(new ItemDTO(
                    entity.getItemCode(), entity.getDescription(),entity.getPackSize(), entity.getUnitPrice(), entity.getQtyOnHand()));
        }
        return allItems;
    }

    @Override
    public String loadOrderCount() throws SQLException, ClassNotFoundException {
        return orderDAO.loadOrderCount();
    }

    @Override
    public ResultSet LastCId() throws SQLException, ClassNotFoundException {
        return customerDAO.lastCustomerId();
    }
}
