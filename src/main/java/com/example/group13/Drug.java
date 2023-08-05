package com.example.group13;

public class Drug {
    private static int count = 1;
    private int drugID;
    private String drugName;
    private String brandName;
    private double drugPrice;

    public Drug(String drugName, String brandName, double drugPrice) {
        this.drugID = count++;
        this.drugName = drugName;
        this.brandName = brandName;
        this.drugPrice = drugPrice;
    }

    public int getDrugID() {
        return this.drugID;
    }

    public String getName(){
        return this.drugName;
    }

    public String getBrandName() {
        return this.brandName;
    }

    public double getDrugPrice() {
        return this.drugPrice;
    }

}
