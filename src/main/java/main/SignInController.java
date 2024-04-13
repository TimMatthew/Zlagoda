package main;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import services.AuthorizationService;
import sessionmanagement.UserInfo;

import java.io.IOException;
import java.sql.SQLException;

public class SignInController {
    public Label loginLabel;
    public TextField userLogin;
    public PasswordField userPassword;
    public Button signInButton;
    public Label errorLabel;

    protected static final int WIDTH = 400, HEIGHT = 300;

    @FXML
    protected void goToMainMenu(ActionEvent e) throws IOException, SQLException {
        String login = userLogin.getText().trim();
        String password = userPassword.getText().trim();

        if (login.equals("") || password.equals("")){
            setErrorMessage("Empty values.");
            return;
        }

        AuthorizationService as = new AuthorizationService();
        if (as.signIn(login, password)) {
            HelloApplication.mainStage.hide();
            errorLabel.setVisible(false);
            if (UserInfo.position.equals("Manager"))
                HelloApplication.setScene(HelloApplication.mainStage, ManagerMainMenu.MANAGER_MENU_FXML_LOADER, ManagerMainMenu.WIDTH, ManagerMainMenu.HEIGHT, "Manager Main Menu");
            else
                HelloApplication.setScene(HelloApplication.mainStage, CashierMainMenu.CASHIER_MENU_FXML_LOADER, CashierMainMenu.WIDTH, CashierMainMenu.HEIGHT, "Cashier Main Menu");
        }
        else
            setErrorMessage("Incorrect login or password.");
    }

    @FXML
    public void Cancel(ActionEvent actionEvent) {
        Platform.exit();
    }

    private void setErrorMessage(String msg){
        errorLabel.setVisible(true);
        errorLabel.setText(msg);

    }
}
