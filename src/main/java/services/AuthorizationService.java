package services;


import Entities.Employee;
import sessionmanagement.UserInfo;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;
import java.util.UUID;

public class AuthorizationService {
    private final DataBaseHandler dataBaseHandler;

    public AuthorizationService() {
        dataBaseHandler = new DataBaseHandler();
    }

    public boolean signUp(Employee employee, String login, String password) throws SQLException {
        if (isEmployeeExist(employee.getPhone_number()) || isUserExist(login))
            return false;

        password = getHashedPassword(password);
        Connection connection = dataBaseHandler.getConnection();

        String sql = "INSERT INTO employee (id_employee, empl_surname, empl_name, empl_patronymic, empl_role, " +
                "salary, date_of_birth, date_of_start, phone_number, city, street, zip_code) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, employee.getId_employee());
            statement.setString(2, employee.getEmpl_surname());
            statement.setString(3, employee.getEmpl_name());
            statement.setString(4, employee.getEmpl_patronymic());
            statement.setString(5, employee.getEmpl_role());
            statement.setString(6, employee.getSalary());
            statement.setDate(7, new Date(employee.getDate_of_birth().getTime()));
            statement.setDate(8, new Date(employee.getDate_of_start().getTime()));
            statement.setString(9, employee.getPhone_number());
            statement.setString(10, employee.getCity());
            statement.setString(11, employee.getStreet());
            statement.setString(12, employee.getZip_code());

            statement.executeUpdate();
        }

        sql = "INSERT INTO user (user_login, user_password, id_employee)" +
              "VALUES (?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            statement.setString(2, password);
            statement.setString(3, employee.getId_employee());

            statement.executeUpdate();
        }

        return true;
    }

    public boolean signIn(String login, String password) throws SQLException {
        password = getHashedPassword(password);
        Connection connection = dataBaseHandler.getConnection();

        String sql = "SELECT * FROM user WHERE user_login =? AND user_password =?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, login);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next())
            return false;

        String employee_id = resultSet.getString("id_employee");

        statement.close();
        resultSet.close();


        sql = "SELECT empl_role FROM employee WHERE id_employee =?";
        PreparedStatement statement1 = connection.prepareStatement(sql);
        statement1.setString(1, employee_id);
        ResultSet resultSet1 = statement1.executeQuery();
        if (!resultSet1.next())
            return false;

        String empl_role = resultSet1.getString("empl_role");

        statement1.close();
        resultSet1.close();

        UserInfo.id = employee_id;
        UserInfo.position = empl_role;
        return true;
    }

    public boolean isEmployeeExist(String phone_number) throws SQLException {
        String sql = "SELECT * FROM employee WHERE phone_number = ?";

        PreparedStatement statement = dataBaseHandler.getConnection().prepareStatement(sql);
        statement.setString(1, phone_number);

        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }

    public boolean isUserExist(String login) throws SQLException {
        String sql = "SELECT * FROM user WHERE user_login = ?";

        PreparedStatement statement = dataBaseHandler.getConnection().prepareStatement(sql);
        statement.setString(1, login);

        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }

    public static String getHashedPassword(String password){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = md.digest(password.getBytes());
            BigInteger bigInteger = new BigInteger(1, messageDigest);
            return  bigInteger.toString(16);
        } catch (Exception ignored){}

        return null;
    }

}
