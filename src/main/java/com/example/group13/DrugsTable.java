package com.example.group13;

public class DrugsTable {
    private int drug_id;
    private String brand_name;
    private String drug_name;
    private double unit_price;

    public DrugsTable(int drug_id, String brand_name, String drug_name, double unit_price) {
        this.drug_id = drug_id;
        this.brand_name = brand_name;
        this.drug_name = drug_name;
        this.unit_price = unit_price;
    }

    // Getter methods
    public int getDrug_id() {
        return drug_id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public String getDrug_name() {
        return drug_name;
    }

    public double getUnit_price() {
        return unit_price;
    }
}
