package com.example.group13;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {

    @FXML
    private Button addMed_btn;

    @FXML
    private Button addMedicines_addBtn;

    @FXML
    private TextField addMedicines_brand;

    @FXML
    private TableView<DrugsTable> drug_table;

    @FXML
    private TableColumn<DrugsTable, String> addMedicines_col_brand;

    @FXML
    private TableColumn<DrugsTable, Integer> addMedicines_col_medicineID;

    @FXML
    private TableColumn<DrugsTable, Double> addMedicines_col_price;

    @FXML
    private TableColumn<DrugsTable, String> addMedicines_col_productName;

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
    private Button logout;

    @FXML
    private TableColumn<DrugsTable, String> medicineName_col;

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
    String url = "jdbc:mysql://localhost:3306/test";
    @FXML
    String db_username = "root";
    @FXML
    String password = "nicetrylol";
    private Connection connection;
    private PreparedStatement prepare;
    private ResultSet result;

    private List<Drug> drugsList;
    public DashboardController() {
        drugsList = new ArrayList<>();
    }

    //Method to connect to the database
    public void connectToDatabase(String url, String db_username, String password) {
        try {
            connection = DriverManager.getConnection(url, db_username, password);
            System.out.println("Database Connected Successfully");
        } catch (SQLException error) {
            System.out.println(error);
        }
    }

    //method to add drug to the system
    public void addDrugs(ActionEvent event) {
        if(event.getSource() == addMedicines_addBtn) {
            String drugBrand = addMedicines_brand.getText();
            String drugName = addMedicines_productName.getText();
            double drugPrice = Double.valueOf(addMedicines_price.getText());

            Drug newDrug = new Drug(drugName, drugBrand, drugPrice);
            drugsList.add(newDrug);

            connectToDatabase(url, db_username, password);

            try {
                String query = "INSERT INTO drugs (drug_id, drug_name, brand_name, unit_price) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, newDrug.getDrugID());
                preparedStatement.setString(2, drugName);
                preparedStatement.setString(3, drugBrand);
                preparedStatement.setDouble(4, drugPrice);
                preparedStatement.executeUpdate();
                System.out.println("Drug inserted into the database successfully.");
                showAlert("Success", "Drug added successfully.", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                System.out.println("Error while inserting the drug into the database: " + e.getMessage());
                showAlert("Error", "Error while adding drug.", Alert.AlertType.ERROR);
            }

            //clear text fields
            addMedicines_brand.clear();
            addMedicines_productName.clear();
            addMedicines_price.clear();
        }
    }

    //Display drugs in the drugs table

    public ObservableList<DrugsTable> addDrugsListData() {
        ObservableList<DrugsTable> drugsList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM drugs";

        try {
            connectToDatabase(url, db_username, password);
            prepare = connection.prepareStatement(sql);
            result = prepare.executeQuery();
            DrugsTable drug;

            while (result.next()) {
                drug = new DrugsTable(
                        result.getInt("drug_id"),
                        result.getString("brand_name"),
                        result.getString("drug_name"),
                        result.getDouble("unit_price")
                );
                drugsList.add(drug);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Make sure to close the connection, statement, and result set to release resources
            try {
                if (result != null) result.close();
                if (prepare != null) prepare.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return drugsList;
    }


    private ObservableList<DrugsTable> addDrugsList;
    public void addDrugsShowListData() {
        addDrugsList = addDrugsListData();

        addMedicines_col_medicineID.setCellValueFactory(new PropertyValueFactory<>("drug_id"));
        addMedicines_col_brand.setCellValueFactory(new PropertyValueFactory<>("brand_name"));
        addMedicines_col_productName.setCellValueFactory(new PropertyValueFactory<>("drug_name"));
        addMedicines_col_price.setCellValueFactory(new PropertyValueFactory<>("unit_price"));

        drug_table.setItems(addDrugsList);
    }

    // Method to show an alert
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //Method to delete drugs
    public void deleteDrug(ActionEvent event) {
        if (event.getSource() == addMedicines_deleteBtn) {
            String drugName = addMedicines_productName.getText();

            // Search for the drug with the corresponding name in the drugsList ArrayList
            Drug drugToDelete = null;
            for (Drug drug : drugsList) {
                if (drug.getName().equals(drugName)) {
                    drugToDelete = drug;
                    break;
                }
            }

            if (drugToDelete != null) {
                try {
                    connectToDatabase(url, db_username, password);
                    // Delete the drug from the database using its ID
                    String query = "DELETE FROM drugs WHERE drug_id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, drugToDelete.getDrugID());
                    preparedStatement.executeUpdate();
                    System.out.println("Drug deleted from the database successfully.");

                    // Remove the drug from the drugsList ArrayList
                    drugsList.remove(drugToDelete);

                    // Show success alert
                    showAlert("Success", "Drug deleted successfully.", Alert.AlertType.INFORMATION);
                } catch (SQLException e) {
                    System.out.println("Error while deleting the drug from the database: " + e.getMessage());

                    // Show error alert
                    showAlert("Error", "Error while deleting drug.", Alert.AlertType.ERROR);
                }
            } else {
                // If drugToDelete is null, the drug with the specified name was not found
                showAlert("Error", "Drug not found.", Alert.AlertType.ERROR);
            }
        }
    }


    //Methods for swapping views
    public void viewDashboard(ActionEvent event) {
        if(event.getSource() == dashboard_btn) {
            dashboard_form.setVisible(true);
            add_medince_anchor.setVisible(false);
        }
    }

    public void viewAddMedicine(ActionEvent event) throws SQLException {
        if(event.getSource() == addMed_btn) {
            dashboard_form.setVisible(false);
            add_medince_anchor.setVisible(true);
            addDrugsShowListData();
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
