package com.example.fi15game;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class RecordsActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        TextView recordsTextView = findViewById(R.id.recordsTextView);

        SharedPreferences prefs = getSharedPreferences("Records", MODE_PRIVATE);
        Map<String, ?> records = prefs.getAll();

        StringBuilder recordsText = new StringBuilder();
        for (Map.Entry<String, ?> entry : records.entrySet()) {
            recordsText.append(entry.getValue().toString()).append("\n");
        }

        recordsTextView.setText(recordsText.toString());
    }
}