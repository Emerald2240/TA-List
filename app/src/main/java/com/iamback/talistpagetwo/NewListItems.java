package com.iamback.talistpagetwo;

import androidx.annotation.NonNull;

public class NewListItems {
    private String titleName;
    private Double Price;
    private String quant;

    NewListItems(String name, String quant, Double price){
        this.titleName = name;
        this.Price = price;
        this.quant = quant;
    }

    public void setTitleName(String name2) {
        this.titleName = name2;
    }

    public void setPrice(Double price2) {
        this.Price = price2;
    }

     String getQuantity() {
        return quant;
    }

    public void setDate(String quant) {
        this.quant = quant;
    }

    String getTitleName() {
        return titleName;
    }

    public Double getPrice() {
        return Price;
    }

    @NonNull
    @Override
    public String toString() {
        return this.titleName;
    }
}
