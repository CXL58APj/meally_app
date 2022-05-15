package com.example.meally_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginCustomerActivity extends AppCompatActivity {
    // Call Firebaseuth
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Initialize Firebaseuth
        mAuth = FirebaseAuth.getInstance();
//        Initialization of all the buttons and textboxes
        setContentView(R.layout.activity_login_customer);
        TextInputLayout txtcustomeremailloginlayout = findViewById(R.id.textInputLayoutLoginCustomerEmail);
        TextInputLayout txtcustomerpasswordloginlayout = findViewById(R.id.textInputLayoutLoginCustomerPassword);
        Button btnLoginCustomer = findViewById(R.id.btnLoginCustomer);
        ProgressBar myprogressBar = findViewById(R.id.progressBarLogin);
        TextInputEditText txtcustomeremail = findViewById(R.id.LoginCustomerEmail);
        TextInputEditText txtcustomerpassword = findViewById(R.id.LoginCustomerPassword);
        TextView btnForgotPass = findViewById(R.id.CustomerForgotPassword);

        btnLoginCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the customer(user) input from the textboxes
                String customeremail = txtcustomeremail.getText().toString().trim();
                String customerpassword = txtcustomerpassword.getText().toString().trim();

//                VALIDATIONS AND SHIT START
                if (customeremail.isEmpty()){
                    txtcustomeremail.setError("Please provide a valid email");
                    txtcustomeremail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(customeremail).matches()){
                    txtcustomeremail.setError("Please provide a valid email");
                    txtcustomeremail.requestFocus();
                    return;
                }
                if (customerpassword.isEmpty()){
                    txtcustomerpassword.setError("Please provide a valid password");
                    txtcustomerpassword.requestFocus();
                    return;
                }
                // END OF VALIDATIONS


//                  show the circular progress bar
                myprogressBar.setVisibility(View.VISIBLE);
//                  check the authentication in firebase if the user exist
                mAuth.signInWithEmailAndPassword(customeremail, customerpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
//                            check if the customer(user) email is verified
                            FirebaseUser customer = FirebaseAuth.getInstance().getCurrentUser();
                            // if email is verified display the DashboardActivity and remove all the activities from the past
                            if(customer.isEmailVerified()){
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // display Dashboard Activity and remove all the activities from the past.
                                        Intent i= new Intent(LoginCustomerActivity.this, DashboardActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                        finish();
                                    }
                                },100);
                            }else{
//                                if not verified, send an email verification and display a message
                                customer.sendEmailVerification();
                                Toast.makeText(LoginCustomerActivity.this,  "Please check your email to verify your account", Toast.LENGTH_LONG).show();
                            }

                        }else{
//                             if no internet connection or any errors , display this message
                            Toast.makeText(LoginCustomerActivity.this,  task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
//                          remove the circular progress bar
                        myprogressBar.setVisibility(View.GONE);
                    }
                });

            }
        });


        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // display Dashboard Activity and remove all the activities from the past.
                        Intent i= new Intent(LoginCustomerActivity.this, ForgotPasswordCustomerActivity.class);
                        startActivity(i);
                    }
                },100);
            }
        });



    }
}

