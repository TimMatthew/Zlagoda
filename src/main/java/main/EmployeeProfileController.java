package main;

import Entities.Employee;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import sessionmanagement.UserInfo;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeProfileController implements Initializable {

    public Label nameLabel;
    public Label surnameLabel;
    public Label patronymicLabel;
    public Label positionLabel;
    public Label salaryLabel;
    public Label birthdayLabel;
    public Label startLabel;
    public Label phoneLabel;
    public Label cityLabel;
    public Label streetLabel;
    public Label zipcodeLabel;

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
    }
}
