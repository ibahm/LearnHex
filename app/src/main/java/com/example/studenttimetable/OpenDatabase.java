package com.example.studenttimetable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;

public class OpenDatabase extends SQLiteOpenHelper {

    public static final String CREATE_EVENT_TABLE = "create table " + Database.EVENT_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Database.EVENT + " TEXT, " + Database.TIME + " TEXT, " + Database.DATE + " TEXT, " + Database.MONTH + " TEXT, "
            + Database.YEAR + " TEXT)";

    public static final String EVENTS_TABLE = "DROP TABLE IF EXISTS " + Database.EVENT_TABLE_NAME;

    public OpenDatabase(@Nullable Context context) {
        super(context, Database.DB_NAME, null, Database.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EVENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(EVENTS_TABLE);
        onCreate(db);
    }

    public void SaveEvent(String event, String time, String date, String month, String year, SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.EVENT, event);
        contentValues.put(Database.TIME, time);
        contentValues.put(Database.DATE, date);
        contentValues.put(Database.MONTH, month);
        contentValues.put(Database.YEAR, year);
        database.insert(Database.EVENT_TABLE_NAME, null, contentValues);

    }

    public Cursor ReadEvents(String date, SQLiteDatabase database) {
        String[] Projections = {Database.EVENT, Database.TIME, Database.DATE, Database.MONTH, Database.YEAR};
        String Selection = Database.DATE + "=?";
        String[] SelectionArgs = {date};
        return database.query(Database.EVENT_TABLE_NAME, Projections, Selection, SelectionArgs, null, null, null);

    }

    public Cursor ReadEventsperMonth(String month, String year, SQLiteDatabase database) {
        String[] Projections = {Database.EVENT, Database.TIME, Database.DATE, Database.MONTH, Database.YEAR};
        String Selection = Database.MONTH + "=? and " + Database.YEAR + "=?";
        String[] SelectionArgs = {month, year};
        return database.query(Database.EVENT_TABLE_NAME, Projections, Selection, SelectionArgs, null, null, null);

    }

    public void DeleteEvents(String event, String date, String time, SQLiteDatabase database) {
        String selection = Database.EVENT+"=? and "+Database.DATE+"=? and "+Database.TIME+"=?";
        String[] selectionArg = {event, date, time};
        database.delete(Database.EVENT_TABLE_NAME,selection,selectionArg);

    }
}
