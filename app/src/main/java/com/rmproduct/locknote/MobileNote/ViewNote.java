package com.rmproduct.locknote.MobileNote;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rmproduct.locknote.DatabaseHelper;
import com.rmproduct.locknote.LockNote.SecurityCheck;
import com.rmproduct.locknote.R;

public class ViewNote extends AppCompatActivity {

    private TextView title, body;
    private FloatingActionButton lockNote;
    private String note_id;
    private DatabaseHelper helper;
    private String netTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        helper = new DatabaseHelper(this);
        SQLiteDatabase database = helper.getWritableDatabase();

        title = findViewById(R.id.noteTitle);
        body = findViewById(R.id.noteBody);
        lockNote = findViewById(R.id.noteLock);

        Intent intent = getIntent();

        int noteId = intent.getIntExtra("note_id", 0);


        note_id = String.valueOf(noteId);
        title.setText(helper.noteTitle(noteId));
        body.setText(helper.noteBody(noteId));

        lockNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MakeNote note = new MakeNote();

                String date = note.getDate();
                String time = note.getTime();
                String stTitle = title.getText().toString().trim();
                String stPass = body.getText().toString();
                int keys = 0;

                setUser(date, time, stTitle, stPass, keys);

            }
        });
    }

    private void setUser(final String date, final String time, final String title, final String pass, final int keys) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.user_dialog, null);

        helper = new DatabaseHelper(this);
        SQLiteDatabase database = helper.getWritableDatabase();

        dialogBuilder.setView(dialogView);

        final EditText user = dialogView.findViewById(R.id.userId);
        final Button setTitle = dialogView.findViewById(R.id.setTitle);

        dialogBuilder.setTitle("Set a user ID for this note:");
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


        setTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = user.getText().toString();

                if (TextUtils.isEmpty(string)) {
                    user.setError("Set a user ID!");
                    setTitle.requestFocus();
                } else {
                    long row = helper.insertPassBook(date, time, title, string, pass, keys);

                    if (row == -1) {
                        Toast.makeText(getApplicationContext(), "Something went wrong, please try again later!!", Toast.LENGTH_LONG).show();
                    } else {
                        startActivity(new Intent(ViewNote.this, SecurityCheck.class));
                    }

                    alertDialog.dismiss();
                }

            }
        });


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
                        startActivity(new Intent(ViewNote.this, HomePage.class));
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
                        startActivity(new Intent(ViewNote.this, HomePage.class));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
