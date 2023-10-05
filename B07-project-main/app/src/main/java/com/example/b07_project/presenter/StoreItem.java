package com.example.b07_project.presenter;

public class StoreItem {
    private String itemName;
    private double itemPrice;
    private int itemQuantity;
    private int imageResourceId; // Add this field to store the image resource ID

    public StoreItem(String itemName, double itemPrice, int imageResourceId) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.imageResourceId = imageResourceId;
        this.itemQuantity = 0;
    }

    public String getItemName() {
        return itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}
