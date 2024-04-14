package main;

import Entities.Employee;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import services.EmployeeService;
import sessionmanagement.UserInfo;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class EmployeeProfileController implements Initializable {

    @FXML
    public Label errorLabel;
    @FXML
    private Label nameLabel, surnameLabel, patronymicLabel, positionLabel, salaryLabel,
            birthdayLabel, startLabel, phoneLabel, cityLabel, streetLabel, zipcodeLabel;

    @FXML
    private TextField nameField, surnameField, patronymicField, salaryField,
            birthdayField, startField, phoneField, cityField, streetField, zipcodeField;

    @FXML
    private Button editButton, saveButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Employee e = UserInfo.employeeProfile;
        nameLabel.setText(e.getEmpl_name());
        surnameLabel.setText(e.getEmpl_surname());
        patronymicLabel.setText(e.getEmpl_patronymic());
        positionLabel.setText(e.getEmpl_role());
        salaryLabel.setText(e.getSalary());
        birthdayLabel.setText(e.getDate_of_birth().toString());
        startLabel.setText(e.getDate_of_start().toString());
        phoneLabel.setText(e.getPhone_number());
        cityLabel.setText(e.getCity());
        streetLabel.setText(e.getStreet());
        zipcodeLabel.setText(e.getZip_code());

        saveButton.setVisible(false);

        setLabelsVisible(true);
        setTextFieldsVisible(false);
    }

    @FXML
    private void handleEditButtonAction() {
        setLabelsVisible(false);
        setTextFieldsVisible(true);

        nameField.setText(nameLabel.getText());
        surnameField.setText(surnameLabel.getText());
        patronymicField.setText(patronymicLabel.getText());
        salaryField.setText(salaryLabel.getText());
        birthdayField.setText(birthdayLabel.getText());
        startField.setText(startLabel.getText());
        phoneField.setText(phoneLabel.getText());
        cityField.setText(cityLabel.getText());
        streetField.setText(streetLabel.getText());
        zipcodeField.setText(zipcodeLabel.getText());

        editButton.setVisible(false);
        saveButton.setVisible(true);
    }

    @FXML
    private void handleSaveButtonAction() {
        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String patronymic = patronymicField.getText().trim();
        String salary = salaryField.getText().trim();
        String birthday = birthdayField.getText().trim();
        String start = startField.getText().trim();
        String phone = phoneField.getText().trim();
        String city = cityField.getText().trim();
        String street = streetField.getText().trim();
        String zipcode = zipcodeField.getText().trim();

        if (name.equals(nameLabel.getText()) && surname.equals(surnameLabel.getText()) &&
                patronymic.equals(patronymicLabel.getText()) && salary.equals(salaryLabel.getText()) &&
                birthday.equals(birthdayLabel.getText()) && start.equals(startLabel.getText()) &&
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

        Employee upd = new Employee(UserInfo.id, name, surname, patronymic, UserInfo.position, salary, Date.valueOf(birthday), Date.valueOf(start), phone, city, street, zipcode);
        EmployeeService service = new EmployeeService();
        try {
            service.updateEmployee(upd);
        } catch (SQLException e) {
            setErrorMessage(e.getMessage());
            return;
        }
        UserInfo.updateEmployeeProfile();

        setLabelsVisible(true);
        setTextFieldsVisible(false);

        nameLabel.setText(nameField.getText());
        surnameLabel.setText(surnameField.getText());
        patronymicLabel.setText(patronymicField.getText());
        salaryLabel.setText(salaryField.getText());
        birthdayLabel.setText(birthdayField.getText());
        startLabel.setText(startField.getText());
        phoneLabel.setText(phoneField.getText());
        cityLabel.setText(cityField.getText());
        streetLabel.setText(streetField.getText());
        zipcodeLabel.setText(zipcodeField.getText());

        editButton.setVisible(true);
        saveButton.setVisible(false);
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
    }

    private boolean valuesValidation(String name, String surname, String patronymic, String salary, String birthday,
                                     String start, String phone, String city, String street, String zipcode){

        if (name.isEmpty() || surname.isEmpty() ||
                salary.isEmpty() || birthday.isEmpty() || start.isEmpty() ||
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
            LocalDate.parse(birthday);
        } catch (DateTimeParseException e) {
            setErrorMessage("Invalid birthday format");
            return false;
        }

        try {
            LocalDate.parse(start);
        } catch (DateTimeParseException e) {
            setErrorMessage("Invalid start date format");
            return false;
        }

        try {
            Double.parseDouble(salary);
        } catch (NumberFormatException e) {
            setErrorMessage("Invalid salary format");
            return false;
        }

        if (!Pattern.matches("\\d{5}", zipcode)) {
            setErrorMessage("Invalid zipcode format");
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
}
