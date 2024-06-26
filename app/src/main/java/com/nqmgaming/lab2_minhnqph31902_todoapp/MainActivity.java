package com.nqmgaming.lab2_minhnqph31902_todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nqmgaming.lab2_minhnqph31902_todoapp.adapter.TodoAdapter;
import com.nqmgaming.lab2_minhnqph31902_todoapp.dao.TodoDAO;
import com.nqmgaming.lab2_minhnqph31902_todoapp.dto.TodoDTO;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerViewTodo;
    TodoAdapter todoAdapter;
    ArrayList<TodoDTO> todoArrayList;
    static TodoDAO todoDAO;
    FloatingActionButton fabAdd;
    FirebaseFirestore database;

    private long backPressedTime; // Variable to track the last back button press time
    private static final long DOUBLE_BACK_PRESS_TIMEOUT = 2000; // Timeout duration (in milliseconds)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseFirestore.getInstance();
        ListenFirebaseStore();
        recyclerViewTodo = findViewById(R.id.rvTodoList);
        fabAdd = findViewById(R.id.fab);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewTodo.setLayoutManager(linearLayoutManager);

        todoDAO = new TodoDAO(this);
        todoArrayList = todoDAO.getAll();
        todoAdapter = new TodoAdapter(this, todoArrayList);
        recyclerViewTodo.setAdapter(todoAdapter);

        fabAdd.setOnClickListener(v -> {
            Intent intent1 = new Intent(MainActivity.this, AddTodoActivity.class);
            startActivity(intent1);
        });

    }

    @Override
    public void onBackPressed() {
        // Check if enough time has passed since the last back button press
        if (backPressedTime + DOUBLE_BACK_PRESS_TIMEOUT > System.currentTimeMillis()) {
            // If within the timeout duration, exit the app
            getOnBackPressedDispatcher().onBackPressed();
        } else {
            // If not within the timeout duration, show a toast message
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis(); // Update the last back button press time
    }

    private void ListenFirebaseStore() {
        database.collection("todos").addSnapshotListener((value, error) -> {
            if (error != null) {
                return;
            }
            if (value != null) {
                for (DocumentChange dc : value.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            dc.getDocument().toObject(TodoDTO.class);
                            todoArrayList.add(dc.getDocument().toObject(TodoDTO.class));
                            todoAdapter.notifyItemInserted(todoArrayList.size() - 1);
                            break;
                        case MODIFIED:
                            TodoDTO todoDTO = dc.getDocument().toObject(TodoDTO.class);
                            if (dc.getOldIndex() == dc.getNewIndex()) {
                                todoArrayList.set(dc.getOldIndex(), todoDTO);
                                todoAdapter.notifyItemChanged(dc.getOldIndex());
                            } else {
                                todoArrayList.remove(dc.getOldIndex());
                                todoArrayList.add(dc.getNewIndex(), todoDTO);
                                todoAdapter.notifyItemMoved(dc.getOldIndex(), dc.getNewIndex());
                            }
                            break;
                        case REMOVED:
                            dc.getDocument().toObject(TodoDTO.class);
                            todoArrayList.remove(dc.getOldIndex());
                            todoAdapter.notifyItemRemoved(dc.getOldIndex());
                            break;
                    }
                }
            }
        });
    }

}
