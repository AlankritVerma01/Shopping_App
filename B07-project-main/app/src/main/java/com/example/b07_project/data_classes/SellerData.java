package com.example.b07_project.data_classes;

public class SellerData {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String shopId;
    public SellerData(String firstName, String lastName, String email, String shopId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.shopId = shopId;
    }
    
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getShopId() { return shopId; }
}
