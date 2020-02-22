package com.example.studyprofile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    public static final String TAG = "TAG";  // create variables
    EditText uName,uEmail,uPassword;
    Button uSignButton;
    TextView uSignInstead;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ProgressBar uProgressBar;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        uName = findViewById(R.id.name);    // assign variables
        uEmail = findViewById(R.id.email);
        uPassword = findViewById(R.id.password2);
        uSignButton = findViewById(R.id.signButton);
        uSignInstead = findViewById(R.id.signInstead);
        fAuth = FirebaseAuth.getInstance();
        uProgressBar = findViewById(R.id.progressBar2);
        fStore = FirebaseFirestore.getInstance();

        if (fAuth.getCurrentUser() != null) {   // Send user to main activity if already logged in // O(1)
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

        uSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final String e = uEmail.getText().toString().trim();
               final String p = uPassword.getText().toString().trim();
               final String n = uName.getText().toString();

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

                fAuth.createUserWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() { // create user in FireBase
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { // check if user is created  // O(1)
                            Toast.makeText(Register.this,"Account Created", Toast.LENGTH_SHORT).show(); // display message to the user
                            userID = fAuth.getCurrentUser().getUid();   // assign userID to user's id number
                            DocumentReference documentReference = fStore.collection("users").document(userID);  // create a collection('users') of documents('userID')
                            Map<String,Object> user = new HashMap<>();  // create and store data to the users document
                            user.put("ID", userID);
                            user.put("Name", n);
                            user.put("Password", p);
                            user.put("Email", e);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) { // error\success catch
                                    Log.d(TAG, "Success! : user profile is created for "+ userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Exception : "+ e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));  // send user to MainActivity
                        }
                        else {
                            Toast.makeText(Register.this,"Error Occurred " + task.getException().getMessage() , Toast.LENGTH_SHORT).show(); // Show error message to user
                            uProgressBar.setVisibility(View.GONE);  // hide progress bar
                        }
                    }
                });

            }
        });

        uSignInstead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));    // redirect to sign in instead
            }
        });

    }
}
