package com.example.group13;

public class Purchase {
    private static int count = 1;
    private int purchase_id;
    private String drug_name;
    private int quantity;
    private double total_amount;
    private String date;

    public Purchase(String drug_name, int quantity, double total_amount, String date) {
        this.drug_name = drug_name;
        this.quantity = quantity;
        this.total_amount = total_amount;
        this.date = date;
        this.purchase_id = count++;
    }

    public int getPurchase_id() {
        return this.purchase_id;
    }

    public String getDrug_name() {
        return this.drug_name;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public double getTotal_amount() {
        return this.total_amount;
    }

    public String getDate() {
        return this.date;
    }
}
