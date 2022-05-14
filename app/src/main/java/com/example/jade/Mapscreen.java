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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class Mapscreen extends AppCompatActivity {

    TextInputLayout start, end;
    String start_point, end_point;
    Button search_btn;

    MaterialToolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapscreen);

        start = findViewById(R.id.startdest);
        end = findViewById(R.id.enddest);

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
                        i = new Intent(Mapscreen.this,Homescreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_translate:
                        i = new Intent(Mapscreen.this,Translatescreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_maps:
                        i = new Intent(Mapscreen.this,Mapscreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_docs:
                        i = new Intent(Mapscreen.this,Docsscreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_pay:
                        i = new Intent(Mapscreen.this,Paymentscreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_food:
                        i = new Intent(Mapscreen.this,Foodscreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_logout:
                        i = new Intent(Mapscreen.this,Loginscreen.class);
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

        search_btn = findViewById(R.id.searchbtn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                start_point = start.getEditText().getText().toString().trim().toLowerCase();
                end_point = end.getEditText().getText().toString().trim().toLowerCase();

                if(start_point.isEmpty() || end_point.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter both locations!",Toast.LENGTH_SHORT).show();
                }
                else{
                    DisplayTrack(start_point,end_point);
                }
            }
        });
    }

    private void DisplayTrack(String start_point, String end_point) {
        try{
            //When google maps is installed
            Uri uri = Uri.parse("http://www.google.co.in/maps/dir/"+ start_point + "/" + end_point);

            //Initialize intent with action view
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);

            //Set package
            intent.setPackage("com.google.android.apps.maps");

            //Set flag
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //Start Activity
            startActivity(intent);

        }catch(ActivityNotFoundException e){

            //When google maps is not found
            Uri uri = Uri.parse("http://www.google.com/store/apps/details?id=com.google.android.apps.maps");

            //Initialize intent with action view
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);

            //Set flag
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //Start Activity
            startActivity(intent);
        }
    }
}