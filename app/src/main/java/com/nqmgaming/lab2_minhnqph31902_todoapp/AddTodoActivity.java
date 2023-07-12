package com.nqmgaming.lab2_minhnqph31902_todoapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.nqmgaming.lab2_minhnqph31902_todoapp.DAO.TodoDTO;
import com.nqmgaming.lab2_minhnqph31902_todoapp.DTO.TodoDAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddTodoActivity extends AppCompatActivity {
    EditText edtTitle, edtDescription, edtDate, edtType;
    TextInputLayout tilDate;
    Button btnAdd, btnCancel;
    ImageView btnBack;
    TodoDAO todoDAO;

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

        View rootView = findViewById(android.R.id.content);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Nếu người dùng chạm vào một vị trí khác ngoài EditText, ẩn bàn phím
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return false;
            }
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

        tilDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddTodoActivity.this, (view, year12, month12, dayOfMonth) -> {
                String selectedDate = year12 + "/" + (month12 + 1) + "/" + dayOfMonth;
                edtDate.setText(selectedDate);
            }, year, month, day);
            datePickerDialog.show();
        });

        btnBack.setOnClickListener(v -> finish());

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

                TodoDTO todoDTO = new TodoDTO(title, description, date, type, status);
                todoDAO = new TodoDAO(AddTodoActivity.this);
                long result = todoDAO.insert(todoDTO);
                if (result > 0) {
                    Toast.makeText(AddTodoActivity.this, "Add successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddTodoActivity.this, MainActivity.class);
                    intent.putExtra("done", 1);
                    startActivity(intent);
                    finish();
                }


            } catch (Exception e) {
                // Xử lý ngoại lệ ở đây
                e.printStackTrace();
            }
        });

        btnCancel.setOnClickListener(v -> {
            Intent intent = new Intent(AddTodoActivity.this, MainActivity.class);
            intent.putExtra("done", 0);
            startActivity(intent);
            finish();
        });


    }
    //onPause

}