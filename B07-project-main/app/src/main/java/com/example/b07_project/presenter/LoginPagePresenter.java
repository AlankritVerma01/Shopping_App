package com.example.b07_project.presenter;

import com.example.b07_project.mvp_interface.LoginPageContract;

public class LoginPagePresenter implements LoginPageContract.Presenter {
    private final LoginPageContract.View view;
    private final LoginPageContract.Model model;

    public LoginPagePresenter(LoginPageContract.View view, LoginPageContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onLoginButtonClicked() {
        // Get data from view
        String email = view.getEmailField();
        String password = view.getPasswordField();

        // Verify data
        if (!model.verifyEmailFormat(email)) {
            view.toastEmailFormatError();
            return;
        }

        // Login
        model.login(email, password,
                () -> {
                    view.toastWelcome();
                    view.navigateLoginSuccessPage();
                },
                exception -> view.toastException(exception));
    }

    @Override
    public void onRegisterButtonClicked() {
        view.navigateRegisterPage();
    }
}
