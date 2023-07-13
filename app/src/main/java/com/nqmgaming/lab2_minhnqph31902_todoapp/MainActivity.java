package com.nqmgaming.lab2_minhnqph31902_todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nqmgaming.lab2_minhnqph31902_todoapp.Adapter.TodoAdapter;
import com.nqmgaming.lab2_minhnqph31902_todoapp.DTO.TodoDTO;
import com.nqmgaming.lab2_minhnqph31902_todoapp.DAO.TodoDAO;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerViewTodo;
    TodoAdapter todoAdapter;
    ArrayList<TodoDTO> todoArrayList;
    static TodoDAO todoDAO;
    FloatingActionButton fabAdd;

    private long backPressedTime; // Variable to track the last back button press time
    private static final long DOUBLE_BACK_PRESS_TIMEOUT = 2000; // Timeout duration (in milliseconds)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewTodo = findViewById(R.id.rvTodoList);
        fabAdd = findViewById(R.id.fab);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewTodo.setLayoutManager(linearLayoutManager);

        todoDAO = new TodoDAO(this);
        todoArrayList = todoDAO.getAll();
        todoAdapter = new TodoAdapter(this, todoArrayList);
        recyclerViewTodo.setAdapter(todoAdapter);

        Intent intent = getIntent();
        boolean isAdd = intent.getBooleanExtra("isAdd", false);
        boolean isEdit = intent.getBooleanExtra("isEdit", false);
        if (isAdd || isEdit) {
            refreshList();
        }


        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTodoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void refreshList() {
        todoArrayList.clear();
        todoArrayList.addAll(todoDAO.getAll());
        todoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        // Check if enough time has passed since the last back button press
        if (backPressedTime + DOUBLE_BACK_PRESS_TIMEOUT > System.currentTimeMillis()) {
            // If within the timeout duration, exit the app
            super.onBackPressed();
        } else {
            // If not within the timeout duration, show a toast message
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis(); // Update the last back button press time
    }
}
