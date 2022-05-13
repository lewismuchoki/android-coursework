package com.example.androidcoursework;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private Button update;
    private Switch aSwitch;

    private TextView username;
    private TextView userFname;
    private TextView userLname;
    private TextView userEmail;

    String users;
    String fname;
    String lname;
    String email;
    String pin;


    public ProfileFragment() {
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        username = view.findViewById(R.id.username);
        userFname = view.findViewById(R.id.firstName);
        userLname = view.findViewById(R.id.lastName);
        userEmail = view.findViewById(R.id.email);
        update = view.findViewById(R.id.updateAccount);
        aSwitch = view.findViewById(R.id.enableEncryption);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("user-details").child(auth.getCurrentUser().getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails user = snapshot.getValue(UserDetails.class);
                username.setText("Username: " + user.getUsername());
                userFname.setText("First Name: " + user.getFirstName());
                userLname.setText("Last Name: " + user.getLastName());
                userEmail.setText("Email: " + user.getEmail());

                users = user.getUsername();
                fname = user.getFirstName();
                lname = user.getLastName();
                email = user.getEmail();
                pin = user.getPin();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        update.setOnClickListener(view1 -> updateUser());

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("State", Context.MODE_PRIVATE);
        SharedPreferences.Editor preferences = sharedPreferences.edit();
        aSwitch.setChecked(sharedPreferences.getBoolean("isChecked", false));
        aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && pin.equals("null")) {
                Toast.makeText(getContext(), "You need to set a passcode first!!", Toast.LENGTH_SHORT).show();
                aSwitch.setChecked(false);
            } else if (isChecked) {
                preferences.putBoolean("isChecked", true);
            } else {
                preferences.putBoolean("isChecked", false);
            }
            preferences.apply();
        });

        return view;
    }

    private void updateUser() {
        DatabaseReference updateData = FirebaseDatabase.getInstance().getReference("user-details").child(auth.getCurrentUser().getUid());
        AlertDialog.Builder myDialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.update_user, null);
        myDialog.setView(view);

        AlertDialog dialog = myDialog.create();

        EditText uFname = view.findViewById(R.id.updateFname);
        EditText uLname = view.findViewById(R.id.updateLname);
        EditText uUser = view.findViewById(R.id.updateUsername);
        EditText userPin = view.findViewById(R.id.userPin);

        uUser.setText(users);
        uFname.setText(fname);
        uLname.setText(lname);
        userPin.setText(pin);

        Button cancelBtn = view.findViewById(R.id.cancelBtn);
        Button updateButton = view.findViewById(R.id.update);

        updateButton.setOnClickListener(v -> {
            fname = uFname.getText().toString().trim();
            lname = uLname.getText().toString().trim();
            users = uUser.getText().toString().trim();
            String pins = userPin.getText().toString().trim();

            if (TextUtils.isEmpty(users)) {
                uUser.setError("Username is required");
            }

            if (TextUtils.isEmpty(fname)) {
                uFname.setError("First Name is required");
            }

            if (TextUtils.isEmpty(fname)) {
                uLname.setError("Last Name is required");
            } else {
                updateData.child("username").setValue(users);
                updateData.child("firstName").setValue(fname);
                updateData.child("lastName").setValue(lname);
                updateData.child("pin").setValue(pins);


                Toast.makeText(getContext(), "Account Updated Successful", Toast.LENGTH_SHORT).show();

            }

            dialog.dismiss();

        });


        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
