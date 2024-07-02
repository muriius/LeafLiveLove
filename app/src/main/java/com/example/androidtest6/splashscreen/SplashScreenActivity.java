package com.example.androidtest6.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidtest6.MainActivity;
import com.example.androidtest6.R;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 1000; // 1 Sekunde

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Handler, um den Splash Screen nach der SPLASH_DISPLAY_LENGTH Millisekunden zu beenden
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Starte die MainActivity und beende den Splash Screen
                Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
