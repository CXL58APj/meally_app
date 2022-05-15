package com.example.meally_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordCustomerActivity extends AppCompatActivity {
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_customer);
        Button btnForgotPassword = findViewById(R.id.btnForgotPasswordCustomer);
        EditText customerEmail = findViewById(R.id.editTextTextEmailAddressCustomer);
        ProgressBar myprogressBar = findViewById(R.id.progressBarCustomerResetPassword);
        myprogressBar.bringToFront();
        auth = FirebaseAuth.getInstance();
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = customerEmail.getText().toString().trim();
                if (email.isEmpty()){
                    customerEmail.setError("Please provide a valid email");
                    customerEmail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    customerEmail.setError("Please provide a valid email");
                    customerEmail.requestFocus();
                    return;
                }
                myprogressBar.setVisibility(View.VISIBLE);
                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ForgotPasswordCustomerActivity.this,  "Please check your email to reset the password of your account", Toast.LENGTH_LONG).show();
                            finish();
                        }else{
                            Toast.makeText(ForgotPasswordCustomerActivity.this,  task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                        myprogressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
}