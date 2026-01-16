package com.example.trackerst;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class stepsTable extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "steps", COLUMN_1_ID = "ID", COLUMN_2_STEPS = "STEPS", COLUMN_3_DATE = "DATE";

    public stepsTable(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COLUMN_1_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_2_STEPS + " INTEGER, " + COLUMN_3_DATE + " DATE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertData(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void deleteData(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_1_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public ArrayList<Steps> getData() {
        ArrayList<Steps> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            int steps = cursor.getInt(1);
            String date = cursor.getString(2);

            list.add(new Steps(id, steps, date));
        }

        cursor.close();
        db.close();
        return list;
    }
}
