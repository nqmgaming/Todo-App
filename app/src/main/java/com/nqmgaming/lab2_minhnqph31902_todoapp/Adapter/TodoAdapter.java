package com.nqmgaming.lab2_minhnqph31902_todoapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nqmgaming.lab2_minhnqph31902_todoapp.DAO.TodoDTO;
import com.nqmgaming.lab2_minhnqph31902_todoapp.DTO.TodoDAO;
import com.nqmgaming.lab2_minhnqph31902_todoapp.EditTodoActivity;
import com.nqmgaming.lab2_minhnqph31902_todoapp.R;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    TodoDTO todoDTO;
    TodoDAO todoDAO;
    private final Context context;
    private final ArrayList<TodoDTO> todoDTOArrayList;

    public TodoAdapter(Context context, ArrayList<TodoDTO> todoDTOArrayList) {
        this.context = context;
        this.todoDTOArrayList = todoDTOArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_todo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TodoDTO todoDTO = todoDTOArrayList.get(position);
        holder.tvTitle.setText(todoDTO.getTitle());
        holder.tvDate.setText(todoDTO.getDate());
        holder.rbTodo.setChecked(todoDTO.getStatus() == 1);
        if (todoDTO.getStatus() == 1) {
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        holder.rbTodo.setOnClickListener(v -> {
            if (!holder.rbTodo.isChecked()) {
                todoDTO.setStatus(0);
                todoDAO = new TodoDAO(context);
                int result = todoDAO.updateStatus(todoDTO);
                if (result > 0) {
                    Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Update fail", Toast.LENGTH_SHORT).show();
                }
                holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            } else {
                todoDTO.setStatus(1);
                todoDAO = new TodoDAO(context);
                int result = todoDAO.updateStatus(todoDTO);
                if (result > 0) {
                    Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Update fail", Toast.LENGTH_SHORT).show();
                }
                holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            holder.rbTodo.setChecked(todoDTO.getStatus() == 1);
        });

        holder.btnDelete.setOnClickListener(v -> {
            todoDAO = new TodoDAO(context);
            int result = todoDAO.delete(todoDTO);
            if (result > 0) {
                todoDTOArrayList.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Delete success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Delete fail", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditTodoActivity.class);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoDTOArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate;
        CheckBox rbTodo;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            rbTodo = itemView.findViewById(R.id.rbTodo);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}