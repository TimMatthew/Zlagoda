package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import services.DataBaseHandler;

import java.io.IOException;
import java.sql.*;

public class HelloApplication extends Application {

    public static Stage mainStage;
    //public static JMetro jMetro;
    @Override
    public void start(Stage stage) throws IOException, SQLException {

        mainStage = stage;
        mainStage.setResizable(false);
        mainStage.getIcons().add(new Image(getClass().getResourceAsStream("img/logo.png")));

        //jMetro = new JMetro(Style.DARK);

        setScene(mainStage, Authorization.FXML_LOADER(), Authorization.WIDTH, Authorization.HEIGHT, "Ласкаво просимо в ZLAGODA!");
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        new DataBaseHandler().getConnection().close();
    }

    public static void setScene(Stage st, FXMLLoader fxmlLoader, int width, int height, String title) throws IOException {
        Scene sc = new Scene(fxmlLoader.load(), width, height);
        //jMetro.setScene(sc);
        st.setTitle(title);
        st.setScene(sc);
        st.show();
    }

    public static void main(String[] args) {
        launch();
    }
}