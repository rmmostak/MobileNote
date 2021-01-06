package com.rmproduct.locknote.LockNote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.rmproduct.locknote.DatabaseHelper;
import com.rmproduct.locknote.MobileNote.MakeNote;
import com.rmproduct.locknote.R;

public class MakePassNote extends AppCompatActivity {

    private EditText passTitle, passUID, passPass;
    private DatabaseHelper databaseHelper;

    private EditText password;
    private ImageButton visible;
    private  int EYE=0;
    private StringBuffer buffer=new StringBuffer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_pass_note);

        passTitle = findViewById(R.id.passTitle);
        passUID = findViewById(R.id.passUID);
        passPass = findViewById(R.id.passPass);

        databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
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

            MakeNote activity = new MakeNote();


            String title = passTitle.getText().toString().trim();
            String user = passUID.getText().toString().trim();
            String pass = passPass.getText().toString();
            String time = activity.getTime();
            String date = activity.getDate();
            int keys = 0;

            if (user.isEmpty()) {
                user = "UID not given!";
            }

            if (!TextUtils.isEmpty(title)) {

                if (!TextUtils.isEmpty(pass)) {

                    long row = databaseHelper.insertPassBook(date, time, title, user, pass, keys);

                    if (row == -1) {
                        Toast.makeText(getApplicationContext(), "Data insertion failed!", Toast.LENGTH_LONG).show();
                    } else {
                        startActivity(new Intent(MakePassNote.this, PasswordActivity.class));
                    }

                } else {
                    passPass.setError("Please fill password field!!");
                    passPass.requestFocus();
                }

            } else {
                passTitle.setError("Please enter a note title!!");
                passTitle.requestFocus();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
