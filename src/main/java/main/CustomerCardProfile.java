package main;

import Entities.Customer_Card;
import Entities.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.AuthorizationService;
import services.CustomerCardService;
import services.EmployeeService;
import sessionmanagement.UserInfo;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class CustomerCardProfile implements Initializable {
    public static Customer_Card currentCustomer;
    private static Stage stage;

    protected static FXMLLoader FXML_LOADER(){
        return new FXMLLoader(CustomerCardProfile.class.getResource("CustomerCardProfile.fxml"));
    }
    protected static final int WIDTH = 428, HEIGHT = 480;

    @FXML
    public Label errorLabel;
    @FXML
    private Label nameLabel, surnameLabel, patronymicLabel, phoneLabel, cityLabel, streetLabel, zipcodeLabel, percentLabel;
    @FXML
    private TextField nameField, surnameField, patronymicField, phoneField, cityField, streetField, zipcodeField, percentField;
    @FXML
    private Button editButton, saveButton, deleteButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (currentCustomer != null) {
            nameLabel.setText(currentCustomer.getCust_name());
            surnameLabel.setText(currentCustomer.getCust_surname());
            patronymicLabel.setText(currentCustomer.getCust_patronymic());
            phoneLabel.setText(currentCustomer.getPhone_number());
            cityLabel.setText(currentCustomer.getCity());
            streetLabel.setText(currentCustomer.getStreet());
            zipcodeLabel.setText(currentCustomer.getZip_code());
            percentLabel.setText(currentCustomer.getPercent());

            if (UserInfo.position.equals("Cashier"))
                editButton.setVisible(false);
            saveButton.setVisible(false);

            setLabelsVisible(true);
            setTextFieldsVisible(false);
        } else {
            editButton.setVisible(false);
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
        phoneField.setText(phoneLabel.getText());
        cityField.setText(cityLabel.getText());
        streetField.setText(streetLabel.getText());
        zipcodeField.setText(zipcodeLabel.getText());
        percentField.setText(percentLabel.getText());

        editButton.setVisible(false);
        deleteButton.setVisible(true);
        saveButton.setVisible(true);
    }

    @FXML
    private void handleSaveButtonAction() throws SQLException {
        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String patronymic = patronymicField.getText().trim();
        String phone = phoneField.getText().trim();
        String city = cityField.getText().trim();
        String street = streetField.getText().trim();
        String zipcode = zipcodeField.getText().trim();
        String percent = percentField.getText().trim();

        if (currentCustomer != null && name.equals(nameLabel.getText()) && surname.equals(surnameLabel.getText()) &&
                patronymic.equals(patronymicLabel.getText()) && phone.equals(phoneLabel.getText()) && city.equals(cityLabel.getText()) &&
                 street.equals(streetLabel.getText()) && zipcode.equals(zipcodeLabel.getText()) && percent.equals(percentLabel.getText())) {
            setLabelsVisible(true);
            setTextFieldsVisible(false);
            editButton.setVisible(true);
            saveButton.setVisible(false);
            deleteButton.setVisible(true);
            return;
        }

        if (!valuesValidation(name, surname, patronymic, phone, city, street, zipcode, percent))
            return;

        CustomerCardService ccs = new CustomerCardService();
        if (currentCustomer == null){
            Customer_Card newCard = new Customer_Card(surname, name, patronymic, phone, city, street, zipcode, percent);
            if (!ccs.addCustomerCard(newCard)){
                setErrorMessage("Unexpected error was occurred.");
                return;
            }
            MainMenu.customerCardData.add(newCard);
            stage.close();
        } else {
            Customer_Card upd = new Customer_Card(currentCustomer.getCard_number(), surname, name, patronymic, phone, city, street, zipcode, percent);
            try {
                ccs.updateCustomerCard(upd);
                int index = MainMenu.customerCardData.indexOf(currentCustomer);
                MainMenu.customerCardData.add(index, upd);
                MainMenu.customerCardData.remove(currentCustomer);
            } catch (SQLException e) {
                setErrorMessage(e.getMessage());
                return;
            }
        }

        setLabelsVisible(true);
        setTextFieldsVisible(false);

        nameLabel.setText(nameField.getText());
        surnameLabel.setText(surnameField.getText());
        patronymicLabel.setText(patronymicField.getText());
        phoneLabel.setText(phoneField.getText());
        cityLabel.setText(cityField.getText());
        streetLabel.setText(streetField.getText());
        zipcodeLabel.setText(zipcodeField.getText());
        percentLabel.setText(percentField.getText());


        editButton.setVisible(true);
        saveButton.setVisible(false);
        deleteButton.setVisible(false);
    }

    @FXML
    public void handleDeleteButtonAction(ActionEvent actionEvent) throws SQLException {
        if (currentCustomer != null){
            new CustomerCardService().deleteCustomerCard(currentCustomer.getCard_number());
            MainMenu.customerCardData.remove(currentCustomer);
            currentCustomer = null;
        }
        stage.close();
    }

    private void setLabelsVisible(boolean visible) {
        nameLabel.setVisible(visible);
        surnameLabel.setVisible(visible);
        patronymicLabel.setVisible(visible);
        phoneLabel.setVisible(visible);
        cityLabel.setVisible(visible);
        streetLabel.setVisible(visible);
        zipcodeLabel.setVisible(visible);
        percentLabel.setVisible(visible);
    }

    private void setTextFieldsVisible(boolean visible) {
        nameField.setVisible(visible);
        surnameField.setVisible(visible);
        patronymicField.setVisible(visible);
        phoneField.setVisible(visible);
        cityField.setVisible(visible);
        streetField.setVisible(visible);
        zipcodeField.setVisible(visible);
        percentField.setVisible(visible);
    }

    private boolean valuesValidation(String name, String surname, String patronymic, String phone, String city, String street, String zipcode, String percent) throws SQLException {

        if (name.isEmpty() || surname.isEmpty() || phone.isEmpty() || percent.isEmpty()) {
            setErrorMessage("All fields are required");
            return false;
        }

        if (!(Pattern.matches("\\+380\\d{5}", phone) || Pattern.matches("\\+380\\d{9}", phone))) {
            setErrorMessage("Invalid phone number");
            return false;
        }

        if (!zipcode.isEmpty() && !Pattern.matches("\\d{9}", zipcode)) {
            setErrorMessage("Invalid zipcode format");
            return false;
        }

        double d_percent = Double.parseDouble(percent);
        if (d_percent < 0 || d_percent > 100) {
            setErrorMessage("Invalid discount format");
            return false;
        }

        if ((currentCustomer == null || !phone.equals(currentCustomer.getPhone_number())) && new CustomerCardService().isCustomerCardExist(phone)){
            setErrorMessage("The phone number is already taken.");
            return false;
        }

        if (name.length() > 50 || surname.length() > 50 || patronymic.length() > 50 ||
                city.length() > 50 || street.length() > 50 || zipcode.length() > 9) {
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

    public static void initProfile(Customer_Card customerCard, String title){
        currentCustomer = customerCard;
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(HelloApplication.mainStage);
        try {
            HelloApplication.setScene(stage, FXML_LOADER(), WIDTH, HEIGHT, title);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
