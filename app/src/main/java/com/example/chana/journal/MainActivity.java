package com.example.chana.journal;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText Username;
    private EditText passEditText;
    private Button Login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Username = (EditText) findViewById(R.id.UsernameEditText);
        passEditText = (EditText) findViewById(R.id.passEditText);
        Login = (Button) findViewById(R.id.btLogin);
        Login.setOnClickListener(this);
        addListenerOnButton();
    }
    public void addListenerOnButton() {

        final Context context = this;

        Login = (Button) findViewById(R.id.btLogin);

        Login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Main2Activity.class);
                startActivity(intent);

            }
        });
    }

    public void onClick(View v){

        if(!validate()) {
            Toast.makeText(MainActivity.this,"SignIn Successful",Toast.LENGTH_LONG).show();
        }
    }

    public boolean validate() {
        if (Username.getText().toString().trim().length() <= 0) {

            Toast.makeText(MainActivity.this,"Please Enter Username",Toast.LENGTH_SHORT).show();
            return true;
        } else if (passEditText.getText().toString().trim().length() <= 0) {

            Toast.makeText(MainActivity.this,"Please Enter Password",Toast.LENGTH_SHORT).show();

            return true;

        }
        return false;
    }
}