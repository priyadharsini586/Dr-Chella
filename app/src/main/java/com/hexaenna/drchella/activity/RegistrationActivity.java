package com.hexaenna.drchella.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.hexaenna.drchella.R;
import com.hexaenna.drchella.adapter.GenderSpinnerAdapter;

import java.util.ArrayList;

import fr.ganfra.materialspinner.MaterialSpinner;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    MaterialSpinner gerderSpinner;
    GenderSpinnerAdapter genderSpinnerAdapter ;
    ArrayList<String> genderList;
    Button btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        genderList = new ArrayList<>();
        genderList.add("Male");
        genderList.add("Female");
        genderList.add("Transant");
        genderSpinnerAdapter = new GenderSpinnerAdapter(getApplicationContext(),genderList);
        gerderSpinner = (MaterialSpinner) findViewById(R.id.spinner1);
        gerderSpinner.setAdapter(genderSpinnerAdapter);
         btnRegister = (Button) findViewById(R.id.btnReg);
        btnRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnReg:
                Intent intent = new Intent(getApplicationContext(),OTPActivity.class);
                startActivity(intent);
                RegistrationActivity.this.finish();
                break;
        }
    }
}
