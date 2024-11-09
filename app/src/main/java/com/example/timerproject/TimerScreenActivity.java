package com.example.timerproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TimerScreenActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "TimerPrefs";
    private static final String KEY_SELECTED_SOUND = "selectedSound";
    private TextView timerDisplay;
    private EditText inputHours, inputMinutes, inputSeconds;
    private Button startButton, pauseButton, resetButton;
    private CountDownTimer timer;
    private long timeLeftInMillis;
    private boolean isTimerRunning = false;
    private boolean isTimerPaused = false; // Track pause state
    private long pauseTimeInMillis; // Track time when paused

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
        pauseButton.setOnClickListener(v -> pauseOrResumeTimer());
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

        startCountdown(timeLeftInMillis);
    }

    private void startCountdown(long millis) {
        timer = new CountDownTimer(millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerDisplay();
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                timerDisplay.setText("Time's up!");
                playNotificationSound();

                // Get the current time for history logging
                String endTime = java.text.DateFormat.getTimeInstance().format(new java.util.Date());
                String duration = timerDisplay.getText().toString();

                // Save the timer history
                saveTimerHistory(duration, endTime);
            }

        };

        timer.start();
        isTimerRunning = true;
        isTimerPaused = false; // Reset pause state
    }

    private void pauseOrResumeTimer() {
        if (isTimerRunning) {
            // Pause the timer
            timer.cancel();
            pauseTimeInMillis = timeLeftInMillis;
            isTimerRunning = false;
            isTimerPaused = true;
        } else if (isTimerPaused) {
            // Resume the timer
            startCountdown(pauseTimeInMillis);
            isTimerRunning = true;
            isTimerPaused = false;
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
        isTimerPaused = false;
    }

    private void updateTimerDisplay() {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timerDisplay.setText(timeFormatted);
    }

    private void playNotificationSound() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedSoundId = sharedPreferences.getInt(KEY_SELECTED_SOUND, -1);

        MediaPlayer mediaPlayer;
        if (savedSoundId == R.id.soundOption1) {
            mediaPlayer = MediaPlayer.create(this, R.raw.sound1);
        } else if (savedSoundId == R.id.soundOption2) {
            mediaPlayer = MediaPlayer.create(this, R.raw.sound2);
        } else if (savedSoundId == R.id.soundOption3) {
            mediaPlayer = MediaPlayer.create(this, R.raw.sound3);
        } else {
            // Default sound or no sound selected
            mediaPlayer = MediaPlayer.create(this, R.raw.sound2); // Play default sound2 if no selection
        }

        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(MediaPlayer::release);
    }

    private void saveTimerHistory(String duration, String endTime) {
        SQLiteDatabase database = openOrCreateDatabase("TimerHistoryDB", MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS TimerHistory (id INTEGER PRIMARY KEY AUTOINCREMENT, duration TEXT, endTime TEXT)");
        String query = "INSERT INTO TimerHistory (duration, endTime) VALUES (?, ?)";
        database.execSQL(query, new Object[]{duration, endTime});
        database.close();
    }


}
