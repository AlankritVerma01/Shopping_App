package com.example.b07_project.data_classes;

import com.example.b07_project.util.Utility;

public class ProductData {
    /**
     * Get instance of ProductData with default fields. Useful to display temporarily while awaiting asynchronous data retrieval.
     * @return Default instance of ProductData
     */
    public static ProductData getDefault() {
        return new ProductData("", "Loading...", "Loading...", 0, 0);
    }

    private final String productId;
    private final String productName;
    private final String productDescription;
    private final int productPrice;
    private final int productQuantity;

    public ProductData(String productId, String productName, String productDescription, Integer productPrice, Integer productQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice != null ? productPrice : 0;
        this.productQuantity = productQuantity != null ? productQuantity : 0;
    }

    public String getProductId() { return productId; }

    public String getProductName() { return productName; }

    public String getProductDescription() { return productDescription; }

    public int getProductPrice() { return productPrice; }

    public String getProductPriceString() { return Utility.priceIntToString(productPrice); }

    public int getProductQuantity() { return productQuantity; }
}
