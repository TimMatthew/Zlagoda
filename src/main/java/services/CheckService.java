package services;

import Entities.Check;
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

                //store_products.add(new Store_Product(Integer.parseInt(id_product), product_upc, Double.parseDouble(selling_price), Integer.parseInt(products_number), Integer.parseInt(isPromotional)));
                checks.add(new Check(check_number, id_employee, card_number, print_date, sum_total, vat));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employees from database: " + e.getMessage());
        }
        return checks;
    }
}
