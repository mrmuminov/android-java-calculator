package com.example.birinchidastur;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private String display = "";
    private EditText inputText;
    private TextView textView;
    private String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton deletevar = (ImageButton) findViewById(R.id.butdelet);
        deletevar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNumber();
            }
        });


        inputText = findViewById(R.id.input_box);
        textView = findViewById(R.id.result_box);
    }


    private void appendToLast(String str) {
        this.inputText.getText().append(str);
    }

    public void onClickNumber(View v) {
        Button b = (Button) v;
        display += b.getText();
        appendToLast(display);
        display = "";
    }

    public void onClickOperator(View v) {
        Button b = (Button) v;
        display += b.getText();
        if (endsWithOperator()) {
            replace(display);
            display = "";
        } else {
            appendToLast(display);
            display = "";
        }

    }

    public void onClearButton(View v) {
        inputText.getText().clear();
        textView.setText("");
    }

    public void deleteNumber() {
        int length = getInput().length();
        if (length > 0) {
            inputText.getText().delete(length - 1, length);
        }
    }

    private String getInput() {
        return this.inputText.getText().toString();
    }

    private boolean endsWithOperator() {
        return getInput().endsWith("+") || getInput().endsWith("-") || getInput().endsWith("÷") || getInput().endsWith("x");
    }

    private void replace(String str) {
        inputText.getText().replace(getInput().length() - 1, getInput().length(), str);
    }

    public void equalResult(View v) {
        String input = getInput();

        if (!endsWithOperator()) {

            if (input.contains("x")) {
                input = input.replaceAll("x", "*");
            }

            if (input.contains("÷")) {
                input = input.replaceAll("÷", "/");
            }


            int lastDivIndex = input.lastIndexOf("/0");
            if (lastDivIndex == input.length() - 1) {
                input = input.substring(0, input.length() - 1);
            }
            try {
                Expression expression = new ExpressionBuilder(input).build();
                textView.setText(String.valueOf(expression.evaluate()));
            } catch (java.lang.ArithmeticException e) {
                switch (Objects.requireNonNull(e.getMessage())) {
                    case "Division by zero!":
                        textView.setText("Nolga bo'lish xatoligi!");
                        break;
                    case "Invalid number of operands available":
                        textView.setText("Noto'g'ri operatorlar kiritildi!");
                        break;
                    default:
                        textView.setText(e.getMessage());
                        break;
                }
            } catch (java.lang.NumberFormatException e) {
                textView.setText("Noto'g'ri amallar kiritildi!");
            } catch (java.lang.IllegalArgumentException e) {
                textView.setText("Noto'g'ri ketma-ketliklar kiritildi!");
            } catch (Exception e) {
                System.out.println(e);
                textView.setText(e.getMessage());
            }
        } else textView.setText("");
    }

    public void openTelegram(View view) {
        String link = "https://t.me/bahriddin_blog";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(intent);
    }
}