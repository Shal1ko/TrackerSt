package com.example.trackerst;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class weightDB extends SQLiteOpenHelper {
    //ESTIMATED_OR_INPUT means if the weight in the table was estimated by the system or input by the user
    public static final String TABLE_NAME = "weight", COLUMN_1_ID = "ID",COLUMN_2_CALORIES_BURNT = "CALORIES_BURNT" ,COLUMN_3_WEIGHT = "WEIGHT", COLUMN_4_DATE = "DATE", COLUMN_5_ESTIMATED_OR_INPUT = "INPUT_TYPE";
    Inflater inflater;


    public weightDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("+ COLUMN_1_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_2_CALORIES_BURNT + " REAL, " + COLUMN_3_WEIGHT + " INTEGER, " + COLUMN_4_DATE + " DATE," + COLUMN_5_ESTIMATED_OR_INPUT + " TEXT)");
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

    public ArrayList<Weight> getData() {
        ArrayList<Weight> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            double caloriesBurnt = cursor.getDouble(1);
            int weight = cursor.getInt(2);
            String date = cursor.getString(3);
            String inputType = cursor.getString(4);

            list.add(new Weight(id, caloriesBurnt, weight, date, inputType));
        }

        cursor.close();
        db.close();

        return list;
    }
}
