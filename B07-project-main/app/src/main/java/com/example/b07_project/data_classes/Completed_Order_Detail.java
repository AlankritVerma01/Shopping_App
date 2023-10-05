package com.example.b07_project.data_classes;

public class Completed_Order_Detail {
    private String name;
    private String Store_name;
    private String price;
    private String quantity;
    public Completed_Order_Detail(String name, String Store_name, String price, String quantity){
        this.name= name;
        this.Store_name = Store_name;
        this.price= price;
        this.quantity=quantity;
    }

    public String getStore_name() {
        return Store_name;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }
}
