package com.rmproduct.locknote.LockNote;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.rmproduct.locknote.DatabaseHelper;
import com.rmproduct.locknote.MobileNote.MakeNote;
import com.rmproduct.locknote.R;

public class PassView extends AppCompatActivity {

    private EditText title, user, pass;
    private DatabaseHelper helper;
    int id = 0;
    String pass_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_view);

        title = findViewById(R.id.passTitle);
        user = findViewById(R.id.passUID);
        pass = findViewById(R.id.passPass);

        helper = new DatabaseHelper(this);
        SQLiteDatabase database = helper.getWritableDatabase();

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        pass_id=String.valueOf(id);

        title.setText(helper.getPassTitle(id));
        user.setText(helper.getPassUser(id));
        pass.setText(helper.getPassPassword(id));
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

            user.setFocusableInTouchMode(true);
            user.requestFocus();

            pass.setFocusableInTouchMode(true);
            pass.requestFocus();

            return false;
        }

        if (id == R.id.save) {

            MakeNote noteActivity = new MakeNote();
            String date = noteActivity.getDate();
            String time = noteActivity.getTime();
            String stTitle = title.getText().toString().trim();
            String stUser = user.getText().toString();
            String stPass=pass.getText().toString();

            if (!stTitle.equals("")) {

                if (!stPass.equals("")) {

                    int check = helper.updatePass(pass_id, date, time, stTitle, stUser, stPass);
                    if (check > -1) {
                        startActivity(new Intent(PassView.this, PasswordActivity.class));
                    }

                } else {
                    pass.setError("You have to add something!!");
                    pass.requestFocus();
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
                    int check = helper.deletePass(pass_id);

                    if (check > 0) {
                        startActivity(new Intent(PassView.this, PasswordActivity.class));
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
    public void onBackPressed() {
        super.onBackPressed();
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
