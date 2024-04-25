package main;

import Entities.Category;
import Entities.Customer_Card;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.CategoryService;
import services.CustomerCardService;
import sessionmanagement.UserInfo;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class CategoryManager implements Initializable {
    public static Category currentCategory;
    private static Stage stage;

    protected static FXMLLoader FXML_LOADER(){
        return new FXMLLoader(CategoryManager.class.getResource("CategoryWindow.fxml"));
    }
    protected static final int WIDTH = 428, HEIGHT = 300;

    @FXML
    public Label errorLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private TextField nameField;
    @FXML
    private Button editButton, saveButton, deleteButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (currentCategory != null) {
            nameLabel.setText(currentCategory.getCategory_name());

            if (UserInfo.position.equals("Cashier"))
                editButton.setVisible(false);
            saveButton.setVisible(false);
            deleteButton.setVisible(false);

            nameLabel.setVisible(true);
            nameField.setVisible(false);
        } else {
            editButton.setVisible(false);
            nameLabel.setVisible(false);
            nameField.setVisible(true);
        }

    }

    public void handleEditButtonAction(ActionEvent actionEvent) {
        nameLabel.setVisible(false);
        nameField.setVisible(true);

        nameField.setText(nameLabel.getText());

        editButton.setVisible(false);
        deleteButton.setVisible(true);
        saveButton.setVisible(true);
    }

    public void handleSaveButtonAction(ActionEvent actionEvent) throws SQLException {
        String name = nameField.getText().trim();

        if (name.equals(nameLabel.getText())) {
            nameLabel.setVisible(true);
            nameField.setVisible(false);
            editButton.setVisible(true);
            saveButton.setVisible(false);
            deleteButton.setVisible(false);
            return;
        }

        if (!valuesValidation(name))
            return;

        CategoryService cs = new CategoryService();
        if (currentCategory == null){
            Category category = new Category(name);
            if (!cs.addCategory(category)){
                setErrorMessage("The name is already taken.");
                return;
            }
            MainMenu.categoryData.add(category);
            stage.close();
        } else {
            Category upd = new Category(currentCategory.getCategory_number(), name);
            try {
                cs.updateCategory(upd);
                int index = MainMenu.customerCardData.indexOf(currentCategory);
                MainMenu.categoryData.add(index, upd);
                MainMenu.categoryData.remove(currentCategory);
            } catch (SQLException e) {
                setErrorMessage(e.getMessage());
                return;
            }
        }

        nameLabel.setVisible(true);
        nameField.setVisible(false);

        nameLabel.setText(nameField.getText());


        editButton.setVisible(true);
        saveButton.setVisible(false);
        deleteButton.setVisible(false);
    }

    public void handleDeleteButtonAction(ActionEvent actionEvent) throws SQLException {
        if (currentCategory != null){
            new CategoryService().deleteCategory(currentCategory.getCategory_number());
            MainMenu.categoryData.remove(currentCategory);
            currentCategory = null;
        }
        stage.close();
    }

    private void setErrorMessage(String msg){
        errorLabel.setVisible(true);
        errorLabel.setText(msg);
    }

    private boolean valuesValidation(String name) {

        if (name.isEmpty()) {
            setErrorMessage("Name is required");
            return false;
        }
        if (name.length() > 50) {
            setErrorMessage("The maximum name length has been exceeded.");
            return false;
        }
        errorLabel.setVisible(false);
        return true;
    }

    public static void initProfile(Category category, String title){
        currentCategory = category;
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(HelloApplication.mainStage);
        try {
            HelloApplication.setScene(stage, FXML_LOADER(), WIDTH, HEIGHT, title);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
