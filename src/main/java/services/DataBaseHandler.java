package services;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseHandler {
    private static final String dbHost = "127.0.0.1";
    private static final String dbPort = "3306";
    private static final String dbName = "zlagoda";
    private static final String dbLogin = "root";
    private static final String dbPass = "root";

    private static Connection con;

    public  DataBaseHandler() {
        if (con == null)
            initConnection();
    }

    private void initConnection(){
        String connectionString = String.format("jdbc:mysql://%s:%s/%s", dbHost, dbPort, dbName);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(connectionString, dbLogin, dbPass);
        } catch (Exception e){
            System.err.println(e.getMessage());
        }
    }
    public Connection getConnection(){
        return con;
    }
}
