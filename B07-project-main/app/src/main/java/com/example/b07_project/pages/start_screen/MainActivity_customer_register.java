package com.example.b07_project.pages.start_screen;

import android.annotation.SuppressLint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.b07_project.CustomerMainActivity;
import com.example.b07_project.R;
import com.example.b07_project.model.CustomerAccountModel;
import com.example.b07_project.mvp_interface.RegisterPageContract;
import com.example.b07_project.presenter.CustomerRegisterPagePresenter;

public class MainActivity_customer_register extends AppCompatActivity implements RegisterPageContract.View {
    RegisterPageContract.Presenter presenter;
    EditText first_name;
    EditText last_name;
    EditText email;
    EditText password;
    EditText confirm_password;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new CustomerRegisterPagePresenter(this, new CustomerAccountModel());

        setContentView(R.layout.activity_main_customer_register);
        first_name = findViewById(R.id.first_name_customer);
        last_name = findViewById(R.id.last_name_customer);
        email = findViewById(R.id.email_register_customer);
        password = findViewById(R.id.password_register_customer);
        confirm_password = findViewById(R.id.confirm_password_register_customer);

        Button register_customer_button = findViewById(R.id.register_customer_button);
        register_customer_button.setOnClickListener(view -> presenter.onRegisterButtonClicked());
    }

    @Override
    public String getFirstNameField() {
        return first_name.getText().toString();
    }

    @Override
    public String getLastNameField() {
        return last_name.getText().toString();
    }

    @Override
    public String getEmailField() {
        return email.getText().toString();
    }

    @Override
    public String getPasswordField() {
        return password.getText().toString();
    }

    @Override
    public String getConfirmPasswordField() {
        return confirm_password.getText().toString();
    }

    @Override
    public void toastEmailFormatError() {
        email.setError("Enter correct email. Email format: example@something.com");
    }

    @Override
    public void toastPasswordTooShort() {
        password.setError("Password should be at least 6 characters");
    }

    @Override
    public void toastNotMatchingPasswords() {
        password.setError("Passwords do not match");
    }

    @Override
    public void toastEmptyFields() {
        Toast.makeText(this, "Some fields are still empty", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toastWelcome() {
        Toast.makeText(MainActivity_customer_register.this, "Registration Completed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toastException(Exception e) {
        Toast.makeText(MainActivity_customer_register.this, "Could not register: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void navigateRegisterSuccessPage() {
        startActivity(new Intent(this, CustomerMainActivity.class));
        finish();
    }
}
