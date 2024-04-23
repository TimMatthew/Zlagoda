package main;

import Entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import services.CategoryService;
import services.CustomerCardService;
import services.EmployeeService;
import services.ProductService;
import sessionmanagement.UserInfo;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ManagerMainMenu implements Initializable {


    protected static FXMLLoader FXML_LOADER(){
        return new FXMLLoader(HelloApplication.class.getResource("ManagerMainMenu.fxml"));
    }
    protected static final int WIDTH = 1326;
    protected static final int HEIGHT = 770;

    @FXML
    public TableView dataTable;
    public static ObservableList<Employee> employeeData;
    public static ObservableList<Customer_Card> customerCardData;
    public static ObservableList<Category> categoryData;
    public static ObservableList<Product> productData;
    @FXML
    private TextField managerSearchField;
    @FXML
    public Label employeePIB, employeeTitle;
    @FXML
    private RadioButton employeesMode, managerClientsMode, categoryMode, managerProductsMode, storeProductsMode, managerReceiptsMode;
    @FXML
    private AnchorPane functionsPane;



    //Функції для працівників
    private Button addEmployee, /*emplSortSur, sortCashiersBySurname,*/ emplPhoneAddressBySur, editEmployee;

    //Функції для клієнтів
    private Button addClient, editClient /*clientSortSur, clientSortSurWithCard*/;

    //Функції для категорій товарів
    private Button addCategory, editCategory /*categorySortName*/;

    //Функції для товарів
    private Button addProduct, editProduct /*productSortName, prodsSortNameWithCategory*/;

    //Функції для товарів у магазині
    private Button addStoreProduct, /*storeProdsSortByAmount*/ storeProdInfoByUPC;
    /*private ComboBox<String> promStoreProdsSort, storeProdsSort;*/

    //Функції для чеків
    private ComboBox<String> checksSetCashiersAndTime, checksAllCashiersAndTime;
    private Button goodsAmountSoldInSetTime;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        employeePIB.setText(UserInfo.employeeProfile.getFullName());
        employeeTitle.setText(UserInfo.employeeProfile.getEmpl_role());
    }

    private void initEmployees(){
        addEmployee = new Button("Add employee");
        addEmployee.setLayoutX(50); addEmployee.setLayoutY(20);
        addEmployee.setPrefWidth(120); addEmployee.setPrefHeight(30);
        addEmployee.setFont(new Font(13));
        addEmployee.setOnAction(actionEvent -> addNewEmployee());

        /*emplSortSur = new Button("Sort employees by surname");
        emplSortSur.setLayoutX(20); emplSortSur.setLayoutY(70);
        emplSortSur.setPrefWidth(180); emplSortSur.setPrefHeight(30);
        emplSortSur.setFont(new Font(13));

        sortCashiersBySurname = new Button("Sort cashiers by surname");
        sortCashiersBySurname.setLayoutX(25); sortCashiersBySurname.setLayoutY(120);
        sortCashiersBySurname.setPrefWidth(170); sortCashiersBySurname.setPrefHeight(30);
        sortCashiersBySurname.setFont(new Font(13));*/

        emplPhoneAddressBySur = new Button("Phone and address by surname");
        emplPhoneAddressBySur.setLayoutX(7); emplPhoneAddressBySur.setLayoutY(70);
        emplPhoneAddressBySur.setPrefWidth(200); emplPhoneAddressBySur.setPrefHeight(30);
        emplPhoneAddressBySur.setFont(new Font(13));

        editEmployee = new Button("Edit selected employee");
        editEmployee.setLayoutX(18); editEmployee.setLayoutY(120);
        editEmployee.setPrefWidth(180); editEmployee.setPrefHeight(30);
        editEmployee.setFont(new Font(13));
        editEmployee.setOnAction(actionEvent -> editSelectedEmployee());
    }

    private void initClients(){
        addClient = new Button("Add client");
        addClient.setLayoutX(50); addClient.setLayoutY(20);
        addClient.setPrefWidth(120); addClient.setPrefHeight(30);
        addClient.setFont(new Font(13));
        addClient.setOnAction(actionEvent -> addNewCustomerCard());

        editClient = new Button("Edit selected client");
        editClient.setLayoutX(20); editClient.setLayoutY(70);
        editClient.setPrefWidth(180); editClient.setPrefHeight(30);
        editClient.setFont(new Font(13));
        editClient.setOnAction(actionEvent -> editSelectedCustomerCard());

        /*clientSortSur = new Button("Sort clients by surname");
        clientSortSur.setLayoutX(20); clientSortSur.setLayoutY(70);
        clientSortSur.setPrefWidth(180); clientSortSur.setPrefHeight(30);
        clientSortSur.setFont(new Font(13));

        clientSortSurWithCard = new Button("  Sort clients with\n cards by surname");
        clientSortSurWithCard.setLayoutX(25); clientSortSurWithCard.setLayoutY(120);
        clientSortSurWithCard.setPrefWidth(170); clientSortSurWithCard.setPrefHeight(50);
        clientSortSurWithCard.setFont(new Font(13));*/
    }

    private void initCategories(){
        addCategory = new Button("Add category");
        addCategory.setLayoutX(50); addCategory.setLayoutY(20);
        addCategory.setPrefWidth(120); addCategory.setPrefHeight(30);
        addCategory.setFont(new Font(13));
        addCategory.setOnAction(actionEvent -> addNewCategory());

        editCategory = new Button("Edit category");
        editCategory.setLayoutX(20); editCategory.setLayoutY(70);
        editCategory.setPrefWidth(120); editCategory.setPrefHeight(30);
        editCategory.setFont(new Font(13));
        editCategory.setOnAction(actionEvent -> editSelectedCategory());

        /*categorySortName = new Button("Sort categories by name");
        categorySortName.setLayoutX(20); categorySortName.setLayoutY(70);
        categorySortName.setPrefWidth(180); categorySortName.setPrefHeight(30);
        categorySortName.setFont(new Font(13));*/

    }

    private void initProducts(){
        addProduct = new Button("Add product");
        addProduct.setLayoutX(50); addProduct.setLayoutY(20);
        addProduct.setPrefWidth(120); addProduct.setPrefHeight(30);
        addProduct.setFont(new Font(13));
        addProduct.setOnAction(actionEvent -> addNewProduct());

        editProduct = new Button("Edit selected product");
        editProduct.setLayoutX(20); editProduct.setLayoutY(70);
        editProduct.setPrefWidth(180); editProduct.setPrefHeight(30);
        editProduct.setFont(new Font(13));
        editProduct.setOnAction(actionEvent -> editSelectedProduct());

        /*productSortName = new Button("Sort products by name");
        productSortName.setLayoutX(20); productSortName.setLayoutY(70);
        productSortName.setPrefWidth(180); productSortName.setPrefHeight(30);
        productSortName.setFont(new Font(13));


        prodsSortNameWithCategory = new Button("     Sort products of\n set category by name");
        prodsSortNameWithCategory.setLayoutX(20); prodsSortNameWithCategory.setLayoutY(120);
        prodsSortNameWithCategory.setPrefWidth(180); prodsSortNameWithCategory.setPrefHeight(50);
        prodsSortNameWithCategory.setFont(new Font(13));*/

    }

    private void initStoreProducts(){
        addStoreProduct = new Button("Add product");
        addStoreProduct.setLayoutX(50); addStoreProduct.setLayoutY(20);
        addStoreProduct.setPrefWidth(120); addStoreProduct.setPrefHeight(30);
        addStoreProduct.setFont(new Font(13));

        /*storeProdsSortByAmount = new Button("Sort products by amount");
        storeProdsSortByAmount.setLayoutX(20); storeProdsSortByAmount.setLayoutY(70);
        storeProdsSortByAmount.setPrefWidth(180); storeProdsSortByAmount.setPrefHeight(30);
        storeProdsSortByAmount.setFont(new Font(13));*/

        storeProdInfoByUPC = new Button("Product Info by UPC");
        storeProdInfoByUPC.setLayoutX(20); storeProdInfoByUPC.setLayoutY(70);
        storeProdInfoByUPC.setPrefWidth(180); storeProdInfoByUPC.setPrefHeight(30);
        storeProdInfoByUPC.setFont(new Font(13));

        /*promStoreProdsSort = new ComboBox<>(FXCollections.observableArrayList("By names", "By amount"));
        promStoreProdsSort.setPromptText("Promotionals sorted");
        promStoreProdsSort.setLayoutX(20); promStoreProdsSort.setLayoutY(170);
        promStoreProdsSort.setPrefWidth(180); promStoreProdsSort.setPrefHeight(30);


        storeProdsSort = new ComboBox<>(FXCollections.observableArrayList("By names", "By amount"));
        storeProdsSort.setPromptText("Products sorted");
        storeProdsSort.setLayoutX(20); storeProdsSort.setLayoutY(220);
        storeProdsSort.setPrefWidth(180); storeProdsSort.setPrefHeight(30);*/
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
            /*functionsPane.getChildren().add(emplSortSur);
            functionsPane.getChildren().add(sortCashiersBySurname);*/
            functionsPane.getChildren().add(emplPhoneAddressBySur);
            functionsPane.getChildren().add(editEmployee);
            managerSearchField.setPromptText("Employees search...");
            showEmployees();
        }
        else if(managerClientsMode.isSelected()){
            initClients();
            functionsPane.getChildren().add(addClient);
            /*functionsPane.getChildren().add(clientSortSur);
            functionsPane.getChildren().add(clientSortSurWithCard);*/
            functionsPane.getChildren().add(editClient);
            managerSearchField.setPromptText("Clients search...");
            showCustomerCards();
        }
        else if(categoryMode.isSelected()){
            initCategories();
            functionsPane.getChildren().add(addCategory);
            functionsPane.getChildren().add(editCategory);
            //functionsPane.getChildren().add(categorySortName);
            managerSearchField.setPromptText("Categories search...");
            showCategories();
        }
        else if(managerProductsMode.isSelected()){
            initProducts();
            functionsPane.getChildren().add(addProduct);
            functionsPane.getChildren().add(editProduct);
           /* functionsPane.getChildren().add(productSortName);
            functionsPane.getChildren().add(prodsSortNameWithCategory);*/
            managerSearchField.setPromptText("Products search...");
            showProducts();
        }
        else if(storeProductsMode.isSelected()){
            initStoreProducts();
            functionsPane.getChildren().add(addStoreProduct);
            //functionsPane.getChildren().add(storeProdsSortByAmount);
            functionsPane.getChildren().add(storeProdInfoByUPC);
            /*functionsPane.getChildren().add(promStoreProdsSort);
            functionsPane.getChildren().add(storeProdsSort);*/
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
    protected void openManagerProfile(ActionEvent e) {
        EmployeeProfile.initProfile(UserInfo.employeeProfile, "Manager profile");
    }

    @FXML
    public void quit(ActionEvent actionEvent) throws IOException {
        UserInfo.id = null;
        UserInfo.position = null;
        UserInfo.employeeProfile = null;
        HelloApplication.setScene(HelloApplication.mainStage, Authorization.FXML_LOADER(), Authorization.WIDTH, Authorization.HEIGHT, "Sign In");
    }

//region data table content
    private void showEmployees(){
        TableColumn<Employee, String> surname = new TableColumn<>("Surname");
        TableColumn<Employee, String> name = new TableColumn<>("Name");
        TableColumn<Employee, String> patronymic = new TableColumn<>("Patronymic");
        TableColumn<Employee, String> role = new TableColumn<>("Role");
        TableColumn<Employee, String> salary = new TableColumn<>("Salary");
        TableColumn<Employee, String> date_of_birth = new TableColumn<>("Birthday");
        TableColumn<Employee, String> date_of_start = new TableColumn<>("Start Date");
        TableColumn<Employee, String> phone_number = new TableColumn<>("Phone");
        TableColumn<Employee, String> city = new TableColumn<>("City");
        TableColumn<Employee, String> street = new TableColumn<>("Street");
        TableColumn<Employee, String> zip_code = new TableColumn<>("Zip code");


        TableColumn fullName = new TableColumn<>("Full name");
        TableColumn address = new TableColumn<>("Address");

        fullName.getColumns().addAll(surname, name, patronymic);
        address.getColumns().addAll(city, street, zip_code);

        surname.setCellValueFactory(new PropertyValueFactory<>("empl_surname"));
        name.setCellValueFactory(new PropertyValueFactory<>("empl_name"));
        patronymic.setCellValueFactory(new PropertyValueFactory<>("empl_patronymic"));
        role.setCellValueFactory(new PropertyValueFactory<>("empl_role"));
        salary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        date_of_birth.setCellValueFactory(new PropertyValueFactory<>("date_of_birth"));
        date_of_start.setCellValueFactory(new PropertyValueFactory<>("date_of_start"));
        phone_number.setCellValueFactory(new PropertyValueFactory<>("phone_number"));
        city.setCellValueFactory(new PropertyValueFactory<>("city"));
        street.setCellValueFactory(new PropertyValueFactory<>("street"));
        zip_code.setCellValueFactory(new PropertyValueFactory<>("zip_code"));

        dataTable.getColumns().clear();
        dataTable.getColumns().addAll(fullName, role, salary, date_of_birth, date_of_start, phone_number, address);
        updateEmployeesTable();
        dataTable.setItems(employeeData);
    }

    private void updateEmployeesTable(){
        employeeData = new EmployeeService().getAllEmployees();
    }
    private void addNewEmployee() {
        EmployeeProfile.initProfile(null, "New employee profile");
    }
    private void editSelectedEmployee() {
        int selectedIndex = dataTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Employee selected = (Employee)dataTable.getItems().get(selectedIndex);
            EmployeeProfile.initProfile(selected, selected.getFullName() + " profile");
        }
    }

    private void showCustomerCards(){
        TableColumn<Customer_Card, String> surname = new TableColumn<>("Surname");
        TableColumn<Customer_Card, String> name = new TableColumn<>("Name");
        TableColumn<Customer_Card, String> patronymic = new TableColumn<>("Patronymic");
        TableColumn<Customer_Card, String> phone_number = new TableColumn<>("Phone");
        TableColumn<Customer_Card, String> city = new TableColumn<>("City");
        TableColumn<Customer_Card, String> street = new TableColumn<>("Street");
        TableColumn<Customer_Card, String> zip_code = new TableColumn<>("Zip code");
        TableColumn<Customer_Card, String> percent = new TableColumn<>("Discount");

        TableColumn fullName = new TableColumn<>("Full name");
        TableColumn address = new TableColumn<>("Address");

        fullName.getColumns().addAll(surname, name, patronymic);
        address.getColumns().addAll(city, street, zip_code);

        surname.setCellValueFactory(new PropertyValueFactory<>("cust_surname"));
        name.setCellValueFactory(new PropertyValueFactory<>("cust_name"));
        patronymic.setCellValueFactory(new PropertyValueFactory<>("cust_patronymic"));
        phone_number.setCellValueFactory(new PropertyValueFactory<>("phone_number"));
        city.setCellValueFactory(new PropertyValueFactory<>("city"));
        street.setCellValueFactory(new PropertyValueFactory<>("street"));
        zip_code.setCellValueFactory(new PropertyValueFactory<>("zip_code"));
        percent.setCellValueFactory(new PropertyValueFactory<>("percent"));

        dataTable.getColumns().clear();
        dataTable.getColumns().addAll(fullName, phone_number, address, percent);
        updateCustomerCardTable();
        dataTable.setItems(customerCardData);
    }
    private void updateCustomerCardTable(){
        customerCardData = new CustomerCardService().getAllCustomers();
    }
    private void addNewCustomerCard() {
        CustomerCardProfile.initProfile(null, "New customer card profile");
    }
    private void editSelectedCustomerCard() {
        int selectedIndex = dataTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Customer_Card selected = (Customer_Card)dataTable.getItems().get(selectedIndex);
            CustomerCardProfile.initProfile(selected, selected.getFullName() + " profile");
        }
    }


    private void showCategories(){
        TableColumn<Category, Integer> id = new TableColumn<>("ID");
        TableColumn<Category, String> name = new TableColumn<>("Name");
        TableColumn<Category, Integer> count = new TableColumn<>("Unique products count");

        id.setCellValueFactory(new PropertyValueFactory<>("category_number"));
        name.setCellValueFactory(new PropertyValueFactory<>("category_name"));
        //count.setCellValueFactory(new PropertyValueFactory<>("products_count"));

        dataTable.getColumns().clear();
        dataTable.getColumns().addAll(id, name);
        updateCategoryTable();
        dataTable.setItems(categoryData);
    }
    private void updateCategoryTable(){
        categoryData = new CategoryService().getAllCategories();
    }
    private void addNewCategory() {
        CategoryManager.initProfile(null, "New category");
    }
    private void editSelectedCategory() {
        int selectedIndex = dataTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Category selected = (Category)dataTable.getItems().get(selectedIndex);
            CategoryManager.initProfile(selected, selected.getCategory_name() + " category");
        }
    }
    private void showProducts(){
        TableColumn<Product, Integer> id = new TableColumn<>("ID");
        TableColumn<Product, String> name = new TableColumn<>("Name");
        TableColumn<Product, Integer> category = new TableColumn<>("Category");
        TableColumn<Product, Integer> characteristic = new TableColumn<>("Characteristics");

        id.setCellValueFactory(new PropertyValueFactory<>("id_product"));
        name.setCellValueFactory(new PropertyValueFactory<>("product_name"));
        category.setCellValueFactory(new PropertyValueFactory<>("category_name"));
        characteristic.setCellValueFactory(new PropertyValueFactory<>("characteristics"));

        dataTable.getColumns().clear();
        dataTable.getColumns().addAll(id, name, category, characteristic);
        updateProductTable();
        dataTable.setItems(productData);
    }
    private void updateProductTable(){
        productData = new ProductService().getAllProducts();
    }
    private void addNewProduct() {
        ProductManager.initProfile(null, "New product");
    }
    private void editSelectedProduct() {
        int selectedIndex = dataTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Product selected = (Product)dataTable.getItems().get(selectedIndex);
            ProductManager.initProfile(selected, selected.getProduct_name() + " product");
        }
    }
//endregion
}
