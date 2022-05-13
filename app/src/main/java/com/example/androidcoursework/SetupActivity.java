package com.example.androidcoursework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SetupActivity extends AppCompatActivity {
    private Button skip;
    private Button updateAccount;
    private EditText username;
    private EditText first_name;
    private EditText last_name;
    private TextView updateMessage;

    private FirebaseAuth auth;
    private String onlineUserId = "";
    private DatabaseReference reference;
    private ProgressBar progressBar3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        username = findViewById(R.id.accountUsername);
        first_name = findViewById(R.id.accountFirstName);
        last_name = findViewById(R.id.accountLastName);

        skip = findViewById(R.id.skipButton);
        updateAccount = findViewById(R.id.updateButton);

        progressBar3 = findViewById(R.id.progressbar3);
        updateMessage = findViewById(R.id.updateMessage);

        auth = FirebaseAuth.getInstance();
        onlineUserId = auth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("user-details").child(auth.getCurrentUser().getUid());

        updateAccount.setOnClickListener(view -> {
            String uname = username.getText().toString();
            String fname = first_name.getText().toString();
            String lname = last_name.getText().toString();


            reference.child("username").setValue(uname);
            reference.child("firstName").setValue(fname);
            reference.child("lastName").setValue(lname);

            updateMessage.setVisibility(View.VISIBLE);
            progressBar3.setVisibility(View.VISIBLE);
            updateAccount.setEnabled(false);

            new Handler().postDelayed(() -> {
                Intent intent =  new Intent(SetupActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }, 3000);
        });

        skip.setOnClickListener(view -> {
            Intent intent = new Intent(SetupActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}