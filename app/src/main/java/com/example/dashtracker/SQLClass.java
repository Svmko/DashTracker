package com.example.dashtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class SQLClass extends SQLiteOpenHelper {
    private Context context;
    private static final String ENTRIES = "ENTRIES";
    private static final String ID = "ID";
    private static final String DATE = "DATE";
    private static final String DISTANCE = "DISTANCE";

    public SQLClass(@Nullable Context context) {
        super(context, "dashes.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + ENTRIES +
                " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DATE + " TEXT, " +
                DISTANCE + " TEXT );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + ENTRIES);
        onCreate(sqLiteDatabase);
    }

    void addToDB(String entryDate, String entryDistance){

        SQLiteDatabase sql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DATE, entryDate);
        cv.put(DISTANCE, entryDistance);

        sql.insert(ENTRIES, null, cv);
    }

    void removeFromDB(String orderID){

        SQLiteDatabase sql = this.getWritableDatabase();

        sql.delete(ENTRIES, "ID=?", new String[]{orderID});
    }

    Cursor pullFromDB(){

        SQLiteDatabase sql = this.getReadableDatabase();

        Cursor c = null;

        if(sql != null){
            c = sql.rawQuery("SELECT * FROM " + ENTRIES, null);
        }
        return c;
    }
}
