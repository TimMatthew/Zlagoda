package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class ManagerMainMenu {

    @FXML
    private TextField managerSearchField;
    @FXML
    private RadioButton employeesMode, managerClientsMode, categoryMode, managerProductsMode, storeProductsMode, managerReceiptsMode;
    @FXML
    private AnchorPane functionsPane;

    protected static final FXMLLoader MANAGER_MENU_FXML_LOADER = new FXMLLoader(HelloApplication.class.getResource("ManagerMainMenu.fxml"));
    protected static final int WIDTH = 1130;
    protected static final int HEIGHT = 770;

    //Функції для працівників
    private Button addEmployee, emplSortSur, sortCashiersBySurname, emplPhoneAddressBySur;

    //Функції для клієнтів
    private Button addClient, clientSortSur, clientSortSurWithCard;

    //Функції для категорій товарів
    private Button addCategory, categorySortName;

    //Функції для товарів
    private Button addProduct, productSortName, prodsSortNameWithCategory;

    //Функції для товарів у магазині
    private Button addStoreProduct, storeProdsSortByAmount, storeProdInfoByUPC;
    private ComboBox<String> promStoreProdsSort, storeProdsSort;

    //Функції для чеків
    private ComboBox<String> checksSetCashiersAndTime, checksAllCashiersAndTime;
    private Button goodsAmountSoldInSetTime;

    private void initEmployees(){
        addEmployee = new Button("Add employee");
        addEmployee.setLayoutX(50); addEmployee.setLayoutY(20);
        addEmployee.setPrefWidth(120); addEmployee.setPrefHeight(30);
        addEmployee.setFont(new Font(13));

        emplSortSur = new Button("Sort employees by surname");
        emplSortSur.setLayoutX(20); emplSortSur.setLayoutY(70);
        emplSortSur.setPrefWidth(180); emplSortSur.setPrefHeight(30);
        emplSortSur.setFont(new Font(13));

        sortCashiersBySurname = new Button("Sort cashiers by surname");
        sortCashiersBySurname.setLayoutX(25); sortCashiersBySurname.setLayoutY(120);
        sortCashiersBySurname.setPrefWidth(170); sortCashiersBySurname.setPrefHeight(30);
        sortCashiersBySurname.setFont(new Font(13));

        emplPhoneAddressBySur = new Button("Phone and address by surname");
        emplPhoneAddressBySur.setLayoutX(7); emplPhoneAddressBySur.setLayoutY(170);
        emplPhoneAddressBySur.setPrefWidth(200); emplPhoneAddressBySur.setPrefHeight(30);
        emplPhoneAddressBySur.setFont(new Font(13));

    }

    private void initClients(){
        addClient = new Button("Add client");
        addClient.setLayoutX(50); addClient.setLayoutY(20);
        addClient.setPrefWidth(120); addClient.setPrefHeight(30);
        addClient.setFont(new Font(13));

        clientSortSur = new Button("Sort clients by surname");
        clientSortSur.setLayoutX(20); clientSortSur.setLayoutY(70);
        clientSortSur.setPrefWidth(180); clientSortSur.setPrefHeight(30);
        clientSortSur.setFont(new Font(13));

        clientSortSurWithCard = new Button("  Sort clients with\n cards by surname");
        clientSortSurWithCard.setLayoutX(25); clientSortSurWithCard.setLayoutY(120);
        clientSortSurWithCard.setPrefWidth(170); clientSortSurWithCard.setPrefHeight(50);
        clientSortSurWithCard.setFont(new Font(13));
    }

    private void initCategories(){
        addCategory = new Button("Add category");
        addCategory.setLayoutX(50); addCategory.setLayoutY(20);
        addCategory.setPrefWidth(120); addCategory.setPrefHeight(30);
        addCategory.setFont(new Font(13));

        categorySortName = new Button("Sort categories by name");
        categorySortName.setLayoutX(20); categorySortName.setLayoutY(70);
        categorySortName.setPrefWidth(180); categorySortName.setPrefHeight(30);
        categorySortName.setFont(new Font(13));

    }

    private void initProducts(){
        addProduct = new Button("Add product");
        addProduct.setLayoutX(50); addProduct.setLayoutY(20);
        addProduct.setPrefWidth(120); addProduct.setPrefHeight(30);
        addProduct.setFont(new Font(13));

        productSortName = new Button("Sort products by name");
        productSortName.setLayoutX(20); productSortName.setLayoutY(70);
        productSortName.setPrefWidth(180); productSortName.setPrefHeight(30);
        productSortName.setFont(new Font(13));

        prodsSortNameWithCategory = new Button("     Sort products of\n set category by name");
        prodsSortNameWithCategory.setLayoutX(20); prodsSortNameWithCategory.setLayoutY(120);
        prodsSortNameWithCategory.setPrefWidth(180); prodsSortNameWithCategory.setPrefHeight(50);
        prodsSortNameWithCategory.setFont(new Font(13));

    }

    private void initStoreProducts(){
        addStoreProduct = new Button("Add product");
        addStoreProduct.setLayoutX(50); addStoreProduct.setLayoutY(20);
        addStoreProduct.setPrefWidth(120); addStoreProduct.setPrefHeight(30);
        addStoreProduct.setFont(new Font(13));

        storeProdsSortByAmount = new Button("Sort products by amount");
        storeProdsSortByAmount.setLayoutX(20); storeProdsSortByAmount.setLayoutY(70);
        storeProdsSortByAmount.setPrefWidth(180); storeProdsSortByAmount.setPrefHeight(30);
        storeProdsSortByAmount.setFont(new Font(13));

        storeProdInfoByUPC = new Button("Product Info by UPC");
        storeProdInfoByUPC.setLayoutX(20); storeProdInfoByUPC.setLayoutY(120);
        storeProdInfoByUPC.setPrefWidth(180); storeProdInfoByUPC.setPrefHeight(30);
        storeProdInfoByUPC.setFont(new Font(13));

        promStoreProdsSort = new ComboBox<>(FXCollections.observableArrayList("By names", "By amount"));
        promStoreProdsSort.setPromptText("Promotionals sorted");
        promStoreProdsSort.setLayoutX(20); promStoreProdsSort.setLayoutY(170);
        promStoreProdsSort.setPrefWidth(180); promStoreProdsSort.setPrefHeight(30);


        storeProdsSort = new ComboBox<>(FXCollections.observableArrayList("By names", "By amount"));
        storeProdsSort.setPromptText("Products sorted");
        storeProdsSort.setLayoutX(20); storeProdsSort.setLayoutY(220);
        storeProdsSort.setPrefWidth(180); storeProdsSort.setPrefHeight(30);
    }


    private void initRecepits(){

        goodsAmountSoldInSetTime = new Button("Goods amount sold\n      for set period");
        goodsAmountSoldInSetTime.setLayoutX(20); goodsAmountSoldInSetTime.setLayoutY(100);
        goodsAmountSoldInSetTime.setPrefWidth(180); goodsAmountSoldInSetTime.setPrefHeight(50);
        goodsAmountSoldInSetTime.setFont(new Font(13));

        //Якщо всі касири - вилізає віконце тільки промптом на період
        //Якщо конкретний касир - буде ще промпт і на прізвище
        checksSetCashiersAndTime = new ComboBox<>(FXCollections.observableArrayList("Of all cashiers", "Of set cashiers"));
        checksSetCashiersAndTime.setPromptText("Receipts for set period");
        checksSetCashiersAndTime.setLayoutX(20); checksSetCashiersAndTime.setLayoutY(170);
        checksSetCashiersAndTime.setPrefWidth(180); checksSetCashiersAndTime.setPrefHeight(30);

        //Аналогічний принцип
        checksAllCashiersAndTime = new ComboBox<>(FXCollections.observableArrayList("Of all cashiers", "Of set cashiers"));
        checksAllCashiersAndTime.setPromptText("Sum of sold goods");
        checksAllCashiersAndTime.setLayoutX(20); checksAllCashiersAndTime.setLayoutY(220);
        checksAllCashiersAndTime.setPrefWidth(180); checksAllCashiersAndTime.setPrefHeight(30);
    }

    @FXML
    private void provideMode(ActionEvent e){

        functionsPane.getChildren().clear();

        if(employeesMode.isSelected()){
            initEmployees();
            functionsPane.getChildren().add(addEmployee);
            functionsPane.getChildren().add(emplSortSur);
            functionsPane.getChildren().add(sortCashiersBySurname);
            functionsPane.getChildren().add(emplPhoneAddressBySur);
            managerSearchField.setPromptText("Employees search...");
        }
        else if(managerClientsMode.isSelected()){
            initClients();
            functionsPane.getChildren().add(addClient);
            functionsPane.getChildren().add(clientSortSur);
            functionsPane.getChildren().add(clientSortSurWithCard);
            managerSearchField.setPromptText("Clients search...");
        }
        else if(categoryMode.isSelected()){
            initCategories();
            functionsPane.getChildren().add(addCategory);
            functionsPane.getChildren().add(categorySortName);
            managerSearchField.setPromptText("Categories search...");
        }
        else if(managerProductsMode.isSelected()){
            initProducts();
            functionsPane.getChildren().add(addProduct);
            functionsPane.getChildren().add(productSortName);
            functionsPane.getChildren().add(prodsSortNameWithCategory);
            managerSearchField.setPromptText("Products search...");
        }
        else if(storeProductsMode.isSelected()){
            initStoreProducts();
            functionsPane.getChildren().add(addStoreProduct);
            functionsPane.getChildren().add(storeProdsSortByAmount);
            functionsPane.getChildren().add(storeProdInfoByUPC);
            functionsPane.getChildren().add(promStoreProdsSort);
            functionsPane.getChildren().add(storeProdsSort);
            managerSearchField.setPromptText("Products in store search...");
        }
        else if(managerReceiptsMode.isSelected()){
            initRecepits();
            functionsPane.getChildren().add(goodsAmountSoldInSetTime);
            functionsPane.getChildren().add(checksSetCashiersAndTime);
            functionsPane.getChildren().add(checksAllCashiersAndTime);
            managerSearchField.setPromptText("Receipts search...");
        }
    }

    @FXML
    protected void openManagerProfile(ActionEvent e) throws IOException {
        Stage profileStage = new Stage();
        FXMLLoader cashierProfileLoader = new FXMLLoader(HelloApplication.class.getResource("EmployeeProfile.fxml"));
        HelloApplication.setScene(profileStage, cashierProfileLoader, 428, 483, "Manager profile");
    }
}
