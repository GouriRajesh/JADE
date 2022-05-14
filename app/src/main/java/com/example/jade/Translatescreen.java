package com.example.jade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.InputStream;

public class Translatescreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    MaterialToolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    TextInputLayout inputToTranslate,translatedTv;
    String originalText, translatedText,lang;
    Button translateButton;
    boolean connected;
    Translate translate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translatescreen);

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
                        i = new Intent(Translatescreen.this,Homescreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_translate:
                        i = new Intent(Translatescreen.this,Translatescreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_maps:
                        i = new Intent(Translatescreen.this,Mapscreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_docs:
                        i = new Intent(Translatescreen.this,Docsscreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_pay:
                        i = new Intent(Translatescreen.this,Paymentscreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_food:
                        i = new Intent(Translatescreen.this,Foodscreen.class);
                        startActivity(i);
                        break;
                    case R.id.nav_logout:
                        i = new Intent(Translatescreen.this,Loginscreen.class);
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


        Spinner spinner = findViewById(R.id.languagespinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,  R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        inputToTranslate = findViewById(R.id.sourcetext);
        translatedTv = findViewById(R.id.translatetext);
        translateButton = findViewById(R.id.translatebtn);

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkInternetConnection()) {
                    //If there is internet connection, get translate service and start translation:
                    getTranslateService();
                    translate();

                } else {
                    //If not, display "no connection" warning:
                    translatedTv.getEditText().setText(getResources().getString(R.string.no_connection));
                }
            }
        });
    }

    public void getTranslateService() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try (InputStream is = getResources().openRawResource(R.raw.credentials)) {

            //Get credentials:
            final GoogleCredentials myCredentials = GoogleCredentials.fromStream(is);

            //Set credentials and get translate service:
            TranslateOptions translateOptions = TranslateOptions.newBuilder().setCredentials(myCredentials).build();
            translate = translateOptions.getService();

        } catch (IOException ioe) {
            ioe.printStackTrace();

        }
    }

    public void translate() {

        //Get input text to be translated:
        originalText = inputToTranslate.getEditText().getText().toString();
        Translation translation = translate.translate(originalText, Translate.TranslateOption.targetLanguage(lang), Translate.TranslateOption.model("base"));
        translatedText = translation.getTranslatedText();

        //Translated text and original text are set to TextViews:
        translatedTv.getEditText().setText(translatedText);

    }

    public boolean checkInternetConnection() {

        //Check internet connection:
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //Means that we are connected to a network (mobile or wi-fi)
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        return connected;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
        lang = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(getApplicationContext(), lang, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Nothing here
    }
}