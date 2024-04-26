package services;

import Entities.Employee;
import Entities.Store_Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.LogAction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeService {
    private final Connection connection;

    public EmployeeService(){
        connection = new DataBaseHandler().getConnection();
    }

    public void updateEmployee(Employee employee) throws SQLException {
        String sql = "UPDATE zlagoda_schema.employee SET empl_surname=?, empl_name=?, empl_patronymic=?, " +
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
            new LogService().addLog(LogAction.UPDATE_EMPLOYEE, LogService.getLogMessage("updated the employee: " + employee));
        }
    }
    public ObservableList<Employee> getAllEmployees() {
        ObservableList<Employee> employees = FXCollections.observableArrayList();
        String sql = "SELECT * FROM employee ORDER BY empl_surname";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id_employee");
                String surname = rs.getString("empl_surname");
                String name = rs.getString("empl_name");
                String patronymic = rs.getString("empl_patronymic");
                String role = rs.getString("empl_role");
                String salary = rs.getString("salary");
                java.sql.Date dateOfBirth = rs.getDate("date_of_birth");
                java.sql.Date dateOfStart = rs.getDate("date_of_start");
                String phoneNumber = rs.getString("phone_number");
                String city = rs.getString("city");
                String street = rs.getString("street");
                String zipCode = rs.getString("zip_code");

                employees.add(new Employee(id, surname, name, patronymic, role, salary, dateOfBirth, dateOfStart, phoneNumber, city, street, zipCode));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employees from database: " + e.getMessage());
        }
        return employees;
    }

    public Employee getEmployee(String id) {
        String sql = "SELECT * FROM employee WHERE id_employee =?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()){
                    String surname = rs.getString("empl_surname");
                    String name = rs.getString("empl_name");
                    String patronymic = rs.getString("empl_patronymic");
                    String role = rs.getString("empl_role");
                    String salary = rs.getString("salary");
                    java.sql.Date dateOfBirth = rs.getDate("date_of_birth");
                    java.sql.Date dateOfStart = rs.getDate("date_of_start");
                    String phoneNumber = rs.getString("phone_number");
                    String city = rs.getString("city");
                    String street = rs.getString("street");
                    String zipCode = rs.getString("zip_code");

                    return new Employee(id, surname, name, patronymic, role, salary, dateOfBirth, dateOfStart, phoneNumber, city, street, zipCode);
                }
                else
                    return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteEmployee(String employeeId) throws SQLException {
        String sql = "DELETE FROM employee WHERE id_employee = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, employeeId);
            new LogService().addLog(LogAction.UPDATE_EMPLOYEE, LogService.getLogMessage("deleted the employee: " + getEmployee(employeeId)));
            statement.executeUpdate();
        }
    }

    public ObservableList<Employee> getEmployeesByPropertyStartsWith(String property, String startsWith) {
        ObservableList<Employee> employees = FXCollections.observableArrayList();
        String sql = "SELECT * FROM employee WHERE " + property + " LIKE ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, startsWith + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id_employee");
                String surname = rs.getString("empl_surname");
                String name = rs.getString("empl_name");
                String patronymic = rs.getString("empl_patronymic");
                String role = rs.getString("empl_role");
                String salary = rs.getString("salary");
                java.sql.Date dateOfBirth = rs.getDate("date_of_birth");
                java.sql.Date dateOfStart = rs.getDate("date_of_start");
                String phoneNumber = rs.getString("phone_number");
                String city = rs.getString("city");
                String street = rs.getString("street");
                String zipCode = rs.getString("zip_code");

                employees.add(new Employee(id, surname, name, patronymic, role, salary, dateOfBirth, dateOfStart, phoneNumber, city, street, zipCode));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }
}
