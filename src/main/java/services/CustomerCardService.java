package services;

import Entities.Customer_Card;
import Entities.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.LogAction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerCardService {
    private final Connection connection;

    public CustomerCardService(){
        connection = new DataBaseHandler().getConnection();
    }

    public boolean addCustomerCard(Customer_Card card) throws SQLException {
        if (isCustomerCardExist(card.getPhone_number()))
            return false;

        String sql = "INSERT INTO customer_card (card_number, cust_surname, cust_name, cust_patronymic, phone_number, city, street, zip_code, percent) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, card.getCard_number());
            statement.setString(2, card.getCust_surname());
            statement.setString(3, card.getCust_name());
            statement.setString(4, card.getCust_patronymic());
            statement.setString(5, card.getPhone_number());
            statement.setString(6, card.getCity());
            statement.setString(7, card.getStreet());
            statement.setString(8, card.getZip_code());
            statement.setString(9, card.getPercent());

            statement.executeUpdate();
        }

        new LogService().addLog(LogAction.ADD_CUSTOMER_CARD, LogService.getLogMessage("added the new customer card: " + card));
        return true;
    }

    public void updateCustomerCard(Customer_Card card) throws SQLException {
        String sql = "UPDATE customer_card SET cust_name=?, cust_surname=?, cust_patronymic=?, phone_number=?, " +
                "city=?, street=?, zip_code=?, percent=? WHERE card_number=?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, card.getCust_name());
            statement.setString(2, card.getCust_surname());
            statement.setString(3, card.getCust_patronymic());
            statement.setString(4, card.getPhone_number());
            statement.setString(5, card.getCity());
            statement.setString(6, card.getStreet());
            statement.setString(7, card.getZip_code());
            statement.setString(8, card.getPercent());
            statement.setString(9, card.getCard_number());

            new LogService().addLog(LogAction.UPDATE_CUSTOMER_CARD, LogService.getLogMessage("updated the customer card: " + card));
            statement.executeUpdate();
        }
    }
    public ObservableList<Customer_Card> getAllCustomers() {
        ObservableList<Customer_Card> customers = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customer_card ORDER BY cust_surname";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String id = rs.getString("card_number");
                String surname = rs.getString("cust_surname");
                String name = rs.getString("cust_name");
                String patronymic = rs.getString("cust_patronymic");
                String phoneNumber = rs.getString("phone_number");
                String city = rs.getString("city");
                String street = rs.getString("street");
                String zipCode = rs.getString("zip_code");
                String percent = rs.getString("percent");

                customers.add(new Customer_Card(id, surname, name, patronymic, phoneNumber, city, street, zipCode, percent));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employees from database: " + e.getMessage());
        }
        return customers;
    }
    public void deleteCustomerCard(String card_number) throws SQLException {
        String sql = "DELETE FROM customer_card WHERE card_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, card_number);
            statement.executeUpdate();
            new LogService().addLog(LogAction.DELETE_CUSTOMER_CARD, LogService.getLogMessage("deleted the customer card: " + card_number));
        }
    }

    public boolean isCustomerCardExist(String phone_number) throws SQLException {
        String sql = "SELECT * FROM customer_card WHERE phone_number = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, phone_number);

        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }

}
