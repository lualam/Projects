package com.example.demo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AmbulanceActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnOne, btnTwo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_);

        btnOne = findViewById(R.id.button10);
        btnTwo = findViewById(R.id.button11);

        btnOne.setOnClickListener(this);
        btnTwo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==btnOne){
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "111", null)));
        }else if (v==btnTwo){
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "999", null)));
        }
    }
}
