package com.example.travelmalaysia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText mEmail = findViewById(R.id.register_et_email);
        EditText mPassword = findViewById(R.id.register_et_password);
        Button mSubmit = findViewById(R.id.register_et_submit);
        Button mRegister = findViewById(R.id.login_btn_register);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:: implement login function
                Toast.makeText(LoginActivity.this, "I have no check, just give u login", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
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
}
