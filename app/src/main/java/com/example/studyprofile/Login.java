package com.example.studyprofile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText uEmail, uPassword; // create variables
    Button uLoginButton;
    TextView uCreateInstead;
    ProgressBar uProgressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        uEmail = findViewById(R.id.email);  // assign variables
        uPassword = findViewById(R.id.password);
        uLoginButton = findViewById(R.id.loginButton);
        uCreateInstead = findViewById(R.id.createInstead);
        fAuth = FirebaseAuth.getInstance();
        uProgressBar = findViewById(R.id.progressBar);

        uLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e = uEmail.getText().toString().trim();
                String p = uPassword.getText().toString().trim();

                if (TextUtils.isEmpty(e)) { // check if email field is empty // O(N)
                    uEmail.setError("Enter Email");
                    return;
                }

                if (TextUtils.isEmpty(p)) { // check if password field is empty // O(N)
                    uPassword.setError("Enter Password");
                    return;
                }

                if (p.length() < 8) {   // check if password is greater than 7 characters // O(N)
                    uEmail.setError("Password must be more than 7 characters");
                    return;
                }

                uProgressBar.setVisibility(View.VISIBLE);   // show progress bar

                fAuth.signInWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() { // authenticate user
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) { // O(1)
                            Toast.makeText(Login.this,"Signed In", Toast.LENGTH_SHORT).show();  // show user message
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else {
                            Toast.makeText(Login.this,"Error Occurred " + task.getException().getMessage() , Toast.LENGTH_SHORT).show();    // show user message
                            uProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }

        });

        uCreateInstead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class)); // redirect to sign up instead
            }
        });

    }
}
