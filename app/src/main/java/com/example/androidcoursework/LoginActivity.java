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

public class LoginActivity extends AppCompatActivity {

    private EditText logEmail;
    private EditText logPassword;
    private Button logBtn;
    private TextView gotoRegister;
    private TextView loginMessage;


    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logEmail = findViewById(R.id.loginEmail);
        logPassword = findViewById(R.id.loginPassword);
        logBtn = findViewById(R.id.loginButton);
        gotoRegister = findViewById(R.id.signupScreen);
        progressBar = findViewById(R.id.progressbar);
        loginMessage = findViewById(R.id.loginMessage);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }

        gotoRegister.setOnClickListener(this::onRegistrationClick);

        logBtn.setOnClickListener(v -> {
            String emailString = logEmail.getText().toString();
            String passwordString = logPassword.getText().toString();

            if (TextUtils.isEmpty(emailString)) {
                logEmail.setError("Email is Required");
            }

            if (TextUtils.isEmpty(passwordString)) {
                logPassword.setError("Password is Required");
            } else {

                loginMessage.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                logBtn.setEnabled(false);

                mAuth.signInWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        loginMessage.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        logBtn.setEnabled(true);
                    }
                });
            }

        });
    }

    public void onRegistrationClick(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
    }
}