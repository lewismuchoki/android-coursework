package com.example.androidcoursework;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ExerciseScreen extends AppCompatActivity {
    CheckBox checkBox1;
    CheckBox checkBox2;
    CheckBox checkBox3;
    String type = "";
    TextView workoutName;
    TextView workoutDesc;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_screen);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Home Workout");

        checkBox1 = findViewById(R.id.checkBox);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);

        workoutName = findViewById(R.id.workout_name);
        workoutDesc = findViewById(R.id.workout_description);

        if (getIntent().getExtras() != null) {
            type = getIntent().getStringExtra("type");
            if (type.equals("absb")) {
                if (checkBox1.isChecked()) {
                    AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
                    LayoutInflater inflater = LayoutInflater.from(this);
                    View mView = inflater.inflate(R.layout.workout_return, null);

                    myDialog.setView(mView);
                    final AlertDialog dialog = myDialog.create();
                    dialog.setCancelable(false);
                    dialog.show();

                    workoutName.setText("Abs");
                    workoutDesc.setText("Abs description");
                }


            } else if (type.equals("absi")) {


            } else if (type.equals("absa")) {


            }
        }
    }
}
