package services;


import Entities.Employee;
import sessionmanagement.UserInfo;
import utils.LogAction;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;

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
            statement.setDate(7, employee.getDate_of_birth());
            statement.setDate(8, employee.getDate_of_start());
            statement.setString(9, employee.getPhone_number());
            statement.setString(10, employee.getCity());
            statement.setString(11, employee.getStreet());
            statement.setString(12, employee.getZip_code());

            statement.executeUpdate();
        }

        sql = "INSERT INTO user_t (user_login, user_password, id_employee)" +
              "VALUES (?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            statement.setString(2, password);
            statement.setString(3, employee.getId_employee());

            statement.executeUpdate();
        }
        new LogService().addLog(LogAction.ADD_EMPLOYEE, LogService.getLogMessage("registered new employee " + employee.getFullName() + " with login " + login));

        return true;
    }

    public boolean signIn(String login, String password) throws SQLException {
        password = getHashedPassword(password);
        Connection connection = dataBaseHandler.getConnection();

        String sql = "SELECT * FROM user_t WHERE user_login =? AND user_password =?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, login);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next())
            return false;

        UserInfo.id = resultSet.getString("id_employee");
        UserInfo.login = login;

        statement.close();
        resultSet.close();

        UserInfo.updateEmployeeProfile();
        new LogService().addLog(LogAction.LOGIN, LogService.getLogMessage("signed in Zlagoda application."));

        UserInfo.position = UserInfo.employeeProfile.getEmpl_role();
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
        String sql = "SELECT * FROM user_t WHERE user_login = ?";

        PreparedStatement statement = dataBaseHandler.getConnection().prepareStatement(sql);
        statement.setString(1, login);

        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }

    public boolean changePassword(String oldPassword, String password) {
        oldPassword = getHashedPassword(oldPassword);
        password = getHashedPassword(password);
        String sql = "UPDATE user_t SET user_password =? WHERE user_login =? AND user_password =?";
        try(PreparedStatement statement = dataBaseHandler.getConnection().prepareStatement(sql)) {
            statement.setString(1, password);
            statement.setString(2, UserInfo.login);
            statement.setString(3, oldPassword);

            int result = statement.executeUpdate();
            if (result != 0)
                new LogService().addLog(LogAction.CHANGE_PASSWORD, LogService.getLogMessage("changed password."));
            return result != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
