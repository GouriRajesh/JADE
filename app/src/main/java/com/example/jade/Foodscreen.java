package com.example.jade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Foodscreen extends AppCompatActivity {

    MaterialToolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Button food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodscreen);

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
                        i = new Intent(Foodscreen.this,Homescreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_translate:
                        i = new Intent(Foodscreen.this,Translatescreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_maps:
                        i = new Intent(Foodscreen.this,Mapscreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_docs:
                        i = new Intent(Foodscreen.this,Docsscreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_pay:
                        i = new Intent(Foodscreen.this,Paymentscreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_food:
                        i = new Intent(Foodscreen.this,Foodscreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_logout:
                        i = new Intent(Foodscreen.this,Loginscreen.class);
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

        food  = findViewById(R.id.foodbtn);

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    Uri uri  = Uri.parse("https://www.swiggy.com/");

                    Intent swiggyintent = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(swiggyintent);

                }catch(ActivityNotFoundException e){

                    //When swiggy is not found redirect to play store
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=in.swiggy.android&hl=en_IN&gl=US");

                    //Initialize intent with action view
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);

                    //Set flag
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    //Start Activity
                    startActivity(intent);
                }
            }
        });
    }
}