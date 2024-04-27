package main;

import Entities.Store_Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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
    public Button topProductsByPriceButton, topCategoriesByAvgPriceButton, topEmployeesByChecksButton;
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

    public void showTopEmployeesByChecksPrices(ActionEvent actionEvent) {
        logo.setVisible(false);
        XYChart.Series<String, Double> series = new XYChart.Series<>();

        Map<String, Double> sells = new ProductService().getProductSellsByPerson();
        for (String key : sells.keySet()) {
            series.getData().add(new XYChart.Data<>(key, sells.get(key)));
        }

        barChart.getData().clear();
        barChart.getData().addAll(series);
        barChart.setVisible(true);
    }

    public void setupListView(Stage primaryStage) {
        ListView<String> listView = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList(
                "Строка 1",
                "Строка 2",
                "Строка 3",
                "Строка 4",
                "Строка 5"
        );

        // Устанавливаем строки в ListView
        listView.setItems(items);
        VBox root = new VBox(listView);

        // Создаем сцену и устанавливаем корневой узел
        Scene scene = new Scene(root, 300, 250);

        // Устанавливаем сцену для primaryStage и отображаем его
        primaryStage.setScene(scene);
        primaryStage.setTitle("Пример ListView");
        primaryStage.show();
    }

    public void customMethod1(ActionEvent actionEvent) {
        logo.setVisible(false);
        XYChart.Series<String, Double> series = new XYChart.Series<>();

        Map<String, Double> worker = new ProductService().getCustomMethod1();
        for (String key : worker.keySet()) {
            series.getData().add(new XYChart.Data<>(key, worker.get(key)));
        }

        barChart.getData().clear();
        barChart.getData().addAll(series);
        barChart.setVisible(true);
    }

    public void customMethod2(ActionEvent actionEvent) {
        Map<String, String> products = new ProductService().getCustomMethod2();
    }
    public void customMethod3(ActionEvent actionEvent) {
        logo.setVisible(false);

        barChart.getData().clear();
        barChart.setVisible(true);
    }
    public void customMethod4(ActionEvent actionEvent) {
        logo.setVisible(false);

        barChart.getData().clear();
        barChart.setVisible(true);
    }
    public void customMethod5(ActionEvent actionEvent) {
        logo.setVisible(false);

        barChart.getData().clear();
        barChart.setVisible(true);
    }
    public void customMethod6(ActionEvent actionEvent) {
        logo.setVisible(false);

        barChart.getData().clear();
        barChart.setVisible(true);
    }
}
