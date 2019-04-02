package com.example.demo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sharedpreferences = getSharedPreferences(StaticVariable.MyPREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.profile_action_logout) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void sh(View v) {
        startActivity(new Intent(HomeActivity.this, SearchActivity.class));
    }
    public void ah(View v) {
        startActivity(new Intent(HomeActivity.this, AmbulanceActivity.class));
    }
    public void th(View v) {
        startActivity(new Intent(HomeActivity.this, TipsActivity.class));

    }
}
