package main;

import Entities.Sale;
import Entities.Store_Product;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EnterAmount implements Initializable {
    public Button confirmButton;
    public Label invalid;
    public Label outnumbered;
    public TextField input;

    protected static Stage st;
    public static Store_Product storeProduct;

    protected static void setStoreProduct(Store_Product sp){
        storeProduct=sp;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        invalid.setVisible(false);
        outnumbered.setVisible(false);

    }

    public void confirmAddition(ActionEvent actionEvent) {
        String res = input.getText();
        if(!res.matches("[0-9]*$")) {
            outnumbered.setVisible(false);
            invalid.setVisible(true);
        }
        else if(Integer.parseInt(res)>storeProduct.getProducts_number()){
            outnumbered.setVisible(true);
            invalid.setVisible(false);
        }
        else{
            int storeAmountForCheck = Integer.parseInt(res);
            outnumbered.setVisible(false);
            invalid.setVisible(false);
            st.close();
            CheckMenu.addProductIntoCheck(storeProduct, storeAmountForCheck);
        }
    }
}
