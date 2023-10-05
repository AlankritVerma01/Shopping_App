package com.example.b07_project.pages.start_screen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.b07_project.R;
import com.example.b07_project.StoreOwnerMainActivity;
import com.example.b07_project.model.SellerAccountModel;
import com.example.b07_project.mvp_interface.LoginPageContract;
import com.example.b07_project.presenter.LoginPagePresenter;

public class MainActivity_seller_login extends AppCompatActivity implements LoginPageContract.View {
    private LoginPageContract.Presenter presenter;
    private EditText email;
    private EditText password;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new LoginPagePresenter(this, new SellerAccountModel());

        setContentView(R.layout.activity_main_seller_login);
        email = findViewById(R.id.email_login_seller);
        password = findViewById(R.id.password_login_seller);

        Button register_seller_button = findViewById(R.id.register_seller_button);
        Button login_seller_button = findViewById(R.id.login_button);
        register_seller_button.setOnClickListener(view -> presenter.onRegisterButtonClicked());
        login_seller_button.setOnClickListener(view -> presenter.onLoginButtonClicked());
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
    public void toastEmailFormatError() {
        email.setError("Enter correct email. Email format: example@somthing.com");
    }

    @Override
    public void toastException(Exception exception) {
        Toast.makeText(MainActivity_seller_login.this, "Could not login: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toastWelcome() {
        Toast.makeText(MainActivity_seller_login.this, "Welcome", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateLoginSuccessPage() {
        startActivity(new Intent(this, StoreOwnerMainActivity.class));
        finish();
    }

    @Override
    public void navigateRegisterPage() {
        startActivity(new Intent(MainActivity_seller_login.this, MainActivity_seller_register.class));
        finish();
    }
}
