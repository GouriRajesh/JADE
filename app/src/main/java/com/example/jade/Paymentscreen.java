package com.example.jade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class Paymentscreen extends AppCompatActivity implements PaymentResultListener {

    TextInputLayout pay_amt;
    Button pay_btn;
    String uemail;
    String uphone;
    String uname;

    String currentUserId;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    MaterialToolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentscreen);

        toolbar = findViewById(R.id.topAppbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;
                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id)
                {
                    case R.id.nav_home:
                        i = new Intent(Paymentscreen.this,Homescreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_translate:
                        i = new Intent(Paymentscreen.this,Translatescreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_maps:
                        i = new Intent(Paymentscreen.this,Mapscreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_docs:
                        i = new Intent(Paymentscreen.this,Docsscreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_pay:
                        i = new Intent(Paymentscreen.this,Paymentscreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_food:
                        i = new Intent(Paymentscreen.this,Foodscreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_logout:
                        i = new Intent(Paymentscreen.this,Loginscreen.class);
                        startActivity(i);
                        finish();
                        FirebaseAuth.getInstance().signOut();
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });

        // For faster loading of checkout form
        Checkout.preload(getApplicationContext());

        //Details from Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        //Toast.makeText(getApplicationContext(), currentUserId,Toast.LENGTH_SHORT).show();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.hasChild("Full Name")){
                        uname = snapshot.child("Full Name").getValue().toString();
                    }
                    if(snapshot.hasChild("Email")){
                        uemail = snapshot.child("Email").getValue().toString();
                    }
                    if(snapshot.hasChild("Phone Number")){
                        uphone = snapshot.child("Phone Number").getValue().toString();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Error in fetching details",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Firebase read failed!",Toast.LENGTH_SHORT).show();

            }
        });

        pay_amt = findViewById(R.id.payamt);

        pay_btn = findViewById(R.id.paybtn);
        pay_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pay_amt.getEditText().getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please enter a valid amount!", Toast.LENGTH_SHORT).show();
                }else {
                    startPayment();
                }
            }
        });

    }

        public void startPayment() {

            //You need to pass current activity in order to let Razorpay create CheckoutActivity
            AppCompatActivity activity = Paymentscreen.this;
            Checkout co = new Checkout();
            co.setKeyID("rzp_test_ub91kDDRTbi6eQ");

            try {
                JSONObject options = new JSONObject();
                options.put("name", "JADE");
                options.put("description", "Payment on the go!");
                options.put("send_sms_hash", true);
                options.put("allow_rotation", true);
                options.put("currency", "INR");

                String payment = pay_amt.getEditText().getText().toString();
                double total = Double.parseDouble(payment);
                total = total * 100;
                options.put("amount", total );

                JSONObject preFill = new JSONObject();
                preFill.put("name", uname);
                preFill.put("email", uemail);
                preFill.put("contact", uphone);
                options.put("theme.color","#152042");
                options.put("modal.confirm_close",true);

                options.put("prefill", preFill);

                co.open(activity, options);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onPaymentSuccess (String s){
            try {
                Toast.makeText(getApplicationContext(), "Payment Successful: " + s, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Exception in onPaymentSuccess: " + e, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onPaymentError( int i, String s){
            try {
                Toast.makeText(getApplicationContext(), "Error! Payment failed: " + i + " " + s, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Exception in onPaymentError: " + e, Toast.LENGTH_SHORT).show();
            }
        }
}
