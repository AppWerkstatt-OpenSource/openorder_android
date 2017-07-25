package com.example.tobias.openorder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void click_User(View view){
        Toast.makeText(getApplicationContext(),"Diese Funktion ist derzeit leider nicht Verfügbar",Toast.LENGTH_LONG).show();
    }

    public void click_Bestellung(View view){
        Intent i=new Intent(this,TableActivity.class);
        i.putExtra("Bestellen", true);
        startActivity(i);
    }

    public void click_Bezahlen(View view){
        Toast.makeText(getApplicationContext(),"Diese Funktion ist derzeit leider nicht Verfügbar",Toast.LENGTH_LONG).show();
    }

    public void click_Nachricht(View view){
        Toast.makeText(getApplicationContext(),"Diese Funktion ist derzeit leider nicht Verfügbar",Toast.LENGTH_LONG).show();
    }

    public void click_Einstellungen(View view){
        Toast.makeText(getApplicationContext(),"Diese Funktion ist derzeit leider nicht Verfügbar",Toast.LENGTH_LONG).show();
    }

}
