package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ManagerMainMenu {

    @FXML
    private RadioButton employeesModeRadio, manClientsModeRadio, categoryModeRadio, manGoodsMode, storeGoodsMode;
    @FXML
    private AnchorPane functionsPane;

    protected static final FXMLLoader MANAGER_MENU_FXML_LOADER = new FXMLLoader(HelloApplication.class.getResource("ManagerMainMenu.fxml"));
    protected static final int WIDTH = 1130;
    protected static final int HEIGHT = 770;

    //Кнопки для працівників
    private Button addEmployeeButton, sortBySurnameButton;

    private void initEmployees(){
        addEmployeeButton = new Button("Add employee");
        addEmployeeButton.setLayoutX(30); addEmployeeButton.setLayoutY(20);
        addEmployeeButton.setPrefWidth(100); addEmployeeButton.setPrefHeight(25);

        sortBySurnameButton = new Button("Sort employees by surname");
        sortBySurnameButton.setLayoutX(30); sortBySurnameButton.setLayoutY(60);
        sortBySurnameButton.setPrefWidth(120); sortBySurnameButton.setPrefHeight(25);
    }

    @FXML
    private void provideMode(ActionEvent e){

        functionsPane.getChildren().clear();

        if(employeesModeRadio.isSelected()){
            initEmployees();
            functionsPane.getChildren().add(addEmployeeButton);
            functionsPane.getChildren().add(sortBySurnameButton);
        }
        else if(manClientsModeRadio.isSelected()){

        }
        else if(categoryModeRadio.isSelected()){

        }
        else if(manGoodsMode.isSelected()){

        }
        else if(storeGoodsMode.isSelected()){

        }
    }

    @FXML
    protected void openManagerProfile(ActionEvent e) throws IOException {
        Stage profileStage = new Stage();
        FXMLLoader cashierProfileLoader = new FXMLLoader(HelloApplication.class.getResource("CashierProfile.fxml"));
        HelloApplication.setScene(profileStage, cashierProfileLoader, 428, 483, "Cashier profile");
        /*Scene sc = new Scene(cashierProfileLoader.load(), , );
        profileStage.setTitle("Cashier profile");
        profileStage.setScene(sc);
        profileStage.show();*/
    }
}
