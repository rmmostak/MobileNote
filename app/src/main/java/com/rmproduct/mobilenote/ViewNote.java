package com.rmproduct.mobilenote;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class ViewNote extends AppCompatActivity {

    private TextView title, body;
    private String note_id;
    private DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        title = findViewById(R.id.noteTitle);
        body = findViewById(R.id.noteBody);
        helper = new DatabaseHelper(this);
        SQLiteDatabase database = helper.getWritableDatabase();

        Intent intent = getIntent();

        int noteId = intent.getIntExtra("note_id", 0);


        note_id = String.valueOf(noteId);
        title.setText(helper.noteTitle(noteId));
        body.setText(helper.noteBody(noteId));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_save_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.edit) {

            title.setFocusableInTouchMode(true);
            title.requestFocus();

            body.setFocusableInTouchMode(true);
            title.requestFocus();

            return false;
        }

        if (id == R.id.save) {

            MakeNote noteActivity = new MakeNote();
            String date = noteActivity.getDate();
            String time = noteActivity.getTime();
            String stTitle = title.getText().toString().trim();
            String stBody = body.getText().toString().trim();

            if (!stTitle.equals("")) {

                if (!stBody.equals("")) {

                    int check = helper.updateNote(note_id, date, time, stTitle, stBody);
                    if (check > -1) {
                        startActivity(new Intent(ViewNote.this, MainActivity.class));
                    }

                } else {
                    body.setError("You have to add something!!");
                    body.requestFocus();
                }

            } else {
                title.setError("Please enter a note title!");
                title.requestFocus();
            }


            return false;
        }

        if (id == R.id.delete) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure to delete this note?");
            builder.setTitle("Alert:");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    int check = helper.deleteNote(note_id);

                    if (check > 0) {
                        startActivity(new Intent(ViewNote.this, MainActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong, please try again later!", Toast.LENGTH_LONG).show();
                    }
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    return;
                }
            });

            builder.show();
        }

        return super.onOptionsItemSelected(item);
    }

}
