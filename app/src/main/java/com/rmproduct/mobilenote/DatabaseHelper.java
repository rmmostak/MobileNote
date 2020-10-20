package com.rmproduct.mobilenote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "MobileNotes.db";
    private static int VERSION = 5;
    private static final String TABLE_NAME = "MyNotes";
    private static final String ID = "id";
    private static final String DATE = "date";
    private static final String TIME = "time";
    private static final String TITLE = "title";
    private static final String BODY = "body";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DATE + " VARCHAR(12), " + TIME + " VARCHAR(10), " + TITLE + " VARCHAR(100), " + BODY + " VARCHAR(1000) );";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String SELECT_NOTE = "SELECT * FROM " + TABLE_NAME+" ORDER BY "+ID+" DESC";
    private static final String SELECT_TITLE="SELECT "+TITLE+" FROM "+TABLE_NAME+" WHERE id= "+ID;
    private static final String SELECT_BODY="SELECT "+BODY+" FROM "+TABLE_NAME+" WHERE id = "+ID;

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try {

            sqLiteDatabase.execSQL(CREATE_TABLE);
            Toast.makeText(context, "Database created successfully!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(context, "Exception: " + e, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        try {

            sqLiteDatabase.execSQL(DROP_TABLE);
            Toast.makeText(context, "Table upgraded successfully!", Toast.LENGTH_SHORT).show();
            onCreate(sqLiteDatabase);

        } catch (Exception e) {
            Toast.makeText(context, "Exception: " + e, Toast.LENGTH_LONG).show();
        }

    }

    public long insertNote(String date, String time, String title, String body) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DATE, date);
        values.put(TIME, time);
        values.put(TITLE, title);
        values.put(BODY, body);

        return sqLiteDatabase.insert(TABLE_NAME, null, values);
    }


    public List<Model> noteList() {
        List<Model> models=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(SELECT_NOTE, null);

        if (cursor.moveToFirst()) {
            do {
                Model model=new Model();

                model.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                model.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                model.setTime(cursor.getString(cursor.getColumnIndex(TIME)));
                model.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                model.setBody(cursor.getString(cursor.getColumnIndex(BODY)));

                models.add(model);
            } while ((cursor.moveToNext()));
        }
        sqLiteDatabase.close();

        return models;
    }

    public String noteTitle(String id) {
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor= database.rawQuery(SELECT_TITLE, null);

        return cursor.getString(cursor.getColumnIndex(TITLE));
    }

    public String noteBody(String id) {
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor= database.rawQuery(SELECT_BODY, null);

        return cursor.getString(cursor.getColumnIndex(BODY));
    }

    public int updateNote(String id, String date, String time, String title, String body) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ID, id);
        values.put(DATE, date);
        values.put(TIME, time);
        values.put(TITLE, title);
        values.put(BODY, body);

        return sqLiteDatabase.update(TABLE_NAME, values, ID+" = ?", new String[]{id});
    }

    public int deleteNote(String id) {
        SQLiteDatabase database=this.getWritableDatabase();
        return database.delete(TABLE_NAME, ID+" = ?", new String[]{id});
    }
}
