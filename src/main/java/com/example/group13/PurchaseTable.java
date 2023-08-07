package com.example.group13;

public class PurchaseTable {
    private int purchase_id;
    private String drug_name;
    private int quantity;
    private double total_amount;
    private String purchase_date;

    public PurchaseTable(int purchase_id, String drug_name, int quantity, double total_amount, String purchase_date) {
        this.purchase_id = purchase_id;
        this.drug_name = drug_name;
        this.quantity = quantity;
        this.total_amount = total_amount;
        this.purchase_date = purchase_date;
    }

    public int getPurchase_id() {
        return purchase_id;
    }

    public String getDrug_name() {
        return drug_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public String getPurchase_date() {
        return purchase_date;
    }
}

