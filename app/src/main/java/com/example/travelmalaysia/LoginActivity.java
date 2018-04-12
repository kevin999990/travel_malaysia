package com.example.travelmalaysia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
    private View mProgressView;
    private View mLoginView;
    private String savedEmail;
    private String savedPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeVariable();

        if (checkPreviousLogin()) {
            Toast.makeText(this, "Logging In to Previous User.....", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onCreate: Auto Login by using sharedPreference");
            login(savedEmail, savedPassword);
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    //show progress bar is login is in progress
    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void login(final String email, final String password) {
        showProgress(true);

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
                            if (response.getString("result").equalsIgnoreCase("success")) {
                                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();

                                //write login data to shared perference to remain login
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                JSONObject jsonUser = response.getJSONObject("user");

                                editor.putInt("id", jsonUser.getInt("userId"));
                                editor.putString("name", jsonUser.getString("userName"));
                                editor.putString("email", jsonUser.getString("userEmail"));
                                editor.putString("password", jsonUser.getString("userPassword"));
                                editor.putInt("points", jsonUser.getInt("userPoints"));
                                editor.commit();

                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            } else {
                                showProgress(false);
                                Toast.makeText(LoginActivity.this, "Wrong Email or Password", Toast.LENGTH_LONG).show();
                                mPassword.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Something Error at Login()", Toast.LENGTH_LONG).show();
                Log.e(TAG, "onErrorResponse: " + error.getMessage());
                Log.e(TAG, "onErrorResponse: " + error);
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    private void initializeVariable() {
        url = getResources().getString(R.string.url_login_control);

        mEmail = findViewById(R.id.register_et_email);
        mPassword = findViewById(R.id.register_et_password);
        mSubmit = findViewById(R.id.register_et_submit);
        mRegister = findViewById(R.id.login_btn_register);

        mProgressView = findViewById(R.id.login_progress_layout);
        mLoginView = findViewById(R.id.login_form);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        //read Login user from shared preference
        sharedPreferences = getSharedPreferences(getString(R.string.loginPreference), Context.MODE_PRIVATE);
        savedEmail = sharedPreferences.getString("email", "NA");
        savedPassword = sharedPreferences.getString("password", "NA");

        setButtonListener();
    }

    private boolean checkPreviousLogin() {
        //check is any previous login
        if (!savedEmail.equalsIgnoreCase("NA")) {
            if (!savedPassword.equalsIgnoreCase("NA")) {
                return true;
            }
        }
        return false;
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    private void setButtonListener() {
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();

                String errText = "This Field Required";
                String errEmail = "Email Not Valid";
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                //check if any field is empty
                if (mEmail.getText().toString().isEmpty()) {
                    mEmail.requestFocus();
                    mEmail.setError(errText);
                    return;
                } else if (!isValidEmail(mEmail.getText().toString())) {
                    mEmail.requestFocus();
                    mEmail.setError(errEmail);
                    return;
                } else if (mPassword.getText().toString().isEmpty()) {
                    mPassword.requestFocus();
                    mPassword.setError(errText);
                    return;
                } else
                    login(email, password);
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            }
        });
    }
}