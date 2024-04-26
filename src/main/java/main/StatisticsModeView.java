package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import main.Authorization;
import main.EmployeeProfile;
import main.HelloApplication;
import main.MainMenu;
import services.CategoryService;
import sessionmanagement.UserInfo;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class StatisticsModeView implements Initializable {
    @FXML
    public Label employeePIB;
    @FXML
    public Button exitAccountButton, profileButton, topCategoriesByAvgPriceButton, backButton;
    @FXML
    public BarChart<String, Double> topCategoriesByAvgPriceChart;
    public ImageView logo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    public void quit(ActionEvent actionEvent) throws IOException {
        UserInfo.id = null;
        UserInfo.position = null;
        UserInfo.employeeProfile = null;
        HelloApplication.setScene(HelloApplication.mainStage, Authorization.FXML_LOADER(), Authorization.WIDTH, Authorization.HEIGHT, "Ласкаво просимо в ZLAGODA!");
    }

    @FXML
    protected void openProfile(ActionEvent e) {
        EmployeeProfile.initProfile(UserInfo.employeeProfile, "Manager profile");
    }

    @FXML
    public void backToMainMenu(ActionEvent actionEvent) throws IOException {
        HelloApplication.setScene(HelloApplication.mainStage, new FXMLLoader(HelloApplication.class.getResource("MainMenu.fxml")), MainMenu.WIDTH, MainMenu.HEIGHT, "Main Menu");
    }

    public void showTopCategoriesByAvgPrice(ActionEvent actionEvent) {
        logo.setVisible(false);
        XYChart.Series<String, Double> series = new XYChart.Series<>();

        Map<String, Double> data = new CategoryService().getCategoryAveragePrices();
        for (String s : data.keySet()){
            series.getData().add(new XYChart.Data<>(s, data.get(s)));
        }

        topCategoriesByAvgPriceChart.getData().addAll(series);
        topCategoriesByAvgPriceChart.setVisible(true);
    }
}
