package com.example.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper  extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "AutoDb";
    public static final String TABLE_AUTO = "auto";
    public static final String KEY_ID = "_id";
    public static final String KEY_STAMP = "stamp";
    public static final String KEY_MODEL = "model";
    public static final String KEY_YEAR = "year";
    public static final String KEY_MILEAGE = "mileage";

    public static final String TABLE_AUTHORIZATION = "authorization";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_PASSWORD = "password";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_AUTHORIZATION + "(" + KEY_LOGIN + " text,"
                + KEY_PASSWORD + " text" + ")");

        db.execSQL("create table " + TABLE_AUTO + "(" + KEY_ID
                + " integer primary key," + KEY_STAMP + " text," + KEY_MODEL + " text,"
                + KEY_YEAR + " text," + KEY_MILEAGE + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_AUTO);
        db.execSQL("drop table if exists " + TABLE_AUTHORIZATION);

        onCreate(db);
    }
}
