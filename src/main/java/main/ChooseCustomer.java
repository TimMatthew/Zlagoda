package main;

import Entities.Customer_Card;
import Entities.Sale;
import Entities.Store_Product;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.CustomerCardService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ChooseCustomer implements Initializable {
    static Stage stage;
    public Button nextButton;
    public ChoiceBox<Customer_Card> customerChoiceBox;
    public ObservableList<Customer_Card> custData;
    public TextField phoneField;
    public Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            custData = new CustomerCardService().getAllCustomers();
            customerChoiceBox.setItems(custData);
            customerChoiceBox.setValue(custData.get(0));


    }
    public static void initWindow(Stage owner){
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(owner);
        stage.setResizable(false);
        try {
            HelloApplication.setScene(stage, new FXMLLoader(ChooseCustomer.class.getResource("ChooseCustomer.fxml")), 400, 300, "Choose customer card");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void noCard(ActionEvent actionEvent) throws SQLException {
        CheckMenu.customerCard = null;
        CheckMenu.createCheck();
    }

    public void chooseCard(ActionEvent actionEvent) throws SQLException {
        String query = phoneField.getText().trim();
        if (!Pattern.matches("\\+380\\d{9}", query)){
            errorLabel.setText("Invalid number");
            return;
        }
        ObservableList<Customer_Card> card = new CustomerCardService().getCustomersByPropertyStartsWith("phone_number", query);
        if (card.size() == 0){
            errorLabel.setText("No customers was found by the phone number.");
            return;
        }
        CheckMenu.customerCard = card.get(0);
        CheckMenu.createCheck();
    }
}
