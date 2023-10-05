package com.example.b07_project.mvp_interface;

import java.util.function.Consumer;

public class RegisterPageContract {
    public interface View {
        String getFirstNameField();
        String getLastNameField();
        String getEmailField();
        String getPasswordField();
        String getConfirmPasswordField();
        void toastEmailFormatError();
        void toastPasswordTooShort();
        void toastNotMatchingPasswords();
        void toastEmptyFields();
        void toastWelcome();
        void toastException(Exception e);
        void navigateRegisterSuccessPage();
    }
    public interface Presenter {
        void onRegisterButtonClicked();
    }
    public interface Model<RegisterDataClass> {
        boolean verifyEmailFormat(String email);
        boolean verifyPasswordLength(String password);
        void register(String email, String password, RegisterDataClass data, Runnable onSuccess, Consumer<Exception> onError);
    }
}
