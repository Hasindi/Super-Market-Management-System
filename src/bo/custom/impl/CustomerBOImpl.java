package bo.custom.impl;

import bo.custom.CustomerBO;
import dao.DAOFactory;
import dao.custom.CustomerDAO;
import dao.custom.impl.CustomerDAOImpl;
import entity.Customer;
import model.CustomerDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerBOImpl implements CustomerBO {

    private final CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);

    @Override
    public ArrayList<CustomerDTO> loadAllCustomers() throws SQLException, ClassNotFoundException {
        ArrayList<Customer> all = customerDAO.loadAll();
        ArrayList<CustomerDTO> allCustomers= new ArrayList<>();
        for (Customer customer : all) {
            allCustomers.add(new CustomerDTO(
                    customer.getCustID(),customer.getCustTitle(),customer.getCustName(),customer.getCustAddress(),customer.getCity(),customer.getProvince(),customer.getPostCode()));
        }
        return allCustomers;
    }

    @Override
    public boolean addCustomer(CustomerDTO dto) throws SQLException, ClassNotFoundException {
        return customerDAO.add(new Customer(
                dto.getCustID(),dto.getCustTitle(),dto.getCustName(),dto.getCustAddress(),dto.getCity(),dto.getProvince(),dto.getPostCode()));
    }

    @Override
    public boolean updateCustomer(CustomerDTO dto) throws SQLException, ClassNotFoundException {
        return customerDAO.update(new Customer(
                dto.getCustID(),dto.getCustTitle(),dto.getCustName(),dto.getCustAddress(),dto.getCity(),dto.getProvince(),dto.getPostCode()));
    }

    @Override
    public boolean customerExist(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.exist(id);
    }

    @Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.delete(id);
    }

    @Override
    public String generateNewCustomerID() throws SQLException, ClassNotFoundException {
        return customerDAO.generateNewID();
    }

    @Override
    public String loadCustomerCount() throws SQLException, ClassNotFoundException {
        return customerDAO.loadCustomerCount();
    }

    @Override
    public ArrayList<CustomerDTO> searchCustomers(String enteredText) throws SQLException, ClassNotFoundException {
        ArrayList<Customer> customers = customerDAO.searchCustomers(enteredText);
        ArrayList<CustomerDTO> cusDTO = new ArrayList<>();

        for (Customer customer : customers) {
            cusDTO.add(new CustomerDTO(
                    customer.getCustID(),
                    customer.getCustTitle(),
                    customer.getCustName(),
                    customer.getCustAddress(),
                    customer.getCity(),
                    customer.getProvince(),
                    customer.getPostCode()
            ));
        }
        return cusDTO;
    }
}
