package com.example.timerproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class FlashScreenActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(FlashScreenActivity.this, TimerScreenActivity.class);
            startActivity(intent);
            finish(); // Close the FlashScreenActivity
        }, SPLASH_DELAY);
    }
}
