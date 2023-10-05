package com.example.b07_project.data_classes;

public class customer_register_data {
    String firstName;
    String lastName;
    String email;
    public customer_register_data(String firstName, String lastName, String email){
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public customer_register_data(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
