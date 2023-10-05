package com.example.b07_project.data_classes;

import com.example.b07_project.util.StoreHashTable;

public class completedOrder {
    private StoreHashTable items_ordered;
    private String num_suborders;
    private String dateOrdered;
    private String orderValue;
    private String orderid;
    public completedOrder(StoreHashTable items_ordered, String num_suborders, String dateOrdered, String orderValue,String orderid) {
        this.num_suborders = num_suborders;
        this.orderValue = orderValue;
        this.dateOrdered = dateOrdered;
        this.items_ordered = items_ordered;
        this.orderid=orderid;
    }

    public StoreHashTable getitems_ordered() {
        return items_ordered;
    }

    public String getDateOrdered() {
        return dateOrdered;
    }

    public String getOrderValue() {
        return orderValue;
    }

    public String getNum_suborders() {
        return num_suborders;
    }

    public String getOrderid() {
        return orderid;
    }
}



