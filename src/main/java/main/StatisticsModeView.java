package main;

import Entities.Store_Product;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import services.CategoryService;
import services.ProductService;
import sessionmanagement.UserInfo;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class StatisticsModeView implements Initializable {
    @FXML
    public Label employeePIB;

    //UI core buttons
    @FXML
    public Button exitAccountButton, profileButton, backButton;

    //BarChartMode buttons
    @FXML
    public Button topProductsByPriceButton, topCategoriesByAvgPriceButton, topProductsBySoldAmountsButton, topCategoriesBySoldAmountsButton, topEmployeesByChecksButton, workerOfTheWeekButton;
    @FXML
    public BarChart<String, Double> barChart;
    public ImageView logo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        employeePIB.setText(UserInfo.employeeProfile.getFullName());
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

    public void showTopProductsByPrice(ActionEvent actionEvent) {
        logo.setVisible(false);
        XYChart.Series<String, Double> series = new XYChart.Series<>();
        ObservableList<Store_Product> data = new ProductService().getStoreProducts();
        Map<String, Double> data2 = new LinkedHashMap<>();

        for (Store_Product sp : data){
            data2.put(sp.getProductName(), sp.getSelling_price());
        }

        for (String key : data2.keySet()){
            series.getData().add(new XYChart.Data<>(key, data2.get(key)));
        }

        barChart.getData().clear();
        barChart.getData().addAll(series);
        barChart.setVisible(true);
    }

    public void showTopProductsBySoldAmounts(ActionEvent actionEvent) {
        logo.setVisible(false);
        XYChart.Series<String, Double> series = new XYChart.Series<>();
//
//        Map<String, Double> data = new CategoryService().getCategoryAveragePrices();
//        for (String s : data.keySet()){
//            series.getData().add(new XYChart.Data<>(s, data.get(s)));
//        }
//
        barChart.getData().clear();
        barChart.getData().addAll(series);
        barChart.setVisible(true);
    }

    public void showTopCategoriesByAvgPrice(ActionEvent actionEvent) {
        logo.setVisible(false);
        XYChart.Series<String, Double> series = new XYChart.Series<>();

        Map<String, Double> data = new CategoryService().getCategoryAveragePrices();
        for (String s : data.keySet()){
            series.getData().add(new XYChart.Data<>(s, data.get(s)));
        }

        barChart.getData().clear();
        barChart.getData().addAll(series);
        barChart.setVisible(true);
    }



    public void showTopCategoriesBySoldAmounts(ActionEvent actionEvent) {
        logo.setVisible(false);
        XYChart.Series<String, Double> series = new XYChart.Series<>();
//
//        Map<String, Double> data = new CategoryService().getCategoryAveragePrices();
//        for (String s : data.keySet()){
//            series.getData().add(new XYChart.Data<>(s, data.get(s)));
//        }
//
        barChart.getData().clear();
        barChart.getData().addAll(series);
        barChart.setVisible(true);
    }

    public void showTopEmployeesByChecksButton(ActionEvent actionEvent) {
        logo.setVisible(false);
        XYChart.Series<String, Double> series = new XYChart.Series<>();
//
//        Map<String, Double> data = new CategoryService().getCategoryAveragePrices();
//        for (String s : data.keySet()){
//            series.getData().add(new XYChart.Data<>(s, data.get(s)));
//        }
//
        barChart.getData().clear();
        barChart.getData().addAll(series);
        barChart.setVisible(true);
    }

    public void showWorkerOfTheWeek(ActionEvent actionEvent) {
        logo.setVisible(false);
        XYChart.Series<String, Double> series = new XYChart.Series<>();
//
//        Map<String, Double> data = new CategoryService().getCategoryAveragePrices();
//        for (String s : data.keySet()){
//            series.getData().add(new XYChart.Data<>(s, data.get(s)));
//        }
//
        barChart.getData().clear();
        barChart.getData().addAll(series);
        barChart.setVisible(true);
    }
}
