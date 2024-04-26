package services;

import Entities.Store_Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import services.DataBaseHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBaseRequestManager {

    private static final DataBaseHandler dataBaseHandler = new DataBaseHandler();

    public static ObservableList<Store_Product> getStoreProducts() {
        ObservableList<Store_Product> store_products = FXCollections.observableArrayList();
        Connection connection = dataBaseHandler.getConnection();
        String sql = "SELECT store_product.id_product, product_name, UPC, selling_price, products_number, promotional_product FROM store_product JOIN product ON store_product.id_product = product.id_product";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String id_product = rs.getString("id_product");
                String product_upc = rs.getString("UPC");
                String selling_price = rs.getString("selling_price");
                String products_number = rs.getString("products_number");
                String isPromotional = rs.getString("promotional_product");
                store_products.add(new Store_Product(Integer.parseInt(id_product), product_upc, Double.parseDouble(selling_price), Integer.parseInt(products_number), Integer.parseInt(isPromotional)));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employees from database: " + e.getMessage());
        }
        return store_products;
    }

    public static ObservableList<Store_Product> getPromotedStoreProducts(boolean promoted) {
        ObservableList<Store_Product> store_products = FXCollections.observableArrayList();
        Connection connection = dataBaseHandler.getConnection();
        String sql;
        if (promoted) {
            sql = "SELECT store_product.id_product, product.product_name, UPC, selling_price, products_number, promotional_product FROM store_product JOIN product ON store_product.id_product = product.id_product WHERE store_product.promotional_product = 1";
        } else {
            sql = "SELECT store_product.id_product, product.product_name, UPC, selling_price, products_number, promotional_product FROM store_product JOIN product ON store_product.id_product = product.id_product WHERE store_product.promotional_product = 0";
        }
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String id_product = rs.getString("id_product");
                String product_upc = rs.getString("UPC");
                String selling_price = rs.getString("selling_price");
                String products_number = rs.getString("products_number");
                String isPromotional = rs.getString("promotional_product");
                store_products.add(new Store_Product(Integer.parseInt(id_product), product_upc, Double.parseDouble(selling_price), Integer.parseInt(products_number), Integer.parseInt(isPromotional)));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employees from database: " + e.getMessage());
        }
        return store_products;
    }
}
