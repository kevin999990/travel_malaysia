package com.example.travelmalaysia;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName = findViewById(R.id.register_et_name);
        mEmail = findViewById(R.id.register_et_email);
        mPassword = findViewById(R.id.register_et_password);


        mSubmit = findViewById(R.id.register_et_submit);
        mCancel = findViewById(R.id.register_btn_back);


        final String errorText = getResources().getString(R.string.error_text);

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
                name = mName.getText().toString();
                email = mEmail.getText().toString();
                password = mPassword.getText().toString();

                if (name.isEmpty()) {
                    mName.setError(errorText);
                    return;
                } else if (email.isEmpty()) {
                    mEmail.setError(errorText);
                    return;
                } else if (password.isEmpty()) {
                    mPassword.setError(errorText);
                    return;
                } else {
                    //TODO....
                    Register();
                }
            }
        });
    }

    private void Register() {
        url = "http://kvintech.esy.es/travelmalaysia/register_control.php";
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name", name);
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        request = new JsonObjectRequest(Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("result").equalsIgnoreCase("success")) {
                                Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Error Please Try Again", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RegisterActivity.this, "Something Error at Register()", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onErrorResponse: " + error);
                        error.printStackTrace();
                    }
                });
        requestQueue.add(request);

    }
}
