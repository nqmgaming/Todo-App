package com.nqmgaming.lab2_minhnqph31902_todoapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nqmgaming.lab2_minhnqph31902_todoapp.DAO.TodoDAO;
import com.nqmgaming.lab2_minhnqph31902_todoapp.DTO.TodoDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicReference;

public class EditTodoActivity extends AppCompatActivity {
    EditText etTitleEdit, etDescriptionEdit, etDateEdit, etTypeEdit;
    Button btnCancelEdit, btnAddEdit;
    ImageView ivBackEdit;
    ConstraintLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);

        etTitleEdit = findViewById(R.id.etTitleEdit);
        etDescriptionEdit = findViewById(R.id.etDescriptionEdit);
        etDateEdit = findViewById(R.id.etDateEdit);
        etTypeEdit = findViewById(R.id.etTypeEdit);

        btnCancelEdit = findViewById(R.id.btnCancelEdit);
        btnAddEdit = findViewById(R.id.btnAddEdit);

        ivBackEdit = findViewById(R.id.ivBackEdit);
        rootView = findViewById(R.id.rootViewEdit);
        rootView.setOnTouchListener((v, event) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            return true;
        });

        etTypeEdit.setOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setTitle("Select Difficulty");
            builder.setIcon(R.drawable.categorization);
            String[] difficultyOptions = {"Easy", "Normal", "Hard"};
            builder.setItems(difficultyOptions, (dialog, which) -> {
                String selectedDifficulty = difficultyOptions[which];
                etTypeEdit.setText(selectedDifficulty);
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
        //Set data
        TodoDAO todoDAO = new TodoDAO(this);
        AtomicReference<TodoDTO> todoDTO = new AtomicReference<>(MainActivity.todoDAO.getTodoById(id));
        etTitleEdit.setText(todoDTO.get().getTitle());
        etDescriptionEdit.setText(todoDTO.get().getDescription());
        etDateEdit.setText(todoDTO.get().getDate());
        etTypeEdit.setText(todoDTO.get().getType());
        int status = todoDTO.get().getStatus();

        //Update data
        btnAddEdit.setOnClickListener(v -> {
            try {
                String title = etTitleEdit.getText().toString();
                String description = etDescriptionEdit.getText().toString();
                String date = etDateEdit.getText().toString();
                String type = etTypeEdit.getText().toString();

                if (title.isEmpty()) {
                    etTitleEdit.setError("Title is required");
                    return;
                }
                if (description.isEmpty()) {
                    etDescriptionEdit.setError("Description is required");
                    return;
                }
                if (date.isEmpty()) {
                    etDateEdit.setError("Date is required");
                    return;
                }
                if (title.trim().isEmpty()){
                    etTitleEdit.setError("Title is required");
                    return;
                }
                if (description.trim().isEmpty()){
                    etDescriptionEdit.setError("Description is required");
                    return;
                }
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    dateFormat.setLenient(false);
                    dateFormat.parse(date);
                } catch (ParseException e) {
                    etDateEdit.setError("Invalid date format");
                    return;
                }
                if (type.isEmpty()) {
                    etTypeEdit.setError("Type is required");
                    return;
                }

                todoDTO.get().setTitle(title);
                todoDTO.get().setDescription(description);
                todoDTO.get().setDate(date);
                todoDTO.get().setType(type);
                todoDTO.get().setStatus(status);


                int result = todoDAO.update(todoDTO.get());
                if (result > 0) {
                    Toast.makeText(EditTodoActivity.this, "Update successfully", Toast.LENGTH_SHORT).show();
                    Intent intentToMain = new Intent(EditTodoActivity.this, MainActivity.class);
                    intentToMain.putExtra("isEdit", true);
                    startActivity(intentToMain);
                    finish();
                } else {
                    etTitleEdit.setError("Update failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        etDateEdit.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    EditTodoActivity.this, (view, year12, month12, dayOfMonth) -> {
                String selectedDate = year12 + "/" + (month12 + 1) + "/" + dayOfMonth;
                etDateEdit.setText(selectedDate);
            }, year, month, day);
            datePickerDialog.show();
        });

        ivBackEdit.setOnClickListener(v -> {
            Intent intentToMain = new Intent(EditTodoActivity.this, MainActivity.class);
            startActivity(intentToMain);
            finish();
        });

        btnCancelEdit.setOnClickListener(v -> {
            Intent intentToMain = new Intent(EditTodoActivity.this, MainActivity.class);
            startActivity(intentToMain);
            finish();
        });
    }
}