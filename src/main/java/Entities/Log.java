package Entities;

import main.MainMenu;

import java.time.LocalDateTime;

public class Log {
    private String id_employee;
    private String employee_name;
    private LocalDateTime log_time;
    private String log_action;
    private String log_message;

    public Log(String id_employee, LocalDateTime log_time, String log_action, String log_message) {
        this.id_employee = id_employee;
        this.log_time = log_time;
        this.log_action = log_action;
        this.log_message = log_message;
    }

    public String getId_employee() {
        return id_employee;
    }

    public void setId_employee(String id_employee) {
        this.id_employee = id_employee;
    }

    public LocalDateTime getLog_time() {
        return log_time;
    }

    public void setLog_time(LocalDateTime log_time) {
        this.log_time = log_time;
    }

    public String getLog_action() {
        return log_action;
    }

    public void setLog_action(String log_action) {
        this.log_action = log_action;
    }

    public String getLog_message() {
        return log_message;
    }

    public void setLog_message(String log_message) {
        this.log_message = log_message;
    }

    public String getEmployee_name() {
        employee_name = MainMenu.employeeDataMapGetName.get(id_employee);
        return employee_name;
    }
}