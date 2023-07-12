package com.nqmgaming.lab2_minhnqph31902_todoapp.DTO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nqmgaming.lab2_minhnqph31902_todoapp.DataBase.DataBaseTodoHelper;

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


}
