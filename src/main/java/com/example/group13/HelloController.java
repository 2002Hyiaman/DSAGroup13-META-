package com.example.group13;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HelloController {
    @FXML
    private Label welcomeText;

    //Database Credentials
    String url = "jdbc:mysql://localhost:3306/pharmacy";
    String username = "root";
    String password = "mysqlpearlkorkor@19";
    private Connection connection;

    //Method to connect to the database
    public void connectToDatabase(String url, String username, String password) {
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database Connected Successfully");
        } catch (SQLException error) {
            System.out.println(error);
        }
    }
}

