package com.example.demo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, DonorAdapter.OnClickSelectListener {

    public static final String TAG = "SearchActivity";
    AutoCompleteTextView autoCompleteTextView;
    private ProgressDialog dialog;
    ArrayList<String> townArrayList;
    ArrayAdapter<String> adapter;
    List<DonorModel> donorArrayList;
    DonorAdapter donorAdapter;
    TextView tvBloodGroup;

    RecyclerView recyclerView;

    Button btnSearch;


    final int CODE_FOR_BLOOD = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_activity);

        autoCompleteTextView = findViewById(R.id.tvTown);
        tvBloodGroup = findViewById(R.id.textView10);
        recyclerView = findViewById(R.id.recyclerView);
        btnSearch = findViewById(R.id.button8);

        tvBloodGroup.setOnClickListener(this);
        btnSearch.setOnClickListener(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        dialog = new ProgressDialog(this);

        donorArrayList = new ArrayList<>();
        donorAdapter = new DonorAdapter(this, donorArrayList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(donorAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpTownSpinner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (in != null) in.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == tvBloodGroup) {
            hideKeyboard();
            startActivityForResult(new Intent(this, BloodGroupActivity.class), CODE_FOR_BLOOD);
        } else if (v == btnSearch) {
            search();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_FOR_BLOOD && resultCode == RESULT_OK) {
            tvBloodGroup.setText(data.getStringExtra("bloodgroup"));
        }
    }

    private void setUpTownSpinner() {
        dialog.setMessage("Please wait.");
        dialog.setCancelable(false);
        dialog.show();

        townArrayList = new ArrayList<>();

        final RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2/project/get_town.php";

        // Request a string response from the provided URL.
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getInt("status") == 200) {
                                JSONArray array = jsonObject.getJSONArray("data");
                                int length = array.length();
                                for (int i = 0; i < length; i++) {
                                    JSONObject jsonObject1 = array.getJSONObject(i);
                                    townArrayList.add(jsonObject1.getString("town"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            adapter = new ArrayAdapter<>
                                    (SearchActivity.this, R.layout.spinner_item, townArrayList);
                            autoCompleteTextView.setThreshold(2);
                            autoCompleteTextView.setAdapter(adapter);
                            autoCompleteTextView.setOnItemClickListener(SearchActivity.this);
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
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String town = parent.getItemAtPosition(position).toString();
        autoCompleteTextView.setText(town);

    }


    @Override
    public void onPhoneButtonClicked(String phone) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null)));
    }

    private void search() {
        final String town = autoCompleteTextView.getText().toString();
        final String bloodGroup = tvBloodGroup.getText().toString();

        if (town.isEmpty() || bloodGroup.isEmpty()) {
            Toast.makeText(this, "Please select search parameter", Toast.LENGTH_SHORT).show();
        } else {
            dialog.setMessage("Please wait.");
            dialog.setCancelable(false);
            dialog.show();

            townArrayList = new ArrayList<>();

            final RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://10.0.2.2/project/search.php";

            // Request a string response from the provided URL.
            final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: " + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getInt("status") == 200) {
                                    donorArrayList.clear();
                                    JSONArray array = jsonObject.getJSONArray("data");
                                    int length = array.length();
                                    for (int i = 0; i < length; i++) {
                                        Log.d(TAG, "onResponse: loop="+i);
                                        JSONObject jsonObject1 = array.getJSONObject(i);
                                        DonorModel donorModel = new DonorModel();
                                        donorModel.setName(jsonObject1.getString("name"));
                                        donorModel.setBloodgroup(jsonObject1.getString("bloodgroup"));
                                        donorModel.setEmail(jsonObject1.getString("email"));
                                        donorModel.setPhone(jsonObject1.getString("phone"));
                                        donorArrayList.add(donorModel);
                                    }
                                    donorAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(SearchActivity.this, "Sorry. No Donor found", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                e.printStackTrace();
                            }

                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("town", town);
                    params.put("bloodgroup", bloodGroup);
                    return params;
                }
            };

            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(8 * 1000, -1, 0));
            queue.add(stringRequest);
        }
    }
}
