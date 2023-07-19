package com.nqmgaming.lab2_minhnqph31902_todoapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.nqmgaming.lab2_minhnqph31902_todoapp.DAO.TodoDAO;
import com.nqmgaming.lab2_minhnqph31902_todoapp.DTO.TodoDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.github.cutelibs.cutedialog.CuteDialog;

public class AddTodoActivity extends AppCompatActivity {
    EditText edtTitle, edtDescription, edtDate, edtType;
    TextInputLayout tilDate;
    Button btnAdd, btnCancel;
    ImageView btnBack;
    TodoDAO todoDAO;
    ConstraintLayout rootView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        edtTitle = findViewById(R.id.etTitle);
        edtDescription = findViewById(R.id.etDescription);
        edtDate = findViewById(R.id.etDate);
        edtType = findViewById(R.id.etType);

        tilDate = findViewById(R.id.tilDate);

        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);

        btnBack = findViewById(R.id.ivBack);

        rootView = findViewById(R.id.rootView);
        rootView.setOnTouchListener((v, event) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            return true;
        });


        edtType.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Difficulty");
            builder.setIcon(R.drawable.categorization);
            String[] difficultyOptions = {"Easy", "Normal", "Hard"};
            builder.setItems(difficultyOptions, (dialog, which) -> {
                String selectedDifficulty = difficultyOptions[which];
                edtType.setText(selectedDifficulty);
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });



        edtDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddTodoActivity.this, (view, year1, month1, dayOfMonth) -> {
                String selectedDate = year1 + "/" + (month1 + 1) + "/" + dayOfMonth;
                edtDate.setText(selectedDate);
            }, year, month, day);
            datePickerDialog.show();
        });

        btnBack.setOnClickListener(v -> {
            Intent intentToMain = new Intent(AddTodoActivity.this, MainActivity.class);
            startActivity(intentToMain);
            finish();
        });

        btnAdd.setOnClickListener(v -> {
            try {
                String title = edtTitle.getText().toString();
                String description = edtDescription.getText().toString();
                String date = edtDate.getText().toString();
                String type = edtType.getText().toString();
                int status = 0;

                if (title.isEmpty()) {
                    edtTitle.setError("Title is required");
                    return;
                }
                if (description.isEmpty()) {
                    edtDescription.setError("Description is required");
                    return;
                }
                if (date.isEmpty()) {
                    edtDate.setError("Date is required");
                    return;
                }
                if (title.isBlank()) {
                    edtTitle.setError("Title is required");
                    return;
                }
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    dateFormat.setLenient(false);
                    dateFormat.parse(date);
                } catch (ParseException e) {
                    edtDate.setError("Invalid date format");
                    return;
                }
                if (type.isEmpty()) {
                    edtType.setError("Type is required");
                    return;
                }

                if (title.trim().isEmpty()) {
                    edtTitle.setError("Title is required");
                    return;
                }
                if (description.trim().isEmpty()) {
                    edtDescription.setError("Description is required");
                    return;
                }

                TodoDTO todoDTO = new TodoDTO(title, description, date, type, status);
                todoDAO = new TodoDAO(AddTodoActivity.this);
                long result = todoDAO.insert(todoDTO);
                if (result > 0) {
                    new CuteDialog.withAnimation(this)
                            .setAnimation(R.raw.successfull)
                            .setTitle("Add successfully")
                            .setDescription("Let's try your best to complete it!")
                            .setTitleTextColor(R.color.black)
                            .hideNegativeButton(true)
                            .setPositiveButtonText("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intentToMain = new Intent(AddTodoActivity.this, MainActivity.class);
                                    intentToMain.putExtra("isAdd", true);
                                    startActivity(intentToMain);
                                    finish();
                                }
                            })
                            .show();


                } else {
                    Toast.makeText(AddTodoActivity.this, "Add failed", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // Xử lý ngoại lệ ở đây
                e.printStackTrace();
            }
        });

        btnCancel.setOnClickListener(v -> {
            Intent intent = new Intent(AddTodoActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });


    }
    //onPause

}