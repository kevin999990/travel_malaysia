package com.example.travelmalaysia;

import android.content.Context;
import android.content.Intent;
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

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private String url;
    private RequestQueue requestQueue;
    private JsonObjectRequest request;
    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private String name;
    private String email;
    private String password;
    private Button mSubmit;
    private Button mCancel;
    private String errorText;
    private View mProgressView;
    private View mRegisterView;

    public final static boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeVariable();
    }

    private void Register() {
        showProgress(true);
        JSONObject jsonObject = new JSONObject();


        try {
            jsonObject.put("name", name);
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("result").equalsIgnoreCase("success")) {
                                Toast.makeText(RegisterActivity.this, "Register Success, Redirect to Login Page", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                finish();
                            } else {
                                showProgress(false);
                                Toast.makeText(RegisterActivity.this, "Error, " + response.getString("message"), Toast.LENGTH_LONG).show();
                                mName.setText("");
                                mEmail.setText("");
                                mPassword.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, "Something Error at Register()", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "onErrorResponse: " + error);
                        error.printStackTrace();
                    }
                });
        requestQueue.add(request);

    }

    private void initializeVariable() {
        url = getResources().getString(R.string.url_register_control);
        mName = findViewById(R.id.register_et_name);
        mEmail = findViewById(R.id.register_et_email);
        mPassword = findViewById(R.id.register_et_password);

        mProgressView = findViewById(R.id.register_progress_layout);
        mRegisterView = findViewById(R.id.register_form);

        mSubmit = findViewById(R.id.register_et_submit);
        mCancel = findViewById(R.id.register_btn_back);

        errorText = getResources().getString(R.string.error_text);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        setButtonListener();
    }

    private void setButtonListener() {
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                name = mName.getText().toString();
                email = mEmail.getText().toString();
                password = mPassword.getText().toString();

                if (name.isEmpty()) {
                    mName.requestFocus();
                    mName.setError(errorText);
                    return;
                } else if (email.isEmpty()) {
                    mEmail.requestFocus();
                    mEmail.setError(errorText);
                    return;
                } else if (password.isEmpty()) {
                    mPassword.requestFocus();
                    mPassword.setError(errorText);
                    return;
                } else if (!isValidEmail(email)) {
                    mEmail.requestFocus();
                    mEmail.setError("Email Not Valid");
                    return;
                } else {
                    Register();
                }
            }
        });
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    //show progress bar is Register is in progress
    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mRegisterView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
