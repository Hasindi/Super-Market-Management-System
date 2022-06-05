package db;

import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static AnchorPane anchorPane;
    private static DBConnection dbConnection = null;
    private Connection connection;

    public DBConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/SuperMarket","root","1234");

    }

    public static DBConnection getInstance() throws SQLException, ClassNotFoundException {
        return (dbConnection==null)?dbConnection=new DBConnection():dbConnection;
    }

    public Connection getConnection() {
        return connection;
    }
}
