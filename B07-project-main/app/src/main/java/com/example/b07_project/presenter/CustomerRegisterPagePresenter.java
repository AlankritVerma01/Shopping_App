package com.example.b07_project.presenter;

import com.example.b07_project.data_classes.customer_register_data;
import com.example.b07_project.mvp_interface.RegisterPageContract;

public class CustomerRegisterPagePresenter implements RegisterPageContract.Presenter {
    private final RegisterPageContract.View view;
    private final RegisterPageContract.Model<customer_register_data> model;

    public CustomerRegisterPagePresenter(RegisterPageContract.View view, RegisterPageContract.Model<customer_register_data> model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onRegisterButtonClicked() {
        // Get data from view
        String firstName = view.getFirstNameField();
        String lastName = view.getLastNameField();
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
        model.register(email, password, new customer_register_data(firstName, lastName, email),
                () -> {
                    view.toastWelcome();
                    view.navigateRegisterSuccessPage();
                },
                exception -> view.toastException(exception));
    }
}
