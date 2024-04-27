package main;

import Entities.Check;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.CategoryService;
import services.CheckService;
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
    public TableView dataTable;
    public Spinner<Integer> discountSpinner;
    public AnchorPane thirdTaskPane;
    public ChoiceBox<String> selectCategory;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        employeePIB.setText(UserInfo.employeeProfile.getFullName());
        thirdTaskPane.setVisible(false);
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
        thirdTaskPane.setVisible(false);
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
        barChart.setTitle("Top Products By Price");
    }

    public void showTopCategoriesByAvgPrice(ActionEvent actionEvent) {
        thirdTaskPane.setVisible(false);
        logo.setVisible(false);
        XYChart.Series<String, Double> series = new XYChart.Series<>();

        Map<String, Double> data = new CategoryService().getCategoryAveragePrices();
        for (String s : data.keySet()){
            series.getData().add(new XYChart.Data<>(s, data.get(s)));
        }

        barChart.getData().clear();
        barChart.getData().addAll(series);
        barChart.setVisible(true);
        barChart.setTitle("Top Categories By Avg Price");
    }

    public void showTopEmployeesByChecksPrices(ActionEvent actionEvent) {
        thirdTaskPane.setVisible(false);
        logo.setVisible(false);
        XYChart.Series<String, Double> series = new XYChart.Series<>();

        Map<String, Double> sells = new ProductService().getProductSellsByPerson();
        for (String key : sells.keySet()) {
            series.getData().add(new XYChart.Data<>(key, sells.get(key)));
        }

        barChart.getData().clear();
        barChart.getData().addAll(series);
        barChart.setVisible(true);
        barChart.setTitle("Uncategorized Non-LA products");

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

        listView.setItems(items);
        VBox root = new VBox(listView);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setScene(scene);
        primaryStage.setTitle(" ListView");
        primaryStage.show();
    }

    public void customMethod1(ActionEvent actionEvent) {
        thirdTaskPane.setVisible(false);
        logo.setVisible(false);
        XYChart.Series<String, Double> series = new XYChart.Series<>();

        Map<String, Double> worker = new ProductService().getCustomMethod1();
        for (String key : worker.keySet()) {
            series.getData().add(new XYChart.Data<>(key, worker.get(key)));
        }

        barChart.getData().clear();
        barChart.getData().addAll(series);
        barChart.setVisible(true);
        barChart.setTitle("Worker of the day");
    }

    public void customMethod2(ActionEvent actionEvent) {
        Map<String, String> products = new ProductService().getCustomMethod2();
    }
    public void customMethod3(ActionEvent actionEvent) {
        barChart.setVisible(false);
        thirdTaskPane.setVisible(true);
        logo.setVisible(false);

        selectCategory.setItems(FXCollections.observableList(MainMenu.categoryMapGetID.keySet().stream().toList()));
        selectCategory.setValue(MainMenu.categoryMapGetID.keySet().stream().toList().get(0));


        discountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 25, 5));
        

    }
    public void customMethod4(ActionEvent actionEvent) {
        thirdTaskPane.setVisible(false);
        logo.setVisible(false);

        barChart.getData().clear();
        barChart.setVisible(true);
    }
    public void customMethod5(ActionEvent actionEvent) {
        thirdTaskPane.setVisible(false);
        logo.setVisible(false);

        barChart.getData().clear();
        barChart.setVisible(true);
    }
    public void customMethod6(ActionEvent actionEvent) {
        thirdTaskPane.setVisible(false);
        logo.setVisible(false);

        barChart.getData().clear();
        barChart.setVisible(true);
    }

    public void doTable(ActionEvent actionEvent) {
        TableColumn<Check, String> check_number = new TableColumn<>("Check No");
        TableColumn<Check, Date> dateOfCreation = new TableColumn<>("Print date");
        TableColumn<Check, Double> total_sum = new TableColumn<>("Total Sum");

        ObservableList<Check> afterQuery = new CheckService().getChecksByProductCategory(discountSpinner.getValue(), MainMenu.categoryMapGetID.get(selectCategory.getValue()));

        check_number.setCellValueFactory(new PropertyValueFactory<>("check_number"));
        dateOfCreation.setCellValueFactory(new PropertyValueFactory<>("print_date"));
        total_sum.setCellValueFactory(new PropertyValueFactory<>("sum_total"));

        dataTable.getColumns().clear();
        dataTable.getColumns().addAll(check_number, dateOfCreation, total_sum);
        dataTable.setItems(afterQuery);


    }
}
