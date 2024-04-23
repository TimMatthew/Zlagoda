package services;

import Entities.Category;
import Entities.Customer_Card;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryService {
    private final Connection connection;

    public CategoryService(){
        connection = new DataBaseHandler().getConnection();
    }

    public boolean addCategory(Category category) throws SQLException {
        if (isCategoryExist(category.getCategory_name()))
            return false;

        String sql = "INSERT INTO category (category_name) " +
                "VALUES (?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category.getCategory_name());

            statement.executeUpdate();
        }

        return true;
    }

    public void updateCategory(Category category) throws SQLException {
        String sql = "UPDATE category SET category_name=? WHERE category_number=?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category.getCategory_name());
            statement.setInt(2, category.getCategory_number());

            statement.executeUpdate();
        }
    }
    public ObservableList<Category> getAllCategories() {
        ObservableList<Category> customers = FXCollections.observableArrayList();
        String sql = "SELECT * FROM category ORDER BY category_name";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("category_number");
                String name = rs.getString("category_name");

                customers.add(new Category(id, name));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employees from database: " + e.getMessage());
        }
        return customers;
    }
    public void deleteCategory(int card_number) throws SQLException {
        String sql = "DELETE FROM customer_card WHERE card_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, card_number);
            statement.executeUpdate();
        }
    }

    public boolean isCategoryExist(String name) throws SQLException {
        String sql = "SELECT * FROM category WHERE category_name = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, name);

        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }

}
