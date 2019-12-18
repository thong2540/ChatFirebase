package com.example.chatfirebase.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatfirebase.R;
import com.example.chatfirebase.ui.login.LoginActivity;
import com.example.chatfirebase.ui.main.MainActivity;
import com.example.chatfirebase.util.ActivityTransition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashActivity extends AppCompatActivity {

    private TextView textView_Splash;
    private ImageView imageView_Splash_logo;
    FirebaseUser firebaseUser;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        setView();

        setOnClick();

        setInit();
    }

    private void setView() {
        textView_Splash = findViewById(R.id.tv1);
        imageView_Splash_logo = findViewById(R.id.logo);

    }

    private void setOnClick() {

    }

    private void setInit() {

        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.mytra);
        imageView_Splash_logo.startAnimation(myanim);
        textView_Splash.startAnimation(myanim);

    }

    private void startMain() {
        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            ActivityTransition.GoLeft(SplashActivity.this);
            finish();

        }, 3000); //1000 = 1 วิ
    }

    private void startLogin() {
        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
            ActivityTransition.GoLeft(SplashActivity.this);
            finish();

        }, 3000); //1000 = 1 วิ
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseUser != null) {
            startMain();
        } else {
            startLogin();
        }
    }
}
