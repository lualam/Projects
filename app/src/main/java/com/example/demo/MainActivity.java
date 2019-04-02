package com.example.demo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button btnSingUp, btnSignIn;
    EditText edtEmail, edtPassword;
    private ProgressDialog dialog;

    public static final String TAG = "MainActivity";

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences(StaticVariable.MyPREFERENCES, Context.MODE_PRIVATE);


        dialog = new ProgressDialog(this);

        edtEmail = findViewById(R.id.editText);
        edtPassword = findViewById(R.id.editText2);

        btnSignIn = findViewById(R.id.button);
        btnSingUp = findViewById(R.id.button2);

        btnSingUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (sharedpreferences.getBoolean(StaticVariable.LoggedIn, false)) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnSingUp) {
            startActivity(new Intent(this, SignUpActivity.class));
        } else if (v == btnSignIn) {
            signIn();
        }
    }

    private void signIn() {
        dialog.setMessage("Please wait.");
        dialog.setCancelable(false);
        dialog.show();

        final String email = edtEmail.getText().toString();
        final String passsword = edtPassword.getText().toString();


        if (email.isEmpty() || passsword.isEmpty()) {

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            Toast.makeText(this, "Please Complete the form", Toast.LENGTH_SHORT).show();

        } else {

            // Instantiate the RequestQueue.
            final RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://10.0.2.2/project/login.php";

            // Request a string response from the provided URL.
            final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            Log.d(TAG, "onResponse: " + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getInt("status") == 200) {
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putBoolean(StaticVariable.LoggedIn, true);
                                    editor.apply();

                                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                    finish();
                                } else if (jsonObject.getInt("status") == 400) {
                                    Toast.makeText(MainActivity.this, "No User Found. Please Sign Up", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", passsword);
                    return params;
                }
            };

            // Add the request to the RequestQueue.
            queue.add(stringRequest);

        }
    }
}
