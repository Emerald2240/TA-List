package com.iamback.talistpagetwo;

import androidx.annotation.NonNull;

public class Items {
   private String name1;
   private Double price1;
   private String quantity1;

    public Items(String name, Double price, String quantity){
      this.name1 = name;
      this.price1 = price;
      this.quantity1 = quantity;
    }

    public void setName1(String name2) {
        this.name1 = name2;
    }

    public void setPrice1(Double price2) {
        this.price1 = price2;
    }

    public String getQuantity1() {
        return quantity1;
    }

    public void setQuantity1(String quantity1) {
        this.quantity1 = quantity1;
    }

    public String getName1() {
        return name1;
    }

    public Double getPrice1() {
        return price1;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name1;
    }
}
