package main;

import Entities.Category;
import Entities.Customer_Card;
import Entities.Employee;
import Entities.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import services.CategoryService;
import services.CustomerCardService;
import services.EmployeeService;
import services.ProductService;
import sessionmanagement.UserInfo;
import javafx.scene.control.*;
import java.io.IOException;


public class MainMenu{

    protected static final int WIDTH = 1326, HEIGHT = 770;
    //protected static final FXMLLoader MAIN_MENU_FXML_LOADER =

    private boolean employeeType;

    //Функції для працівників
    private Button addEmployee,emplPhoneAddressBySur, editEmployee;

    //Функції для клієнтів
    private Button addClient, editClient;

    //Функції для категорій товарів
    private Button addCategory, editCategory;

    //Функції для товарів
    private Button addProduct, editProduct;

    //Функції для товарів у магазині
    private Button addStoreProduct, storeProdInfoByUPC;

    //Функції для чеків
    private ComboBox<String> checksSetCashiersAndTime, checksAllCashiersAndTime;
    private Button goodsAmountSoldInSetTime;
    public static ObservableList<Employee> employeeData;
    public static ObservableList<Customer_Card> customerCardData;
    public static ObservableList<Category> categoryData;
    public static ObservableList<Product> productData;

    //Кнопки для товарів
    private Button goodSortNameButton, goodShopSortNameButton, goodPromotionSortNameButton, goodPromotionSortAmountButton;

    //Кнопки для клієнтів
    private Button addLoyalClientButton, clientSortNameButton;

    //Кнопки для чеків
    private Button receiptsTodayButton, receiptsPeriodButton;

    // Для менеджера
    @FXML
    private RadioButton employeesMode, managerClientsMode, categoryMode, managerProductsMode, storeProductsMode, managerReceiptsMode;
    @FXML
    private AnchorPane functionsPane;
    @FXML
    private Button printReportButton;
    @FXML
    private TextField searchField;

    // Для касира
    @FXML
    private RadioButton goodsModeRadio, clientsModeRadio, receiptsModeRadio;
    @FXML
    private Button createCheckButton;
    @FXML
    public TableView dataTable;
    protected void initCashier(Stage st, int width, int height, String title) throws IOException {

        Parent root = new FXMLLoader(HelloApplication.class.getResource("MainMenu.fxml")).load();
        Scene sc = new Scene(root, width, height);
        st.setTitle(title);
        st.setScene(sc);
        st.show();

        employeesMode = (RadioButton) root.lookup("#employeesMode");
        managerClientsMode = (RadioButton) root.lookup("#managerClientsMode");
        categoryMode = (RadioButton) root.lookup("#categoryMode");
        managerProductsMode = (RadioButton) root.lookup("#managerProductsMode");
        storeProductsMode = (RadioButton) root.lookup("#storeProductsMode");
        managerReceiptsMode = (RadioButton) root.lookup("#managerReceiptsMode");
        printReportButton = (Button) root.lookup("#printReportButton");

        employeesMode.setVisible(false);
        managerClientsMode.setVisible(false);
        categoryMode.setVisible(false);
        managerProductsMode.setVisible(false);
        storeProductsMode.setVisible(false);
        managerReceiptsMode.setVisible(false);
        printReportButton.setVisible(false);
    }

    protected void initManager(Stage st, int width, int height, String title) throws IOException{

        Parent root = new FXMLLoader(HelloApplication.class.getResource("MainMenu.fxml")).load();
        Scene sc = new Scene(root, width, height);
        st.setTitle(title);
        st.setScene(sc);
        st.show();

        createCheckButton = (Button) root.lookup("#createCheckButton");
        goodsModeRadio = (RadioButton) root.lookup("#goodsModeRadio");
        clientsModeRadio = (RadioButton) root.lookup("#clientsModeRadio");
        receiptsModeRadio = (RadioButton) root.lookup("#receiptsModeRadio");

        createCheckButton.setVisible(false);
        goodsModeRadio.setVisible(false);
        clientsModeRadio.setVisible(false);
        receiptsModeRadio.setVisible(false);
    }

    @FXML
    protected void openProfile(ActionEvent e) {
        EmployeeProfile.initProfile(UserInfo.employeeProfile, "Manager profile");
    }

    protected void initCashierGoods(){
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

    protected void initCashierClients(){
        addLoyalClientButton = new Button("Add loyal client");
        addLoyalClientButton.setLayoutX(25); addLoyalClientButton.setLayoutY(20);
        addLoyalClientButton.setPrefWidth(110); addLoyalClientButton.setPrefHeight(25);

        clientSortNameButton = new Button("Sort by name");
        clientSortNameButton.setLayoutX(25); clientSortNameButton.setLayoutY(70);
        clientSortNameButton.setPrefWidth(110); clientSortNameButton.setPrefHeight(25);
    }

    protected void initCashierReceipts(){
        receiptsTodayButton = new Button("Receipts for today");
        receiptsTodayButton.setLayoutX(13); receiptsTodayButton.setLayoutY(20);
        receiptsTodayButton.setPrefWidth(140); receiptsTodayButton.setPrefHeight(25);

        receiptsPeriodButton = new Button("Receipts for set period");
        receiptsPeriodButton.setLayoutX(13); receiptsPeriodButton.setLayoutY(70);
        receiptsPeriodButton.setPrefWidth(140); receiptsPeriodButton.setPrefHeight(25);
    }

    @FXML
    protected void provideCashierMode(ActionEvent e){

        functionsPane.getChildren().clear();

        if(goodsModeRadio.isSelected()){
            initCashierGoods();
            functionsPane.getChildren().add(goodSortNameButton);
            functionsPane.getChildren().add(goodShopSortNameButton);
            functionsPane.getChildren().add(goodPromotionSortNameButton);
            functionsPane.getChildren().add(goodPromotionSortAmountButton);
            searchField.setPromptText("Goods search...");

        }
        else if(clientsModeRadio.isSelected()){
            initCashierClients();
            functionsPane.getChildren().add(addLoyalClientButton);
            functionsPane.getChildren().add(clientSortNameButton);
            searchField.setPromptText("Clients search...");
        }
        else if(receiptsModeRadio.isSelected()){
            initCashierReceipts();
            functionsPane.getChildren().add(receiptsTodayButton);
            functionsPane.getChildren().add(receiptsPeriodButton);
            searchField.setPromptText("Receipts search...");
        }
    }

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

    private void updateCustomerCardTable(){
        customerCardData = new CustomerCardService().getAllCustomers();
    }
    private void updateProductTable(){
        productData = new ProductService().getAllProducts();
    }
    private void updateCategoryTable(){
        categoryData = new CategoryService().getAllCategories();
    }
    private void updateEmployeesTable(){
        employeeData = new EmployeeService().getAllEmployees();
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

    private void initEmployees(){
        addEmployee = new Button("Add employee");
        addEmployee.setLayoutX(50); addEmployee.setLayoutY(20);
        addEmployee.setPrefWidth(120); addEmployee.setPrefHeight(30);
        addEmployee.setFont(new Font(13));
        addEmployee.setOnAction(actionEvent -> addNewEmployee());

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
    }

    private void initStoreProducts(){
        addStoreProduct = new Button("Add product");
        addStoreProduct.setLayoutX(50); addStoreProduct.setLayoutY(20);
        addStoreProduct.setPrefWidth(120); addStoreProduct.setPrefHeight(30);
        addStoreProduct.setFont(new Font(13));


        storeProdInfoByUPC = new Button("Product Info by UPC");
        storeProdInfoByUPC.setLayoutX(20); storeProdInfoByUPC.setLayoutY(70);
        storeProdInfoByUPC.setPrefWidth(180); storeProdInfoByUPC.setPrefHeight(30);
        storeProdInfoByUPC.setFont(new Font(13));
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
            functionsPane.getChildren().add(emplPhoneAddressBySur);
            functionsPane.getChildren().add(editEmployee);
            searchField.setPromptText("Employees search...");
            showEmployees();
        }
        else if(managerClientsMode.isSelected()){
            initClients();
            functionsPane.getChildren().add(addClient);
            functionsPane.getChildren().add(editClient);
            searchField.setPromptText("Clients search...");
            showCustomerCards();
        }
        else if(categoryMode.isSelected()){
            initCategories();
            functionsPane.getChildren().add(addCategory);
            functionsPane.getChildren().add(editCategory);
            searchField.setPromptText("Categories search...");
            showCategories();
        }
        else if(managerProductsMode.isSelected()){
            initProducts();
            functionsPane.getChildren().add(addProduct);
            functionsPane.getChildren().add(editProduct);
            searchField.setPromptText("Products search...");
            showProducts();
        }
        else if(storeProductsMode.isSelected()){
            initStoreProducts();
            functionsPane.getChildren().add(addStoreProduct);
            functionsPane.getChildren().add(storeProdInfoByUPC);
            searchField.setPromptText("Products in store search...");
        }
        else if(managerReceiptsMode.isSelected()){
            initRecepits();
            functionsPane.getChildren().add(goodsAmountSoldInSetTime);
            functionsPane.getChildren().add(checksSetCashiersAndTime);
            functionsPane.getChildren().add(checksAllCashiersAndTime);
            searchField.setPromptText("Receipts search...");
        }
    }

    @FXML
    public void quit(ActionEvent actionEvent) throws IOException {
        UserInfo.id = null;
        UserInfo.position = null;
        UserInfo.employeeProfile = null;
        HelloApplication.setScene(HelloApplication.mainStage, Authorization.FXML_LOADER(), Authorization.WIDTH, Authorization.HEIGHT, "Sign In");
    }

    public void createCheck(ActionEvent actionEvent) {
    }
}
