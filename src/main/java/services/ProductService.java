package services;

import Entities.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductService {
    private final Connection connection;

    public ProductService(){
        connection = new DataBaseHandler().getConnection();
    }

    public boolean addProduct(Product product) throws SQLException {
        if (isProductExist(product.getProduct_name(), product.getCategory_number()))
            return false;

        String sql = "INSERT INTO product (id_product, category_number, product_name, characteristics) " +
                "VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, product.getId_product());
            statement.setInt(2, product.getCategory_number());
            statement.setString(3, product.getProduct_name());
            statement.setString(4, product.getCharacteristics());

            statement.executeUpdate();
        }

        return true;
    }

    public void updateProduct(Product card) throws SQLException {
        String sql = "UPDATE product SET category_number=?, product_name=?, characteristics=? WHERE id_product=?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, card.getCategory_number());
            statement.setString(2, card.getProduct_name());
            statement.setString(3, card.getCharacteristics());
            statement.setInt(4, card.getId_product());

            statement.executeUpdate();
        }
    }
    public ObservableList<Product> getAllProducts() {
        ObservableList<Product> customers = FXCollections.observableArrayList();
        String sql = "SELECT * FROM product GROUP BY id_product";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id_product");
                int category_id = rs.getInt("category_number");
                String name = rs.getString("product_name");
                String characteristics = rs.getString("characteristics");

                customers.add(new Product(id, category_id, name, characteristics));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employees from database: " + e.getMessage());
        }
        return customers;
    }
    public void deleteProduct(int id) throws SQLException {
        String sql = "DELETE FROM product WHERE  id_product= ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public boolean isProductExist(String name, int category_id) throws SQLException {
        String sql = "SELECT * FROM product WHERE product_name = ? AND category_number =?";

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setInt(2, category_id);

            try(ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public int getNewProductID() {
        String sql = "SELECT MAX(id_product) AS max_id_product FROM product";
        int max_id_product = 0;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                max_id_product = resultSet.getInt("max_id_product");
            }
        } catch (SQLException ignored) {}

        return max_id_product + 1;
    }

}
