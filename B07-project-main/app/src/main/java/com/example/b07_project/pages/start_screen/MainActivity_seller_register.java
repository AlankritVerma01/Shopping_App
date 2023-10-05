package com.example.b07_project.pages.start_screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.b07_project.R;
import com.example.b07_project.StoreOwnerMainActivity;
import com.example.b07_project.model.SellerAccountModel;
import com.example.b07_project.mvp_interface.RegisterPageContract;
import com.example.b07_project.mvp_interface.SellerRegisterPageViewContract;
import com.example.b07_project.presenter.SellerRegisterPagePresenter;

public class MainActivity_seller_register extends AppCompatActivity implements SellerRegisterPageViewContract {
    RegisterPageContract.Presenter presenter;
    EditText first_name;
    EditText last_name;
    EditText email;
    EditText password;
    EditText confirm_password;
    EditText shop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new SellerRegisterPagePresenter(this, new SellerAccountModel());

        setContentView(R.layout.activity_main_seller_register);
        first_name = findViewById(R.id.first_name_seller);
        last_name = findViewById(R.id.last_name_seller);
        email = findViewById(R.id.register_email_seller);
        password = findViewById(R.id.register_password_seller);
        confirm_password = findViewById(R.id.confirm_password_register_seller);
        shop = findViewById(R.id.shop_name);

        Button register_button = findViewById(R.id.register_seller_button);
        register_button.setOnClickListener(view -> presenter.onRegisterButtonClicked());
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
        Toast.makeText(MainActivity_seller_register.this, "Registration Completed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toastException(Exception e) {
        Toast.makeText(MainActivity_seller_register.this, "Could not register: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateRegisterSuccessPage() {
        startActivity(new Intent(this, StoreOwnerMainActivity.class));
        finish();
    }

    @Override
    public String getShopName() {
        return shop.getText().toString();
    }
}