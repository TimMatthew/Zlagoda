package services;

import Entities.Sale;
import utils.LogAction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SaleService {
    private final Connection connection;
    public SaleService(){
        connection = new DataBaseHandler().getConnection();
    }

    public boolean addSale(Sale sale) throws SQLException {
        if (isSaleExist(sale.getStore_Product_UPC(), sale.getCheck_check_number()))
            return false;

        String sql = "INSERT INTO sale (UPC, check_number, product_number, selling_price) " +
                "VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, sale.getStore_Product_UPC());
            statement.setString(2, sale.getCheck_check_number());
            statement.setInt(3, sale.getProduct_number());
            statement.setDouble(4, sale.getSelling_price());

            statement.executeUpdate();
        }

        new LogService().addLog(LogAction.ADD_SALE, LogService.getLogMessage("added the new sale: " + sale));

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

    private boolean isSaleExist(String UPC, String checkNumber) throws SQLException {
        String sql = "SELECT * FROM sale WHERE UPC = ? AND check_number = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, UPC);
        statement.setString(2, checkNumber);

        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }
}
