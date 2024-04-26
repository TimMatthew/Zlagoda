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

public class StoreProductService {
    private final Connection connection;

    public StoreProductService(){
        connection = new DataBaseHandler().getConnection();
    }

    public boolean addStoreProduct(Store_Product product) throws SQLException {
        if (isStoreProductExist(product.getProduct_id_product(), product.isPromotional_product()))
            return false;

        String sql = "INSERT INTO store_product (upc, upc_prom, id_product, selling_price, products_number, promotional_product) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, product.getStore_Product_UPC());
            statement.setString(2, product.getSale_UPC_prom());
            statement.setInt(3, product.getProduct_id_product());
            statement.setDouble(4, product.getSelling_price());
            statement.setInt(5, product.getProducts_number());
            statement.setBoolean(6, product.isPromotional_product());

            statement.executeUpdate();
        }
        new LogService().addLog(LogAction.ADD_PRODUCT_TO_STORE, LogService.getLogMessage("addded to store: " + product));

        return true;
    }

    public void updateStoreProduct(Store_Product storeProduct){
        String sql = "UPDATE store_product SET upc_prom=?, id_product=?, selling_price=?, products_number=?, promotional_product=? WHERE UPC=?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, storeProduct.getSale_UPC_prom());
            statement.setInt(2, storeProduct.getProduct_id_product());
            statement.setDouble(3, storeProduct.getSelling_price());
            statement.setInt(4, storeProduct.getProducts_number());
            statement.setBoolean(5, storeProduct.isPromotional_product());
            statement.setString(6, storeProduct.getStore_Product_UPC());

            statement.executeUpdate();
            new LogService().addLog(LogAction.UPDATE_PRODUCT_IN_STORE, LogService.getLogMessage("updated the product in store: " + storeProduct));

        } catch (SQLException e) {
            System.err.println("Error fetching employees from database: " + e.getMessage());
        }
    }
    public ObservableList<Store_Product> getAllStoreProducts() {
        ObservableList<Store_Product> storeProducts = FXCollections.observableArrayList();
        String sql = "SELECT * FROM store_product ";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String upc = rs.getString("UPC");
                String upc_prom = rs.getString("upc_prom");
                int id_product = rs.getInt("id_product");
                double selling_price = rs.getDouble("selling_price");
                int products_number = rs.getInt("products_number");
                boolean promotional_product = rs.getBoolean("promotional_product");

                storeProducts.add(new Store_Product(upc, upc_prom, id_product, selling_price, products_number, promotional_product));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employees from database: " + e.getMessage());
        }
        return storeProducts;
    }
    public void deleteStoreProduct(String upc) throws SQLException {
        String sql = "DELETE FROM store_product WHERE  UPC= ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, upc);
            new LogService().addLog(LogAction.DELETE_PRODUCT_IN_STORE, LogService.getLogMessage("deleted the product in store: " + getStoreProduct(upc)));
            statement.executeUpdate();
        }
    }

    public boolean isStoreProductExist(int id_product, boolean promotionalProduct) throws SQLException {
        String sql = "SELECT * FROM store_product WHERE id_product =? AND promotional_product =?";

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id_product);
            statement.setBoolean(2, promotionalProduct);

            try(ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
    public Store_Product getStoreProduct(String upc) {
        String sql = "SELECT * FROM store_product WHERE UPC =?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, upc);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()){
                    String upc2 = rs.getString("UPC");
                    String upc_prom = rs.getString("upc_prom");
                    int id_product = rs.getInt("id_product");
                    double selling_price = rs.getDouble("selling_price");
                    int products_number = rs.getInt("products_number");
                    boolean promotional_product = rs.getBoolean("promotional_product");
                    return new Store_Product(upc2, upc_prom, id_product, selling_price, products_number, promotional_product);
                }
                else
                    return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public Store_Product getStoreProductByProductId(int id_product) {
        String sql = "SELECT * FROM store_product WHERE id_product =? AND promotional_product =?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id_product);
            statement.setBoolean(2, false);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()){
                    String upc2 = rs.getString("UPC");
                    String upc_prom = rs.getString("upc_prom");
                    double selling_price = rs.getDouble("selling_price");
                    int products_number = rs.getInt("products_number");
                    boolean promotional_product = rs.getBoolean("promotional_product");
                    return new Store_Product(upc2, upc_prom, id_product, selling_price, products_number, promotional_product);
                }
                else
                    return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public boolean changeProductsNumberBy(String upc, int n) throws SQLException {
        Store_Product storeProduct = getStoreProduct(upc);
        if (storeProduct != null) {
            int currentProductsNumber = storeProduct.getProducts_number();
            int newProductsNumber = currentProductsNumber + n;

            String sql = "UPDATE store_product SET products_number = ? WHERE UPC = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, newProductsNumber);
                statement.setString(2, upc);
                statement.executeUpdate();
                return true;
            }
        }
        return false;
    }

    public boolean isUniqueUPC(String upc){
        String sql = "SELECT * FROM store_product WHERE UPC =?";

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, upc);

            try(ResultSet resultSet = statement.executeQuery()) {
                return !resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public ObservableList<Store_Product> getStoreProductsByPropertyStartsWith(String property, String category, String startsWith) {
        ObservableList<Store_Product> storeProducts = FXCollections.observableArrayList();
        String sql = "SELECT * FROM store_product WHERE " + property + " LIKE ?";
        if (property.equals("product_name"))
            sql = "SELECT * FROM store_product JOIN product p on store_product.id_product = p.id_product WHERE p.product_name LIKE ?";
        if (!category.equals("All categories"))
            sql = sql + " AND p.category_number =?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, startsWith + "%");
            if (!category.equals("All categories"))
                pst.setInt(2, new CategoryService().getCategoryID(category));
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String upc = rs.getString("UPC");
                String upc_prom = rs.getString("upc_prom");
                int id_product = rs.getInt("id_product");
                double selling_price = rs.getDouble("selling_price");
                int products_number = rs.getInt("products_number");
                boolean promotional_product = rs.getBoolean("promotional_product");

                storeProducts.add(new Store_Product(upc, upc_prom, id_product, selling_price, products_number, promotional_product));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employees from database: " + e.getMessage());
        }
        return storeProducts;
    }

    public double promotionalPrice(double price){
        return price * 0.8;
    }

}
