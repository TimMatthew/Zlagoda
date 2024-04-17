package main;

import Entities.Employee;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.AuthorizationService;
import services.EmployeeService;
import sessionmanagement.UserInfo;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class EmployeeProfile implements Initializable {

    public static Employee currentEmployee;
    private static Stage stage;
    public Button deleteButton;

    protected static FXMLLoader FXML_LOADER(){
        return new FXMLLoader(HelloApplication.class.getResource("EmployeeProfile.fxml"));
    }
    protected static final int WIDTH = 428, HEIGHT = 540;
    @FXML
    public Label errorLabel;
    @FXML
    private Label nameLabel, surnameLabel, patronymicLabel, positionLabel, salaryLabel,
            birthdayLabel, startLabel, phoneLabel, cityLabel, streetLabel, zipcodeLabel, loginLabel, passwordLabel;

    @FXML
    private TextField nameField, surnameField, patronymicField, salaryField,
            phoneField, cityField, streetField, zipcodeField, loginField;

    @FXML
    private DatePicker birthdayField, startField;

    @FXML
    public ChoiceBox<String> positionChoiceBox;

    @FXML
    private Button editButton, saveButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        positionChoiceBox.setItems(FXCollections.observableArrayList("Manager", "Cashier"));

        if (currentEmployee != null) {
            nameLabel.setText(currentEmployee.getEmpl_name());
            surnameLabel.setText(currentEmployee.getEmpl_surname());
            patronymicLabel.setText(currentEmployee.getEmpl_patronymic());
            positionLabel.setText(currentEmployee.getEmpl_role());
            salaryLabel.setText(currentEmployee.getSalary());
            birthdayLabel.setText(currentEmployee.getDate_of_birth().toString());
            startLabel.setText(currentEmployee.getDate_of_start().toString());
            phoneLabel.setText(currentEmployee.getPhone_number());
            cityLabel.setText(currentEmployee.getCity());
            streetLabel.setText(currentEmployee.getStreet());
            zipcodeLabel.setText(currentEmployee.getZip_code());

            positionChoiceBox.setValue(currentEmployee.getEmpl_role());
            positionChoiceBox.setVisible(false);
            deleteButton.setVisible(false);
            loginField.setVisible(false);
            loginLabel.setVisible(false);
            passwordLabel.setVisible(false);

            if (UserInfo.position.equals("Cashier"))
                editButton.setVisible(false);
            saveButton.setVisible(false);

            setLabelsVisible(true);
            setTextFieldsVisible(false);
        } else {
            setLabelsVisible(false);
            setTextFieldsVisible(true);
        }

    }

    @FXML
    private void handleEditButtonAction() {
        setLabelsVisible(false);
        setTextFieldsVisible(true);

        nameField.setText(nameLabel.getText());
        surnameField.setText(surnameLabel.getText());
        patronymicField.setText(patronymicLabel.getText());
        salaryField.setText(salaryLabel.getText());
        birthdayField.setValue(LocalDate.parse(birthdayLabel.getText()));
        startField.setValue(LocalDate.parse(startLabel.getText()));
        phoneField.setText(phoneLabel.getText());
        cityField.setText(cityLabel.getText());
        streetField.setText(streetLabel.getText());
        zipcodeField.setText(zipcodeLabel.getText());
        positionLabel.setText(positionChoiceBox.getValue());

        editButton.setVisible(false);
        saveButton.setVisible(true);
    }

    @FXML
    private void handleSaveButtonAction() throws SQLException {
        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String patronymic = patronymicField.getText().trim();
        String role = positionChoiceBox.getValue();
        String salary = salaryField.getText().trim();
        LocalDate birthday = birthdayField.getValue();
        LocalDate start = startField.getValue();
        String phone = phoneField.getText().trim();
        String city = cityField.getText().trim();
        String street = streetField.getText().trim();
        String zipcode = zipcodeField.getText().trim();

        if (currentEmployee != null && name.equals(nameLabel.getText()) && surname.equals(surnameLabel.getText()) &&
                patronymic.equals(patronymicLabel.getText()) && role.equals(positionLabel.getText()) && salary.equals(salaryLabel.getText()) &&
                birthday.equals(LocalDate.parse(birthdayLabel.getText())) && start.equals(LocalDate.parse(startLabel.getText())) &&
                phone.equals(phoneLabel.getText()) && city.equals(cityLabel.getText()) &&
                street.equals(streetLabel.getText()) && zipcode.equals(zipcodeLabel.getText())) {
            setLabelsVisible(true);
            setTextFieldsVisible(false);
            editButton.setVisible(true);
            saveButton.setVisible(false);
            return;
        }

        if (!valuesValidation(name, surname, patronymic, salary, birthday, start, phone, city, street, zipcode))
            return;

        if (currentEmployee == null){
            Employee newEmployee = new Employee(surname, name, patronymic, role, salary, Date.valueOf(birthday), Date.valueOf(start), phone, city, street, zipcode);
            AuthorizationService as = new AuthorizationService();
            if (!as.signUp(newEmployee, loginField.getText().trim(), "PASSWORD")){
                setErrorMessage("Login is already taken.");
                return;
            }
            stage.close();
        } else {
        Employee upd = new Employee(currentEmployee.getId_employee(), name, surname, patronymic, role, salary, Date.valueOf(birthday), Date.valueOf(start), phone, city, street, zipcode);
        EmployeeService service = new EmployeeService();
        try {
            service.updateEmployee(upd);
        } catch (SQLException e) {
            setErrorMessage(e.getMessage());
            return;
        }

        if (UserInfo.id.equals(currentEmployee.getId_employee()))
            UserInfo.updateEmployeeProfile();
        }

        setLabelsVisible(true);
        setTextFieldsVisible(false);

        nameLabel.setText(nameField.getText());
        surnameLabel.setText(surnameField.getText());
        patronymicLabel.setText(patronymicField.getText());
        positionLabel.setText(positionChoiceBox.getValue());
        salaryLabel.setText(salaryField.getText());
        birthdayLabel.setText(birthdayField.getValue().toString());
        startLabel.setText(startField.getValue().toString());
        phoneLabel.setText(phoneField.getText());
        cityLabel.setText(cityField.getText());
        streetLabel.setText(streetField.getText());
        zipcodeLabel.setText(zipcodeField.getText());

        editButton.setVisible(true);
        saveButton.setVisible(false);
    }

    @FXML
    public void handleDeleteButtonAction(ActionEvent actionEvent) throws SQLException {
        if (currentEmployee != null){
            new EmployeeService().deleteEmployee(currentEmployee.getId_employee());
            currentEmployee = null;
        }
        stage.close();
    }
    private void setLabelsVisible(boolean visible) {
        nameLabel.setVisible(visible);
        surnameLabel.setVisible(visible);
        patronymicLabel.setVisible(visible);
        salaryLabel.setVisible(visible);
        birthdayLabel.setVisible(visible);
        startLabel.setVisible(visible);
        phoneLabel.setVisible(visible);
        cityLabel.setVisible(visible);
        streetLabel.setVisible(visible);
        zipcodeLabel.setVisible(visible);
    }

    private void setTextFieldsVisible(boolean visible) {
        nameField.setVisible(visible);
        surnameField.setVisible(visible);
        patronymicField.setVisible(visible);
        salaryField.setVisible(visible);
        birthdayField.setVisible(visible);
        startField.setVisible(visible);
        phoneField.setVisible(visible);
        cityField.setVisible(visible);
        streetField.setVisible(visible);
        zipcodeField.setVisible(visible);

        if (currentEmployee != null && !UserInfo.id.equals(currentEmployee.getId_employee())) {
            positionChoiceBox.setVisible(visible);
            deleteButton.setVisible(visible);
        }
    }

    private boolean valuesValidation(String name, String surname, String patronymic, String salary, LocalDate birthday,
                                     LocalDate start, String phone, String city, String street, String zipcode) throws SQLException {

        if (name.isEmpty() || surname.isEmpty() || salary.isEmpty() ||
                phone.isEmpty() || city.isEmpty() || street.isEmpty() ||
                zipcode.isEmpty()) {
            setErrorMessage("All fields are required");
            return false;
        }

        if (!Pattern.matches("\\+380\\d{9}", phone)) {
            setErrorMessage("Invalid phone number");
            return false;
        }

        try {
            double d_salary = Double.parseDouble(salary);
            if (d_salary <= 0)
                throw new NumberFormatException();
        } catch (NumberFormatException e) {
            setErrorMessage("Invalid salary format");
            return false;
        }

        if (!Pattern.matches("\\d{5}", zipcode)) {
            setErrorMessage("Invalid zipcode format");
            return false;
        }

        if ((currentEmployee == null || !phone.equals(currentEmployee.getPhone_number())) && new AuthorizationService().isEmployeeExist(phone)){
            setErrorMessage("The phone number is already taken.");
            return false;
        }

        LocalDate currentDate = LocalDate.now();
        if (birthday.isAfter(currentDate.minusYears(18))) {
            setErrorMessage("The person must be at least 18 years old");
            return false;
        }

        if (start.isAfter(currentDate)) {
            setErrorMessage("Start date can't be in the future");
            return false;
        }

        if (birthday.isBefore(currentDate.minusYears(100))) {
            setErrorMessage("The person can't be over 100 years old");
            return false;
        }

        if (name.length() > 50 || surname.length() > 50 || patronymic.length() > 50 ||
                city.length() > 50 || street.length() > 50 || zipcode.length() > 12) {
            setErrorMessage("The maximum field length has been exceeded.");
            return false;
        }
        errorLabel.setVisible(false);
        return true;
    }

    private void setErrorMessage(String msg){
        errorLabel.setVisible(true);
        errorLabel.setText(msg);

    }

    public static void initProfile(Employee empl, String title){
        currentEmployee = empl;
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(HelloApplication.mainStage);
        try {
            HelloApplication.setScene(stage, EmployeeProfile.FXML_LOADER(), EmployeeProfile.WIDTH, EmployeeProfile.HEIGHT, title);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
