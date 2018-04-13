package com.example.travelmalaysia;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.travelmalaysia.model.MyUser;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private MyUser mUser;
    private RequestQueue requestQueue;
    private JsonObjectRequest request;
    private SharedPreferences sharedPreferences;
    private String url;

    //widget
    private TextView tvId;
    private TextView tvName;
    private TextView tvEmail;
    private TextView tvPoints;
    private Button mRedeemBtn;
    private Button mLocationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeVariable();
        getMyUser();
    }

    private void initializeVariable() {
        url = getString(R.string.url_profile_control);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        tvName = findViewById(R.id.profile_tv_name);
        tvEmail = findViewById(R.id.profile_tv_email);
        tvPoints = findViewById(R.id.profile_tv_points);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_profile);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        sharedPreferences = getSharedPreferences(getString(R.string.loginPreference), Context.MODE_PRIVATE);

        mRedeemBtn = findViewById(R.id.profile_btn_item_history);
        mRedeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RedeemHistoryActivity.class);
                i.putExtra("user", mUser);
                startActivity(i);
            }
        });

        mLocationBtn = findViewById(R.id.profile_btn_location_history);
        mLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LocationHistoryActivity.class);
                i.putExtra("user", mUser);
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_profile:
                //handle profile action
                //Toast.makeText(getApplicationContext(), "Profile Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_map:
                //handle map action
                //Toast.makeText(getApplicationContext(), "Map Clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                break;
           /* case R.id.nav_quest:
                //handle quest action
                Toast.makeText(getApplicationContext(), "Quest Clicked", Toast.LENGTH_SHORT).show();
                break;*/
            case R.id.nav_reward:
                //handle reward action
                //Toast.makeText(getApplicationContext(), "Reward Clicked", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(getApplicationContext(), RetriveItemData.class));
                Intent i = new Intent(getApplicationContext(), DisplayShop.class);
                i.putExtra("user", mUser);
                startActivity(i);
                break;
          /*  case R.id.nav_help:
                //handle help action
                Toast.makeText(getApplicationContext(), "Help Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_setting:
                //handle setting action
                Toast.makeText(getApplicationContext(), "Settings Clicked", Toast.LENGTH_SHORT).show();
                break;*/
            case R.id.nav_logout:
                //todo:: implement logout function

                AlertDialog.Builder builder = new AlertDialog.Builder(this); //Home is name of the activity
                builder.setMessage("Do you want to logout and exit?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        i.putExtra("finish", true);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities

                        startActivity(i);
                        removeSharedPreference();
                        finish();
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
//                Toast.makeText(getApplicationContext(), "Good Bye", Toast.LENGTH_SHORT).show();
//                finish();
                break;
            case R.id.nav_exit:
                builder = new AlertDialog.Builder(this);
                builder.setMessage("Do you want to exit?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        Intent i = new Intent();
                        i.putExtra("finish", true);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                        finish();
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void removeSharedPreference() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.loginPreference), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("email");
        editor.remove("password");
        editor.commit();
        Toast.makeText(this, "Remove Cache", Toast.LENGTH_SHORT).show();
    }


    private void getMyUser() {
        int myId = sharedPreferences.getInt("id", 0);
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id", myId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        request = new JsonObjectRequest(
                Request.Method.POST,
                url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("result").equalsIgnoreCase("success")) {

                                JSONObject jsonUser = response.getJSONObject("user");

                                int id = jsonUser.getInt("userId");
                                String name = jsonUser.getString("userName");
                                String email = jsonUser.getString("userEmail");
                                String password = jsonUser.getString("userPassword");
                                int points = jsonUser.getInt("userPoints");

                                mUser = new MyUser(id, name, email, password, points);

                                tvName.setText(mUser.getName());
                                tvEmail.setText(mUser.getEmail());
                                tvPoints.setText(String.valueOf(mUser.getPoints()));

                            } else {
                                Toast.makeText(getApplicationContext(), "Error: " + response.getString("message"), Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Something Error at Login()", Toast.LENGTH_LONG).show();
                Log.e(TAG, "onErrorResponse: " + error.getMessage());
                Log.e(TAG, "onErrorResponse: " + error);
                error.printStackTrace();
            }
        }
        );
        requestQueue.add(request);
    }
}
