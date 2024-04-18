package main;

import Entities.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.AuthorizationService;

import java.io.IOException;

public class ChangePassword {

    private static Stage stage;
    protected static final int WIDTH = 300, HEIGHT = 280;
    protected static FXMLLoader FXML_LOADER(){
        return new FXMLLoader(ChangePassword.class.getResource("ChangePassword.fxml"));
    }
    @FXML
    public PasswordField oldPassword, newPassword,  passwordConfirmation;
    @FXML
    public Label errorLabel;
    public Button saveButton;

    public static void initWindow(){
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(HelloApplication.mainStage);
        try {
            HelloApplication.setScene(stage, FXML_LOADER(), WIDTH, HEIGHT, "Changing password...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void changePassword(ActionEvent actionEvent) {
        if (!newPassword.getText().equals(passwordConfirmation.getText())){
            setErrorMessage("Passwords don't match.");
            return;
        }

        if (!new AuthorizationService().changePassword(newPassword.getText())){
            setErrorMessage("Incorrect password.");
            return;
        }

        stage.close();
    }


    private void setErrorMessage(String msg){
        errorLabel.setVisible(true);
        errorLabel.setText(msg);
    }
}
