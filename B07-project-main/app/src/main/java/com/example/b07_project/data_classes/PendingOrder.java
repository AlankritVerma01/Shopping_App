
package com.example.b07_project.data_classes;
import com.example.b07_project.util.StoreHashTable;

import java.util.Dictionary;
import java.util.*;

public class PendingOrder {
    private   StoreHashTable pending;
    private String numsuborder;
    private String orderid;
    private String orderValue;
    private String dateOrdered;

    public PendingOrder(StoreHashTable pending, String numsuborder, String orderValue, String dateOrdered,String orderid) {
        this.pending = pending;
        this.numsuborder = numsuborder;
        this.orderValue = orderValue;
        this.dateOrdered = dateOrdered;
        this.orderid=orderid;
    }


    public StoreHashTable getpending() {
        return pending;
    }

    public String getNumItems() {
        return numsuborder;
    }

    public String getOrderValue() {
        return orderValue;
    }



    public String getDateOrdered() {
        return dateOrdered;
    }

    public StoreHashTable getPending() {
        return pending;
    }


    public String getOrderid() {
        return orderid;
    }
}

