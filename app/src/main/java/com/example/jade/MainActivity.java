package com.example.jade;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000; // 5 seconds

    // Variables for Animation
    Animation topAnim, bottomAnim;

    ImageView town_img;
    TextView jade_name,jade_slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Removing the notification bar from screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //Hooks
        town_img = findViewById(R.id.townimg);
        jade_name = findViewById(R.id.jadename);
        jade_slogan = findViewById(R.id.jadeslogan);

        //Assigning the animations
        town_img.setAnimation(topAnim);
        jade_name.setAnimation(bottomAnim);
        jade_slogan.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Loginscreen.class);

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(town_img, "logo_image");
                pairs[1] = new Pair<View, String>(jade_name, "logo_text");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                startActivity(intent, options.toBundle());
                finish(); // Stops this activity and removes it from the list
            }
                },SPLASH_SCREEN);

    }
}