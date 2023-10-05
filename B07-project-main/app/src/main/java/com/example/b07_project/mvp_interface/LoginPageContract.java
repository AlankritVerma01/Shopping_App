package com.example.b07_project.mvp_interface;

import java.util.function.Consumer;

public class LoginPageContract {
    public interface View {
        String getEmailField();
        String getPasswordField();
        void toastEmailFormatError();
        void toastWelcome();
        void toastException(Exception e);
        void navigateLoginSuccessPage();
        void navigateRegisterPage();
    }
    public interface Presenter {
        void onLoginButtonClicked();
        void onRegisterButtonClicked();
    }
    public interface Model {
        boolean verifyEmailFormat(String email);
        void login(String email, String password, Runnable onSuccess, Consumer<Exception> onError);
    }
}
