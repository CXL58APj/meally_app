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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterCustomerActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_customer_store);
        TextInputLayout txtcustomerfnamelayout = findViewById(R.id.textInputLayout9fnamecutomerreg);
        TextInputLayout txtcustomerlnamelayout = findViewById(R.id.textInputLayout9lnamecutomerreg);
        TextInputLayout txtcustomeremaillayout = findViewById(R.id.textInputLayout9Emailcutomerreg);
        TextInputLayout txtcustomerpasswordlayout = findViewById(R.id.textInputLayout10Passwordcustomerreg);
        Button btnRegCustomer = findViewById(R.id.btnregcustomer);
        ProgressBar myprogressBar = findViewById(R.id.progressBar);
        TextInputEditText txtcustomerfname = findViewById(R.id.customerfname);
        TextInputEditText txtcustomerlname = findViewById(R.id.customerlname);
        TextInputEditText txtcustomeremail = findViewById(R.id.customeremail);
        TextInputEditText txtcustomerpassword = findViewById(R.id.customerpassword);
        btnRegCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String customerfname = txtcustomerfname.getText().toString().trim();
                String customerlname = txtcustomerlname.getText().toString().trim();
                String customeremail = txtcustomeremail.getText().toString().trim();
                String customerpassword = txtcustomerpassword.getText().toString().trim();
//                Toast.makeText(getContext(), customerfname, Toast.LENGTH_SHORT).show();

//                validations skrt skrt
                if (customerfname.isEmpty()){
                    txtcustomerfnamelayout.setError("Please provide a valid firstname");
                    txtcustomerfname.requestFocus();
                    return;
                }
                if (customerfname.length()<2) {
                    txtcustomerfnamelayout.setError("Minimum firstname length is 2 characters");
                    txtcustomerfname.requestFocus();
                    return;
                }
                if (customerlname.isEmpty()){
                    txtcustomerlnamelayout.setError("Please provide a valid lastname");
                    txtcustomerlname.requestFocus();
                    return;
                }
                if (customerlname.length()<2){
                    txtcustomerlnamelayout.setError("Minimum lastname length is 2 characters");
                    txtcustomerlname.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(customeremail).matches()){
                    txtcustomeremaillayout.setError("Please provide a valid email");
                    txtcustomeremail.requestFocus();
                    return;
                }

                if (customerpassword.length()<6){
                    txtcustomerpasswordlayout.setError("Minimum password length is 6 characters");
                    txtcustomerpassword.requestFocus();
                    return;
                }
                if (customerpassword.isEmpty()){
                    txtcustomerpasswordlayout.setError("Please provide a valid password");
                    txtcustomerpassword.requestFocus();
                    return;
                }
                // Add the user in the authentication in firebase
                mAuth.createUserWithEmailAndPassword(customeremail,customerpassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    // show the circular progress bar
                                    myprogressBar.setVisibility(View.VISIBLE);
                                    // after adding the the customer(user) in the authentication in firebase, make a copy in the realtime database
                                    Customer customer= new Customer(customerfname, customerlname, customeremail);
                                    FirebaseDatabase.getInstance().getReference("Customers")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(customer).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // remove the circular progressbar
                                            myprogressBar.setVisibility(View.GONE);
                                            if(task.isSuccessful()){
                                                // if successfully added the customer(user) in the authentication and realtime database,
                                                // display a message , clear all the text and redirect to LoginCustomerAcvitity
                                                Toast.makeText(RegisterCustomerActivity.this, "Registered successfully!", Toast.LENGTH_SHORT).show();
                                                txtcustomeremail.setText("");
                                                txtcustomerfname.setText("");
                                                txtcustomerlname.setText("");
                                                txtcustomerpassword.setText("");
                                                txtcustomerfname.requestFocus();

                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Intent i= new Intent(RegisterCustomerActivity.this, LoginCustomerActivity.class);
                                                        startActivity(i);
                                                        finish();
                                                    }
                                                },100);
                                            }else{
                                                // if no internet connection or any errors, display the error message.
                                                Toast.makeText(RegisterCustomerActivity.this,  task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }else{
                                    // if no internet connection or any errors, display the error message.
                                    Toast.makeText(RegisterCustomerActivity.this,  task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }
        });
    }
}