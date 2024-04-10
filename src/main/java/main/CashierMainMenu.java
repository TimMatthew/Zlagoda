package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class CashierMainMenu {

    protected static final int WIDTH = 900, HEIGHT = 600;
    protected static final FXMLLoader CASHIER_MENU_FXML_LOADER = new FXMLLoader(HelloApplication.class.getResource("CashierMainMenu.fxml"));

    @FXML
    private RadioButton goodsModeRadio, clientsModeRadio, receiptsModeRadio;
    @FXML
    private AnchorPane functionsPane;
    @FXML
    private TextField cashierSearchField;

    //Кнопки для товарів
    private Button goodSortNameButton, goodShopSortNameButton, goodPromotionSortNameButton, goodPromotionSortAmountButton;

    //Кнопки для клієнтів
    private Button addLoyalClientButton, clientSortNameButton;

    //Кнопки для чеків
    private Button receiptsTodayButton, receiptsPeriodButton;

    protected void initGoods(){
        goodSortNameButton = new Button("Sort by names");
        goodSortNameButton.setLayoutX(30); goodSortNameButton.setLayoutY(20);
        goodSortNameButton.setPrefWidth(100); goodSortNameButton.setPrefHeight(25);

        goodShopSortNameButton = new Button("Present by names");
        goodShopSortNameButton.setLayoutX(25); goodShopSortNameButton.setLayoutY(70);
        goodShopSortNameButton.setPrefWidth(110); goodShopSortNameButton.setPrefHeight(25);

        goodPromotionSortNameButton = new Button("Sort promoted by names");
        goodPromotionSortNameButton.setLayoutX(0); goodPromotionSortNameButton.setLayoutY(120);
        goodPromotionSortNameButton.setPrefWidth(160); goodPromotionSortNameButton.setPrefHeight(34);

        goodPromotionSortAmountButton = new Button("Sort promoted by amount");
        goodPromotionSortAmountButton.setLayoutX(0); goodPromotionSortAmountButton.setLayoutY(180);
        goodPromotionSortAmountButton.setPrefWidth(160); goodPromotionSortAmountButton.setPrefHeight(34);
    }

    protected void initClients(){
        addLoyalClientButton = new Button("Add loyal client");
        addLoyalClientButton.setLayoutX(25); addLoyalClientButton.setLayoutY(20);
        addLoyalClientButton.setPrefWidth(110); addLoyalClientButton.setPrefHeight(25);

        clientSortNameButton = new Button("Sort by name");
        clientSortNameButton.setLayoutX(25); clientSortNameButton.setLayoutY(70);
        clientSortNameButton.setPrefWidth(110); clientSortNameButton.setPrefHeight(25);
    }

    protected void initReceipts(){
        receiptsTodayButton = new Button("Receipts for today");
        receiptsTodayButton.setLayoutX(13); receiptsTodayButton.setLayoutY(20);
        receiptsTodayButton.setPrefWidth(140); receiptsTodayButton.setPrefHeight(25);

        receiptsPeriodButton = new Button("Receipts for set period");
        receiptsPeriodButton.setLayoutX(13); receiptsPeriodButton.setLayoutY(70);
        receiptsPeriodButton.setPrefWidth(140); receiptsPeriodButton.setPrefHeight(25);
    }

    @FXML
    protected void provideMode(ActionEvent e){

        functionsPane.getChildren().clear();

        if(goodsModeRadio.isSelected()){
            initGoods();
            functionsPane.getChildren().add(goodSortNameButton);
            functionsPane.getChildren().add(goodShopSortNameButton);
            functionsPane.getChildren().add(goodPromotionSortNameButton);
            functionsPane.getChildren().add(goodPromotionSortAmountButton);
            cashierSearchField.setPromptText("Goods search...");

        }
        else if(clientsModeRadio.isSelected()){
            initClients();
            functionsPane.getChildren().add(addLoyalClientButton);
            functionsPane.getChildren().add(clientSortNameButton);
            cashierSearchField.setPromptText("Clients search...");
        }
        else if(receiptsModeRadio.isSelected()){
            initReceipts();
            functionsPane.getChildren().add(receiptsTodayButton);
            functionsPane.getChildren().add(receiptsPeriodButton);
            cashierSearchField.setPromptText("Receipts search...");
        }
    }

    @FXML
    protected void openCashierProfile(ActionEvent e) throws IOException {
        Stage profileStage = new Stage();
        FXMLLoader cashierProfileLoader = new FXMLLoader(HelloApplication.class.getResource("EmployeeProfile.fxml"));
        HelloApplication.setScene(profileStage, cashierProfileLoader, 428, 483, "Cashier profile");
        /*Scene sc = new Scene(cashierProfileLoader.load(), , );
        profileStage.setTitle("Cashier profile");
        profileStage.setScene(sc);
        profileStage.show();*/
    }
}
