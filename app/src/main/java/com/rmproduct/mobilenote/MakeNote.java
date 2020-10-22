package com.rmproduct.mobilenote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.TimeZoneFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MakeNote extends AppCompatActivity {

    private EditText noteTitle, noteBody;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_note);

        noteTitle = findViewById(R.id.noteTitle);
        noteBody = findViewById(R.id.noteBody);

        databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        /*saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = noteTitle.getText().toString().trim();
                String body = noteBody.getText().toString().trim();
                String time = getTime();
                String date = getDate();

                if (!TextUtils.isEmpty(title)) {

                    if (!TextUtils.isEmpty(body)) {

                        long row = databaseHelper.insertNote(date, time, title, body);

                        if (row == -1) {
                            Toast.makeText(getApplicationContext(), "Data insertion failed!", Toast.LENGTH_LONG).show();
                        } else {
                            startActivity(new Intent(MakeNote.this, MainActivity.class));
                        }

                    } else {
                        noteBody.setError("You have to add something!!");
                        noteBody.requestFocus();
                        return;
                    }

                } else {
                    noteTitle.setError("Please enter a note title!");
                    noteTitle.requestFocus();
                    return;
                }
            }

        });*/
    }

    public String getDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String strDate = formatter.format(date);

        return strDate;
    }

    public String getTime() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
        String strTime = formatter.format(date);

        return strTime;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.saveNote) {

            String title = noteTitle.getText().toString().trim();
            String body = noteBody.getText().toString().trim();
            String time = getTime();
            String date = getDate();

            if (!TextUtils.isEmpty(title)) {

                if (!TextUtils.isEmpty(body)) {

                    long row = databaseHelper.insertNote(date, time, title, body);

                    if (row == -1) {
                        Toast.makeText(getApplicationContext(), "Data insertion failed!", Toast.LENGTH_LONG).show();
                    } else {
                        startActivity(new Intent(MakeNote.this, MainActivity.class));
                    }

                } else {
                    noteBody.setError("You have to add something!!");
                    noteBody.requestFocus();
                }

            } else {
                noteTitle.setError("Please enter a note title!");
                noteTitle.requestFocus();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
