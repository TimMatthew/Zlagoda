package main;

import Entities.*;
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
import services.*;
import sessionmanagement.UserInfo;
import javafx.scene.control.*;
import utils.DataPrinter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MainMenu{

    protected static final int WIDTH = 1326, HEIGHT = 770;

    //Функції для працівників
    private Button addEmployee, emplPhoneAddressBySur, editEmployee;

    //Функції для клієнтів
    private Button addClient, editClient;

    //Функції для категорій товарів
    private Button addCategory, editCategory;

    //Функції для товарів
    private Button addProduct, editProduct;

    //Функції для товарів у магазині
    private Button addStoreProduct, editStoreProduct;

    //Функції для чеків
    private ComboBox<String> checksSetCashiersAndTime, checksAllCashiersAndTime;
    private Button goodsAmountSoldInSetTime;
    public static ObservableList<Employee> employeeData;
    public static Map<String, String> employeeDataMapGetName;
    public static Map<String, String> employeeDataMapGetID;
    public static ObservableList<Customer_Card> customerCardData;
    public static ObservableList<Category> categoryData;
    public static Map<String, Integer> categoryMapGetID;
    public static Map<Integer, String> categoryMapGetName;
    public static ObservableList<Product> productData;
    public static Map<String, Integer> productMapGetID;
    public static Map<Integer, String> productMapGetName;
    public static ObservableList<Store_Product> storeProductData;

    //Кнопки для товарів
    private Button genericProductsButton, storeProductsButton, promotedProductsButton, nonPromotedProductsButton;

    //Кнопки для категорій
    private Button listedCategoriesButton;

    //Кнопки для клієнтів
    private Button loyalClientsButton, addLoyalClientButton, clientSortNameButton;

    //Кнопки для чеків
    private Button receiptsTodayButton, receiptsPeriodButton;

    // Для менеджера
    @FXML
    private RadioButton employeesMode, managerClientsMode, categoryMode, managerProductsMode, storeProductsMode, managerReceiptsMode;
    @FXML
    private AnchorPane functionsPane;
    @FXML
    private Button printReportButton, checkLogButton;
    @FXML
    private TextField searchField;

    // Для касира
    @FXML
    private RadioButton goodsModeRadio, categoriesModeRatio, clientsModeRadio, receiptsModeRadio;
    @FXML
    private Button createCheckButton;
    @FXML
    public TableView dataTable;

    protected void initCashier(Stage st) throws IOException {
        Parent root = new FXMLLoader(HelloApplication.class.getResource("MainMenu.fxml")).load();
        Scene sc = new Scene(root, MainMenu.WIDTH, MainMenu.HEIGHT);
        st.setTitle("Особистий кабінет касира");
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
        checkLogButton.setVisible(false);

        updateCustomerCardTable();
        updateCategoryTable();
        updateProductTable();
        updateStoreProductTable();
    }

    protected void initManager(Stage st) throws IOException{
        Parent root = new FXMLLoader(HelloApplication.class.getResource("MainMenu.fxml")).load();
        Scene sc = new Scene(root, MainMenu.WIDTH, MainMenu.HEIGHT);
        st.setTitle("Особистий кабінет менеджера");
        st.setScene(sc);
        st.show();

        createCheckButton = (Button) root.lookup("#createCheckButton");
        goodsModeRadio = (RadioButton) root.lookup("#goodsModeRadio");
        categoriesModeRatio = (RadioButton) root.lookup("#categoriesModeRatio");
        clientsModeRadio = (RadioButton) root.lookup("#clientsModeRadio");
        receiptsModeRadio = (RadioButton) root.lookup("#receiptsModeRadio");

        createCheckButton.setVisible(false);
        goodsModeRadio.setVisible(false);
        categoriesModeRatio.setVisible(false);
        clientsModeRadio.setVisible(false);
        receiptsModeRadio.setVisible(false);

        updateEmployeesTable();
        updateCustomerCardTable();
        updateCategoryTable();
        updateProductTable();
        updateStoreProductTable();
    }


    protected void initCashierGoodsModes() {
        genericProductsButton = new Button("Generic products");
        genericProductsButton.setLayoutX(20); genericProductsButton.setLayoutY(20);
        genericProductsButton.setPrefWidth(150); genericProductsButton.setPrefHeight(30);
        genericProductsButton.setFont(new Font(13));
        genericProductsButton.setOnAction(actionEvent -> showGenericProducts());

        storeProductsButton = new Button("Present in store goods");
        storeProductsButton.setLayoutX(20); storeProductsButton.setLayoutY(70);
        storeProductsButton.setPrefWidth(150); storeProductsButton.setPrefHeight(30);
        storeProductsButton.setFont(new Font(13));
        storeProductsButton.setOnAction(actionEvent -> showStoreProducts());

        promotedProductsButton = new Button("Promoted goods");
        promotedProductsButton.setLayoutX(20); promotedProductsButton.setLayoutY(120);
        promotedProductsButton.setPrefWidth(150); promotedProductsButton.setPrefHeight(30);
        promotedProductsButton.setFont(new Font(13));
        promotedProductsButton.setOnAction(actionEvent -> showPromotedStoreProducts(true));

        nonPromotedProductsButton = new Button("Non-promoted goods");
        nonPromotedProductsButton.setLayoutX(20); nonPromotedProductsButton.setLayoutY(170);
        nonPromotedProductsButton.setPrefWidth(150); nonPromotedProductsButton.setPrefHeight(30);
        nonPromotedProductsButton.setFont(new Font(13));
        nonPromotedProductsButton.setOnAction(actionEvent -> showPromotedStoreProducts(false));
    }

    protected void initCashierCategoriesModes() {
        listedCategoriesButton = new Button("Listed categories");
        listedCategoriesButton.setLayoutX(20); listedCategoriesButton.setLayoutY(20);
        listedCategoriesButton.setPrefWidth(150); listedCategoriesButton.setPrefHeight(30);
        listedCategoriesButton.setFont(new Font(13));
        listedCategoriesButton.setOnAction(actionEvent -> showCategories());
    }

    protected void initCashierClientsModes() {
        loyalClientsButton = new Button("Loyal clients");
        loyalClientsButton.setLayoutX(20); loyalClientsButton.setLayoutY(20);
        loyalClientsButton.setPrefWidth(150); loyalClientsButton.setPrefHeight(30);
        loyalClientsButton.setFont(new Font(13));
        loyalClientsButton.setOnAction(actionEvent -> showClientsCards());
    }

    protected void initCashierClientsManipulationTools(){
        addLoyalClientButton = new Button("Add loyal client");
        addLoyalClientButton.setLayoutX(20); addLoyalClientButton.setLayoutY(220);
        addLoyalClientButton.setPrefWidth(120); addLoyalClientButton.setPrefHeight(30);

        clientSortNameButton = new Button("Sort by name");
        clientSortNameButton.setLayoutX(20); clientSortNameButton.setLayoutY(270);
        clientSortNameButton.setPrefWidth(120); clientSortNameButton.setPrefHeight(30);
    }

    protected void initCashierReceiptsManipulationTools(){
        receiptsTodayButton = new Button("Receipts for today");
        receiptsTodayButton.setLayoutX(20); receiptsTodayButton.setLayoutY(20);
        receiptsTodayButton.setPrefWidth(150); receiptsTodayButton.setPrefHeight(30);

        receiptsPeriodButton = new Button("Receipts for set period");
        receiptsPeriodButton.setLayoutX(20); receiptsPeriodButton.setLayoutY(70);
        receiptsPeriodButton.setPrefWidth(150); receiptsPeriodButton.setPrefHeight(30);
    }

    @FXML
    protected void provideCashierMode(ActionEvent e){

        functionsPane.getChildren().clear();

        if(goodsModeRadio.isSelected()){
            initCashierGoodsModes();
            initGoodsManipulationTools();
            functionsPane.getChildren().add(genericProductsButton);
            functionsPane.getChildren().add(storeProductsButton);
            functionsPane.getChildren().add(promotedProductsButton);
            functionsPane.getChildren().add(nonPromotedProductsButton);

            functionsPane.getChildren().add(addProduct);
            functionsPane.getChildren().add(editProduct);
            searchField.setPromptText("Goods search...");
        }
        else if(categoriesModeRatio.isSelected()) {
            initCashierCategoriesModes();
            initCategoriesManipulationTools();
            functionsPane.getChildren().add(listedCategoriesButton);

            functionsPane.getChildren().add(addCategory);
            functionsPane.getChildren().add(editCategory);
            searchField.setPromptText("Categories search...");
        }
        else if(clientsModeRadio.isSelected()){
            initCashierClientsModes();
            initCashierClientsManipulationTools();
            functionsPane.getChildren().add(loyalClientsButton);

            functionsPane.getChildren().add(addLoyalClientButton);
            functionsPane.getChildren().add(clientSortNameButton);
            searchField.setPromptText("Clients search...");
        }
        else if(receiptsModeRadio.isSelected()){
            initCashierReceiptsManipulationTools();

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
        productMapGetID = productData.stream()
                .collect(Collectors.toMap(Product::getProduct_name, Product::getId_product));
        productMapGetName = productData.stream()
                .collect(Collectors.toMap(Product::getId_product, Product::getProduct_name));
    }
    private void updateCategoryTable(){
        categoryData = new CategoryService().getAllCategories();
        categoryMapGetID = categoryData.stream()
                .collect(Collectors.toMap(Category::getCategory_name, Category::getCategory_number));
        categoryMapGetName = categoryData.stream()
                .collect(Collectors.toMap(Category::getCategory_number, Category::getCategory_name));
    }
    private void updateEmployeesTable(){
        employeeData = new EmployeeService().getAllEmployees();
        employeeDataMapGetName = employeeData.stream()
                .collect(Collectors.toMap(Employee::getId_employee, Employee::getFullName));
        employeeDataMapGetID = employeeData.stream()
                .collect(Collectors.toMap(Employee::getFullName, Employee::getId_employee));
    }

    private void showGenericProducts(){
        TableColumn<Product, Integer> id = new TableColumn<>("ID");
        TableColumn<Product, String> name = new TableColumn<>("Name");
        TableColumn<Product, Integer> category = new TableColumn<>("Category");
        TableColumn<Product, Double> price = new TableColumn<>("Price");
        TableColumn<Product, Integer> characteristic = new TableColumn<>("Characteristics");

        id.setCellValueFactory(new PropertyValueFactory<>("id_product"));
        name.setCellValueFactory(new PropertyValueFactory<>("product_name"));
        category.setCellValueFactory(new PropertyValueFactory<>("category_name"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        characteristic.setCellValueFactory(new PropertyValueFactory<>("characteristics"));

        dataTable.getColumns().clear();
        dataTable.getColumns().addAll(id, name, category, characteristic);
        updateProductTable();
        dataTable.setItems(productData);
    }

    private void showStoreProducts(){
        TableColumn<Store_Product, Integer> id = new TableColumn<>("ID");
        TableColumn<Store_Product, String> name = new TableColumn<>("Name");
        TableColumn<Store_Product, String> upc = new TableColumn<>("UPC");
        TableColumn<Store_Product, Double> price = new TableColumn<>("Price");
        TableColumn<Store_Product, Integer> products_num = new TableColumn<>("Quantity");
        TableColumn<Store_Product, Boolean> promotional = new TableColumn<>("Is promotional");

        ObservableList<Store_Product> store_products =  new ProductService().getStoreProducts();

        id.setCellValueFactory(new PropertyValueFactory<>("Product_id_product"));
        name.setCellValueFactory(new PropertyValueFactory<>("productName"));
        upc.setCellValueFactory(new PropertyValueFactory<>("Store_Product_UPC"));
        price.setCellValueFactory(new PropertyValueFactory<>("selling_price"));
        products_num.setCellValueFactory(new PropertyValueFactory<>("products_number"));
        promotional.setCellValueFactory(new PropertyValueFactory<>("promotional_product"));

        dataTable.getColumns().clear();
        dataTable.getColumns().addAll(id, name, upc, price, products_num, promotional);
        dataTable.setItems(store_products);
    }

    private void showPromotedStoreProducts(boolean promoted) {
        TableColumn<Store_Product, Integer> id = new TableColumn<>("ID");
        TableColumn<Store_Product, String> name = new TableColumn<>("Name");
        TableColumn<Store_Product, String> upc = new TableColumn<>("UPC");
        TableColumn<Store_Product, Double> price = new TableColumn<>("Price");
        TableColumn<Store_Product, Integer> products_num = new TableColumn<>("Quantity");
        TableColumn<Store_Product, Boolean> promotional = new TableColumn<>("Is promotional");

        ObservableList<Store_Product> store_products = new ProductService().getPromotedStoreProducts(promoted);

        id.setCellValueFactory(new PropertyValueFactory<>("Product_id_product"));
        name.setCellValueFactory(new PropertyValueFactory<>("productName"));
        upc.setCellValueFactory(new PropertyValueFactory<>("Store_Product_UPC"));
        price.setCellValueFactory(new PropertyValueFactory<>("selling_price"));
        products_num.setCellValueFactory(new PropertyValueFactory<>("products_number"));
        promotional.setCellValueFactory(new PropertyValueFactory<>("promotional_product"));

        dataTable.getColumns().clear();
        dataTable.getColumns().addAll(id, name, upc, price, products_num, promotional);
        dataTable.setItems(store_products);
    }

    private void showCategories(){
        updateProductTable();
        updateCategoryTable();

        TableColumn<Category, Integer> id = new TableColumn<>("ID");
        TableColumn<Category, String> name = new TableColumn<>("Name");
        TableColumn<Category, Integer> count = new TableColumn<>("Overall products of this category");
        TableColumn<Category, Integer> count_store = new TableColumn<>("Available products of this category");

        id.setCellValueFactory(new PropertyValueFactory<>("category_number"));
        name.setCellValueFactory(new PropertyValueFactory<>("category_name"));

        for(int i = 0; i < categoryData.size(); i++){
            int counter = 0;
            for(int j = 0; j < productData.size(); j++){
                if (categoryData.get(i).getCategory_number() == productData.get(j).getCategory_number()) {
                    counter++;
                }
            }
            categoryData.get(i).setProducts_count(counter);
        }

        count.setCellValueFactory(new PropertyValueFactory<>("products_count"));

        LinkedHashMap<String, List<String>> dependencies = new CategoryService().getAvailableProductsByCategory();
        for (String category : dependencies.keySet()) {
            List<String> products = dependencies.get(category);
            for(int i = 0; i < categoryData.size(); i++) {
                if (Integer.parseInt(category) == categoryData.get(i).getCategory_number()) {
                    categoryData.get(i).setAvailableProducts_count(products.size());
                }
            }
        }

        count_store.setCellValueFactory(new PropertyValueFactory<>("availableProducts_count"));

        dataTable.getColumns().clear();
        dataTable.getColumns().addAll(id, name, count, count_store);
        dataTable.setItems(categoryData);
    }

    private void showClientsCards(){
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

    public static void updateStoreProductTable(){
        storeProductData = new StoreProductService().getAllStoreProducts();
    }
    private void addNewStoreProduct() {
        StoreProductManager.initProfile(null, null, null, "New store product");
    }
    private void editSelectedStoreProduct() {
        int selectedIndex = dataTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Store_Product selected = (Store_Product)dataTable.getItems().get(selectedIndex);
            Store_Product prom;
            if (selected.isPromotional_product()){
                prom = selected;
                selected = storeProductData.stream()
                        .filter(product -> prom.getStore_Product_UPC() != null && prom.getStore_Product_UPC().equals(product.getSale_UPC_prom()))
                        .findFirst()
                        .orElse(null);
            } else if (selected.getSale_UPC_prom() != null){
                Store_Product finalSelected = selected;
                prom = storeProductData.stream()
                        .filter(product -> finalSelected.getSale_UPC_prom() != null && finalSelected.getSale_UPC_prom().equals(product.getStore_Product_UPC()))
                        .findFirst()
                        .orElse(null);;
            } else {
                prom = null;
            }
            StoreProductManager.initProfile(selected, prom, null, selected.getProductName() + " product");
        }
    }

    private void initEmployeesManipulationTools(){
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

    private void initClientsManipulationTools(){
        addClient = new Button("Add client");
        addClient.setLayoutX(50); addClient.setLayoutY(220);
        addClient.setPrefWidth(120); addClient.setPrefHeight(30);
        addClient.setFont(new Font(13));
        addClient.setOnAction(actionEvent -> addNewCustomerCard());

        editClient = new Button("Edit client");
        editClient.setLayoutX(20); editClient.setLayoutY(270);
        editClient.setPrefWidth(120); editClient.setPrefHeight(30);
        editClient.setFont(new Font(13));
        editClient.setOnAction(actionEvent -> editSelectedCustomerCard());
    }

    private void initCategoriesManipulationTools(){
        addCategory = new Button("Add category");
        addCategory.setLayoutX(20); addCategory.setLayoutY(220);
        addCategory.setPrefWidth(120); addCategory.setPrefHeight(30);
        addCategory.setFont(new Font(13));
        addCategory.setOnAction(actionEvent -> addNewCategory());

        editCategory = new Button("Edit category");
        editCategory.setLayoutX(20); editCategory.setLayoutY(270);
        editCategory.setPrefWidth(120); editCategory.setPrefHeight(30);
        editCategory.setFont(new Font(13));
        editCategory.setOnAction(actionEvent -> editSelectedCategory());
    }

    private void initGoodsManipulationTools(){
        addProduct = new Button("Add product");
        addProduct.setLayoutX(20); addProduct.setLayoutY(220);
        addProduct.setPrefWidth(120); addProduct.setPrefHeight(30);
        addProduct.setFont(new Font(13));
        addProduct.setOnAction(actionEvent -> addNewProduct());

        editProduct = new Button("Edit product");
        editProduct.setLayoutX(20); editProduct.setLayoutY(270);
        editProduct.setPrefWidth(120); editProduct.setPrefHeight(30);
        editProduct.setFont(new Font(13));
        editProduct.setOnAction(actionEvent -> editSelectedProduct());
    }

    private void initStoreProducts(){
        addStoreProduct = new Button("Add product");
        addStoreProduct.setLayoutX(20); addStoreProduct.setLayoutY(220);
        addStoreProduct.setPrefWidth(120); addStoreProduct.setPrefHeight(30);
        addStoreProduct.setFont(new Font(13));
        addStoreProduct.setOnAction(actionEvent -> addNewStoreProduct());

        editStoreProduct = new Button("Edit store product");
        editStoreProduct.setLayoutX(20); editStoreProduct.setLayoutY(270);
        editStoreProduct.setPrefWidth(120); editStoreProduct.setPrefHeight(30);
        editStoreProduct.setFont(new Font(13));
        editStoreProduct.setOnAction(actionEvent -> editSelectedStoreProduct());
    }

    private void initReceipts(){
        goodsAmountSoldInSetTime = new Button("Goods amount sold\n      for set period");
        goodsAmountSoldInSetTime.setLayoutX(20); goodsAmountSoldInSetTime.setLayoutY(100);
        goodsAmountSoldInSetTime.setPrefWidth(120); goodsAmountSoldInSetTime.setPrefHeight(50);
        goodsAmountSoldInSetTime.setFont(new Font(13));

        //Якщо всі касири - вилізає віконце тільки промптом на період
        //Якщо конкретний касир - буде ще промпт і на прізвище
        checksSetCashiersAndTime = new ComboBox<>(FXCollections.observableArrayList("Of all cashiers", "Of set cashiers"));
        checksSetCashiersAndTime.setPromptText("Receipts for set period");
        checksSetCashiersAndTime.setLayoutX(20); checksSetCashiersAndTime.setLayoutY(170);
        checksSetCashiersAndTime.setPrefWidth(120); checksSetCashiersAndTime.setPrefHeight(30);

        //Аналогічний принцип
        checksAllCashiersAndTime = new ComboBox<>(FXCollections.observableArrayList("Of all cashiers", "Of set cashiers"));
        checksAllCashiersAndTime.setPromptText("Sum of sold goods");
        checksAllCashiersAndTime.setLayoutX(20); checksAllCashiersAndTime.setLayoutY(220);
        checksAllCashiersAndTime.setPrefWidth(120); checksAllCashiersAndTime.setPrefHeight(30);
    }

    @FXML
    private void provideManagerMode(ActionEvent e){

        functionsPane.getChildren().clear();

        if(employeesMode.isSelected()){
            initEmployeesManipulationTools();
            functionsPane.getChildren().add(addEmployee);
            functionsPane.getChildren().add(emplPhoneAddressBySur);
            functionsPane.getChildren().add(editEmployee);
            searchField.setPromptText("Employees search...");
            showEmployees();
        }
        else if(managerClientsMode.isSelected()){
            initClientsManipulationTools();
            functionsPane.getChildren().add(addClient);
            functionsPane.getChildren().add(editClient);
            searchField.setPromptText("Clients search...");
            showClientsCards();
        }
        else if(categoryMode.isSelected()){
            initCategoriesManipulationTools();
            functionsPane.getChildren().add(addCategory);
            functionsPane.getChildren().add(editCategory);
            searchField.setPromptText("Categories search...");
            showCategories();
        }
        else if(managerProductsMode.isSelected()){
            initGoodsManipulationTools();
            functionsPane.getChildren().add(addProduct);
            functionsPane.getChildren().add(editProduct);
            searchField.setPromptText("Products search...");
            showGenericProducts();
        }
        else if(storeProductsMode.isSelected()){
            initStoreProducts();
            functionsPane.getChildren().add(addStoreProduct);
            functionsPane.getChildren().add(editStoreProduct);
            searchField.setPromptText("Products in store search...");
            showStoreProducts();
        }
        else if(managerReceiptsMode.isSelected()){
            initReceipts();
            functionsPane.getChildren().add(goodsAmountSoldInSetTime);
            functionsPane.getChildren().add(checksSetCashiersAndTime);
            functionsPane.getChildren().add(checksAllCashiersAndTime);
            searchField.setPromptText("Receipts search...");
        }
    }

    @FXML
    protected void openProfile(ActionEvent e) {
        EmployeeProfile.initProfile(UserInfo.employeeProfile, "Manager profile");
    }
    @FXML
    public void quit(ActionEvent actionEvent) throws IOException {
        UserInfo.id = null;
        UserInfo.position = null;
        UserInfo.employeeProfile = null;
        HelloApplication.setScene(HelloApplication.mainStage, Authorization.FXML_LOADER(), Authorization.WIDTH, Authorization.HEIGHT, "Ласкаво просимо в ZLAGODA!");
    }

    public void createCheck(ActionEvent actionEvent) throws IOException {
        Stage s = new Stage();
        HelloApplication.setScene(s, new FXMLLoader(HelloApplication.class.getResource("CheckMenu.fxml")), 967, 644, "Каса");
    }

    @FXML
    public void printReport(ActionEvent actionEvent) {
        String template = "";
        if (managerClientsMode.isSelected())
            template = "customer_report";
        else if (employeesMode.isSelected())
            template = "employee_report";
        else if (categoryMode.isSelected())
            template = "category_report";
        else if (managerProductsMode.isSelected())
            template = "product_report";
        else if (storeProductsMode.isSelected())
            template = "store_product_report";
        else
            return;
        DataPrinter.createReport(new DataBaseHandler().getConnection(), new HashMap<>(), new JasperService().getReport(template));
        DataPrinter.showReport();
    }
    @FXML
    public void checkLog(ActionEvent actionEvent) throws IOException {
        HelloApplication.setScene(HelloApplication.mainStage,  new FXMLLoader(LogModeView.class.getResource("LogView.fxml")), WIDTH, HEIGHT, "Log Mode View");
    }
}
