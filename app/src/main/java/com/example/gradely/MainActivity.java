package com.example.gradely;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etSubject, etCredit;
    Spinner spGrade;
    Button btnAdd, btnCalculate, btnReset;
    TextView tvList, tvResult;

    // grade list and its point value (4.00 scale)
    String[] gradeName = {"A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "F"};
    double[] gradePoint = {4.00, 3.75, 3.50, 3.00, 2.75, 2.50, 2.00, 1.75, 1.50, 1.00, 0.00};

    // running totals
    double totalPoint = 0;
    int totalCredit = 0;
    int subjectNo = 0;
    String subjectList = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSubject = findViewById(R.id.etSubject);
        etCredit = findViewById(R.id.etCredit);
        spGrade = findViewById(R.id.spGrade);
        btnAdd = findViewById(R.id.btnAdd);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnReset = findViewById(R.id.btnReset);
        tvList = findViewById(R.id.tvList);
        tvResult = findViewById(R.id.tvResult);

        // fill the grade spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, gradeName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGrade.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubject();
            }
        });

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateGpa();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAll();
            }
        });
    }

    private void addSubject() {
        String subject = etSubject.getText().toString().trim();
        String creditText = etCredit.getText().toString().trim();

        if (subject.isEmpty()) {
            etSubject.setError("Please enter subject name");
            return;
        }

        if (creditText.isEmpty()) {
            etCredit.setError("Please enter credit hour");
            return;
        }

        int credit = Integer.parseInt(creditText);
        if (credit < 1 || credit > 10) {
            etCredit.setError("Credit hour must be 1 to 10");
            return;
        }

        int position = spGrade.getSelectedItemPosition();
        String grade = gradeName[position];
        double point = gradePoint[position];

        // total point = grade point x credit hour
        totalPoint = totalPoint + (point * credit);
        totalCredit = totalCredit + credit;
        subjectNo = subjectNo + 1;

        subjectList = subjectList + subjectNo + ". " + subject
                + "  |  Grade: " + grade
                + "  |  Credit: " + credit + "\n";

        tvList.setText(subjectList);

        // clear the input so user can key in the next subject
        etSubject.setText("");
        etCredit.setText("");
        etSubject.requestFocus();

        Toast.makeText(this, subject + " added", Toast.LENGTH_SHORT).show();
    }

    private void calculateGpa() {
        if (totalCredit == 0) {
            Toast.makeText(this, "Please add at least one subject", Toast.LENGTH_SHORT).show();
            return;
        }

        double gpa = totalPoint / totalCredit;

        String status;
        if (gpa >= 3.50) {
            status = "Excellent (Dean's List)";
        } else if (gpa >= 3.00) {
            status = "Very Good";
        } else if (gpa >= 2.00) {
            status = "Pass";
        } else {
            status = "Fail";
        }

        tvResult.setText("Total Subject : " + subjectNo
                + "\nTotal Credit Hour : " + totalCredit
                + "\nTotal Grade Point : " + String.format("%.2f", totalPoint)
                + "\n\nYour GPA : " + String.format("%.2f", gpa)
                + "\nStatus : " + status);
    }

    private void resetAll() {
        totalPoint = 0;
        totalCredit = 0;
        subjectNo = 0;
        subjectList = "";

        etSubject.setText("");
        etCredit.setText("");
        spGrade.setSelection(0);
        tvList.setText("No subject added yet.");
        tvResult.setText("Your GPA will appear here.");

        Toast.makeText(this, "All data cleared", Toast.LENGTH_SHORT).show();
    }
}