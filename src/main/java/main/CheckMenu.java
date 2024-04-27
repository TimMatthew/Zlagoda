package main;

import Entities.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.*;
import sessionmanagement.UserInfo;
import utils.LogAction;
import utils.UPC;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class CheckMenu implements Initializable{
    public static Stage stage;

    public static ObservableList<Store_Product> storeProductsData;
    private static ObservableList<Store_Product> storeProductsDataToShow;
    public static ObservableList<Sale> sales;

    @FXML
    public Button editProduct;
    @FXML
    public TableView storeProductsTable;
    @FXML
    public Label checkIllustration;
    public Label checkTitle, operatorTitle;
    @FXML
    public ListView<Sale> saleListView;
    public TextField searchField;
    public ChoiceBox<String> searchModeChoiceBox, categoryChoiceBox;
    @FXML
    private Button addProduct;
    public Check newCheck;
    public static String checkID;
    private Customer_Card cc;
    private static Employee cashier;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        checkID = UPC.generateRandomUPC(10);
        sales = FXCollections.observableArrayList();
        storeProductsData = new StoreProductService().getAllStoreProducts();
        checkTitle.setText("ТМ 'Злагода'\n №" + checkID);
        operatorTitle.setText("Оператор: " + cashier.getFullName());

        TableColumn<Store_Product, Integer> upc = new TableColumn<>("UPC");
        TableColumn<Store_Product, String> product = new TableColumn<>("Product");
        TableColumn<Store_Product, Integer> price = new TableColumn<>("Price");
        TableColumn<Store_Product, Integer> count = new TableColumn<>("Count");
        TableColumn<Store_Product, Boolean> promotional = new TableColumn<>("Promotional");

        upc.setCellValueFactory(new PropertyValueFactory<>("Store_Product_UPC"));
        product.setCellValueFactory(new PropertyValueFactory<>("productName"));
        price.setCellValueFactory(new PropertyValueFactory<>("selling_price"));
        count.setCellValueFactory(new PropertyValueFactory<>("products_number"));
        promotional.setCellValueFactory(new PropertyValueFactory<>("promotional_product"));
        promotional.setCellFactory(column -> new CheckBoxTableCell<>());

        promotional.setCellValueFactory(cellData -> {
            Store_Product cellValue = cellData.getValue();
            return new SimpleBooleanProperty(cellValue.isPromotional_product());
        });

        searchModeChoiceBox.setItems(FXCollections.observableList(List.of(new String[]{
                "Name",
                "UPC"
        })));
        searchModeChoiceBox.setValue("Name");

        categoryChoiceBox.getItems().add("All categories");
        categoryChoiceBox.setValue("All categories");
        categoryChoiceBox.getItems().addAll(new CategoryService().getCategoryNames());

        saleListView.setItems(sales);

        storeProductsData = new StoreProductService().getAllStoreProducts();
        storeProductsDataToShow = FXCollections.observableArrayList(storeProductsData);
        storeProductsTable.getColumns().addAll(upc, product, price, count, promotional);
        storeProductsTable.setItems(storeProductsDataToShow);
    }

    public static void updateCheckMenuData(Store_Product updatedProduct, Sale sale) {
        for (int i = 0; i < storeProductsData.size(); i++) {
            if (storeProductsData.get(i).getStore_Product_UPC().equals(updatedProduct.getStore_Product_UPC())) {
                storeProductsData.set(i, updatedProduct);
                break;
            }
        }
        for (int i = 0; i < storeProductsDataToShow.size(); i++) {
            if (storeProductsDataToShow.get(i).getStore_Product_UPC().equals(updatedProduct.getStore_Product_UPC())) {
                storeProductsDataToShow.set(i, updatedProduct);
                break;
            }
        }
        if (sale != null) {
            for (int i = 0; i < sales.size(); i++) {
                if (sales.get(i).getStore_Product_UPC().equals(sale.getStore_Product_UPC())) {
                    sales.set(i, sale);
                    break;
                }
            }
        }
    }

    public void addStoreProductToCheck(ActionEvent actionEvent) throws IOException {

        int selectedIndex = storeProductsTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0)
            return;

        Store_Product sp = (Store_Product)storeProductsTable.getItems().get(selectedIndex);
        EnterAmount.initWindow(stage,sp,null);
    }

    public static void addProductIntoCheck(Store_Product storeProduct, int storeAmountForCheck) {
        Sale sale = new Sale(storeProduct.getStore_Product_UPC(), checkID, storeAmountForCheck, storeProduct.getSelling_price());
        sale.setProduct_name(storeProduct.getProductName());
        sales.add(sale);
        storeProduct.setProducts_number(storeProduct.getProducts_number()-storeAmountForCheck);
        updateCheckMenuData(storeProduct, null);
    }

    public void createCheck(ActionEvent actionEvent) throws SQLException {
        if (sales.isEmpty())
            return;

        int custCardPercent = cc==null ? 0 : cc.getPercent();
        String custCardNumber = null;
        double sumTotal=0;

        for(Sale s: sales){
            sumTotal += s.getTotal_price();
        }

        double totalVat = sumTotal*0.2;
        newCheck = new Check(checkID, cashier.getId_employee(), cc == null ? null : cc.getCard_number(), Timestamp.valueOf(LocalDateTime.now()),sumTotal, totalVat);
        new CheckService().addCheck(newCheck);

        SaleService saleService = new SaleService();
        StoreProductService sps = new StoreProductService();
        for (Sale s : sales){
            saleService.addSale(s);
            sps.updateStoreProduct(storeProductsData.stream()
                    .filter(obj -> obj.getStore_Product_UPC().equals(s.getStore_Product_UPC()))
                    .toList().get(0));
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Check registering success");
        alert.setHeaderText("Check was added");
        alert.setContentText("");
        alert.showAndWait();

        new LogService().addLog(LogAction.ADD_CHECK, " added new check " + newCheck);

        stage.close();
    }

    public void editSale(ActionEvent actionEvent) {
        int selectedIndex = saleListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0)
            return;
        Sale sale = saleListView.getItems().get(selectedIndex);
        EnterAmount.initWindow(stage,null,sale);
    }

    public void cancel(ActionEvent actionEvent) {
        stage.close();
    }

    public static void initMenu(){
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(HelloApplication.mainStage);
        stage.setResizable(false);
        cashier = UserInfo.employeeProfile;
        try {
            HelloApplication.setScene(stage, new FXMLLoader(HelloApplication.class.getResource("CheckMenu.fxml")), 967, 644, "Каса");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void search(MouseEvent mouseEvent) {
        String query = searchField.getText().trim();
        String category = categoryChoiceBox.getValue();
        ProductService ps = new ProductService();

        storeProductsDataToShow.clear();

        if (category.equals("All categories")) {
            switch (searchModeChoiceBox.getValue()) {
                case "Name" -> storeProductsDataToShow.addAll(storeProductsData.stream()
                        .filter(obj -> obj.getProductName().startsWith(query))
                        .collect(Collectors.toList()));
                case "UPC" -> storeProductsDataToShow.addAll(storeProductsData.stream()
                        .filter(obj -> obj.getStore_Product_UPC().startsWith(query))
                        .collect(Collectors.toList()));
            }
        } else {

            switch (searchModeChoiceBox.getValue()) {
                case "Name" -> storeProductsDataToShow.addAll(storeProductsData.stream()
                        .filter(obj -> obj.getProductName().startsWith(query) && ps.getProduct(obj.getProduct_id_product()).getCategory_name().startsWith(category))
                        .toList());
                case "UPC" -> storeProductsDataToShow.addAll(storeProductsData.stream()
                        .filter(obj -> obj.getStore_Product_UPC().startsWith(query) && ps.getProduct(obj.getProduct_id_product()).getCategory_name().startsWith(category))
                        .toList());
            }
        }

        storeProductsTable.setItems(storeProductsDataToShow);
    }
}
