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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ComplaintActivity extends AppCompatActivity {
    Button btnSubmit;
    EditText edtName, edtAddress, edtMobile, edtDescription;
    DatabaseReference mRef;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.tv);
        mTitle.setText("Complaint");

        edtName = findViewById(R.id.edtName);
        edtAddress = findViewById(R.id.edtAddress);
        edtMobile = findViewById(R.id.edtMobile);
        edtDescription = findViewById(R.id.edtDescription);
        btnSubmit = findViewById(R.id.btnSubmit);
        loadingBar = new ProgressDialog(this);

        mRef = FirebaseDatabase.getInstance().getReference("Complaint");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComplaint();
            }
        });
    }

    private void addComplaint() {
        String name = edtName.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String mobile = edtMobile.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            edtName.setError("Empty Field");
            edtName.requestFocus();
        } else if (TextUtils.isEmpty(address)) {
            edtAddress.setError("Empty Field");
            edtAddress.requestFocus();
        } else if (TextUtils.isEmpty(mobile)) {
            edtMobile.setError("Empty Field");
            edtMobile.requestFocus();
        } else if (TextUtils.isEmpty(description)) {
            edtDescription.setError("Empty Field");
            edtDescription.requestFocus();
        } else {
            loadingBar.setTitle("Proceeding Complaint");
            loadingBar.setMessage("Please Wait While We Are Proceeding Your Complaint");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap customerMap = new HashMap();
            customerMap.put("Name", name);
            customerMap.put("Address", address);
            customerMap.put("Mobile", mobile);
            customerMap.put("Description", description);
            mRef.child(name).updateChildren(customerMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Complaint Added!!", Toast.LENGTH_SHORT).show();
                        edtName.setText("");
                        edtAddress.setText("");
                        edtMobile.setText("");
                        edtDescription.setText("");
                        loadingBar.dismiss();
                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(getApplicationContext(), "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }
}