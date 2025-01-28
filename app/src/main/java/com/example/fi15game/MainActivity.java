package com.example.fi15game;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.ArrayList;

import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.lang.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;


public class MainActivity extends AppCompatActivity {

    private final ArrayList<Button> tiles = new ArrayList<>();
    public int emptyTileIndex = 15;
    private TextView timerTextView;
    private boolean isTimerRunning = false;
    private long startTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        timerTextView = findViewById(R.id.timerTextView);
        Button resetButton = findViewById(R.id.resetButton);
        Button viewRecordsButton = findViewById(R.id.viewRecordsButton);


        for (int i = 0; i < 16; i++) {
            Button tile = new Button(this);
            tile.setText(i < 15 ? String.valueOf(i + 1) : "");
            tile.setTag(i);
            tile.setOnClickListener(this::onTileClick);
            tiles.add(tile);
            gridLayout.addView(tile, new GridLayout.LayoutParams());
        }
        shuffleTiles();
        resetButton.setOnClickListener(v -> {
            shuffleTiles();
            resetTimer();
        });
        viewRecordsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RecordsActivity.class);
            startActivity(intent);
        });
        startTimer();
    }

    private void onTileClick(View view) {
        int clickedIndex = (int) view.getTag();

        if (swapPosition(clickedIndex, emptyTileIndex)) {
            // Меняем местами плитку и пустое место
            tiles.get(emptyTileIndex).setText(((Button) view).getText());
            ((Button) view).setText("");

            emptyTileIndex = clickedIndex;

            if (isGameWin()) {
                Toast.makeText(this, "Вы выиграли!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean swapPosition(int index1, int index2) {
        int row1 = index1 / 4, col1 = index1 % 4;
        int row2 = index2 / 4, col2 = index2 % 4;

        return (Math.abs(row1 - row2) == 1 && col1 == col2) || (Math.abs(col1 - col2) == 1 && row1 == row2);
    }


    private void shuffleTiles() {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 15; i++) numbers.add(i + 1);
        numbers.add(0);
        Collections.shuffle(numbers);
        emptyTileIndex = numbers.indexOf(0);

        for (int i = 0; i < tiles.size(); i++) {
            tiles.get(i).setText(numbers.get(i) == 0 ? "" : String.valueOf(numbers.get(i)));
        }
    }

    private void startTimer() {
        isTimerRunning = true;
        startTime = System.currentTimeMillis();
        new Thread(() -> {
            while (isTimerRunning) {
                runOnUiThread(() -> {
                    long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
                    timerTextView.setText("Время: " + elapsedTime + " сек.");
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void stopTimer() {
        isTimerRunning = false;
    }

    private void resetTimer() {
        stopTimer();
        startTimer();
    }

    private boolean isGameWin() {
        for (int i = 0; i < 15; i++) {
            if (!tiles.get(i).getText().toString().equals(String.valueOf(i + 1))) {
                return false;
            }
        }
        stopTimer();
        saveRecord();
        return true;
    }

    private void saveRecord() {
        SharedPreferences prefs = getSharedPreferences("Records", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String username = getSharedPreferences("Users", MODE_PRIVATE).getString("currentUser", "Unknown");
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        String record = username + ": " + elapsedTime + " сек.";
        editor.putString("record_" + System.currentTimeMillis(), record);
        editor.apply();
    }
}