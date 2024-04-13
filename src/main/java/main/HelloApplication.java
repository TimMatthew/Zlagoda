package main;

import Entities.Employee;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.AuthorizationService;
import services.DataBaseHandler;

import java.io.IOException;
import java.sql.*;
import java.util.Date;

public class HelloApplication extends Application {

    public static Stage mainStage;
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        mainStage = stage;
        setScene(mainStage, new FXMLLoader(HelloApplication.class.getResource("SignInScene.fxml")), SignInController.WIDTH, SignInController.HEIGHT, "Sign In");
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        new DataBaseHandler().getConnection().close();
    }

    public static void setScene(Stage st, FXMLLoader fxmlLoader, int width, int height, String title) throws IOException {
        Scene sc = new Scene(fxmlLoader.load(), width, height);
        st.setTitle(title);
        st.setScene(sc);
        st.show();
    }

    public static void main(String[] args) {
        launch();
    }
}