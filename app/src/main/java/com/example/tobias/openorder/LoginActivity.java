package com.example.tobias.openorder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Log In");
    }

    public void click_LogIn(View view){
        Intent i = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(i);
    }
}
