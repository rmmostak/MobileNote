package com.rmproduct.locknote.LockNote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.rmproduct.locknote.DatabaseHelper;
import com.rmproduct.locknote.R;

public class SecurityCheck extends AppCompatActivity {

    private int count=0;
    private DatabaseHelper helper;
    private EditText pass, confirmPass;
    private TextInputLayout inputLayout;
    private TextView warning;
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_check);

        pass=findViewById(R.id.pass1);
        confirmPass=findViewById(R.id.pass2);
        warning=findViewById(R.id.warning);
        inputLayout=findViewById(R.id.inputLayout);
        next=findViewById(R.id.next);

        helper=new DatabaseHelper(this);
        SQLiteDatabase database=helper.getWritableDatabase();

        if (helper.getPassword().equals("Not Found!")) {
            pass.setVisibility(View.VISIBLE);
            confirmPass.setVisibility(View.VISIBLE);
            inputLayout.setVisibility(View.VISIBLE);
            count=1;

        } else {
            pass.setVisibility(View.VISIBLE);
            count=2;
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count==1) {
                    String pass1=pass.getText().toString();
                    String pass2=confirmPass.getText().toString();

                    if (!TextUtils.isEmpty(pass1)) {
                        if (!TextUtils.isEmpty(pass2)) {
                            if (pass1.equals(pass2)) {

                                if (pass1.length() < 5) {
                                    warning.setText("Your password length must be at least 6 characters or numbers!");
                                } else {
                                    long row = helper.updatePassword("1", pass1);
                                    if (row > -1) {
                                    Toast.makeText(getApplicationContext(), "You have set password successfully. Please login now!", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(SecurityCheck.this, SecurityCheck.class));
                                    }

                                }

                            } else {
                                warning.setText("Please use same password on both field!");
                            }
                        } else {
                            confirmPass.setError("Confirm your password!");
                            confirmPass.requestFocus();
                            return;
                        }
                    } else {
                        pass.setError("Set a password!");
                        pass.requestFocus();
                        return;
                    }
                } else if (count==2) {
                    String prePass=helper.getPassword();
                    String pass2=pass.getText().toString();
                    if (prePass.equals(pass2)) {
                        pass.setText("");
                        startActivity(new Intent(SecurityCheck.this, PasswordActivity.class));
                    } else {
                        int count = checkTry(prePass, pass2);
                        warning.setText("Wrong password! You have left "+count+" trial(s)!");
                        if (count==0) {
                            next.setEnabled(false);
                        }
                    }
                }
            }
        });
    }

    private int checkTry(String prePass, String pass) {
        int count=3;
        if (prePass != pass) {
            count--;
        }

        return count;
    }
}
