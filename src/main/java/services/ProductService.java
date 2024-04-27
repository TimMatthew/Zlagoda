package services;

import Entities.Product;
import Entities.Store_Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.LogAction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        new LogService().addLog(LogAction.ADD_PRODUCT, LogService.getLogMessage("added the new product: " + product));
        return true;
    }

    public void updateProduct(Product product) throws SQLException {
        String sql = "UPDATE product SET category_number=?, product_name=?, characteristics=? WHERE id_product=?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, product.getCategory_number());
            statement.setString(2, product.getProduct_name());
            statement.setString(3, product.getCharacteristics());
            statement.setInt(4, product.getId_product());

            statement.executeUpdate();
            new LogService().addLog(LogAction.UPDATE_PRODUCT, LogService.getLogMessage("updated the product: " + product));
        }
    }
    public ObservableList<Product> getAllProducts() {
        ObservableList<Product> products = FXCollections.observableArrayList();
        String sql = "SELECT * FROM product GROUP BY id_product";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id_product");
                int category_id = rs.getInt("category_number");
                String name = rs.getString("product_name");
                String characteristics = rs.getString("characteristics");

                products.add(new Product(id, category_id, name, characteristics));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employees from database: " + e.getMessage());
        }
        return products;
    }
    public void deleteProduct(int id) throws SQLException {
        String sql = "DELETE FROM product WHERE  id_product= ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            new LogService().addLog(LogAction.DELETE_PRODUCT, LogService.getLogMessage("deleted the product: " + getProduct(id)));
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
    public String getProductName(int id) {
        String sql = "SELECT product_name FROM product WHERE id_product = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return resultSet.getString("product_name");
                else
                    return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Product getProduct(int id) {
        String sql = "SELECT * FROM product WHERE id_product = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("product_name");
                    String characteristics = rs.getString("characteristics");
                    int category_id = rs.getInt("category_number");

                    return new Product(id, category_id, name, characteristics);
                }
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
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


    public ObservableList<Store_Product> getStoreProducts() {
        ObservableList<Store_Product> store_products = FXCollections.observableArrayList();
        String sql = "SELECT store_product.id_product, product_name, UPC, selling_price, products_number, promotional_product FROM store_product JOIN product ON store_product.id_product = product.id_product ORDER BY selling_price DESC";
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

    public ObservableList<Store_Product> getPromotedStoreProducts(boolean promoted) {
        ObservableList<Store_Product> store_products = FXCollections.observableArrayList();
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


    public ObservableList<Product> getProductsByPropertyStartsWith(String property, String category, String startsWith) {
        ObservableList<Product> products = FXCollections.observableArrayList();
        String sql = "SELECT * FROM product WHERE " + property + " LIKE ?";
        if (!category.equals("All categories"))
            sql = sql + " AND category_number =? ";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, startsWith + "%");
            if (!category.equals("All categories"))
                pst.setInt(2, new CategoryService().getCategoryID(category));
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id_product");
                int category_id = rs.getInt("category_number");
                String name = rs.getString("product_name");
                String characteristics = rs.getString("characteristics");

                products.add(new Product(id, category_id, name, characteristics));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employees from database: " + e.getMessage());
        }
        return products;
    }

    public Map<String, Double> getProductSellsByPerson() {
        Map<String, Double> sells = new HashMap<>();
        String sql = "SELECT e.empl_surname || ' ' || e.empl_name AS employee_name, SUM(c.sum_total) AS total_amount FROM employee e JOIN check_t c ON e.id_employee = c.id_employee GROUP BY e.empl_surname, e.empl_name ORDER BY total_amount DESC";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String employee_name = rs.getString("employee_name");
                String total_amount = rs.getString("total_amount");
                sells.put(employee_name, Double.parseDouble(total_amount));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employees from database: " + e.getMessage());
        }
        return sells;
    }

    public Map<String, Double> getCustomMethod1() {
        Map<String, Double> worker = new HashMap<>();
        String sql = "SELECT e.empl_surname || ' ' || e.empl_name AS employee_name, SUM(c.sum_total) AS total_amount FROM employee e JOIN check_t c ON e.id_employee = c.id_employee WHERE c.print_date >= DATE_SUB(NOW(), INTERVAL 0 DAY) GROUP BY e.empl_surname, e.empl_name ORDER BY total_amount DESC LIMIT 1";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String employee_name = rs.getString("employee_name");
                String total_amount = rs.getString("total_amount");
                worker.put(employee_name, Double.parseDouble(total_amount));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employees from database: " + e.getMessage());
        }
        return worker;
    }

    public Map<String, String> getCustomMethod2() {
        Map<String, String> products = new HashMap<>();
        String sql = "SELECT c.check_number, c.print_date, c.sum_total FROM check_t c WHERE NOT EXISTS ( SELECT 1 FROM sale s JOIN store_product sp ON s.UPC = sp.UPC JOIN product p ON sp.id_product = p.id_product WHERE s.check_number = c.check_number AND p.id_product IN (10, 20, 30)) AND c.card_number NOT IN (SELECT card_number FROM customer_card WHERE percent > 10)";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String check_number = rs.getString("check_number");
                String print_date = rs.getString("print_date");
                products.put(check_number, print_date);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employees from database: " + e.getMessage());
        }
        return products;
    }

    public  Map<String, List<String>> getCustomMethod3() {
        Map<String, List<String>> products = new HashMap<>();
        String sql = "SELECT p.id_product, p.product_name, p.characteristics FROM product p WHERE NOT EXISTS (SELECT 1 FROM store_product sp JOIN sale s ON sp.UPC = s.UPC WHERE sp.id_product = p.id_product) AND p.category_number NOT IN (SELECT category_number FROM category WHERE category_number > :category_param)";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String id_product = rs.getString("id_product");
                String product_name = rs.getString("product_name");
                String characteristics = rs.getString("characteristics");
                List<String> list = new ArrayList<>();
                list.add(product_name);
                list.add(characteristics);
                products.put(id_product, list);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employees from database: " + e.getMessage());
        }
        return products;
    }

    public void getCustomMethod4() {

    }

    public void getCustomMethod5() {

    }

    public void getCustomMethod6() {

    }
}
