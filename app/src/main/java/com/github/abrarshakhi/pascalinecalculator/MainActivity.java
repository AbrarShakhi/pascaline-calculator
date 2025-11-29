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

import com.github.abrarshakhi.pascalinecalculator.data.QuickStore;
import com.github.abrarshakhi.pascalinecalculator.database.HistoryDao;
import com.github.abrarshakhi.pascalinecalculator.database.HistoryDatabase;
import com.github.abrarshakhi.pascalinecalculator.database.HistoryEntity;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final int CLEAR_DISPLAY = 0;
    private static final int CLEAR_HISTORY = 1;
    private final static int ANS = 0, A = 1, B = 2, X = 3, Y = 4;
    private final Map<Integer, Double> variables = new HashMap<>();
    private EditText display;
    private ListView history;
    private TextView historyToggle, clear;
    private LinearLayout calcButtons;
    private CalcState state;
    private List<HistoryEntity> historyEntityList;
    private HistoryAdapter historyAdapter;
    private View.OnClickListener[] clearButtonListener;
    private HistoryDao dao;
    private QuickStore qs;
    private boolean isEnableSaveBtn;

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
        isEnableSaveBtn = false;

        historyEntityList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(this, historyEntityList);
        history.setAdapter(historyAdapter);

        qs = QuickStore.firstInstance(this);
        dao = HistoryDatabase.getInstance(this).historyDao();

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

    @Override
    protected void onResume() {
        super.onResume();
        toggleState(CalcState.CALC_BUTTONS);
        loadVariables();
        display.setText("");
        insertTextAtCursor(qs.loadDisplayText());
    }

    private void loadVariables() {
        variables.put(ANS, qs.loadAns());
        variables.put(A, qs.loadA());
        variables.put(B, qs.loadB());
        variables.put(X, qs.loadX());
        variables.put(Y, qs.loadY());
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveVariables();

        // Save display text
        qs.saveDisplayText(display.getText().toString());
    }

    private void saveVariables() {
        qs.saveAns(Objects.requireNonNullElse(variables.get(ANS), 0.0));
        qs.saveA(Objects.requireNonNullElse(variables.get(A), 0.0));
        qs.saveB(Objects.requireNonNullElse(variables.get(B), 0.0));
        qs.saveX(Objects.requireNonNullElse(variables.get(X), 0.0));
        qs.saveY(Objects.requireNonNullElse(variables.get(Y), 0.0));
    }

    private void clearDisplay() {
        display.setText("");
        insertTextAtCursor("0");
    }

    private void initBasicArithmetic() {
        findViewById(R.id.btnAdd).setOnClickListener(v -> insertTextAtCursor("+"));
        findViewById(R.id.btnSub).setOnClickListener(v -> insertTextAtCursor("-"));
        findViewById(R.id.btnMul).setOnClickListener(v -> insertTextAtCursor("*"));
        findViewById(R.id.btnDiv).setOnClickListener(v -> insertTextAtCursor("/"));
        findViewById(R.id.btnAns).setOnClickListener(v -> insertTextAtCursor("ans"));
        findViewById(R.id.btnEq).setOnClickListener(v -> {
            saveVariables();
            loadVariables();
            Argument x = new Argument("x = " + variables.get(X));
            Argument y = new Argument("y = " + variables.get(Y));
            Argument a = new Argument("a = " + variables.get(A));
            Argument b = new Argument("b = " + variables.get(B));
            Argument lastAns = new Argument("ans = " + variables.get(ANS));

            String expr = display.getText().toString();
            Expression e = new Expression(expr, x, y, a, b, lastAns);
            double ans = e.calculate();
            if (Double.isNaN(ans)) {
                display.setText("!!");
            } else {
                String result = String.valueOf(ans);
                variables.put(ANS, ans);
                qs.saveDisplayText(result);
                new Thread(() -> {
                    dao.insert(new HistoryEntity(expr, result));
                    loadHistory();
                    runOnUiThread(() -> historyAdapter.notifyDataSetChanged());
                }).start();
                display.setText(result);
            }
            int len = display.getText().length();
            if (len > 0) display.setSelection(len);
            saveVariables();
            loadVariables();
        });
        findViewById(R.id.btnBack).setOnClickListener(v -> {
            String text = display.getText().toString().strip();
            if (text.equals("!!")) {
                display.setText("");
                insertTextAtCursor(qs.loadDisplayText());
            } else {
                deleteCharAtCursor();
            }
        });
        findViewById(R.id.btnOpenBracket).setOnClickListener(v -> insertTextAtCursor("("));
        findViewById(R.id.btnCloseBracket).setOnClickListener(v -> insertTextAtCursor(")"));

        findViewById(R.id.btnX).setOnClickListener(v -> {
            if (isEnableSaveBtn) {
                isEnableSaveBtn = false;
                findViewById(R.id.btnM).setBackgroundColor(getColor(R.color._2e2e2e));
                variables.put(X, variables.get(ANS));
            } else {
                insertTextAtCursor("x");
            }
        });
        findViewById(R.id.btnY).setOnClickListener(v -> {
            if (isEnableSaveBtn) {
                isEnableSaveBtn = false;
                findViewById(R.id.btnM).setBackgroundColor(getColor(R.color._2e2e2e));
                variables.put(Y, variables.get(ANS));
            } else {
                insertTextAtCursor("y");
            }
        });
        findViewById(R.id.btnA).setOnClickListener(v -> {
            if (isEnableSaveBtn) {
                isEnableSaveBtn = false;
                findViewById(R.id.btnM).setBackgroundColor(getColor(R.color._2e2e2e));
                variables.put(A, variables.get(ANS));
            } else {
                insertTextAtCursor("a");
            }
        });
        findViewById(R.id.btnB).setOnClickListener(v -> {
            if (isEnableSaveBtn) {
                isEnableSaveBtn = false;
                findViewById(R.id.btnM).setBackgroundColor(getColor(R.color._2e2e2e));
                variables.put(B, variables.get(ANS));
            } else {
                insertTextAtCursor("b");
            }
        });
        findViewById(R.id.btnM).setOnClickListener(v -> {
            if (isEnableSaveBtn) {
                findViewById(R.id.btnM).setBackgroundColor(getColor(R.color._2e2e2e));
            } else {
                findViewById(R.id.btnM).setBackgroundColor(getColor(R.color._0096ff));
            }
            isEnableSaveBtn = !isEnableSaveBtn;
        });
        findViewById(R.id.btnPi).setOnClickListener(v -> insertTextAtCursor("pi"));
        findViewById(R.id.btnPercentage).setOnClickListener(v -> insertTextAtCursor("/100"));
        findViewById(R.id.btnPow).setOnClickListener(v -> insertTextAtCursor("^"));
        findViewById(R.id.btnEx).setOnClickListener(v -> insertTextAtCursor("e^("));
        findViewById(R.id.btn10Exp).setOnClickListener(v -> insertTextAtCursor("*10^"));
        findViewById(R.id.btnFact).setOnClickListener(v -> insertTextAtCursor("!"));
        findViewById(R.id.btnAbs).setOnClickListener(v -> insertTextAtCursor("abs("));
        findViewById(R.id.btnComma).setOnClickListener(v -> insertTextAtCursor(","));
        findViewById(R.id.btnLn).setOnClickListener(v -> insertTextAtCursor("ln("));
        findViewById(R.id.btnLog).setOnClickListener(v -> insertTextAtCursor("log("));
        findViewById(R.id.btnSin).setOnClickListener(v -> insertTextAtCursor("sin("));
        findViewById(R.id.btnCos).setOnClickListener(v -> insertTextAtCursor("cos("));
        findViewById(R.id.btnTan).setOnClickListener(v -> insertTextAtCursor("tan("));
        findViewById(R.id.btnSqrt).setOnClickListener(v -> insertTextAtCursor("sqrt("));
        findViewById(R.id.btnRoot).setOnClickListener(v -> insertTextAtCursor("root("));
        findViewById(R.id.btnAsin).setOnClickListener(v -> insertTextAtCursor("asin("));
        findViewById(R.id.btnACos).setOnClickListener(v -> insertTextAtCursor("acos("));
        findViewById(R.id.btnAtan).setOnClickListener(v -> insertTextAtCursor("atan("));
        findViewById(R.id.btnSqr).setOnClickListener(v -> insertTextAtCursor("^2"));
        findViewById(R.id.btnCub).setOnClickListener(v -> insertTextAtCursor("^3"));
        findViewById(R.id.btnSinh).setOnClickListener(v -> insertTextAtCursor("sinh("));
        findViewById(R.id.btnCosh).setOnClickListener(v -> insertTextAtCursor("cosh("));
        findViewById(R.id.btnTanh).setOnClickListener(v -> insertTextAtCursor("tanh("));
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
