package com.example.studyprofile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void signout(View view){
        FirebaseAuth.getInstance().signOut(); // signs user out
        startActivity(new Intent(getApplicationContext(),Login.class)); // sends user to login page
        finish();
    }

}
