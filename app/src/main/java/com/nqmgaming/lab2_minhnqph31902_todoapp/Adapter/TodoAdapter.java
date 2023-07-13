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
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nqmgaming.lab2_minhnqph31902_todoapp.DAO.TodoDAO;
import com.nqmgaming.lab2_minhnqph31902_todoapp.DTO.TodoDTO;
import com.nqmgaming.lab2_minhnqph31902_todoapp.EditTodoActivity;
import com.nqmgaming.lab2_minhnqph31902_todoapp.R;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
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

        switch (todoDTO.getType()) {
            case "Easy":
                holder.constraintLayoutItem.setBackgroundResource(R.drawable.easy);
                break;
            case "Normal":
                holder.constraintLayoutItem.setBackgroundResource(R.drawable.normal);
                break;
            case "Hard":
                holder.constraintLayoutItem.setBackgroundResource(R.drawable.hard);
                break;
            default:
                // Xử lý trường hợp không có sự khớp nào
                break;
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
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
            builder.setTitle("Delete Todo");
            builder.setMessage("Are you sure you want to delete this todo?");
            builder.setIcon(R.drawable.delete);
            builder.setPositiveButton("Delete", (dialog, which) -> {
                // Xử lý khi người dùng chọn xóa
                todoDAO = new TodoDAO(context);
                int result = todoDAO.delete(todoDTO);
                if (result > 0) {
                    todoDTOArrayList.remove(position);
                    notifyItemChanged(position);
                    Toast.makeText(context, "Delete success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Delete fail", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditTodoActivity.class);
            intent.putExtra("id", todoDTO.getId());
            context.startActivity(intent);

        });
        holder.cardView.setOnClickListener(v -> {
            String type = todoDTO.getType();
            Toast.makeText(context,"Description: "  + type, Toast.LENGTH_SHORT).show();
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
        CardView cardView;
        ConstraintLayout constraintLayoutItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            rbTodo = itemView.findViewById(R.id.rbTodo);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            cardView = itemView.findViewById(R.id.cardView);
            constraintLayoutItem = itemView.findViewById(R.id.constraintLayoutItem);
        }
    }
}