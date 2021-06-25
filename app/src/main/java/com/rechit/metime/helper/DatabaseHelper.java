package com.rechit.metime.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rechit.metime.model.Time;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "time_db";
    private static final int DATABASE_VERSION=1;
    private static final String TABLE_TIMES="time";
    private static final String KEY_ID="id";
    private static final String KEY_TITLE="title";
    private static final String KEY_TIMETRACK="track";
    private static final String CREATE_TABLE_TIMES=
            "CREATE TABLE "+TABLE_TIMES
                    +"("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                    +KEY_TITLE+" TEXT,"
                    +KEY_TIMETRACK+" TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_TIMES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS'" + TABLE_TIMES + "'");
    }

    public long addToDatabase(String title, String track) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, title);
        values.put(KEY_TIMETRACK, track);

        long insert = db.insert(TABLE_TIMES, null, values);
        return insert;
    }

    public ArrayList<Time>  getFromDatabase() {
        ArrayList<Time> timeList = new ArrayList<>();
        String title ;
        String track ;
        int id ;
        String selectQuery = "SELECT * FROM " + TABLE_TIMES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                id = c.getInt(c.getColumnIndex(KEY_ID));
                title = c.getString(c.getColumnIndex(KEY_TITLE));
                track = c.getString(c.getColumnIndex(KEY_TIMETRACK));
                Time itemMap = new Time();
                itemMap.setId( id + "");
                itemMap.setTitleTime(title);
                itemMap.setTrackingTime(track);
                timeList.add(itemMap);
            } while (c.moveToNext());
        }
        return timeList;
    }

    public void deleteFromDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteQuery = " DELETE FROM "+TABLE_TIMES;
        db.execSQL(deleteQuery);
        db.close();
    }
}
