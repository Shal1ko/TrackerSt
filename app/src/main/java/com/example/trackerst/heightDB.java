package com.example.trackerst;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class heightDB extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "height", COLUMN_1_HEIGHT = "height";
    public heightDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COLUMN_1_HEIGHT + " REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void replaceHeight(double height) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (" + height + ")");
    }

    public double getHeight() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        double height = -1;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            height = cursor.getDouble(0);
            cursor.close();
            db.close();
            return height;
        }
        cursor.close();
        db.close();

        return height;
    }
}
