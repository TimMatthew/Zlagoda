package services;

import Entities.Check;
import utils.LogAction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckService {

    private final Connection connection;
    public CheckService(){
        connection = new DataBaseHandler().getConnection();
    }

    public boolean addCheck(Check check) throws SQLException {
        if (isCheckExist(check.getCheck_number()))
            return false;

        String sql = "INSERT INTO check_t (check_number, id_employee, card_number, print_date, sum_total, vat) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, check.getCheck_number());
            statement.setString(2, check.getEmployee_id_employee());
            statement.setString(3, check.getCustomerCard_card_number());
            statement.setString(4, String.valueOf(check.getPrint_date()));
            statement.setString(5, String.valueOf(check.getSum_total()));
            statement.setString(6, String.valueOf(check.getVat()));

            statement.executeUpdate();
        }

        new LogService().addLog(LogAction.ADD_CHECK, LogService.getLogMessage("added the new check: " + check));

        return true;
    }

    public void deleteCustomerCard(String check_number) throws SQLException {
        String sql = "DELETE FROM check_t WHERE check_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, check_number);
            statement.executeUpdate();
            new LogService().addLog(LogAction.DELETE_CHECK, LogService.getLogMessage("deleted the check: " + check_number));
        }
    }

    public boolean isCheckExist(String check_number) throws SQLException {
        String sql = "SELECT * FROM check_t WHERE check_number = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, check_number);

        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }
}
