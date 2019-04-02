package com.example.demo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "SignUpActivity";

    final int CODE_FOR_BLOOD = 1;

    EditText edtEmail, edtPass, edtName,edtAge, edtPhone, edtStreet, edtTown;
    TextView tvBloodGroup;
    Button btnSignUp;
    RadioButton radioButtonMale, getRadioButtonFemale;

    String gender = "";

    private ProgressDialog dialog;

    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        sharedpreferences = getSharedPreferences(StaticVariable.MyPREFERENCES, Context.MODE_PRIVATE);
        dialog = new ProgressDialog(this);
        edtEmail = findViewById(R.id.editText4);
        edtPass = findViewById(R.id.editText5);
        edtStreet = findViewById(R.id.editText3);
        edtTown = findViewById(R.id.editText6);
        edtName = findViewById(R.id.editText7);
        edtAge = findViewById(R.id.editText8);
        edtPhone = findViewById(R.id.editText9);

        tvBloodGroup = findViewById(R.id.textView12);
        radioButtonMale = findViewById(R.id.radioButton);
        getRadioButtonFemale = findViewById(R.id.radioButton2);

        btnSignUp = findViewById(R.id.button3);


        radioButtonMale.setOnClickListener(this);
        getRadioButtonFemale.setOnClickListener(this);
        tvBloodGroup.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v == tvBloodGroup) {
            startActivityForResult(new Intent(this, BloodGroupActivity.class), CODE_FOR_BLOOD);
        } else if (v == btnSignUp) {
            signUP();
        } else if (v == radioButtonMale) {
            radioButtonMale.setChecked(true);
            getRadioButtonFemale.setChecked(false);
            gender = "M";
        } else if (v == getRadioButtonFemale) {
            radioButtonMale.setChecked(false);
            getRadioButtonFemale.setChecked(true);
            gender = "F";

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_FOR_BLOOD && resultCode == RESULT_OK) {
            tvBloodGroup.setText(data.getStringExtra("bloodgroup"));
        }
    }

    /**
     * This method help to send sign up data to localhost
     */
    private void signUP() {

        dialog.setMessage("Please wait.");
        dialog.setCancelable(false);
        dialog.show();

        final String email = edtEmail.getText().toString();
        final String passsword = edtPass.getText().toString();
        final String name = edtName.getText().toString();
        final String street = edtStreet.getText().toString();
        final String town = edtTown.getText().toString();
        final String phone = edtPhone.getText().toString();
        final String age = edtAge.getText().toString();
        final String bloodGroup = tvBloodGroup.getText().toString();

        if (email.isEmpty() || passsword.isEmpty() || name.isEmpty() || street.isEmpty() || town.isEmpty() ||
                gender.isEmpty() || bloodGroup.equals("Select Blood Group") || phone.isEmpty() || age.isEmpty()) {

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            Toast.makeText(this, "Please Complete the form", Toast.LENGTH_SHORT).show();

        } else {

            // Instantiate the RequestQueue.
            final RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://10.0.2.2/project/add_user.php";

            // Request a string response from the provided URL.
            final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: "+response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getInt("status") == 203){
                                    Toast.makeText(SignUpActivity.this, "User Exists", Toast.LENGTH_SHORT).show();
                                }else if (jsonObject.getInt("status") == 201){
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putBoolean(StaticVariable.LoggedIn, true);
                                    editor.apply();

                                    Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }

                }
            }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", name);
                    params.put("email", email);
                    params.put("password", passsword);
                    params.put("age", age);
                    params.put("gender", gender);
                    params.put("bloodgroup", bloodGroup);
                    params.put("phone", phone);
                    params.put("street", street);
                    params.put("town", town);
                    return params;
                }
            };

            // Add the request to the RequestQueue.
            queue.add(stringRequest);

        }
    }

}
