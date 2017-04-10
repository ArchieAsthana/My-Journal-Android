package com.example.chana.journal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText usernameEditText;
    private EditText passEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameEditText = (EditText) findViewById(R.id.UsernameEditText);
        passEditText = (EditText) findViewById(R.id.passEditText);
        loginButton = (Button) findViewById(R.id.btLogin);
        loginButton.setOnClickListener(this);
    }

    public void onClick(View v) {

        if (validateLogin()) {
            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, Main2Activity.class);
            startActivity(intent);
        }
    }

    public boolean validateLogin() {
        if (usernameEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(MainActivity.this, "Incorrect Username", Toast.LENGTH_SHORT).show();
            return false;
        } else if (passEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(MainActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}