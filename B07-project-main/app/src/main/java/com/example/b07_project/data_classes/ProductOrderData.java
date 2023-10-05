package com.example.b07_project.data_classes;

import com.example.b07_project.util.Utility;

public class ProductOrderData {
    private String productId;
    private String productName;
    private String storeName;
    private int quantity;
    private int price;

    public ProductOrderData(String productId, String productName, String storeName, Integer quantity, Integer price) {
        this.productId = productId;
        this.productName = productName;
        this.storeName = storeName != null ? storeName : ""; // Default store name empty string
        this.quantity = quantity != null ? quantity : 0; // Default quantity of 0
        this.price = price != null ? price : 0; // Default price of 0
    }

    public ProductOrderData(){
        productId = "defaultID";
        productName = "defaultName";
        quantity = 0;
        price = 0;
    }

    // Getters and setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    // Utility method to get product price as a formatted string
    public String getProductPriceString() {
        return Utility.priceIntToString(price);
    }
}

