package services;

import Entities.Log;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sessionmanagement.UserInfo;
import utils.LogAction;

import java.sql.*;
import java.time.LocalDateTime;

public class LogService {
    private final Connection connection;

    public LogService(){
        connection = new DataBaseHandler().getConnection();
    }

    public static String getLogMessage(String actionDescription){
        return String.format("%s %s", UserInfo.employeeProfile.getFullName(), actionDescription);
    }

    public boolean addLog(LogAction logAction, String logMessage) {
        String sql = "INSERT INTO logs (id_employee, log_time, log_action, log_message) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, UserInfo.id);
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            statement.setString(3, String.valueOf(logAction));
            statement.setString(4, logMessage);
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ObservableList<Log> getAllLogs() {
        return getLogs("SELECT * FROM logs");
    }

    public ObservableList<Log> getLogsByAction(LogAction action) {
        return getLogs("SELECT * FROM logs WHERE log_action = ?", String.valueOf(action));
    }

    public ObservableList<Log> getLogsByEmployee(String employeeId) {
        return getLogs("SELECT * FROM logs WHERE id_employee = ?", employeeId);
    }

    public ObservableList<Log> getLogsByActionAndEmployee(LogAction action, String employeeId) {
        return getLogs("SELECT * FROM logs WHERE log_action = ? AND id_employee = ?", String.valueOf(action), employeeId);
    }
    private ObservableList<Log> getLogs(String sql, String... params) {
        ObservableList<Log> logs = FXCollections.observableArrayList();
        try (PreparedStatement statement = connection.prepareStatement(sql + " ORDER BY log_time DESC")) {
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                logs.add(new Log(resultSet.getString("id_employee"), resultSet.getTimestamp("log_time").toLocalDateTime(),resultSet.getString("log_action"), resultSet.getString("log_message")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }
}

