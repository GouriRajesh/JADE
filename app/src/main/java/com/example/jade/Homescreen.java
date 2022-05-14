package com.example.jade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Homescreen extends AppCompatActivity implements View.OnClickListener {

    public CardView card1, card2, card3, card4,card5;
    MaterialToolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        card1 = (CardView) findViewById(R.id.c1);
        card2 = (CardView) findViewById(R.id.c2);
        card3 = (CardView) findViewById(R.id.c3);
        card4 = (CardView) findViewById(R.id.c4);
        card5 = (CardView) findViewById(R.id.c5);

        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
        card3.setOnClickListener(this);
        card4.setOnClickListener(this);
        card5.setOnClickListener(this);

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
                        i = new Intent(Homescreen.this,Homescreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_translate:
                        i = new Intent(Homescreen.this,Translatescreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_maps:
                        i = new Intent(Homescreen.this,Mapscreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_docs:
                        i = new Intent(Homescreen.this,Docsscreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_pay:
                        i = new Intent(Homescreen.this,Paymentscreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_food:
                        i = new Intent(Homescreen.this,Foodscreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_logout:
                        i = new Intent(Homescreen.this,Loginscreen.class);
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
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Homescreen.this,Loginscreen.class);
        startActivity(intent);
        finish();
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
    }
/*
    @Override
    public void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().signOut();
    }
*/
    @Override
    public void onClick(View v) {
    Intent i;

    switch (v.getId()){
        case R.id.c1:
            i = new Intent(Homescreen.this,Translatescreen.class);
            startActivity(i);
            break;
        case R.id.c2:
            i = new Intent(Homescreen.this,Mapscreen.class);
            startActivity(i);
            break;
        case R.id.c3:
            i = new Intent(Homescreen.this,Docsscreen.class);
            startActivity(i);
            break;
        case R.id.c4:
            i = new Intent(Homescreen.this,Paymentscreen.class);
            startActivity(i);
            break;
        case R.id.c5:
            i = new Intent(Homescreen.this,Foodscreen.class);
            startActivity(i);
            break;
         }
    }
}