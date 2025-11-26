package com.github.abrarshakhi.pascalinecalculator;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.abrarshakhi.pascalinecalculator.calc.ExpressionManager;
import com.github.abrarshakhi.pascalinecalculator.database.HistoryDao;
import com.github.abrarshakhi.pascalinecalculator.database.HistoryDatabase;
import com.github.abrarshakhi.pascalinecalculator.database.HistoryEntity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int CLEAR_DISPLAY = 0;
    private static final int CLEAR_HISTORY = 1;

    private EditText display;
    private ListView history;
    private TextView historyToggle, clear;
    private LinearLayout calcButtons;
    private CalcState state;

    private List<HistoryEntity> historyEntityList;
    private HistoryAdapter historyAdapter;
    private View.OnClickListener[] clearButtonListener;

    private HistoryDao dao;
    private ExpressionManager expressionManager;

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
        clearDisplay();
        history = findViewById(R.id.lvHistory);
        historyToggle = findViewById(R.id.btnHistoryToggle);
        calcButtons = findViewById(R.id.llCalcButtons);
        clear = findViewById(R.id.btnClear);

        initBasicArithmetic();
        initNumbers();

        historyEntityList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(this, historyEntityList);
        history.setAdapter(historyAdapter);

        dao = HistoryDatabase.getInstance(this).historyDao();
        expressionManager = new ExpressionManager();

        clearButtonListener = new View.OnClickListener[2];
        clearButtonListener[CLEAR_DISPLAY] = v -> clearDisplay();
        clearButtonListener[CLEAR_HISTORY] = v -> new Thread(() -> {
            dao.clear();
            historyEntityList.clear();
            runOnUiThread(() -> historyAdapter.notifyDataSetChanged());
        }).start();

        toggleState(CalcState.CALC_BUTTONS);

        historyToggle.setOnClickListener(v -> {
            if ((this.state == CalcState.HISTORY)) {
                toggleState(CalcState.CALC_BUTTONS);
            } else {
                toggleState(CalcState.HISTORY);
            }
        });
    }

    private void clearDisplay() {
        display.setText("");
        insertTextAtCursor("0");
    }

    private void initBasicArithmetic() {
        findViewById(R.id.btnAdd).setOnClickListener(v -> insertTextAtCursor("+"));
        findViewById(R.id.btnSub).setOnClickListener(v -> insertTextAtCursor("-"));
        findViewById(R.id.btnMul).setOnClickListener(v -> insertTextAtCursor("×"));
        findViewById(R.id.btnDiv).setOnClickListener(v -> insertTextAtCursor("÷"));
        findViewById(R.id.btnAns).setOnClickListener(v -> {
            String ans = "0";
            if (!historyEntityList.isEmpty()) {
                ans = historyEntityList.get(0).getAns();
            }
            insertTextAtCursor(ans);
        });
        findViewById(R.id.btnEq).setOnClickListener(v -> {
            CharSequence ans = "!!";
            String expr = display.getText().toString();
            try {
                expressionManager.clear();
                expressionManager.parseInfix(expr);
                ans = String.valueOf(expressionManager.calculate());
            } catch (Exception ignored) {
            }
            String finalAns = ans.toString();
            new Thread(() -> {
                dao.insert(new HistoryEntity(expr, finalAns));
                loadHistory();
                runOnUiThread(() -> historyAdapter.notifyDataSetChanged());
            }).start();
            display.setText(ans);

        });
        findViewById(R.id.btnBack).setOnClickListener(v -> deleteCharAtCursor());
    }

    private void initNumbers() {
        findViewById(R.id.btnDot).setOnClickListener(v -> insertTextAtCursor("."));
        findViewById(R.id.btn0).setOnClickListener(v -> insertTextAtCursor("0"));
        findViewById(R.id.btn1).setOnClickListener(v -> insertTextAtCursor("1"));
        findViewById(R.id.btn2).setOnClickListener(v -> insertTextAtCursor("2"));
        findViewById(R.id.btn3).setOnClickListener(v -> insertTextAtCursor("3"));
        findViewById(R.id.btn4).setOnClickListener(v -> insertTextAtCursor("4"));
        findViewById(R.id.btn5).setOnClickListener(v -> insertTextAtCursor("5"));
        findViewById(R.id.btn6).setOnClickListener(v -> insertTextAtCursor("6"));
        findViewById(R.id.btn7).setOnClickListener(v -> insertTextAtCursor("7"));
        findViewById(R.id.btn8).setOnClickListener(v -> insertTextAtCursor("8"));
        findViewById(R.id.btn9).setOnClickListener(v -> insertTextAtCursor("9"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        toggleState(CalcState.CALC_BUTTONS);
    }

    private void toggleState(@NonNull CalcState state) {
        CharSequence toggleBtnSeq = "Show calculator buttons";
        CharSequence clearBtnSeq = "Clear";
        int l = CLEAR_HISTORY;
        switch (state) {
            case HISTORY:
                history.setVisibility(View.VISIBLE);
                calcButtons.setVisibility(View.GONE);
                clearBtnSeq = "Delete all history";
                loadHistory();
                break;
            case CALC_BUTTONS:
                history.setVisibility(View.GONE);
                calcButtons.setVisibility(View.VISIBLE);
                toggleBtnSeq = "Show history";
                l = CLEAR_DISPLAY;
                break;
        }
        this.state = state;
        historyToggle.setText(toggleBtnSeq);
        clear.setText(clearBtnSeq);
        clear.setOnClickListener(clearButtonListener[l]);
    }

    private void loadHistory() {
        new Thread(() -> {
            historyEntityList.clear();
            historyEntityList.addAll(dao.getAll());
            runOnUiThread(() -> historyAdapter.notifyDataSetChanged());
        }).start();
    }

    private void insertTextAtCursor(@NonNull String textToInsert) {
        int start = Math.max(display.getSelectionStart(), 0);
        int end = Math.max(display.getSelectionEnd(), 0);

        Editable editableText = display.getText();
        if (editableText.toString().equals("0")) {
            if (!textToInsert.equals(".")) {
                display.setText(textToInsert);
                start = Math.max(display.getSelectionStart(), 0);
                display.setSelection(start + textToInsert.length());
                return;
            }
        }

        if (start != end) {
            editableText.replace(start, end, "");
        }
        editableText.insert(start, textToInsert);
        display.setSelection(start + textToInsert.length());
    }


    private void deleteCharAtCursor() {
        int cursorPos = display.getSelectionStart();
        if (cursorPos <= 0) {
            return;
        }
        StringBuilder text = new StringBuilder(display.getText());
        text.deleteCharAt(cursorPos - 1);
        if (text.toString().isEmpty()) {
            clearDisplay();
        } else {
            display.setText(text);
            display.setSelection(cursorPos - 1);
        }
    }

    enum CalcState {
        HISTORY, CALC_BUTTONS
    }
}
