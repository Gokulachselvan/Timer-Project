package com.example.timerproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TimerScreenActivity extends AppCompatActivity {
    private TextView timerDisplay;
    private EditText inputHours, inputMinutes, inputSeconds;
    private Button startButton, pauseButton, resetButton;
    private CountDownTimer timer;
    private long timeLeftInMillis;
    private boolean isTimerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_screen);

        // Toolbar buttons
        ImageButton settingsButton = findViewById(R.id.settings_button);
        ImageButton historyButton = findViewById(R.id.history_button);

        // Timer controls
        timerDisplay = findViewById(R.id.timerDisplay);
        inputHours = findViewById(R.id.inputHours);
        inputMinutes = findViewById(R.id.inputMinutes);
        inputSeconds = findViewById(R.id.inputSeconds);
        startButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.pauseButton);
        resetButton = findViewById(R.id.resetButton);

        // Button listeners
        startButton.setOnClickListener(v -> startTimer());
        pauseButton.setOnClickListener(v -> pauseTimer());
        resetButton.setOnClickListener(v -> resetTimer());

        // Toolbar button listeners
        settingsButton.setOnClickListener(v -> openSoundSettings());
        historyButton.setOnClickListener(v -> openTimerHistory());
    }

    private void openSoundSettings() {
        Intent intent = new Intent(this, SoundSettingsActivity.class);
        startActivity(intent);
    }

    private void openTimerHistory() {
        Intent intent = new Intent(this, TimerHistoryActivity.class);
        startActivity(intent);
    }

    private void startTimer() {
        if (isTimerRunning) return;

        int hours = Integer.parseInt(inputHours.getText().toString().isEmpty() ? "0" : inputHours.getText().toString());
        int minutes = Integer.parseInt(inputMinutes.getText().toString().isEmpty() ? "0" : inputMinutes.getText().toString());
        int seconds = Integer.parseInt(inputSeconds.getText().toString().isEmpty() ? "0" : inputSeconds.getText().toString());

        timeLeftInMillis = (hours * 3600 + minutes * 60 + seconds) * 1000;
        if (timeLeftInMillis == 0) {
            timerDisplay.setText("00:00:00");
            return;
        }

        timer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerDisplay();
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                timerDisplay.setText("Time's up!");
            }
        };

        timer.start();
        isTimerRunning = true;
    }

    private void pauseTimer() {
        if (isTimerRunning) {
            timer.cancel();
            isTimerRunning = false;
        }
    }

    private void resetTimer() {
        if (timer != null) timer.cancel();
        timeLeftInMillis = 0;
        updateTimerDisplay();
        inputHours.setText("");
        inputMinutes.setText("");
        inputSeconds.setText("");
        isTimerRunning = false;
    }

    private void updateTimerDisplay() {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timerDisplay.setText(timeFormatted);
    }
}
