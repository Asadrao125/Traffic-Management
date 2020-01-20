package com.example.userinterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeScreen extends AppCompatActivity {
    Button btnLogin;
    EditText edtUser, edtPass;
    public String current_user_id;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.tv);
        mTitle.setText("Traffic Management");

        btnLogin = findViewById(R.id.btnLogin);
        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        loadingBar = new ProgressDialog(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    private void loginUser() {

        loadingBar.setTitle("Loging In");
        loadingBar.setMessage("Please Wait While We Are Loging You In");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(true);

        final String customerNumber = edtUser.getText().toString().toLowerCase().trim();
        final String customerPassword = edtPass.getText().toString().trim();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child(customerNumber).child("username").getValue(String.class);
                String pass = dataSnapshot.child(customerNumber).child("password").getValue(String.class);

                if (TextUtils.isEmpty(customerNumber)) {
                    edtUser.setError("Empty Field!");
                    edtUser.requestFocus();
                    loadingBar.dismiss();
                } else if (TextUtils.isEmpty(customerPassword)) {
                    edtPass.setError("Empty Field!");
                    edtPass.requestFocus();
                    loadingBar.dismiss();
                } else if (!customerNumber.equals(name)) {
                    edtUser.setError("Username Doesnot Exist!");
                    edtUser.requestFocus();
                    loadingBar.dismiss();
                } else if (!customerPassword.equals(pass)) {
                    edtPass.setError("Password Doesnot Exist!");
                    edtPass.requestFocus();
                    loadingBar.dismiss();
                } else {

                    current_user_id = name;
                    Toast.makeText(HomeScreen.this, "Matched!", Toast.LENGTH_SHORT).show();
                    edtPass.setText("");
                    edtUser.setText("");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("uid", current_user_id);
                    startActivity(intent);
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}