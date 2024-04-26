package main;

import Entities.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.CheckService;
import services.StoreProductService;
import sessionmanagement.UserInfo;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class CheckMenu implements Initializable{



    public static ObservableList<Store_Product> storeProductsData;
    protected static HashMap<Store_Product, Integer> storeProductsInCheck;
    protected static ArrayList<Sale> storeProductSales;

    @FXML
    public Button editProduct;
    @FXML
    public TableView storeProductsTable;
    @FXML
    public Label checkIllustration;
    @FXML
    private Button addProduct;
    public Check newCheck;
    private Customer_Card cc;
    private static Stage st;
    private static Employee cashier;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        storeProductsData = new StoreProductService().getAllStoreProducts();
        checkIllustration = new Label();
        checkIllustration.setText("ТМ 'Злагода'\n №4358734954");

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

        storeProductsData = new StoreProductService().getAllStoreProducts();
        storeProductsTable.getColumns().clear();
        storeProductsTable.getColumns().addAll(upc, product, price, count, promotional);
        storeProductsTable.setItems(storeProductsData);
        storeProductsInCheck = new HashMap<>();
        updateCheckMenuData();
    }

    @FXML
    private void quit(ActionEvent actionEvent) throws IOException {
        HelloApplication.setScene(HelloApplication.mainStage, new FXMLLoader(HelloApplication.class.getResource("MainMenu.fxml")), MainMenu.WIDTH, MainMenu.HEIGHT, "Welcome to ZLAGODA!");
        st.close();
    }

    public static void updateCheckMenuData(){
        storeProductsData = new StoreProductService().getAllStoreProducts();
        cashier = UserInfo.employeeProfile;
    }

    public void addStoreProductToCheck(ActionEvent actionEvent) throws IOException {

        Store_Product storeProductToAdd = null;

        int selectedIndex = storeProductsTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            storeProductToAdd = (Store_Product)storeProductsTable.getItems().get(selectedIndex);
            Store_Product prom;
            if (storeProductToAdd.isPromotional_product()){
                prom = storeProductToAdd;
                storeProductToAdd = storeProductsData.stream()
                        .filter(product -> prom.getStore_Product_UPC() != null && prom.getStore_Product_UPC().equals(product.getSale_UPC_prom()))
                        .findFirst()
                        .orElse(null);
            } else if (storeProductToAdd.getSale_UPC_prom() != null){
                Store_Product finalSelected = storeProductToAdd;
                prom = storeProductsData.stream()
                        .filter(product -> finalSelected.getSale_UPC_prom() != null && finalSelected.getSale_UPC_prom().equals(product.getStore_Product_UPC()))
                        .findFirst()
                        .orElse(null);;
            } else {
                prom = null;
            }
        }
        Stage s = new Stage();
        EnterAmount.setStoreProduct(storeProductToAdd);
        HelloApplication.setScene(s, new FXMLLoader(HelloApplication.class.getResource("EnterAmount.fxml")), 290, 178, "Enter an amount");

        EnterAmount.st = s;
    }

    public static void addProductIntoCheck(Store_Product storeProduct, int productAmountForCheck) {

        storeProductsInCheck.put(storeProduct, productAmountForCheck);


        storeProduct.setProducts_number(storeProduct.getProducts_number()-productAmountForCheck);

    }

    public void edit(ActionEvent actionEvent){

    }

    public void createCheck(ActionEvent actionEvent) throws SQLException {

        int custCardPercent = cc==null ? 0 : cc.getPercent();
        String custCardNumber = null;
        double sumTotal=0, totalVat=0;


        Check newCheck = new Check(UUID.randomUUID().toString());
        ArrayList<Sale> productsSales = new ArrayList<>();

        for (Map.Entry<Store_Product, Integer> product : storeProductsInCheck.entrySet()) {
            Sale s = new Sale(product.getKey().getStore_Product_UPC(),
                    newCheck.getCheck_number(),
                    product.getKey().getProducts_number(),
                    product.getKey().getSelling_price());
            productsSales.add(s);
        }

        for(Sale s: productsSales){
            sumTotal += s.getProduct_number()*s.getSelling_price();
        }

        totalVat = sumTotal*0.2;

        newCheck.setEmployee_id_employee(cashier.getId_employee());
        newCheck.setCustomerCard_card_number(null);
        newCheck.setPrint_date(Timestamp.valueOf(LocalDateTime.now()));
        newCheck.setSum_total(sumTotal);
        newCheck.setVat(totalVat);

        new CheckService().addCheck(newCheck);
    }
}
