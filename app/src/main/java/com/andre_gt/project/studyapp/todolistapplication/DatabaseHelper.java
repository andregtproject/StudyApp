package com.andre_gt.project.studyapp.todolistapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "ToDo_Table";
    private static final String COL1 = "ID";
    private static final String COL2 = "Name";
    private static final String COL3 = "Date";
    private static final String COL4 = "Time";

    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "(" + COL1 + " integer primary key, "                + COL2 + " TEXT, "                + COL3 + " DATE, "                + COL4 + " TIME" + ")";
        Log.d(TAG, "Creating table " + createTable);
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Memasukkan data ke database
    public boolean insertData(String item, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item);
        contentValues.put(COL3, date);
        contentValues.put(COL4, time);
        Log.d(TAG, "insertData: Inserting " + item + " to " + TABLE_NAME);
        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return result != -1;
    }

    //Menghapus data dari database
    void deleteData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL1 + "=" + id, null);
    }

    //Memuat semua data ke listview
    public ArrayList<com.andre_gt.project.studyapp.todolistapplication.ModelData> getAllData() {
        ArrayList<com.andre_gt.project.studyapp.todolistapplication.ModelData> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String date = cursor.getString(2);
            String time = cursor.getString(3);
            com.andre_gt.project.studyapp.todolistapplication.ModelData modelData = new com.andre_gt.project.studyapp.todolistapplication.ModelData(id, title, date, time);
            arrayList.add(modelData);
        }
        db.close();
        return arrayList;
    }

}
