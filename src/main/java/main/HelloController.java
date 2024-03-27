package main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("+100 біткоїнів цьому платнику податків!");
    }

    @FXML
    protected void onZradaButtonClick() {
        welcomeText.setText("-100 біткоїнів цьому ухилянтові!");
    }
}