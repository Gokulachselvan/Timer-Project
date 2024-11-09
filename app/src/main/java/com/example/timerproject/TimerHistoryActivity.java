package com.example.timerproject;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
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
    }

    private void loadTimerHistory() {
        timerHistoryArray.clear();

        // Query to fetch all records from TimerHistory table
        Cursor cursor = database.rawQuery("SELECT * FROM TimerHistory", null);
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

        // Notify the adapter to update the ListView
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

