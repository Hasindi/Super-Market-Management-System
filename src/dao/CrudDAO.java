package dao;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CrudDAO<T, ID> extends SuperDAO{
    ArrayList<T> loadAll() throws SQLException, ClassNotFoundException;

    boolean add(T dto) throws SQLException, ClassNotFoundException;

    boolean update(T dto) throws SQLException, ClassNotFoundException;

    T search(ID id)throws SQLException,ClassNotFoundException;

    boolean exist(ID id) throws SQLException, ClassNotFoundException;

    boolean delete(ID id) throws SQLException, ClassNotFoundException;

    String generateNewID() throws SQLException, ClassNotFoundException;

    String loadCustomerCount() throws SQLException, ClassNotFoundException;

    String loadItemCount() throws SQLException, ClassNotFoundException;

    String loadOrderCount() throws SQLException, ClassNotFoundException;

}
