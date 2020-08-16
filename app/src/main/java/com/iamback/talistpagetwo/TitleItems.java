package com.iamback.talistpagetwo;

import androidx.annotation.NonNull;

public class TitleItems {

    private String titleName;
    private Double totalPrice;
    private String date;
    private int imageSrc;
    public TitleItems(String name, Double price, String date, int imageSrc){
        this.titleName = name;
        this.totalPrice = price;
        this.date = date;
        this.imageSrc = imageSrc;
    }
    public void setTitleName(String name2) {
        this.titleName = name2;
    }
    public void setTotalPrice(Double price2) {
        this.totalPrice = price2;
    }
    public int getImageSrc() {
        return imageSrc;
    }
    public void setImageSrc(int imageSrc) {
        this.imageSrc = imageSrc;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTitleName() {
        return titleName;
    }
    public Double getTotalPrice() {
        return totalPrice;
    }
    @NonNull
    @Override
    public String toString() {
        return this.titleName;
    }
}
