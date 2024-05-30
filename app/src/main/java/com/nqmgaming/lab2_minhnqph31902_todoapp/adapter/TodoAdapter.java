package com.nqmgaming.lab2_minhnqph31902_todoapp.adapter;

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
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nqmgaming.lab2_minhnqph31902_todoapp.dao.TodoDAO;
import com.nqmgaming.lab2_minhnqph31902_todoapp.dto.TodoDTO;
import com.nqmgaming.lab2_minhnqph31902_todoapp.EditTodoActivity;
import com.nqmgaming.lab2_minhnqph31902_todoapp.R;

import java.util.ArrayList;

import io.github.cutelibs.cutedialog.CuteDialog;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    TodoDAO todoDAO;
    private final Context context;
    private final ArrayList<TodoDTO> todoDTOArrayList;
    FirebaseFirestore database;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, int positions) {
        database = FirebaseFirestore.getInstance();
        int position = positions;

        TodoDTO todoDTO = todoDTOArrayList.get(position);
        holder.tvTitle.setText(todoDTO.getTitle());
        holder.tvDate.setText(todoDTO.getDate());
        holder.rbTodo.setChecked(todoDTO.getStatus() == 1);
        if (todoDTO.getStatus() == 1) {
            holder.tvTitle.setTextColor(context.getResources().getColor(R.color.colorGray));

            switch (todoDTO.getType()) {
                case "Easy":
                    holder.constraintLayoutItem.setBackgroundResource(R.drawable.easydone);
                    break;
                case "Normal":
                    holder.constraintLayoutItem.setBackgroundResource(R.drawable.normaldone);
                    break;
                case "Hard":
                    holder.constraintLayoutItem.setBackgroundResource(R.drawable.harddone);
                    break;
                default:
                    break;
            }

            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tvTitle.setTextColor(context.getResources().getColor(R.color.black));
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
                    break;
            }
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }


        holder.rbTodo.setOnClickListener(v -> {
            if (!holder.rbTodo.isChecked()) {
                //set status firebase
                database.collection("todos").document(todoDTO.getId()).update("status", 0)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show();
                                holder.tvTitle.setTextColor(context.getResources().getColor(R.color.black));
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
                                        break;
                                }
                                holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Update fail", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                //set status firebase
                database.collection("todos").document(todoDTO.getId()).update("status", 1)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                holder.tvTitle.setTextColor(context.getResources().getColor(R.color.colorGray));
                                switch (todoDTO.getType()) {
                                    case "Easy":
                                        holder.constraintLayoutItem.setBackgroundResource(R.drawable.easydone);
                                        break;
                                    case "Normal":
                                        holder.constraintLayoutItem.setBackgroundResource(R.drawable.normaldone);
                                        break;
                                    case "Hard":
                                        holder.constraintLayoutItem.setBackgroundResource(R.drawable.harddone);
                                        break;
                                    default:
                                        break;
                                }

                                Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(context, "Update fail", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            holder.rbTodo.setChecked(todoDTO.getStatus() == 1);
        });

        holder.btnDelete.setOnClickListener(v -> {
            new CuteDialog.withAnimation(context)
                    .setAnimation(R.raw.delete)
                    .setTitle("Delete")
                    .setDescription("Are you sure you want to delete this item?")
                    .setNegativeButtonText("No", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setPositiveButtonText("Yes", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(context, todoDTO.getId() + "", Toast.LENGTH_SHORT).show();
                            //delete from firebase
                            database.collection("todos").document(todoDTO.getId()).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "Delete success", Toast.LENGTH_SHORT).show();
                                            notifyDataSetChanged();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Delete fail", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    })
                    .show();


        });

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditTodoActivity.class);
            intent.putExtra("id", todoDTO.getId());
            context.startActivity(intent);

        });
        holder.cardView.setOnClickListener(v -> {
            String type = todoDTO.getType();
            Toast.makeText(context, "Description: " + type, Toast.LENGTH_SHORT).show();
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

        KonfettiView konfettiView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            rbTodo = itemView.findViewById(R.id.rbTodo);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            cardView = itemView.findViewById(R.id.cardView);
            constraintLayoutItem = itemView.findViewById(R.id.constraintLayoutItem);

            konfettiView = itemView.findViewById(R.id.konfettiView);

        }
    }

}