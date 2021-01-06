package com.rmproduct.locknote.MakeOnline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rmproduct.locknote.MobileNote.HomePage;
import com.rmproduct.locknote.R;

import java.util.regex.Pattern;

public class Authentication extends AppCompatActivity {

    private EditText email, pass, confirmPass;
    private TextView warning, existingIdMsg, signUp, signInNotice, text;
    private Button signIn;
    private TextInputLayout pass2Layout, inputLayout, passLayout;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        firebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        confirmPass = findViewById(R.id.pass2);
        warning = findViewById(R.id.warning);
        existingIdMsg = findViewById(R.id.existingIdMsg);
        signInNotice = findViewById(R.id.signInNotice);
        inputLayout = findViewById(R.id.emailLayout);
        passLayout = findViewById(R.id.inputLayout);
        text = findViewById(R.id.text);
        signUp = findViewById(R.id.signUp);
        signIn = findViewById(R.id.signIn);
        pass2Layout = findViewById(R.id.pass2Layout);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            signInNotice.setVisibility(View.VISIBLE);
            text.setVisibility(View.GONE);

            inputLayout.setVisibility(View.GONE);
            email.setVisibility(View.GONE);

            pass2Layout.setVisibility(View.GONE);
            pass.setVisibility(View.GONE);

            passLayout.setVisibility(View.GONE);
            confirmPass.setVisibility(View.GONE);

            signIn.setVisibility(View.GONE);
            signUp.setVisibility(View.GONE);
            warning.setVisibility(View.GONE);
            existingIdMsg.setVisibility(View.GONE);
        }

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn.setText("Sign Up");
                signUp.setText("Sign In");
                pass2Layout.setVisibility(View.VISIBLE);
                confirmPass.setVisibility(View.VISIBLE);

                signIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String mail = email.getText().toString().trim();
                        String p1 = pass.getText().toString();
                        String p2 = confirmPass.getText().toString();

                        if (!TextUtils.isEmpty(mail)) {
                            if (Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                                if (!TextUtils.isEmpty(p1)) {
                                    if (!TextUtils.isEmpty(p2)) {
                                        if (p1.equals(p2)) {
                                            signUpforNewUser(mail, p2);
                                        } else {
                                            confirmPass.setError("Set the same password!!");
                                            confirmPass.requestFocus();
                                            return;
                                        }
                                    } else {
                                        confirmPass.setError("Confirm your password!!");
                                        confirmPass.requestFocus();
                                        return;
                                    }
                                } else {
                                    pass.setError("Set a valid password!!");
                                    pass.requestFocus();
                                    return;
                                }
                            } else {
                                email.setError("Enter a valid email address!!");
                                email.requestFocus();
                                return;
                            }
                        } else {
                            email.setError("Enter an email address!!");
                            email.requestFocus();
                            return;
                        }
                    }
                });
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = email.getText().toString().trim();
                String p1 = pass.getText().toString();

                if (!TextUtils.isEmpty(mail)) {
                    if (Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                        if (!TextUtils.isEmpty(p1)) {
                            getDataForExistUser(mail, p1);
                        } else {
                            pass.setError("Enter your password!!");
                            pass.requestFocus();
                            return;
                        }
                    } else {
                        email.setError("Enter a valid email address!!");
                        email.requestFocus();
                        return;
                    }
                } else {
                    email.setError("Enter an email address!!");
                    email.requestFocus();
                    return;
                }
            }
        });
    }

    private void getDataForExistUser(String mail, String p1) {
        firebaseAuth.signInWithEmailAndPassword(mail, p1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    signInNotice.setVisibility(View.VISIBLE);
                    text.setVisibility(View.GONE);

                    inputLayout.setVisibility(View.GONE);
                    email.setVisibility(View.GONE);

                    pass2Layout.setVisibility(View.GONE);
                    pass.setVisibility(View.GONE);

                    passLayout.setVisibility(View.GONE);
                    confirmPass.setVisibility(View.GONE);

                    signIn.setVisibility(View.GONE);
                    signUp.setVisibility(View.GONE);
                    warning.setVisibility(View.GONE);
                    existingIdMsg.setVisibility(View.GONE);
                } else {
                    warning.setText("Error: " + task.getException().getMessage());
                }
            }
        });
    }

    private void signUpforNewUser(String mail, String p2) {
        firebaseAuth.createUserWithEmailAndPassword(mail, p2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    new AlertDialog.Builder(Authentication.this)
                            .setIcon(R.drawable.ic_note_logo)
                            .setTitle("Alert!")
                            .setMessage("You have successfully created your online account. Keep using this app and connect to internet frequently to save your data on server. Thank you!")
                            .setPositiveButton("Back to Home", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    confirmPass.setVisibility(View.GONE);
                                    signIn.setText("Sign In");
                                    signUp.setVisibility(View.GONE);
                                    existingIdMsg.setVisibility(View.GONE);
                                    dialog.dismiss();
                                }
                            }).show();
                } else {
                    warning.setText("Error: " + task.getException().getMessage());
                }

            }
        });
    }
}
