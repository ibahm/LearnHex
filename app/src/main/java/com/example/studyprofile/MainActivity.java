package com.example.studyprofile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    ImageView uOptions;
    TextView uSignOut;
    TextView uUserName;
    ConstraintLayout uOptionsConstraint;
    private static final int PICK_IMAGE_REQUEST = 1;
    TextView uAccountUpload;
    ImageView uProfilePicture;
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uOptions = findViewById(R.id.options);
        uOptionsConstraint = findViewById(R.id.optionsConstraint);
        uSignOut = findViewById(R.id.signOut);
        uUserName = findViewById(R.id.userName);
        uProfilePicture = findViewById(R.id.profilePicture);
        uAccountUpload = findViewById(R.id.accountUpload);

        uOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uOptionsConstraint.getVisibility() == View.VISIBLE) {
                    uOptionsConstraint.setVisibility(View.GONE);
                } else {
                    uOptionsConstraint.setVisibility(View.VISIBLE);
                }
            }
        });

        uAccountUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });

        uSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut(); // signs user out
                startActivity(new Intent(getApplicationContext(), Login.class)); // sends user to login page
                finish();
            }
        });

    }

    private void FileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.with(this).load(mImageUri).into(uProfilePicture);
        }
    }
}
