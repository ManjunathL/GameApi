package com.mygubbi.game.proposal.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Shruthi on 10/11/2017.
 */
public class GSTForProducts
{
    public String category;
    public String producttitle;
    public double priceAfterDiscount;
    public double price;
    public double priceAfterTax;
    public String tax;
    public String productFlag;
    public String categoryType;

    public GSTForProducts(String category, String producttitle,double priceAfterDiscount, double price, double priceAfterTax) {
        this.category = category;
        this.producttitle = producttitle;
        this.priceAfterDiscount=priceAfterDiscount;
        this.price = price;
        this.priceAfterTax = priceAfterTax;
    }
    public GSTForProducts(String category, String producttitle,double priceAfterDiscount, double price, double priceAfterTax,String tax) {
        this.category = category;
        this.producttitle = producttitle;
        this.priceAfterDiscount=priceAfterDiscount;
        this.price = price;
        this.priceAfterTax = priceAfterTax;
        this.tax=tax;
    }

    public GSTForProducts(String category, String producttitle,double priceAfterDiscount, double price, double priceAfterTax,String tax,String productFlag,String CategoryType) {
        this.category = category;
        this.producttitle = producttitle;
        this.priceAfterDiscount=priceAfterDiscount;
        this.price = price;
        this.priceAfterTax = priceAfterTax;
        this.tax=tax;
        this.productFlag=productFlag;
        this.categoryType=CategoryType;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getProductFlag() {
        return productFlag;
    }

    public void setProductFlag(String productFlag) {
        this.productFlag = productFlag;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProducttitle() {
        return producttitle;
    }

    public void setProducttitle(String producttitle) {
        this.producttitle = producttitle;
    }

    public double getPrice() {
        return round(price,2);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPriceAfterTax() {
        return round(priceAfterTax,2);
    }

    public void setPriceAfterTax(double priceAfterTax) {
        this.priceAfterTax = priceAfterTax;
    }

    public double getPriceAfterDiscount() {
        return round(priceAfterDiscount,2);
    }

    public void setPriceAfterDiscount(double priceAfterDiscount) {
        this.priceAfterDiscount = priceAfterDiscount;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    @Override
    public String toString() {
        return "GSTForProducts{" +
                "category='" + category + '\'' +
                ", producttitle='" + producttitle + '\'' +
                ", priceAfterDiscount=" + priceAfterDiscount +
                ", price=" + price +
                ", priceAfterTax=" + priceAfterTax +
                ", tax='" + tax + '\'' +
                ", productFlag='" + productFlag + '\'' +
                ", categoryType='" + categoryType + '\'' +
                '}';
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return value = 0;
        } else {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }
    }
}
