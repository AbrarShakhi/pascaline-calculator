package com.github.abrarshakhi.pascalinecalculator;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText etDisplay;
    ImageButton btnHistory, btnClear, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etDisplay = findViewById(R.id.etDisplay);
        etDisplay.setShowSoftInputOnFocus(false);

        btnHistory = findViewById(R.id.btnHistory);
        btnClear = findViewById(R.id.btnClear);
        btnBack = findViewById(R.id.btnBack);

        initializeButtons();
    }

    private void initializeButtons() {
        btnHistory.setOnClickListener(v -> {
            // TODO: Implement history
        });
        btnClear.setOnClickListener(v -> etDisplay.setText(""));
        btnBack.setOnClickListener(v -> deleteCharAtCursor());
    }

    private void insertTextAtCursor(String textToInsert) {
        int start = Math.max(etDisplay.getSelectionStart(), 0);
        int end = Math.max(etDisplay.getSelectionEnd(), 0);
        etDisplay.getText().replace(Math.min(start, end), Math.max(start, end),
            textToInsert, 0, textToInsert.length());
    }

    private void deleteCharAtCursor() {
        int cursorPos = etDisplay.getSelectionStart();
        if (cursorPos <= 0) {
            return;
        }
        StringBuilder text = new StringBuilder(etDisplay.getText());
        text.deleteCharAt(cursorPos - 1);
        etDisplay.setText(text);
        etDisplay.setSelection(cursorPos - 1);
    }
}
