package services;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseHandler {
    private static final String dbHost = "mysql-zlagoda-zlagoda.b.aivencloud.com";
    private static final String dbPort = "28885";
    private static final String dbName = "zlagoda_schema";
    private static final String dbLogin = "avnadmin";
    private static final String dbPass = "AVNS_R-gLe4KjLQzR9i9Wxh9";

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
