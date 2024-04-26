package services;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JasperService {
    private final Connection connection;

    public JasperService(){
        connection = new DataBaseHandler().getConnection();
    }

    public InputStream getReport(String name){
        String sql = "SELECT report_jasper FROM reports_templates WHERE  report_name =?";
        InputStream in = null;

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                in = rs.getBinaryStream("report_jasper");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return in;
    }
}
