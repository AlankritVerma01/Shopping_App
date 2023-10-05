package com.example.b07_project.pages.start_screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.b07_project.R;
import com.google.firebase.FirebaseApp;

public class MainActivity_question extends AppCompatActivity {
    Button button1;
    Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_login_question);
        button1= findViewById(R.id.seller);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent = new Intent(MainActivity_question.this, MainActivity_seller_login.class);
                startActivity(intent);
            }
        });
        button2= findViewById(R.id.customer);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainActivity_question.this, MainActivity_customer_login.class);
                startActivity(intent2);
            }
        });


    }
}