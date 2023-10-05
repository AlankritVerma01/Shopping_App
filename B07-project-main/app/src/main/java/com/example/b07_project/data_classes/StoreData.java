package com.example.b07_project.data_classes;

import java.util.ArrayList;
import java.util.List;

public class StoreData {
    /**
     * Get instance of StoreData with default fields. Useful to display temporarily while awaiting asynchronous data retrieval.
     * @return Default instance of StoreData
     */
    public static StoreData getDefault() {
        return new StoreData("", "Loading...", "Loading...", new ArrayList<>());
    }


    private final String storeId;
    private final String storeName;
    private final String storeDesc;
    private final List<String> productIds;

    public StoreData(String storeId, String storeName, String storeDesc, List<String> productIds) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeDesc = storeDesc;
        this.productIds = productIds;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStoreDesc() {
        return storeDesc;
    }

    public List<String> getProductIds() {
        return productIds;
    }
}
