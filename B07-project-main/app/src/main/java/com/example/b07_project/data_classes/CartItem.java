package com.example.b07_project.data_classes;

public class CartItem {
    private String itemID;
    private int price;
    private int quantity;
    private String name;

    public CartItem(){
    }

    public CartItem(String itemID, int price, int quantity, String name) {
        this.itemID = itemID;
        this.price = price;
        this.quantity = quantity;
        this.name = name;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName() { this.name = name; }

}
