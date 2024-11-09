package com.example.timerproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class TimerHistoryActivity extends AppCompatActivity {
    private ListView timerHistoryList;
    private ArrayList<String> timerHistoryArray;
    private ArrayAdapter<String> adapter;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_history);

        ImageButton settingsButton = findViewById(R.id.settings_button);
        ImageButton historyButton = findViewById(R.id.history_button);


        timerHistoryList = findViewById(R.id.timerHistoryList);

        // Initialize SQLite database
        database = openOrCreateDatabase("TimerHistoryDB", MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS TimerHistory (id INTEGER PRIMARY KEY AUTOINCREMENT, duration TEXT, endTime TEXT)");

        // Initialize ArrayList and Adapter
        timerHistoryArray = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, timerHistoryArray);
        timerHistoryList.setAdapter(adapter);

        // Load and display timer history
        loadTimerHistory();
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

    private void loadTimerHistory() {
        SQLiteDatabase database = openOrCreateDatabase("TimerHistoryDB", MODE_PRIVATE, null);
        timerHistoryArray.clear();
        Cursor cursor = database.rawQuery("SELECT * FROM TimerHistory ORDER BY id DESC", null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex("duration"));
                @SuppressLint("Range") String endTime = cursor.getString(cursor.getColumnIndex("endTime"));
                timerHistoryArray.add("Duration: " + duration + ", Ended at: " + endTime);
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "No timer history found", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        database.close();
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database when activity is destroyed
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
}

