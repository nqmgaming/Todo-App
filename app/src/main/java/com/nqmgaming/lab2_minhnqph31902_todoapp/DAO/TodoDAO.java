package com.nqmgaming.lab2_minhnqph31902_todoapp.DTO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nqmgaming.lab2_minhnqph31902_todoapp.DAO.TodoDTO;
import com.nqmgaming.lab2_minhnqph31902_todoapp.DataBase.DataBaseTodoHelper;

import java.util.ArrayList;

public class TodoDAO {

    //Create database
    DataBaseTodoHelper dataBaseTodoHelper;
    SQLiteDatabase sqLiteDatabase;

    //Constructor
    public TodoDAO(Context context) {
        dataBaseTodoHelper = new DataBaseTodoHelper(context);
        sqLiteDatabase = dataBaseTodoHelper.getWritableDatabase();
    }

    //Insert todo
    public long insert(TodoDTO todoDTO) {
        long result = -1;
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("title", todoDTO.getTitle());
            contentValues.put("description", todoDTO.getDescription());
            contentValues.put("date", todoDTO.getDate());
            contentValues.put("type", todoDTO.getType());
            contentValues.put("status", todoDTO.getStatus());
            result = sqLiteDatabase.insert("todo", null, contentValues);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //Update todo
    public int update(TodoDTO todoDTO) {
        int result = -1;
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("title", todoDTO.getTitle());
            contentValues.put("description", todoDTO.getDescription());
            contentValues.put("date", todoDTO.getDate());
            contentValues.put("type", todoDTO.getType());
            contentValues.put("status", todoDTO.getStatus());
            String[] condition = new String[]{String.valueOf(todoDTO.getId())};
            result = sqLiteDatabase.update("todo", contentValues, "id = ?", condition);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //Delete todo
    public int delete(TodoDTO todoDTO) {
        int result = -1;
        try {
            String[] condition = new String[]{String.valueOf(todoDTO.getId())};
            result = sqLiteDatabase.delete("todo", "id = ?", condition);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //Delete all todo
    public int deleteAll() {
        int result = -1;
        try {
            result = sqLiteDatabase.delete("todo", null, null);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //Get all todo
    public ArrayList<TodoDTO> getAll() {
        ArrayList<TodoDTO> todoDTOArrayList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM todo";
            Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    TodoDTO todoDTO = new TodoDTO();
                    todoDTO.setId(cursor.getInt(0));
                    todoDTO.setTitle(cursor.getString(1));
                    todoDTO.setDescription(cursor.getString(2));
                    todoDTO.setDate(cursor.getString(3));
                    todoDTO.setType(cursor.getString(4));
                    todoDTO.setStatus(cursor.getInt(5));
                    todoDTOArrayList.add(todoDTO);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return todoDTOArrayList;
    }

    //Update status
    public int updateStatus(TodoDTO todoDTO) {
        int result = -1;
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("status", todoDTO.getStatus());
            String[] condition = new String[]{String.valueOf(todoDTO.getId())};
            result = sqLiteDatabase.update("todo", contentValues, "id = ?", condition);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
