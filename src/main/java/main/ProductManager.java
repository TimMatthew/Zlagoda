package main;

import Entities.Employee;
import Entities.Product;
import Entities.Category;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.CategoryService;
import services.EmployeeService;
import services.ProductService;
import sessionmanagement.UserInfo;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ProductManager implements Initializable {


    public static Product currentProduct;
    private static Stage stage;
    private static Map<String, Integer> categoryMap;

    protected static FXMLLoader FXML_LOADER(){
        return new FXMLLoader(ProductManager.class.getResource("ProductManager.fxml"));
    }
    protected static final int WIDTH = 428, HEIGHT = 430;
    @FXML
    public Label errorLabel;
    @FXML
    private Label nameLabel, characteristicsLabel, categoryLabel;

    @FXML
    private TextField nameField;
    @FXML
    private TextArea characteristicsArea;
    @FXML
    public ComboBox<String> categoriesComboBox;

    @FXML
    private Button editButton, saveButton, deleteButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        categoryMap = MainMenu.categoryData.stream()
                .collect(Collectors.toMap(Category::getCategory_name, Category::getCategory_number));
        categoriesComboBox.setItems(FXCollections.observableArrayList(categoryMap.keySet()));

        if (currentProduct != null) {
            nameLabel.setText(currentProduct.getProduct_name());
            categoryLabel.setText(currentProduct.getCategory_name());
            characteristicsLabel.setText(currentProduct.getCharacteristics());

            categoriesComboBox.setValue(currentProduct.getCategory_name());
            categoriesComboBox.setVisible(false);

            if (UserInfo.position.equals("Cashier"))
                editButton.setVisible(false);
            saveButton.setVisible(false);
            deleteButton.setVisible(false);

            setLabelsVisible(true);
            setTextFieldsVisible(false);
        } else {
            editButton.setVisible(false);
            setLabelsVisible(false);
            setTextFieldsVisible(true);
        }

    }

    @FXML
    private void handleEditButtonAction() {
        setLabelsVisible(false);
        setTextFieldsVisible(true);

        nameField.setText(nameLabel.getText());
        characteristicsArea.setText(characteristicsLabel.getText());
        categoryLabel.setText(categoriesComboBox.getValue());

        editButton.setVisible(false);
        deleteButton.setVisible(true);
        saveButton.setVisible(true);
    }

    @FXML
    private void handleSaveButtonAction() throws SQLException {
        String name = nameField.getText().trim();
        String characteristics = characteristicsArea.getText().trim();
        String category = categoriesComboBox.getValue();
        int category_number;

        if (currentProduct != null && name.equals(nameLabel.getText()) && characteristics.equals(characteristicsLabel.getText()) &&
                category.equals(categoryLabel.getText())) {
            setLabelsVisible(true);
            setTextFieldsVisible(false);
            editButton.setVisible(true);
            saveButton.setVisible(false);
            deleteButton.setVisible(false);
            return;
        }

        if (!valuesValidation(name, characteristics, category))
            return;


        if (!categoryMap.containsKey(category)){
            Category newCategory = new Category(category);
            new CategoryService().addCategory(newCategory);
            category_number = newCategory.getCategory_number();
        } else {
            category_number = categoryMap.get(category);
        }

        ProductService ps = new ProductService();
        if (currentProduct == null){
            Product newProduct = new Product(category_number, name, characteristics);
            if (!ps.addProduct(newProduct)){
                setErrorMessage("The product is already added.");
                return;
            }
            MainMenu.productData.add(newProduct);
            stage.close();
        } else {
            Product upd = new Product(currentProduct.getId_product(), category_number, name, characteristics);
            try {
                ps.updateProduct(upd);
                int index = MainMenu.productData.indexOf(currentProduct);
                MainMenu.productData.add(index, upd);
                MainMenu.productData.remove(currentProduct);
            } catch (SQLException e) {
                setErrorMessage(e.getMessage());
                return;
            } catch (IndexOutOfBoundsException | NullPointerException ignored) {}
        }

        setLabelsVisible(true);
        setTextFieldsVisible(false);

        nameLabel.setText(nameField.getText());
        characteristicsLabel.setText(characteristicsArea.getText());
        categoryLabel.setText(categoriesComboBox.getValue());

        editButton.setVisible(true);
        saveButton.setVisible(false);
    }

    @FXML
    public void handleDeleteButtonAction(ActionEvent actionEvent) throws SQLException {
        if (currentProduct != null){
            new ProductService().deleteProduct(currentProduct.getId_product());
            MainMenu.productData.remove(currentProduct);
            currentProduct = null;
        }
        stage.close();
    }

    private void setLabelsVisible(boolean visible) {
        nameLabel.setVisible(visible);
        characteristicsLabel.setVisible(visible);
        categoryLabel.setVisible(visible);
    }

    private void setTextFieldsVisible(boolean visible) {
        nameField.setVisible(visible);
        characteristicsArea.setVisible(visible);
        categoriesComboBox.setVisible(visible);

    }

    private boolean valuesValidation(String name, String characteristics, String category) {

        if (name.isEmpty() || characteristics.isEmpty() || category.isEmpty()) {
            setErrorMessage("All fields are required");
            return false;
        }

        if (name.length() > 50) {
            setErrorMessage("The maximum name length has been exceeded.(" + name.length() + "/50)");
            return false;
        }

        if (characteristics.length() > 50) {
            setErrorMessage("The maximum characteristics length has been exceeded.(" + characteristics.length() + "/150)");
            return false;
        }

        if (category.length() > 50) {
            setErrorMessage("The maximum category length has been exceeded. (" + category.length() + "/50)");
            return false;
        }


        errorLabel.setVisible(false);
        return true;
    }

    private void setErrorMessage(String msg){
        errorLabel.setVisible(true);
        errorLabel.setText(msg);
    }

    public static void initProfile(Product empl, String title){
        currentProduct = empl;
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
