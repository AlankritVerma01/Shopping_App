package com.example.b07_project.presenter;

import com.example.b07_project.data_classes.seller_register_data;
import com.example.b07_project.mvp_interface.RegisterPageContract;
import com.example.b07_project.mvp_interface.SellerRegisterPageViewContract;

import java.util.UUID;

public class SellerRegisterPagePresenter implements RegisterPageContract.Presenter {
    private final SellerRegisterPageViewContract view;
    private final RegisterPageContract.Model<seller_register_data> model;

    public SellerRegisterPagePresenter(SellerRegisterPageViewContract view, RegisterPageContract.Model<seller_register_data> model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onRegisterButtonClicked() {
        // Get data from view
        String firstName = view.getFirstNameField();
        String lastName = view.getLastNameField();
        String shopName = view.getShopName();
        String shopId = generateShopId();
        String email = view.getEmailField();
        String password = view.getPasswordField();
        String confirmPassword = view.getConfirmPasswordField();

        // Verify data with model
        if (!model.verifyEmailFormat(email)) {
            view.toastEmailFormatError();
            return;
        }
        if(!model.verifyPasswordLength(password)) {
            view.toastPasswordTooShort();
            return;
        }
        if (!password.equals(confirmPassword)) {
            view.toastNotMatchingPasswords();
            return;
        }
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            view.toastEmptyFields();
            return;
        }

        // Register
        model.register(email, password, new seller_register_data(firstName, lastName, shopName, email, shopId),
                () -> {
                    view.toastWelcome();
                    view.navigateRegisterSuccessPage();
                },
                exception -> view.toastException(exception));
    }

    private static String generateShopId() {
        return UUID.randomUUID().toString();
    }
}
