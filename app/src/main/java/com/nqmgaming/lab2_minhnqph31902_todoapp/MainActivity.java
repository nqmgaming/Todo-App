package com.nqmgaming.lab2_minhnqph31902_todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nqmgaming.lab2_minhnqph31902_todoapp.Adapter.TodoAdapter;
import com.nqmgaming.lab2_minhnqph31902_todoapp.DAO.TodoDTO;
import com.nqmgaming.lab2_minhnqph31902_todoapp.DTO.TodoDAO;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerViewTodo;
    TodoAdapter todoAdapter;
    ArrayList<TodoDTO> todoArrayList;
    TodoDAO todoDAO;
    FloatingActionButton fabAdd;

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

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTodoActivity.class);
                startActivity(intent);
            }
        });

    }


}
