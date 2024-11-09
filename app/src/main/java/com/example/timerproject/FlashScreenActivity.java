package com.example.timerproject;

public class FlashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(FlashScreenActivity.this, HomeScreenActivity.class));
            finish();
        }, 2000);
    }
}
