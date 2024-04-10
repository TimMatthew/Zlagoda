package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SignInController {
    @FXML
    private RadioButton managerRadio;
    @FXML
    private RadioButton cashierRadio;
    @FXML
    private AnchorPane functionsPane;

    private char emplType;
    protected static final int WIDTH = 600, HEIGHT = 400;
    @FXML
    protected void setManager(ActionEvent e){
        if(managerRadio.isSelected( )){
            emplType='m';
        }
        else if(cashierRadio.isSelected()){
            emplType='c';
        }
    }
    @FXML
    protected void goToMainMenu(ActionEvent e) throws IOException {
        if(emplType=='m'){
            HelloApplication.mainStage.hide();
            HelloApplication.setScene(HelloApplication.mainStage, ManagerMainMenu.MANAGER_MENU_FXML_LOADER, ManagerMainMenu.WIDTH, ManagerMainMenu.HEIGHT, "Manager Main Menu");
        }
        else if(emplType=='c'){
            HelloApplication.mainStage.hide();
            HelloApplication.setScene(HelloApplication.mainStage, CashierMainMenu.CASHIER_MENU_FXML_LOADER, CashierMainMenu.WIDTH, CashierMainMenu.HEIGHT, "Cashier Main Menu");
        }
        else{
            // Тимчасово поставив, буде вискакувати віконце просто
            System.out.println("Choose the employee type!");
        }
    }

}
