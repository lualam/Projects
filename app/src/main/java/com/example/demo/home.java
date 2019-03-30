package com.example.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
    public void sh(View v) {
        startActivity(new Intent(home.this, search_activity.class));
    }
    public void ah(View v) {
        startActivity(new Intent(home.this, ambulance_Activity.class));
    }
    public void th(View v) {
        startActivity(new Intent(home.this, tips_Activity.class));

    }
}
