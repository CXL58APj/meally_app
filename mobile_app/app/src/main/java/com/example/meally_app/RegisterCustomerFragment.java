package com.example.meally_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterCustomerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterCustomerFragment extends Fragment{
    private FirebaseAuth mAuth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterCustomerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterCustomerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterCustomerFragment newInstance(String param1, String param2) {
        RegisterCustomerFragment fragment = new RegisterCustomerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mAuth = FirebaseAuth.getInstance();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_customer, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputLayout txtcustomerfnamelayout = view.findViewById(R.id.textInputLayout9fnamecutomerreg);
        TextInputLayout txtcustomerlnamelayout = view.findViewById(R.id.textInputLayout9lnamecutomerreg);
        TextInputLayout txtcustomeremaillayout = view.findViewById(R.id.textInputLayout9Emailcutomerreg);
        TextInputLayout txtcustomerpasswordlayout = view.findViewById(R.id.textInputLayout10Passwordcustomerreg);
        Button btnRegCustomer = view.findViewById(R.id.btnregcustomer);
        TextInputEditText txtcustomerfname = view.findViewById(R.id.customerfname);
        TextInputEditText txtcustomerlname = view.findViewById(R.id.customerlname);
        TextInputEditText txtcustomeremail = view.findViewById(R.id.customeremail);
        TextInputEditText txtcustomerpassword = view.findViewById(R.id.customerpassword);
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
                if (customerlname.length()<2){
                    txtcustomerlnamelayout.setError("Minimum lastname length is 2 characters");
                    txtcustomerlname.requestFocus();
                    return;
                }
                if (customerlname.isEmpty()){
                    txtcustomerlnamelayout.setError("Please provide a valid lastname");
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
                mAuth.createUserWithEmailAndPassword(customeremail,customerpassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Customer customer= new Customer(customerfname, customerlname, customeremail);
                                    FirebaseDatabase.getInstance().getReference("Customers")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(customer).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(getContext(), "Registration success!", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(getContext(), "Registration failed!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }else{
                                    Toast.makeText(getContext(), "Registration failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}