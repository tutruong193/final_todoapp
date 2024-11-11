package com.example.final_project_todoapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TaskDB";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "tasks";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TASK_NAME = "name";
    private static final String COLUMN_STATUS = "status";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TASK_NAME + " TEXT, " +
                COLUMN_STATUS + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_TASK_NAME + "=?", new String[]{task.getName()}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.close();
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TASK_NAME, task.getName());
            values.put(COLUMN_STATUS, 0);
            db.insert(TABLE_NAME, null, values);
            cursor.close();
        }
    }
    public void deleteTask(String taskName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_TASK_NAME + "=?", new String[]{taskName});
    }

    public void updateTaskName(String currentName, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, newName);
        db.update(TABLE_NAME, values, COLUMN_TASK_NAME + " = ?", new String[]{currentName});
    }

    public void updateTaskStatusByName(String taskName, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, status);
        db.update(TABLE_NAME, values, COLUMN_TASK_NAME + " = ?", new String[]{taskName});
    }

    public List<Task> getAllTasks(){
        ArrayList<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase(); // Khởi tạo db
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_NAME));
                int status = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STATUS));
                tasks.add(new Task(id, name, status));
            } while (cursor.moveToNext());
        }
        return tasks;
    }
}
