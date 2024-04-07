package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        setScene(stage, new FXMLLoader(HelloApplication.class.getResource("CashierMainMenu.fxml")), "Cashier Menu");
    }

    public static void setScene(Stage st, FXMLLoader fxmlLoader, String title) throws IOException {
        Scene sc = new Scene(fxmlLoader.load(), 900, 600);
        st.setTitle(title);
        st.setScene(sc);
        st.show();
    }

    public static void main(String[] args) {
        launch();
    }
}