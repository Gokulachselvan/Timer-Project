package com.example.timerproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SoundSettingsActivity extends AppCompatActivity {
    private RadioGroup soundOptionsGroup;
    private static final String PREFS_NAME = "TimerPrefs"; // Change from "SoundSettings"
    private static final String KEY_SELECTED_SOUND = "selectedSound";
    private int selectedSoundId;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_settings);

        ImageButton settingsButton = findViewById(R.id.home_button);
        ImageButton historyButton = findViewById(R.id.history_button);

        settingsButton.setOnClickListener(v -> openSoundSettings());
        historyButton.setOnClickListener(v -> openTimerHistory());

        soundOptionsGroup = findViewById(R.id.soundOptionsGroup);

        // Load saved sound selection from SharedPreferences
        loadSoundSelection();

        // Preview sound on selection change
        soundOptionsGroup.setOnCheckedChangeListener((group, checkedId) -> previewSound(checkedId));

        findViewById(R.id.saveSoundButton).setOnClickListener(v -> saveSoundSelection());
    }

    private void previewSound(int soundId) {
        // Stop any currently playing sound
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        // Play selected sound for preview
        if (soundId == R.id.soundOption1) {
            mediaPlayer = MediaPlayer.create(this, R.raw.sound1);
            selectedSoundId = R.id.soundOption1;
        } else if (soundId == R.id.soundOption2) {
            mediaPlayer = MediaPlayer.create(this, R.raw.sound2);
            selectedSoundId = R.id.soundOption2;
        } else if (soundId == R.id.soundOption3) {
            mediaPlayer = MediaPlayer.create(this, R.raw.sound3);
            selectedSoundId = R.id.soundOption3;
        } else {
            selectedSoundId = -1;
            return;
        }

        mediaPlayer.start();
    }


    private void saveSoundSelection() {
        if (selectedSoundId == -1) {
            Toast.makeText(this, "Please select a sound", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save the selected sound in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_SELECTED_SOUND, selectedSoundId); // Saving selected sound ID
        editor.apply();

        Toast.makeText(this, "Sound selection saved successfully", Toast.LENGTH_SHORT).show();
    }


    private void loadSoundSelection() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedSoundId = sharedPreferences.getInt(KEY_SELECTED_SOUND, -1);

        if (savedSoundId != -1) {
            RadioButton savedRadioButton = findViewById(savedSoundId);
            if (savedRadioButton != null) { // Check for null
                savedRadioButton.setChecked(true);
                selectedSoundId = savedSoundId;
            }
        }
    }


    private void openSoundSettings() {
        Intent intent = new Intent(this, TimerScreenActivity.class);
        startActivity(intent);
    }

    private void openTimerHistory() {
        Intent intent = new Intent(this, TimerHistoryActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release MediaPlayer resources if still active
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
