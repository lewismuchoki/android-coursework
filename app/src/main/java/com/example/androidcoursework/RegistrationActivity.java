package com.example.androidcoursework;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private EditText regEmail;
    private EditText regPassword;
    private Button btnReg;
    private TextView gotoLogin;
    private TextView signupMessage;

    private FirebaseAuth mAuth;
    private DatabaseReference users;
    private ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        regEmail = findViewById(R.id.registrationEmail);
        regPassword = findViewById(R.id.registrationPassword);
        btnReg = findViewById(R.id.signupButton);
        gotoLogin = findViewById(R.id.loginScreen);

        progressBar2 = findViewById(R.id.progressbar2);
        signupMessage = findViewById(R.id.signupMessage);

        mAuth = FirebaseAuth.getInstance();

        gotoLogin.setOnClickListener(this::onLoginClick);

        btnReg.setOnClickListener(v -> {
            String email = regEmail.getText().toString();
            String password = regPassword.getText().toString();

            if (TextUtils.isEmpty(email)) {
                regEmail.setError("Email is Required");
            }

            if (TextUtils.isEmpty(password)) {
                regPassword.setError("Password is Required");
            } else {

                UserDetails userDetails = new UserDetails("null","null", "null", email,"null");
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(RegistrationActivity.this, SetupActivity.class);
                        startActivity(intent);
                        finish();
                        signupMessage.setVisibility(View.VISIBLE);
                        progressBar2.setVisibility(View.VISIBLE);
                        btnReg.setEnabled(false);

                        users = FirebaseDatabase.getInstance().getReference().child("user-details").child(mAuth.getCurrentUser().getUid());

                        users.setValue(userDetails).addOnCompleteListener(task1 -> {
                            //Do nothing
                        });
                    } else {
                        Toast.makeText(RegistrationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        signupMessage.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        btnReg.setEnabled(true);
                    }
                });
                signupMessage.setVisibility(View.VISIBLE);
                progressBar2.setVisibility(View.VISIBLE);
                btnReg.setEnabled(false);
            }
        });
    }

    public void onLoginClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.stay);
    }
}