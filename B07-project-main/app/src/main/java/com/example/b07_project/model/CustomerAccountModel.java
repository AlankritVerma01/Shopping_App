package com.example.b07_project.model;

import com.example.b07_project.FirebaseRepository;
import com.example.b07_project.data_classes.customer_register_data;
import com.example.b07_project.mvp_interface.LoginPageContract;
import com.example.b07_project.mvp_interface.RegisterPageContract;

import java.util.function.Consumer;

public class CustomerAccountModel implements LoginPageContract.Model, RegisterPageContract.Model<customer_register_data> {
    private final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@([a-z]+\\.)+[a-z]+";

    @Override
    public boolean verifyEmailFormat(String email) {
        return email.matches(EMAIL_PATTERN);
    }

    @Override
    public boolean verifyPasswordLength(String password) {
        return password.length() >= 6;
    }

    @Override
    public void register(String email, String password, customer_register_data data, Runnable onSuccess, Consumer<Exception> onError) {
        FirebaseRepository.registerCustomer(email, password, data, onSuccess, onError);
    }

    @Override
    public void login(String email, String password, Runnable onSuccess, Consumer<Exception> onError) {
        FirebaseRepository.loginCustomer(email, password, onSuccess, onError);
    }
}
