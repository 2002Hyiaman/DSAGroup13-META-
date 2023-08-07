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
    private Button logout;

    @FXML
    private Button purchase_btn;

    @FXML
    private TableView<PurchaseTable> purchase_tableview;

    @FXML
    private TableColumn<PurchaseTable, Integer> quantity_col;

    @FXML
    private TableColumn<PurchaseTable, Double> totalAmount_col;
    @FXML
    private TableColumn<PurchaseTable, Integer> purchaseID_col;
    @FXML
    private TableColumn<PurchaseTable, String> date_col;
    @FXML
    private TableColumn<PurchaseTable, String> medicineName_col;


    @FXML
    private Label username;

    @FXML
    private Label medicine_count;

    @FXML
    private Label total_income;

    @FXML
    private Label customer_count;

    @FXML
    private AnchorPane purchase_medince_view;

    @FXML
    private TextField purchase_medicine_name;
    @FXML
    private TextField purchase_quantity;
    @FXML
    private TextField purchase_total_amount;

    @FXML
    private TextField purchase_date;

    @FXML
    private Button purchase_purhcase_btn;

    double totalAmount;
    int totalCustomers;

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
        totalAmount = 0;
        totalCustomers = 0;
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

    //Method to display available medicine
    public void displayMedicineCount() throws SQLException {
        total_income.setText(String.valueOf(totalAmount));
        customer_count.setText(String.valueOf(totalCustomers));
        connectToDatabase(url, db_username, password);

        try {
            String sql = "SELECT COUNT(*) FROM drugs";
            prepare = connection.prepareStatement(sql);
            result = prepare.executeQuery();

            if(result.next()) {
                int rowCount = result.getInt(1);
                medicine_count.setText(String.valueOf(rowCount));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }

    }

    //Method to add purhcases to system
    public void purchaseEvent(ActionEvent event) {
        if(event.getSource() == purchase_purhcase_btn) {
            String drugName = purchase_medicine_name.getText();
            int quantity = Integer.parseInt(purchase_quantity.getText());
            double total_amount = Double.parseDouble(purchase_total_amount.getText());
            String date = purchase_date.getText();

            Purchase newPurchase = new Purchase(drugName, quantity, total_amount, date);
            totalAmount += total_amount;
            totalCustomers++;

            connectToDatabase(url, db_username, password);

            try {
                String query = "INSERT INTO purchases (purchase_id, drug_name, quantity, total_amount, purchase_date) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, newPurchase.getPurchase_id());
                preparedStatement.setString(2, drugName);
                preparedStatement.setInt(3, quantity);
                preparedStatement.setDouble(4, total_amount);
                preparedStatement.setString(5, date);

                preparedStatement.executeUpdate();
                System.out.println("Purchase recorded successfully.");
                showAlert("Success", "Purchase recorded successfully.", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                System.out.println("Error while inserting the purchase into the database: " + e.getMessage());
                showAlert("Error", "Error while adding purchase.", Alert.AlertType.ERROR);
            }

            //clear text fields
            purchase_medicine_name.clear();
            purchase_total_amount.clear();
            purchase_quantity.clear();
            purchase_date.clear();
        }
    }

    //Display purchases in the purchases table
    public ObservableList<PurchaseTable> addPurchasesListData() {
        ObservableList<PurchaseTable> purchasesList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM purchases";

        try {
            connectToDatabase(url, db_username, password);
            prepare = connection.prepareStatement(sql);
            result = prepare.executeQuery();
            PurchaseTable purchase;

            while (result.next()) {
                purchase = new PurchaseTable(
                        result.getInt("purchase_id"),
                        result.getString("drug_name"),
                        result.getInt("quantity"),
                        result.getDouble("total_amount"),
                        result.getString("purchase_date")
                );
                purchasesList.add(purchase);
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
        return purchasesList;
    }

    private ObservableList<PurchaseTable> addPurchaseList;
    public void addPurchasesShowListData() {
        addPurchaseList = addPurchasesListData();

        purchaseID_col.setCellValueFactory(new PropertyValueFactory<>("purchase_id"));
        medicineName_col.setCellValueFactory(new PropertyValueFactory<>("drug_name"));
        quantity_col.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalAmount_col.setCellValueFactory(new PropertyValueFactory<>("total_amount"));
        date_col.setCellValueFactory(new PropertyValueFactory<>("purchase_date"));

        purchase_tableview.setItems(addPurchaseList);
    }


    //Methods for swapping views
    public void viewDashboard(ActionEvent event) throws SQLException {
        if(event.getSource() == dashboard_btn) {
            dashboard_form.setVisible(true);
            add_medince_anchor.setVisible(false);
            purchase_medince_view.setVisible(false);
            displayMedicineCount();
            addPurchasesShowListData();
        }
    }

    public void viewAddMedicine(ActionEvent event) throws SQLException {
        if(event.getSource() == addMed_btn) {
            dashboard_form.setVisible(false);
            add_medince_anchor.setVisible(true);
            purchase_medince_view.setVisible(false);
            addDrugsShowListData();
        }
    }

    public void viewPurchaseMedicine(ActionEvent event) {
        if(event.getSource() == purchase_btn) {
            dashboard_form.setVisible(false);
            add_medince_anchor.setVisible(false);
            purchase_medince_view.setVisible(true);
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
