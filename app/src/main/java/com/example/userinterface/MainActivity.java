package com.example.userinterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText edtNamw, edtContact, edtCNIC, edtChechisNumber, edtVehicleName, edtVehicleNumber, edtCode, edtAmount;
    Spinner spinnerType, spinnerPaidType;
    Button btnSubmit;
    String message;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.tv);
        mTitle.setText("Make Challan");

        loadingBar = new ProgressDialog(this);

        message = getIntent().getStringExtra("uid");
       // Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();

        edtNamw = findViewById(R.id.edtName);
        edtContact = findViewById(R.id.edtContact);
        edtCNIC = findViewById(R.id.edtCNIC);
        spinnerType = findViewById(R.id.spinnerType);
        edtChechisNumber = findViewById(R.id.edtChechisNumber);
        edtVehicleName = findViewById(R.id.edtVehicleName);
        edtVehicleNumber = findViewById(R.id.edtVehicleNumber);
        edtCode = findViewById(R.id.edtCode);
        edtAmount = findViewById(R.id.edtAmout);
        spinnerPaidType = findViewById(R.id.spinnerPaidType);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addChallan();
            }
        });
    }

    private void addChallan() {
        String name = edtNamw.getText().toString().trim();
        String contact = edtContact.getText().toString().trim();
        String CNIC = edtCNIC.getText().toString().trim();
        String chechis_number = edtChechisNumber.getText().toString().trim();
        String type = spinnerType.getSelectedItem().toString();
        String vehicle_name = edtVehicleName.getText().toString().trim();
        String vehicle_number = edtVehicleNumber.getText().toString().trim();
        String code = edtCode.getText().toString().trim();
        String amount = edtAmount.getText().toString().trim();
        String paidType = spinnerPaidType.getSelectedItem().toString();

        if (TextUtils.isEmpty(name)) {
            edtNamw.setError("Empty Field");
            edtNamw.requestFocus();
        } else if (TextUtils.isEmpty(contact)) {
            edtContact.setError("Empty Field");
            edtContact.requestFocus();
        } else if (TextUtils.isEmpty(CNIC)) {
            edtCNIC.setError("Empty Field");
            edtCNIC.requestFocus();
        } else if (TextUtils.isEmpty(chechis_number)) {
            edtChechisNumber.setError("Empty Field");
            edtChechisNumber.requestFocus();
        } else if (TextUtils.isEmpty(vehicle_name)) {
            edtVehicleName.setError("Empty Field");
            edtVehicleName.requestFocus();
        } else if (TextUtils.isEmpty(vehicle_number)) {
            edtVehicleNumber.setError("Empty Field");
            edtVehicleNumber.requestFocus();
        } else if (TextUtils.isEmpty(code)) {
            edtCode.setError("Empty Field");
            edtCode.requestFocus();
        } else if (TextUtils.isEmpty(amount)) {
            edtAmount.setError("Empty Field");
            edtAmount.requestFocus();
        } else {

            loadingBar.setTitle("Making Challan");
            loadingBar.setMessage("Please Wait While We Are Making Your Challan");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            DatabaseReference mRef1 = FirebaseDatabase.getInstance().getReference("Chalan");

            HashMap customerMap = new HashMap();
            customerMap.put("Name", name);
            customerMap.put("Contact", contact);
            customerMap.put("CNIC", CNIC);
            customerMap.put("Chechis_number", chechis_number);
            customerMap.put("Type", type);
            customerMap.put("Vehicle_Name", vehicle_name);
            customerMap.put("Vehicle_Number", vehicle_number);
            customerMap.put("Code", code);
            customerMap.put("Amount", amount);
            customerMap.put("Paid_Type", paidType);
            mRef1.child(message).child(name).updateChildren(customerMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Challan Added!", Toast.LENGTH_SHORT).show();
                        edtNamw.setText("");
                        edtContact.setText("");
                        edtCNIC.setText("");
                        edtChechisNumber.setText("");
                        edtVehicleName.setText("");
                        edtVehicleNumber.setText("");
                        edtCode.setText("");
                        edtAmount.setText("");
                        loadingBar.dismiss();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed To Add Challan!", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }
}
