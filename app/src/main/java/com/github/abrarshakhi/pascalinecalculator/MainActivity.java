package com.github.abrarshakhi.pascalinecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tvDisplay;
    private String currentInput = "";
    private String operator = "";
    private double firstNumber = Double.NaN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Display
        tvDisplay = findViewById(R.id.tvDisplay);

        // Number buttons
        int[] numberButtons = {R.id.btnZero, R.id.btnOne, R.id.btnTwo, R.id.btnThree,
            R.id.btnFour, R.id.btnFive, R.id.btnSix, R.id.btnSeven, R.id.btnEight, R.id.btnNine};

        for (int i = 0; i < numberButtons.length; i++) {
            final int num = i;
            Button btn = findViewById(numberButtons[i]);
            btn.setOnClickListener(v -> appendNumber(String.valueOf(num)));
        }

        // Decimal
        findViewById(R.id.btnDecimal).setOnClickListener(v -> appendDecimal());

        // Operators
        findViewById(R.id.btnAdd).setOnClickListener(v -> setOperator("+"));
        findViewById(R.id.btnSub).setOnClickListener(v -> setOperator("-"));
        findViewById(R.id.btnMul).setOnClickListener(v -> setOperator("*"));
        findViewById(R.id.btnDiv).setOnClickListener(v -> setOperator("/"));

        // Equals
        findViewById(R.id.btnEqual).setOnClickListener(v -> calculateResult());

        // Delete
        findViewById(R.id.btnDel).setOnClickListener(v -> deleteLast());

        // Initialize display
        tvDisplay.setText("0");
    }

    private void appendNumber(String num) {
        if (currentInput.equals("0")) currentInput = "";
        currentInput += num;
        tvDisplay.setText(currentInput);
    }

    private void appendDecimal() {
        if (!currentInput.contains(".")) {
            if (currentInput.isEmpty()) currentInput = "0";
            currentInput += ".";
            tvDisplay.setText(currentInput);
        }
    }

    private void setOperator(String op) {
        if (!Double.isNaN(firstNumber)) {
            calculateResult();
        } else {
            firstNumber = currentInput.isEmpty() ? 0 : Double.parseDouble(currentInput);
        }
        operator = op;
        currentInput = "";
    }

    private void calculateResult() {
        if (operator.isEmpty() || currentInput.isEmpty()) return;

        double secondNumber = Double.parseDouble(currentInput);
        double result = 0;

        switch (operator) {
            case "+": result = firstNumber + secondNumber; break;
            case "-": result = firstNumber - secondNumber; break;
            case "*": result = firstNumber * secondNumber; break;
            case "/":
                if (secondNumber != 0) result = firstNumber / secondNumber;
                else {
                    tvDisplay.setText("Error");
                    currentInput = "";
                    operator = "";
                    firstNumber = Double.NaN;
                    return;
                }
                break;
        }

        tvDisplay.setText(String.valueOf(result));
        firstNumber = result;
        currentInput = "";
        operator = "";
    }

    private void deleteLast() {
        if (!currentInput.isEmpty()) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            tvDisplay.setText(currentInput.isEmpty() ? "0" : currentInput);
        }
    }
}
