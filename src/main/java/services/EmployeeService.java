package services;

import Entities.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmployeeService {
    private Connection connection;

    public EmployeeService(){
        connection = new DataBaseHandler().getConnection();
    }

    public void updateEmployee(Employee employee) throws SQLException {
        String sql = "UPDATE employee SET empl_surname=?, empl_name=?, empl_patronymic=?, " +
                "empl_role=?, salary=?, date_of_birth=?, date_of_start=?, phone_number=?, " +
                "city=?, street=?, zip_code=? WHERE id_employee=?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, employee.getEmpl_surname());
            statement.setString(2, employee.getEmpl_name());
            statement.setString(3, employee.getEmpl_patronymic());
            statement.setString(4, employee.getEmpl_role());
            statement.setString(5, employee.getSalary());
            statement.setDate(6, employee.getDate_of_birth());
            statement.setDate(7, employee.getDate_of_start());
            statement.setString(8, employee.getPhone_number());
            statement.setString(9, employee.getCity());
            statement.setString(10, employee.getStreet());
            statement.setString(11, employee.getZip_code());
            statement.setString(12, employee.getId_employee());

            statement.executeUpdate();
        }
    }

}
