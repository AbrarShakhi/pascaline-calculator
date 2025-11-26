package com.github.abrarshakhi.pascalinecalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.abrarshakhi.pascalinecalculator.database.HistoryEntity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText display;
    ListView history;
    Button historyToggle;
    LinearLayout calcButtons;
    CalcState state;
    List<HistoryEntity> historyEntityList;
    HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        display = findViewById(R.id.etDisplay);
        display.setShowSoftInputOnFocus(false);
        history = findViewById(R.id.lvHistory);
        historyToggle = findViewById(R.id.btnHistoryToggle);
        calcButtons = findViewById(R.id.llCalcButtons);

        historyEntityList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(this, historyEntityList);
        history.setAdapter(historyAdapter);
        history.setAdapter(historyAdapter);
        toggleState(CalcState.CALC_BUTTONS);
        initializeButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        toggleState(CalcState.CALC_BUTTONS);

    }

    private void initializeButtons() {
        historyToggle.setOnClickListener(v -> {
            if ((this.state == CalcState.HISTORY)) {
                toggleState(CalcState.CALC_BUTTONS);
            } else {
                toggleState(CalcState.HISTORY);
            }
        });
    }

    private void toggleState(CalcState state) {
        CharSequence seq = "Show calculator buttons";
        switch (state) {
            case HISTORY:
                history.setVisibility(View.VISIBLE);
                calcButtons.setVisibility(View.GONE);
                break;
            case CALC_BUTTONS:
                history.setVisibility(View.GONE);
                calcButtons.setVisibility(View.VISIBLE);
                seq = "Show history";
                break;
        }
        this.state = state;
        historyAdapter.notifyDataSetChanged();
        historyToggle.setText(seq);
    }

    private void insertTextAtCursor(String textToInsert) {
        int start = Math.max(display.getSelectionStart(), 0);
        int end = Math.max(display.getSelectionEnd(), 0);
        display.getText().replace(Math.min(start, end), Math.max(start, end),
            textToInsert, 0, textToInsert.length());
    }

    private void deleteCharAtCursor() {
        int cursorPos = display.getSelectionStart();
        if (cursorPos <= 0) {
            return;
        }
        StringBuilder text = new StringBuilder(display.getText());
        text.deleteCharAt(cursorPos - 1);
        display.setText(text);
        display.setSelection(cursorPos - 1);
    }

    enum CalcState {
        HISTORY, CALC_BUTTONS
    }
}
