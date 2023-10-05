package com.example.b07_project.data_classes;

import java.util.List;

public class OrderData {
    private String orderId;
    private String customerId;
    private String date; // Renamed to match Firebase field name
    private List<ProductOrderData> items;
    private boolean complete;
    private String storeName;

    public OrderData() {
        // Default constructor required for Firebase
        this.complete = false; // Set complete to false by default
    }

    public OrderData(String orderId, String customerId, String date, List<ProductOrderData> items) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.date = date;
        this.items = items;
        this.complete = false; // Set complete to false by default
    }

    public OrderData(String orderId, String customerId, String date, List<ProductOrderData> items, boolean complete) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.date = date;
        this.items = items;
        this.complete = complete;
    }

    // Getters and setters
    public String getOrderId() {
        return orderId;
    }

    public String getStoreName(){
        return storeName;
    }

    public void setStoreName(String storeName){
        this.storeName = storeName;
    }


    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerID() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ProductOrderData> getItems() {
        return items;
    }

    public void setItems(List<ProductOrderData> items) {
        this.items = items;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete != null && complete;
    }

    public long getCost(){
        long totalCost = 0;
        for(ProductOrderData item : items) {
            totalCost += item.getPrice() * item.getQuantity();
        }
        return totalCost;
    }
}

