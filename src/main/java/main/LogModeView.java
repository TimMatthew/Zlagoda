package main;

import Entities.Log;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import services.DataBaseHandler;
import services.JasperService;
import services.LogService;
import sessionmanagement.UserInfo;
import utils.DataPrinter;
import utils.LogAction;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

public class LogModeView implements Initializable {

    @FXML
    public Label employeePIB;
    @FXML
    public Button exitAccountButton, profileButton, printReportButton, backButton;
    @FXML
    public ChoiceBox<String> actionChoiceBox, employeeChoiceBox;
    @FXML
    public TableView<Log> dataTable;
    public ObservableList<Log> data;

    private static final String defaultActionOption = "All actions";
    private static final String defaultEmployeeOption = "All employees";
    private static final LogService logService = new LogService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        employeePIB.setText(UserInfo.employeeProfile.getFullName());

        TableColumn<Log, String> employee = new TableColumn<>("Employee");
        TableColumn<Log, String> action = new TableColumn<>("Action");
        TableColumn<Log, LocalDateTime> dateTime = new TableColumn<>("Time");
        TableColumn<Log, String> message = new TableColumn<>("Message");

        dataTable.getColumns().addAll(employee, action, dateTime, message);

        employee.setCellValueFactory(new PropertyValueFactory<>("employee_name"));
        action.setCellValueFactory(new PropertyValueFactory<>("log_action"));
        dateTime.setCellValueFactory(new PropertyValueFactory<>("log_time"));
        message.setCellValueFactory(new PropertyValueFactory<>("log_message"));

        ObservableList<String> actionList = FXCollections.observableArrayList(defaultActionOption);
        Arrays.stream(LogAction.values())
                .map(String::valueOf)
                .sorted()
                .forEach(actionList::add);

        actionChoiceBox.setItems(actionList);
        actionChoiceBox.setValue(defaultActionOption);

        ObservableList<String> employeeList = FXCollections.observableArrayList(defaultEmployeeOption);
        MainMenu.employeeDataMapGetID.keySet().stream()
                .sorted()
                .forEach(employeeList::add);
        employeeChoiceBox.setItems(employeeList);
        employeeChoiceBox.setValue(defaultEmployeeOption);

        data = FXCollections.observableArrayList();

        refreshTable(null);
        dataTable.setItems(data);
    }
    @FXML
    public void refreshTable(MouseEvent mouseEvent) {
        data.clear();
        if (employeeChoiceBox.getValue().equals(defaultEmployeeOption) && actionChoiceBox.getValue().equals(defaultActionOption))
            data.addAll(logService.getAllLogs());
        else if (employeeChoiceBox.getValue().equals(defaultEmployeeOption))
            data.addAll(logService.getLogsByAction(LogAction.valueOf(actionChoiceBox.getValue())));
        else if (actionChoiceBox.getValue().equals(defaultActionOption))
            data.addAll(logService.getLogsByEmployee(MainMenu.employeeDataMapGetID.get(employeeChoiceBox.getValue())));
        else
            data.addAll(logService.getLogsByActionAndEmployee(LogAction.valueOf(actionChoiceBox.getValue()), MainMenu.employeeDataMapGetID.get(employeeChoiceBox.getValue())));
    }


    @FXML
    public void printReport(ActionEvent actionEvent) {
        DataPrinter.createReport(new DataBaseHandler().getConnection(), new HashMap<>(), new JasperService().getReport("log_report"));
        DataPrinter.showReport();
    }
    @FXML
    public void quit(ActionEvent actionEvent) throws IOException {
        UserInfo.id = null;
        UserInfo.position = null;
        UserInfo.employeeProfile = null;
        HelloApplication.setScene(HelloApplication.mainStage, Authorization.FXML_LOADER(), Authorization.WIDTH, Authorization.HEIGHT, "Ласкаво просимо в ZLAGODA!");
    }

    @FXML
    protected void openProfile(ActionEvent e) {
        EmployeeProfile.initProfile(UserInfo.employeeProfile, "Manager profile");
    }

    @FXML
    public void backToMainMenu(ActionEvent actionEvent) throws IOException {
        new MainMenu().initManager(HelloApplication.mainStage);
    }

}
