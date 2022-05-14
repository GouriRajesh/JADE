package com.example.jade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Loginscreen extends AppCompatActivity {

    //Variables
    Button callSignUp, login_btn;
    ImageView image;
    TextView logoText, sloganText;
    TextInputLayout emailid, password;

    FirebaseAuth mAuth;

    // Checking for empty email field
    private Boolean validateEmail(String email)
    {
        if (email.isEmpty())
        {
            emailid .setError("Please enter a valid email.");
            return false;
        }
        else if(!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
            emailid.setError("Please enter a valid email following the pattern.");
            return false;
        }
        else{
            emailid.setError(null);
            emailid.setErrorEnabled(false);
            return true;
        }
    }
    // Checking for empty password field
    private Boolean validatePassword(String pass)
    {
        if (pass.isEmpty())
        {
            password.setError("Please enter a valid password.");
            return false;
        }
        else{
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Removing status bar from the screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loginscreen);

        //Move to signup screen on click of button + Hooks
        callSignUp = findViewById(R.id.newsignup);
        image = findViewById(R.id.login_logo);
        logoText = findViewById(R.id.login_text1);
        sloganText = findViewById(R.id.login_text2);
        emailid = findViewById(R.id.emailid);
        password = findViewById(R.id.password);
        login_btn = findViewById(R.id.login_btn);


        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Loginscreen.this, Signupscreen.class);

                //startActivity(intent);

                Pair[] pairs = new Pair[7]; // As we have seven elements
                pairs[0] = new Pair<View, String>(image, "logo_image");
                pairs[1] = new Pair<View, String>(logoText, "logo_text");
                pairs[2] = new Pair<View, String>(sloganText, "logo_desc");
                pairs[3] = new Pair<View, String>(emailid, "email_tran");
                pairs[4] = new Pair<View, String>(password, "password_tran");
                pairs[5] = new Pair<View, String>(login_btn, "login_tran");
                pairs[6] = new Pair<View, String>(callSignUp, "logo_signup_tran");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Loginscreen.this, pairs);
                startActivity(intent, options.toBundle());

            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailid.getEditText().getText().toString().trim();
                String pass = password.getEditText().getText().toString().trim();

                if(!validateEmail(email) || !validatePassword(pass)){
                    return;
                }
                else {
                    //Checking with Firebase Authentication
                    mAuth = FirebaseAuth.getInstance();

                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Successful! User exists and is logged in.", Toast.LENGTH_SHORT).show();

                                // Move to Home Screen
                                Intent intent = new Intent(Loginscreen.this, Homescreen.class);
                                startActivity(intent);
                                finish(); // Stops this activity and removes it from the list
                            } else {
                                Toast.makeText(getApplicationContext(), "Error! User doesn't exist.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                }
        });
    }
}