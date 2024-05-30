package com.nqmgaming.lab2_minhnqph31902_todoapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseTodoHelper extends SQLiteOpenHelper {
    //Create name and version of database
    public static final String DATABASE_NAME = "todo.db";
    public static final int DATABASE_VERSION = 1;

    //Constructor
    public DataBaseTodoHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create table todo include id, title, description, date, status
        String sql = "CREATE TABLE todo (\n" +
                "                id INTEGER  PRIMARY KEY AUTOINCREMENT,\n" +
                "                title TEXT,\n" +
                "                description TEXT,\n" +
                "                date TEXT,\n" +
                "                type TEXT,\n" +
                "                status INTEGER\n" +
                "        );";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
