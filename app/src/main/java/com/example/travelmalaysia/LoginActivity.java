package com.example.travelmalaysia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private String url;
    private RequestQueue requestQueue;
    private JsonObjectRequest request;
    private EditText mEmail;
    private EditText mPassword;
    private Button mRegister;
    private Button mSubmit;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.register_et_email);
        mPassword = findViewById(R.id.register_et_password);
        mSubmit = findViewById(R.id.register_et_submit);
        mRegister = findViewById(R.id.login_btn_register);

        sharedPreferences = getSharedPreferences(getString(R.string.loginPreference), Context.MODE_PRIVATE);

        url = "http://kvintech.esy.es/travelmalaysia/user_control.php";
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        //read Login user from shared perference
        String savedEmail = sharedPreferences.getString("email", "NA");
        String savedPassword = sharedPreferences.getString("password", "NA");

        if (!savedEmail.equalsIgnoreCase("NA")) {
            if (!savedPassword.equalsIgnoreCase("NA")) {
                Toast.makeText(this, "Logging In to Last User.....", Toast.LENGTH_SHORT).show();
                setContentView(R.layout.activity_splash_screen);
                Log.d(TAG, "onCreate: Auto Login by using sharedPreference");
                login(savedEmail, savedPassword);
            }
        }

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String errText = "This Field Required";
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if (mEmail.getText().toString().isEmpty()) {
                    mEmail.setError(errText);
                    return;
                } else if (mPassword.getText().toString().isEmpty()) {
                    mPassword.setError(errText);
                    return;
                } else
                    Toast.makeText(LoginActivity.this, "Logging In....", Toast.LENGTH_SHORT).show();
                login(email, password);
            }
        });
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:: implement register function
                Toast.makeText(LoginActivity.this, "I have no check, just give u go register", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });


    }

    private void login(final String email, final String password) {
        requestQueue = Volley.newRequestQueue(LoginActivity.this);

        //create JSONObject to pass to PHP Server for checking
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            Log.d(TAG, "login: " + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        request = new JsonObjectRequest(
                Request.Method.POST,
                url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: " + response);

                        try {
                            JSONObject json = response;//new JSONObject(response);
                            if (json.getString("result").equalsIgnoreCase("success")) {
                                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();

                                //write login data to shared perference to remain login

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("email", email);
                                editor.putString("password", password);
                                editor.commit();


                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Something Error at Login()", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onErrorResponse: " + error.getMessage());
                Log.e(TAG, "onErrorResponse: " + error);
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }
}