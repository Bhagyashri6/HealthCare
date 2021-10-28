package com.example.healthcareapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthcareapp.R;
import com.example.healthcareapp.adapter.UserAdapter;
import com.example.healthcareapp.app.AppController;
import com.example.healthcareapp.helper.SessionData;
import com.example.healthcareapp.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private static final String url = "https://reqres.in/api/users";
    private ArrayList<User> users;
    private ProgressDialog pDialog;
    private UserAdapter userAdapter;
    private RecyclerView rv_list;
    JSONObject Response;
    SessionData sessionData;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sessionData = new SessionData(getApplicationContext());


        rv_list = (RecyclerView) findViewById(R.id.rv_list);

        pDialog = new ProgressDialog(this);
        users = new ArrayList<>();
        userAdapter = new UserAdapter(getApplicationContext(), users);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        rv_list.setLayoutManager(layoutManager);
        rv_list.setItemAnimator(new DefaultItemAnimator());
        rv_list.setAdapter(userAdapter);

        status = sessionData.getObjectAsString("login");
        if (status.equalsIgnoreCase("false") || status.isEmpty() || status.equalsIgnoreCase("Empty")) {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);

        }
        CheckNetwork();
    }

    private void CheckNetwork() {
        if (!isNetworkAvailable(this)) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Alert...");
            alertDialog.setMessage("Please Check Your Network!");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();

        } else {
            UserDetail();
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }

    private void UserDetail() {

        pDialog.setMessage("Downloading json...");
        pDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        pDialog.hide();

                        users.clear();
                        try {
                            String total = response.getString("total");
                            JSONArray result = response.getJSONArray("data");
                            String id, email, first_name, last_name, avatar;
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject object = result.getJSONObject(i);
                                id = object.getString("id");
                                email = object.getString("email");
                                first_name = object.getString("first_name");
                                last_name = object.getString("last_name");
                                avatar = object.getString("avatar");

                                User user = new User(id, email, first_name, last_name, avatar);
                                users.add(user);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        userAdapter.notifyDataSetChanged();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.getMessage());
                        pDialog.hide();
                    }
                });
        AppController.getInstance().addToRequestQueue(request);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.signin:
                if (status.equalsIgnoreCase("false") || status.isEmpty() || status.equalsIgnoreCase("Empty")) {
                    item.setTitle("Log Out");
                }else {
                    item.setTitle("Sign In");
                }
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit the page?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ActivityCompat.finishAffinity(HomeActivity.this);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}