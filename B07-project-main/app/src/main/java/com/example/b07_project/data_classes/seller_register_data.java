package com.example.b07_project.data_classes;

public class seller_register_data {
    String FirstName;
    String LastName;
    String shopname;
    String shopid;
    String email;
public seller_register_data(String FirstName, String LastName, String shopname, String email,String shopid){
    this.email=email;
    this.FirstName= FirstName;
    this.LastName=LastName;
    this.shopname=shopname;
    this.shopid=shopid;
  }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }
}
