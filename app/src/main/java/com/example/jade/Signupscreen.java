package com.example.jade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

public class Signupscreen extends AppCompatActivity {

    //Variables
    TextInputLayout regName, regEmail, regPhone, regPassword;
    Button regBtn, regToLoginBtn;
    String currentUserId;

    DatabaseReference databaseReference;
    FirebaseDatabase reference;
    FirebaseAuth mAuth;

    // Checking for email field
    private Boolean validateEmail(String email) {
        if (email.isEmpty()) {
            regEmail.setError("Please enter a valid email.");
            return false;
        } else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            regEmail.setError("Please enter a valid email following the pattern.");
            return false;
        } else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }

    // Checking for password field
    private Boolean validatePassword(String pass) {
        if (pass.isEmpty()) {
            regPassword.setError("Please enter a valid password.");
            return false;
        } else if (pass.length() <= 6) {
            regPassword.setError("Please enter a valid password greater than 6 characters.");
            return false;
        } else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }

    // Checking for name field
    private Boolean validateName(String name) {
        if (name.isEmpty()) {
            regName.setError("Please enter a valid name.");
            return false;
        } else {
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }

    // Checking for phone field
    private Boolean validatePhone(String phone) {
        if (phone.isEmpty()) {
            regPhone.setError("Please enter a valid phone number.");
            return false;
        } else if (phone.length() < 10) {
            regPhone.setError("Please enter a valid phone number equal to 10 digits.");
            return false;
        } else {
            regPhone.setError(null);
            regPhone.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupscreen);

        //Removing status bar from the screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Hooks to all elements in Sign Up Screen XML file
        regName = findViewById(R.id.name);
        regEmail = findViewById(R.id.email);
        regPassword = findViewById(R.id.password);
        regPhone = findViewById(R.id.phone);
        regBtn = findViewById(R.id.reg_btn);
        regToLoginBtn = findViewById(R.id.movetologin_btn);

        // Store data in firebase on clicking register button
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Get all the values from text fields
                String name = regName.getEditText().getText().toString().trim();
                String email = regEmail.getEditText().getText().toString().trim();
                String password = regPassword.getEditText().getText().toString().trim();
                String phone = regPhone.getEditText().getText().toString().trim();

                if(!validateEmail(email) || !validatePassword(password) || !validateName(name) || !validatePhone(phone)) {
                    return;
                }
                else {
                    // For Firebase Authentication and Saving Data into Real-Time Database
                    mAuth = FirebaseAuth.getInstance();
                    //currentUserId = mAuth.getCurrentUser().getUid();

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Signupscreen.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                databaseReference = reference.getInstance().getReferenceFromUrl("https://jade-2021-default-rtdb.firebaseio.com/");

                                currentUserId = mAuth.getCurrentUser().getUid();

                                //Storing Data
                                HashMap userMap = new HashMap();
                                userMap.put("Full Name", name);
                                userMap.put("Email", email);
                                userMap.put("Phone Number", phone);

                                databaseReference.child("users").child(currentUserId).updateChildren(userMap).addOnCompleteListener(Signupscreen.this, new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "New user created and data is stored!", Toast.LENGTH_SHORT).show();
                                            // Move to Home Screen
                                            Intent intent = new Intent(Signupscreen.this, Homescreen.class);
                                            startActivity(intent);
                                            finish(); // Stops this activity and removes it from the list
                                        } else {
                                            String message = task.getException().getMessage();
                                            Toast.makeText(getApplicationContext(), "Error!" + message, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "Error! New user is not created.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        regToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signupscreen.this, Loginscreen.class);
                startActivity(intent);
                finish();
            }
        });

    }
}