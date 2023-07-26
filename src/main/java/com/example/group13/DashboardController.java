package com.example.group13;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DashboardController {

    @FXML
    private Button addMed_btn;

    @FXML
    private Button addMedicines_addBtn;

    @FXML
    private TextField addMedicines_brand;

    @FXML
    private TableColumn<?, ?> addMedicines_col_brand;

    @FXML
    private TableColumn<?, ?> addMedicines_col_date;

    @FXML
    private TableColumn<?, ?> addMedicines_col_medicineID;

    @FXML
    private TableColumn<?, ?> addMedicines_col_price;

    @FXML
    private TableColumn<?, ?> addMedicines_col_productName;

    @FXML
    private TableColumn<?, ?> addMedicines_col_status;

    @FXML
    private TableColumn<?, ?> addMedicines_col_type;

    @FXML
    private Button addMedicines_deleteBtn;

    @FXML
    private TextField addMedicines_price;

    @FXML
    private TextField addMedicines_productName;

    @FXML
    private AnchorPane add_medince_anchor;

    @FXML
    private AnchorPane dashboard_availableMed;

    @FXML
    private Button dashboard_btn;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private AnchorPane dashboard_totalCustomers;

    @FXML
    private AnchorPane dashboard_totalIncome;

    @FXML
    private TableColumn<?, ?> date_col;

    @FXML
    private TableView<?> drug_table;

    @FXML
    private Button logout;

    @FXML
    private TableColumn<?, ?> medicineName_col;

    @FXML
    private TableColumn<?, ?> purchaseID_col;

    @FXML
    private Button purchase_btn;

    @FXML
    private TableView<?> purchase_tableview;

    @FXML
    private TableColumn<?, ?> quantity_col;

    @FXML
    private TableColumn<?, ?> totalAmount_col;

    @FXML
    private Label username;

    //Database Credentials
    String url = "jdbc:mysql://localhost:3306/pharmacy";
    @FXML
    String db_username = "root";
    @FXML
    String password = "mysqlpearlkorkor@19";
    private Connection connection;

    //Method to connect to the database
    public void connectToDatabase(String url, String db_username, String password) {
        try {
            connection = DriverManager.getConnection(url, db_username, password);
            System.out.println("Database Connected Successfully");
        } catch (SQLException error) {
            System.out.println(error);
        }
    }

    public void viewDashboard(ActionEvent event) {
        if(event.getSource() == dashboard_btn) {
            dashboard_form.setVisible(true);
            add_medince_anchor.setVisible(false);
        }
    }

    public void viewAddMedicine(ActionEvent event) {
        if(event.getSource() == addMed_btn) {
            dashboard_form.setVisible(false);
            add_medince_anchor.setVisible(true);
        }
    }

    public void logout() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);

        stage.initStyle(StageStyle.TRANSPARENT);

        stage.setScene(scene);
        stage.show();

        logout.getScene().getWindow().hide();
    }

}
