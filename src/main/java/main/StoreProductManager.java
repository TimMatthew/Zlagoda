package main;

import Entities.Store_Product;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.StoreProductService;
import sessionmanagement.UserInfo;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class StoreProductManager implements Initializable {

    public static Store_Product currentStoreProduct;
    public static Store_Product currentPromStoreProduct;
    private static String selectedProductName;
    private boolean isPromotional;
    private static StoreProductService service;
    private static Stage stage;

    protected static FXMLLoader FXML_LOADER(){
        return new FXMLLoader(StoreProductManager.class.getResource("StoreProductManager.fxml"));
    }
    protected static final int WIDTH = 428, HEIGHT = 428;
    @FXML
    public Label errorLabel, promCountTitleLabel, promInfoLabel;
    @FXML
    private Label productLabel, priceLabel, countLabel, promCountLabel;
    @FXML
    private Spinner<Integer> countSpinner, promCountSpinner;
    @FXML
    private Spinner<Double> priceSpinner;
    @FXML
    public CheckBox promotionalCheckBox;

    @FXML
    public ChoiceBox<String> productChoiceBox;

    @FXML
    private Button editButton, saveButton, deleteButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productChoiceBox.setItems(FXCollections.observableArrayList(MainMenu.productMapGetID.keySet()));
        service = new StoreProductService();

        if (selectedProductName != null)
            productChoiceBox.setValue(selectedProductName);

        promCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 5));
        countSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 5));
        priceSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, Double.MAX_VALUE, 0, 5));

        if (currentStoreProduct != null) {
            String product = currentStoreProduct.getProductName();
            productLabel.setText(product);
            countLabel.setText(String.valueOf(currentStoreProduct.getProducts_number()));
            priceLabel.setText(String.valueOf(currentStoreProduct.getSelling_price()));

            productChoiceBox.setValue(product);
            productChoiceBox.setVisible(false);

            if (currentPromStoreProduct != null){
                currentPromStoreProduct = service.getStoreProduct(currentStoreProduct.getSale_UPC_prom());
                promotionalCheckBox.setSelected(true);
                isPromotional = true;
                promCountLabel.setText(String.valueOf(currentPromStoreProduct.getProducts_number()));
            }
            promotionalCheckBox.setDisable(true);
            promotionalChanged(null);

            if (UserInfo.position.equals("Cashier"))
                editButton.setVisible(false);
            saveButton.setVisible(false);
            deleteButton.setVisible(false);

            setLabelsVisible(true);
            setTextFieldsVisible(false);
        } else {
            promotionalChanged(null);
            editButton.setVisible(false);
            setLabelsVisible(false);
            setTextFieldsVisible(true);
        }

    }

    @FXML
    private void handleEditButtonAction() {
        setLabelsVisible(false);
        setTextFieldsVisible(true);

        priceSpinner.getValueFactory().setValue(Double.valueOf(priceLabel.getText()));
        countSpinner.getValueFactory().setValue(Integer.valueOf(countLabel.getText()));
        promotionalCheckBox.setDisable(false);
        if (promotionalCheckBox.isSelected())
            promCountSpinner.getValueFactory().setValue(Integer.valueOf(promCountLabel.getText()));

        isPromotional = promotionalCheckBox.isSelected();
        editButton.setVisible(false);
        deleteButton.setVisible(true);
        saveButton.setVisible(true);
    }

    @FXML
    private void handleSaveButtonAction() throws SQLException {
        String productName = productChoiceBox.getValue();

        if (!MainMenu.productMapGetID.containsKey(productName)){
            setErrorMessage("Product must be chosen.");
            return;
        }

        int id_product = MainMenu.productMapGetID.get(productName);
        double price = priceSpinner.getValue();
        int product_number = countSpinner.getValue();
        boolean promotional_product = promotionalCheckBox.isSelected();
        int promProduct_number = 0;


        if (currentStoreProduct != null && productName.equals(productLabel.getText()) && Objects.equals(priceSpinner.getValue(), Double.valueOf(priceLabel.getText())) &&
                Objects.equals(countSpinner.getValue(), Integer.valueOf(countLabel.getText())) &&
                isPromotional == promotional_product && (!isPromotional || Objects.equals(promCountSpinner.getValue(), Integer.valueOf(promCountLabel.getText())))) {
            setLabelsVisible(true);
            setTextFieldsVisible(false);
            editButton.setVisible(true);
            saveButton.setVisible(false);
            deleteButton.setVisible(false);
            return;
        }

        String oldPromUPC = "";
        if (promotional_product) {
            promProduct_number = promCountSpinner.getValue();
            if (currentPromStoreProduct != null)
                currentPromStoreProduct = new Store_Product(currentPromStoreProduct.getStore_Product_UPC(), null, id_product, service.promotionalPrice(price), promProduct_number, true);
            else
                currentPromStoreProduct = new Store_Product(null, id_product, service.promotionalPrice(price), promProduct_number, true);
        }
        else {
            if (isPromotional)
                oldPromUPC = currentPromStoreProduct.getStore_Product_UPC();
            currentPromStoreProduct = null;
        }

        if (!valuesValidation(productName, price, product_number, promProduct_number, promotional_product))
            return;

        String promUPC = promotional_product ? currentPromStoreProduct.getStore_Product_UPC() : null;
        if (currentStoreProduct == null){
            Store_Product newProduct = new Store_Product(promUPC, id_product, price, product_number, false);

            if (!service.addStoreProduct(newProduct)){
                Store_Product oldProduct = service.getStoreProductByProductId(id_product);
                oldProduct.setSelling_price(price);
                oldProduct.setProducts_number(oldProduct.getProducts_number() + product_number);
                if (oldProduct.getSale_UPC_prom() != null){
                    Store_Product oldPromProduct = service.getStoreProduct(oldProduct.getSale_UPC_prom());
                    if(!promotional_product){
                        service.deleteStoreProduct(oldPromProduct.getStore_Product_UPC());
                        oldProduct.setSale_UPC_prom(null);
                    } else {
                        oldPromProduct.setProducts_number(oldPromProduct.getProducts_number() + promProduct_number);
                        oldPromProduct.setSelling_price(service.promotionalPrice(price));
                        service.updateStoreProduct(oldPromProduct);
                    }
                }
                if (oldProduct.getSale_UPC_prom() == null && promotional_product){
                    oldProduct.setSale_UPC_prom(promUPC);
                    service.addStoreProduct(currentPromStoreProduct);
                }
                service.updateStoreProduct(oldProduct);
                stage.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("The store product already exists.");
                alert.setContentText("The price was updated and product count was added.");
                alert.showAndWait();

                return;
            } else {
                if (promotional_product)
                    service.addStoreProduct(currentPromStoreProduct);
            }
            MainMenu.updateStoreProductTable();
            stage.close();
        } else {
            Store_Product upd = new Store_Product(currentStoreProduct.getStore_Product_UPC(), promUPC, id_product, price, product_number, false);
            try {
                if (promotional_product){
                    Store_Product promUpd =  new Store_Product(promUPC, null, id_product, service.promotionalPrice(price), promProduct_number, true);
                    if (isPromotional)
                        service.updateStoreProduct(promUpd);
                    else
                        service.addStoreProduct(promUpd);
                }
                System.out.println(isPromotional + " " + promotional_product);
                if (isPromotional && !promotional_product){
                    service.deleteStoreProduct(oldPromUPC);
                }
                service.updateStoreProduct(upd);
                MainMenu.updateStoreProductTable();
            } catch (IndexOutOfBoundsException | NullPointerException ignored) {}
        }

        setLabelsVisible(true);
        setTextFieldsVisible(false);


        promotionalCheckBox.setDisable(true);
        productLabel.setText(productChoiceBox.getValue());
        priceLabel.setText(String.valueOf(priceSpinner.getValue()));
        countLabel.setText(String.valueOf(countSpinner.getValue()));
        if (promotionalCheckBox.isSelected())
            promCountLabel.setText(String.valueOf(promCountSpinner.getValue()));

        deleteButton.setVisible(false);
        editButton.setVisible(true);
        saveButton.setVisible(false);
    }

    @FXML
    public void handleDeleteButtonAction(ActionEvent actionEvent) throws SQLException {
        if (currentStoreProduct != null){
            if (currentStoreProduct.getSale_UPC_prom() != null)
                service.deleteStoreProduct(currentStoreProduct.getSale_UPC_prom());
            service.deleteStoreProduct(currentStoreProduct.getStore_Product_UPC());
            MainMenu.updateStoreProductTable();
            currentStoreProduct = null;
        }
        stage.close();
    }

    @FXML
    public void promotionalChanged(ActionEvent actionEvent) {
        if (promotionalCheckBox.isSelected()){
            promInfoLabel.setVisible(true);
            promCountTitleLabel.setVisible(true);
            if (editButton.isVisible())
                promCountLabel.setVisible(true);
            else
                promCountSpinner.setVisible(true);
        } else {
            promInfoLabel.setVisible(false);
            promCountTitleLabel.setVisible(false);
            promCountSpinner.setVisible(false);
            promCountLabel.setVisible(false);
        }
    }
    private void setLabelsVisible(boolean visible) {
        productLabel.setVisible(visible);
        priceLabel.setVisible(visible);
        countLabel.setVisible(visible);
        if (promotionalCheckBox.isSelected())
            promCountLabel.setVisible(visible);
    }

    private void setTextFieldsVisible(boolean visible) {
        countSpinner.setVisible(visible);
        priceSpinner.setVisible(visible);
        productChoiceBox.setVisible(visible);
        if (promotionalCheckBox.isSelected())
            promCountSpinner.setVisible(visible);

    }

    private boolean valuesValidation(String productName, double selling_price, int products_number, int promProducts_number, boolean promotional_product) {

        if (productName.isEmpty()) {
            setErrorMessage("All fields are required");
            return false;
        }

        if (selling_price <= 0 || products_number <= 0 || (promotional_product && promProducts_number <= 0)) {
            setErrorMessage("Values must be above 0.");
            return false;
        }


        errorLabel.setVisible(false);
        return true;
    }

    private void setErrorMessage(String msg){
        errorLabel.setVisible(true);
        errorLabel.setText(msg);
    }
    public static void initProfile(Store_Product storeProduct, Store_Product promStoreProduct, String productName, String title){
        currentStoreProduct = storeProduct;
        currentPromStoreProduct = promStoreProduct;
        selectedProductName = productName;
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(HelloApplication.mainStage);
        stage.setResizable(false);

        try {
            HelloApplication.setScene(stage, FXML_LOADER(), WIDTH, HEIGHT, title);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
