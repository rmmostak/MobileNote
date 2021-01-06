package com.rmproduct.locknote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.rmproduct.locknote.MobileNote.Model;
import com.rmproduct.locknote.LockNote.PassModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "MobileNotes.db";

    //for general note
    private static int VERSION = 9;
    private static final String TABLE_NAME = "MyNotes";
    private static final String ID = "id";
    private static final String DATE = "date";
    private static final String TIME = "time";
    private static final String TITLE = "title";
    private static final String BODY = "body";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DATE + " VARCHAR(12), " + TIME + " VARCHAR(10), " + TITLE + " VARCHAR(100), " + BODY + " VARCHAR(1000) );";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String SELECT_NOTE = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + ID + " DESC";

    //for pass book

    private static final String PASS_TABLE = "PassBook";
    private static final String PASS_ID = "id";
    private static final String PASS_DATE = "date";
    private static final String PASS_TIME = "time";
    private static final String PASS_TITLE = "title";
    private static final String PASS_UID = "uid";
    private static final String PASS_PASSWORD = "password";
    private static final String PASS_KEY = "keys";

    private static final String PASS_TABLE_CREATE = "CREATE TABLE " + PASS_TABLE + " ( " + PASS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PASS_DATE + " VARCHAR(12), " + PASS_TIME + " VARCHAR(10), " + PASS_TITLE + " VARCHAR(500), " + PASS_UID + " VARCHAR(500), " + PASS_PASSWORD + " VARCHAR(50), " + PASS_KEY + " INTEGER);";
    private static final String PASS_TABLE_DROP = "DROP TABLE IF EXISTS " + PASS_TABLE;
    private static final String SELECT_TABLE_PASS = "SELECT * FROM " + PASS_TABLE + " ORDER BY " + PASS_ID + " DESC";

    //for security check

    private static final String SEC_TABLE="Password";
    private static final String SEC_ID="id";
    private static final String SEC_PASS="password";

    private static final String SEC_TABLE_CREATE="CREATE TABLE "+SEC_TABLE+" ( "+SEC_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+SEC_PASS+" VARCHAR(50));";
    private static final String SEC_TABLE_DROP="DROP TABLE IF EXISTS "+SEC_TABLE;
    private static final String SEC_TABLE_SELECT="SELECT * FROM "+SEC_TABLE;
    private static final String SEC_FIRST_ROW="INSERT INTO "+SEC_TABLE+" ("+SEC_PASS+" ) VALUES('Not Found!');";

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try {

            sqLiteDatabase.execSQL(CREATE_TABLE);
            sqLiteDatabase.execSQL(PASS_TABLE_CREATE);
            sqLiteDatabase.execSQL(SEC_TABLE_CREATE);
            sqLiteDatabase.execSQL(SEC_FIRST_ROW);

        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        try {

            sqLiteDatabase.execSQL(DROP_TABLE);
            sqLiteDatabase.execSQL(PASS_TABLE_DROP);
            sqLiteDatabase.execSQL(SEC_TABLE_DROP);
            onCreate(sqLiteDatabase);

        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e, Toast.LENGTH_LONG).show();
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
        List<Model> models = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(SELECT_NOTE, null);

        if (cursor.moveToFirst()) {
            do {
                Model model = new Model();

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

    public String noteTitle(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = " + id, null);

        String result = "";
        if (cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndex(TITLE));
        }

        return result;
    }

    public String noteBody(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = " + id, null);

        String result = "";
        if (cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndex(BODY));
        }

        return result;
    }

    public int updateNote(String id, String date, String time, String title, String body) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ID, id);
        values.put(DATE, date);
        values.put(TIME, time);
        values.put(TITLE, title);
        values.put(BODY, body);

        return sqLiteDatabase.update(TABLE_NAME, values, ID + " = ?", new String[]{id});
    }

    public int deleteNote(String id) {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(TABLE_NAME, ID + " = ?", new String[]{id});
    }

    public int deletePass(String id) {
        SQLiteDatabase database=this.getWritableDatabase();

        return database.delete(PASS_TABLE, PASS_ID+" = ?", new String[]{id});
    }


    //passNote portion
    public long insertPassBook(String date, String time, String title, String uid, String password, int keys) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PASS_DATE, date);
        values.put(PASS_TIME, time);
        values.put(PASS_TITLE, title);
        values.put(PASS_UID, uid);
        values.put(PASS_PASSWORD, password);
        values.put(PASS_KEY, keys);

        return database.insert(PASS_TABLE, null, values);
    }

    public List<PassModel> passList() {
        List<PassModel> models=new ArrayList<>();
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=database.rawQuery(SELECT_TABLE_PASS, null);

        if (cursor.moveToFirst()) {
            do {
                PassModel model=new PassModel();

                model.setId(cursor.getInt(cursor.getColumnIndex(PASS_ID)));
                model.setDate(cursor.getString(cursor.getColumnIndex(PASS_DATE)));
                model.setTime(cursor.getString(cursor.getColumnIndex(PASS_TIME)));
                model.setTitle(cursor.getString(cursor.getColumnIndex(PASS_TITLE)));
                model.setUserId(cursor.getString(cursor.getColumnIndex(PASS_UID)));
                model.setPass(cursor.getString(cursor.getColumnIndex(PASS_PASSWORD)));
                model.setKeys(cursor.getInt(cursor.getColumnIndex(PASS_KEY)));

                models.add(model);

            } while (cursor.moveToNext());
        }

        database.close();

        return models;
    }


    public String getPassTitle(int id) {
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=database.rawQuery("SELECT "+PASS_TITLE+" FROM "+PASS_TABLE+" WHERE "+PASS_ID+" = "+id, null);

        String result="";
        if (cursor.moveToFirst()) {
            result=cursor.getString(cursor.getColumnIndex(PASS_TITLE));
        }

        return result;
    }

    public String getPassUser(int id) {

        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=database.rawQuery("SELECT "+PASS_UID+" FROM "+PASS_TABLE+" WHERE "+PASS_ID+" = "+id, null);

        String result="";
        if (cursor.moveToFirst()) {
            result=cursor.getString(cursor.getColumnIndex(PASS_UID));
        }

        return result;

    }

    public String getPassPassword(int id) {

        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=database.rawQuery("SELECT "+PASS_PASSWORD+" FROM "+PASS_TABLE+" WHERE "+PASS_ID+" = "+id, null);

        String result="";
        if (cursor.moveToFirst()) {
            result=cursor.getString(cursor.getColumnIndex(PASS_PASSWORD));
        }

        return result;
    }

    public int updatePass(String id, String date, String time, String title, String user, String pass) {
        SQLiteDatabase database=this.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(PASS_ID, id);
        values.put(PASS_DATE, date);
        values.put(PASS_TIME, time);
        values.put(PASS_TITLE, title);
        values.put(PASS_UID, user);
        values.put(PASS_PASSWORD, pass);

        return database.update(PASS_TABLE, values, PASS_ID+" = ?", new String[]{id});
    }

    //for security check portion

    public long updatePassword(String id, String pass) {
        SQLiteDatabase database=this.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(SEC_PASS, pass);

        return database.update(SEC_TABLE, values, SEC_ID+" = ?", new String[]{id});
    }

    public String getPassword() {
        SQLiteDatabase database=this.getWritableDatabase();

        Cursor cursor=database.rawQuery(SEC_TABLE_SELECT, null);
        String result="";
        if (cursor.moveToFirst()) {
            result=cursor.getString(cursor.getColumnIndex(SEC_PASS));
        }

        return result;
    }

}
