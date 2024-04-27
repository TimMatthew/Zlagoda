package main;

import Entities.Employee;
import Entities.Sale;
import Entities.Store_Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class EnterAmount implements Initializable {
    public static Stage stage;
    public Button confirmButton;
    public Label invalid;
    public Label outnumbered;
    public Spinner<Integer> input;

    public static Store_Product storeProduct;
    public static Sale sale;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        invalid.setVisible(false);
        outnumbered.setVisible(false);

        if (sale != null){
            Optional<Store_Product> firstProductWithIdOne =  CheckMenu.storeProductsData.stream()
                    .filter(product -> product.getStore_Product_UPC().equals(sale.getStore_Product_UPC()))
                    .findFirst();
            firstProductWithIdOne.ifPresent(store_product -> storeProduct = store_product);
        }
        int start = sale == null ? 1 : sale.getProduct_number();
        int max;
        if (sale != null)
            max = Math.max(storeProduct.getProducts_number(), sale.getProduct_number());
        else max = storeProduct.getProducts_number();
        input.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, max, start, 1));
    }

    public void confirmAddition(ActionEvent actionEvent) {
        int res = input.getValue();
        if(res > storeProduct.getProducts_number() || res == 0){
            outnumbered.setVisible(true);
            invalid.setVisible(false);
        }
        else{
            ArrayList<Sale> alreadyAdded = new ArrayList<>(CheckMenu.sales.stream()
                                        .filter(obj -> obj.getStore_Product_UPC().equals(storeProduct.getStore_Product_UPC()))
                                        .toList());
            if (sale == null && alreadyAdded.isEmpty()) {
                outnumbered.setVisible(false);
                invalid.setVisible(false);
                stage.close();
                CheckMenu.addProductIntoCheck(storeProduct, res);
            } else if (sale != null && alreadyAdded.isEmpty()){
                int differance = sale.getProduct_number() - res;
                sale.setProduct_number(res);
                storeProduct.setProducts_number(storeProduct.getProducts_number() + differance);
                outnumbered.setVisible(false);
                invalid.setVisible(false);
                stage.close();
                CheckMenu.updateCheckMenuData(storeProduct, sale);
            }
            else {
                sale = alreadyAdded.get(0);
                sale.setProduct_number(sale.getProduct_number() + res);
                storeProduct.setProducts_number(storeProduct.getProducts_number() - res);
                outnumbered.setVisible(false);
                invalid.setVisible(false);
                stage.close();
                CheckMenu.updateCheckMenuData(storeProduct, sale);
            }
        }
    }


    public static void initWindow(Stage owner, Store_Product sp, Sale sl){
        stage = new Stage();
        storeProduct = sp;
        sale = sl;
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(owner);
        stage.setResizable(false);
        try {
            HelloApplication.setScene(stage, new FXMLLoader(EnterAmount.class.getResource("EnterAmount.fxml")), 311, 200, "Каса");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
