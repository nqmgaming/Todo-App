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

import com.google.firebase.firestore.FirebaseFirestore;
import com.nqmgaming.lab2_minhnqph31902_todoapp.DTO.TodoDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

import io.github.cutelibs.cutedialog.CuteDialog;

public class EditTodoActivity extends AppCompatActivity {
    EditText etTitleEdit, etDescriptionEdit, etDateEdit, etTypeEdit;
    Button btnCancelEdit, btnAddEdit;
    ImageView ivBackEdit;
    ConstraintLayout rootView;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);

        database = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

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
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

        AtomicReference<TodoDTO> todoDTOReference = new AtomicReference<>(new TodoDTO());

        // Get data from Firebase
        database.collection("todos").document(id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        todoDTOReference.set(documentSnapshot.toObject(TodoDTO.class));
                        etTitleEdit.setText(todoDTOReference.get().getTitle());
                        etDescriptionEdit.setText(todoDTOReference.get().getDescription());
                        etDateEdit.setText(todoDTOReference.get().getDate());
                        etTypeEdit.setText(todoDTOReference.get().getType());

                        int status = todoDTOReference.get().getStatus();
                        // Now you have the 'status' value and you can use it wherever needed.
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });

        // Update data
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
                if (title.trim().isEmpty()) {
                    etTitleEdit.setError("Title is required");
                    return;
                }
                if (description.trim().isEmpty()) {
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

                int status = todoDTOReference.get().getStatus();

                TodoDTO updatedTodoDTO = new TodoDTO(id, title, description, date, type, status);
                HashMap<String, Object> mapTodo = updatedTodoDTO.toHashMap();

                // Update to Firebase
                database.collection("todos").document(id).update(mapTodo)
                        .addOnSuccessListener(aVoid -> {
                            new CuteDialog.withAnimation(EditTodoActivity.this)
                                    .setAnimation(R.raw.successfull)
                                    .setTitle("Update successfully")
                                    .setDescription("You have updated successfully")
                                    .hidePositiveButton(true)
                                    .setNegativeButtonText("OK", v1 -> {
                                        Intent intentToMain = new Intent(EditTodoActivity.this, MainActivity.class);
                                        intentToMain.putExtra("isEdit", true);
                                        startActivity(intentToMain);
                                        finish();
                                    })
                                    .show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(EditTodoActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                        });
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
