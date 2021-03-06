package com.example.plantastic.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.plantastic.R;

public class passwordForgetSplash extends AppCompatActivity {


    Animation topAnimation, bottomAnimation;
    TextView update;
    ImageView check;
    private static int SPLASH_SCREEN = 1500;


    //Splashscreen redirects the user to the login screen after 1,5 seconds have passed.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_password_forget_splash);

        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        update = findViewById(R.id.updateTV);
        check = findViewById(R.id.checkIMG);

        update.setAnimation(topAnimation);
        check.setAnimation(bottomAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(passwordForgetSplash.this, Login.class);
                    startActivity(intent);
                    finish();}
        }, SPLASH_SCREEN);
    }
}