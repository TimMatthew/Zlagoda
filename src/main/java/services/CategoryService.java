package services;

import Entities.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.LogAction;

import java.sql.*;
import java.util.*;

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
            new LogService().addLog(LogAction.ADD_CATEGORY, LogService.getLogMessage("added the new category: " + category));
        }

        return true;
    }

    public void updateCategory(Category category) throws SQLException {
        String sql = "UPDATE category SET category_name=? WHERE category_number=?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category.getCategory_name());
            statement.setInt(2, category.getCategory_number());

            statement.executeUpdate();
            new LogService().addLog(LogAction.UPDATE_CATEGORY, LogService.getLogMessage("updated the category: " + category));
        }
    }
    public ObservableList<Category> getAllCategories() {
        ObservableList<Category> categories = FXCollections.observableArrayList();
        String sql = "SELECT c.category_number, c.category_name, " +
                "COUNT(DISTINCT p.id_product) AS products_count, " +
                "COUNT(DISTINCT sp.UPC) AS available_products_count, " +
                "SUM(sp.products_number) AS total_available_products_count " +
                "FROM category c " +
                "LEFT JOIN product p ON c.category_number = p.category_number " +
                "LEFT JOIN store_product sp ON p.id_product = sp.id_product " +
                "GROUP BY c.category_number, c.category_name " +
                "ORDER BY c.category_number";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("category_number");
                String name = rs.getString("category_name");
                int productsCount = rs.getInt("products_count");
                int availableProductsCount = rs.getInt("available_products_count");
                int totalAvailableProductsCount = rs.getInt("total_available_products_count");

                Category category = new Category(id, name);
                category.setProducts_count(productsCount);
                category.setAvailableProducts_count(availableProductsCount);
                category.setTotal_available_products_count(totalAvailableProductsCount);
                categories.add(category);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employees from database: " + e.getMessage());
        }
        return categories;
    }
    public boolean deleteCategory(int category_number) throws SQLException {
        String sql = "DELETE FROM category WHERE category_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, category_number);
            new LogService().addLog(LogAction.DELETE_CATEGORY, LogService.getLogMessage("deleted the category: " + getCategoryName(category_number)));
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
    public int getCategoryID(String name) {
        String sql = "SELECT category_number FROM category WHERE category_name = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return resultSet.getInt("category_number");
                else
                    return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public ObservableList<String> getCategoryNames() {
        ObservableList<String> names = FXCollections.observableArrayList();
        String sql = "SELECT category_name FROM category";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()){
                    names.add(resultSet.getString("category_name"));
                }

                return names;
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

//    public LinkedHashMap<String, List<String>> getAvailableProductsByCategory() {
//        LinkedHashMap<String, List<String>> dependencies = new LinkedHashMap<>();
//        String sql = "SELECT store_product.id_Product, category_number FROM product JOIN store_product ON store_product.id_product = product.id_product WHERE promotional_product = 1";
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            ResultSet rs = statement.executeQuery();
//            while (rs.next()) {
//                String id_product = rs.getString("id_product");
//                String category_number = rs.getString("category_number");
//
//                if (!dependencies.containsKey(category_number)) {
//                    dependencies.put(category_number, new ArrayList<>());
//                }
//                dependencies.get(category_number).add(id_product);
//            }
//        } catch (SQLException e) {
//            System.err.println("Error fetching зкщвгсеі from database: " + e.getMessage());
//        }
//        return dependencies;
//    }
    public ObservableList<Category> getCategoriesByPropertyStartsWith(String property, String startsWith) {
        ObservableList<Category> categories = FXCollections.observableArrayList();
        String sql = "SELECT c.category_number, c.category_name, " +
                "COUNT(DISTINCT p.id_product) AS products_count, " +
                "COUNT(DISTINCT sp.UPC) AS available_products_count, " +
                "SUM(sp.products_number) AS total_available_products_count " +
                "FROM category c " +
                "LEFT JOIN product p ON c.category_number = p.category_number " +
                "LEFT JOIN store_product sp ON p.id_product = sp.id_product " +
                "WHERE c." + property + " LIKE ? " +
                "GROUP BY c.category_number, c.category_name " +
                "ORDER BY c.category_number";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, startsWith + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("category_number");
                String name = rs.getString("category_name");
                int productsCount = rs.getInt("products_count");
                int availableProductsCount = rs.getInt("available_products_count");
                int totalAvailableProductsCount = rs.getInt("total_available_products_count");

                Category category = new Category(id, name);
                category.setProducts_count(productsCount);
                category.setAvailableProducts_count(availableProductsCount);
                category.setTotal_available_products_count(totalAvailableProductsCount);
                categories.add(category);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching categories from database: " + e.getMessage());
        }
        return categories;
    }

    public Map<String, Double> getCategoryAveragePrices() {
        Map<String, Double> categoryPrices = new LinkedHashMap<>();

            String sql = "SELECT c.category_name, AVG(sp.selling_price) AS average_price " +
                    "FROM category c " +
                    "JOIN product p ON c.category_number = p.category_number " +
                    "JOIN store_product sp ON p.id_product = sp.id_product " +
                    "GROUP BY c.category_name " +
                    "ORDER BY average_price DESC";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    String categoryName = resultSet.getString("category_name");
                    double averagePrice = resultSet.getDouble("average_price");
                    categoryPrices.put(categoryName, averagePrice);
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categoryPrices;
    }
}
