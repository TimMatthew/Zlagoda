package services;

import Entities.Check;
import Entities.Employee;
import Entities.Store_Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.LogAction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

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

    public void deleteCheck(String check_number) throws SQLException {
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

    public ObservableList<Check> getChecks() {
        ObservableList<Check> checks = FXCollections.observableArrayList();
        String sql = "SELECT * FROM check_t";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String check_number = rs.getString("check_number");
                String id_employee = rs.getString("id_employee");
                String card_number = rs.getString("card_number");
                Date print_date = rs.getDate("print_date");
                double sum_total = rs.getDouble("sum_total");
                double vat = rs.getDouble("vat");

                checks.add(new Check(check_number, id_employee, card_number, print_date, sum_total, vat));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employees from database: " + e.getMessage());
        }
        return checks;
    }

    public ObservableList<Check> getChecksBetweenDates(Date startDate, Date endDate) {
        ObservableList<Check> checks = FXCollections.observableArrayList();
        String sql = "SELECT * FROM check_t WHERE print_date BETWEEN ? AND ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, new java.sql.Date(startDate.getTime()));
            statement.setDate(2, new java.sql.Date(endDate.getTime()));

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String check_number = rs.getString("check_number");
                String id_employee = rs.getString("id_employee");
                String card_number = rs.getString("card_number");
                Date print_date = rs.getDate("print_date");
                double sum_total = rs.getDouble("sum_total");
                double vat = rs.getDouble("vat");

                checks.add(new Check(check_number, id_employee, card_number, print_date, sum_total, vat));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching checks from database: " + e.getMessage());
        }
        return checks;
    }

    public ObservableList<Check> getChecksByPropertyStartsWith(String property, String query) {
        ObservableList<Check> checks = FXCollections.observableArrayList();
        String sql = "SELECT * FROM check_t WHERE " + property + " LIKE ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, query + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String check_number = rs.getString("check_number");
                String idEmployee = rs.getString("id_employee");
                String cardNumber = rs.getString("card_number");
                java.sql.Date printData = rs.getDate("print_data");
                double sumTotal = rs.getDouble("sum_total");
                double vat = rs.getDouble("vat");

                checks.add(new Check(check_number, idEmployee, cardNumber, printData, sumTotal, vat));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return checks;
    }
    public ObservableList<Check> getChecksByProductCategory(int percent, int categoryNumber) {
        ObservableList<Check> checks = FXCollections.observableArrayList();
        String sql = "SELECT c.check_number, c.print_date, c.sum_total " +
                "FROM check_t c " +
                "WHERE NOT EXISTS ( " +
                "    SELECT 1 " +
                "    FROM sale s " +
                "    JOIN store_product sp ON s.UPC = sp.UPC " +
                "    JOIN product p ON sp.id_product = p.id_product " +
                "    WHERE s.check_number = c.check_number " +
                "      AND p.category_number = ? " +
                ") " +
                "AND c.card_number NOT IN ( " +
                "    SELECT card_number " +
                "    FROM customer_card " +
                "    WHERE percent > ? " +
                ")";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, categoryNumber);
            statement.setInt(2, percent);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String check_number = rs.getString("check_number");
                Date print_date = rs.getDate("print_date");
                double sum_total = rs.getDouble("sum_total");

                checks.add(new Check(check_number, null, null, print_date, sum_total, 0));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching checks from database: " + e.getMessage());
        }
        return checks;
    }
}
