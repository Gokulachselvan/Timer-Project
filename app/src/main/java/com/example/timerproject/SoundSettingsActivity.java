package com.example.timerproject;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SoundSettingsActivity extends AppCompatActivity {
    private RadioGroup soundOptionsGroup;
    private static final String PREFS_NAME = "SoundSettings";
    private static final String KEY_SELECTED_SOUND = "selectedSound";
    private int selectedSoundId;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_settings);

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
        switch (soundId) {
            case R.id.soundOption1:
                mediaPlayer = MediaPlayer.create(this, R.raw.sound1);
                selectedSoundId = R.id.soundOption1;
                break;
            case R.id.soundOption2:
                mediaPlayer = MediaPlayer.create(this, R.raw.sound2);
                selectedSoundId = R.id.soundOption2;
                break;
            case R.id.soundOption3:
                mediaPlayer = MediaPlayer.create(this, R.raw.sound3);
                selectedSoundId = R.id.soundOption3;
                break;
            default:
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

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_SELECTED_SOUND, selectedSoundId);
        editor.apply();

        Toast.makeText(this, "Sound selection saved", Toast.LENGTH_SHORT).show();
    }

    private void loadSoundSelection() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedSoundId = sharedPreferences.getInt(KEY_SELECTED_SOUND, -1);

        if (savedSoundId != -1) {
            RadioButton savedRadioButton = findViewById(savedSoundId);
            savedRadioButton.setChecked(true);
            selectedSoundId = savedSoundId;
        }
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

