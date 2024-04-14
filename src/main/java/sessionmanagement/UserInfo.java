package sessionmanagement;

import Entities.Employee;
import services.DataBaseHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserInfo {
    public static String id;
    public static String position;
    public static Employee employeeProfile;

    public static void updateEmployeeProfile(){
        DataBaseHandler dbh = new DataBaseHandler();
        String sql = "SELECT * FROM employee WHERE id_employee =? ";
        try(PreparedStatement statement = dbh.getConnection().prepareStatement(sql)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                employeeProfile = new Employee(resultSet.getString("id_employee"),
                        resultSet.getString("empl_surname"),
                        resultSet.getString("empl_name"),
                        resultSet.getString("empl_patronymic"),
                        resultSet.getString("empl_role"),
                        resultSet.getString("salary"),
                        resultSet.getDate("date_of_birth"),
                        resultSet.getDate("date_of_start"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("city"),
                        resultSet.getString("street"),
                        resultSet.getString("zip_code"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
