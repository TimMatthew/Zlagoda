package services;

import Entities.Category;
import Entities.Customer_Card;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;

public class CategoryService {
    private final Connection connection;

    public CategoryService(){
        connection = new DataBaseHandler().getConnection();
    }

    public boolean addCategory(Category category) throws SQLException {
        if (isCategoryExist(category.getCategory_name()))
            return false;

        String sql = "INSERT INTO category (category_number, category_name) " +
                "VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, category.getCategory_number());
            statement.setString(2, category.getCategory_name());

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
        String sql = "SELECT * FROM category ORDER BY category_number";
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
    public boolean deleteCategory(int category_number) throws SQLException {
        String sql = "DELETE FROM category WHERE category_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, category_number);
            statement.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e){
            return false;
        }
    }

    public boolean isCategoryExist(String name) throws SQLException {
        String sql = "SELECT * FROM category WHERE category_name = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, name);

        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }

    public String getCategoryName(int id) {
        String sql = "SELECT category_name FROM category WHERE category_number = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return resultSet.getString("category_name");
                else
                    return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getNewCategoryID() {
        String sql = "SELECT MAX(category_number) AS max_category_number FROM category";
        int maxCategoryNumber = 0;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                maxCategoryNumber = resultSet.getInt("max_category_number");
            }
        } catch (SQLException ignored) {}

        return maxCategoryNumber + 1;
    }

}
