package com.example.b07_project.model;

import com.example.b07_project.FirebaseRepository;
import com.example.b07_project.data_classes.seller_register_data;
import com.example.b07_project.mvp_interface.LoginPageContract;
import com.example.b07_project.mvp_interface.RegisterPageContract;

import java.util.function.Consumer;

public class SellerAccountModel implements LoginPageContract.Model, RegisterPageContract.Model<seller_register_data> {
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
    public void register(String email, String password, seller_register_data data, Runnable onSuccess, Consumer<Exception> onError) {
        FirebaseRepository.registerSeller(email, password, data, onSuccess, onError);
    }

    @Override
    public void login(String email, String password, Runnable onSuccess, Consumer<Exception> onError) {
        FirebaseRepository.loginSeller(email, password, onSuccess, onError);
    }
}
