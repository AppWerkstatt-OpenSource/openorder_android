package com.example.tobias.openorder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Log In");
    }

    public void click_LogIn(View view) {
/*
        EditText username = (EditText)findViewById(R.id.edittext_username);
        EditText password = (EditText)findViewById(R.id.edittext_password);

        final String URL = "http://192.168.0.31:3000/api/Users/login";

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", "test@tobias.at");
        params.put("password", "tobias");

        JsonObjectRequest req = new JsonObjectRequest(URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String token = "";
                        try {
                            token = response.getString("id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (token.length() == 0) {
                            // User not logged in. --> Show error message.
                            Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_LONG).show();
                        } else {
                            // User is logged in. --> ok.
                            Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                            startActivity(i);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        Volley.newRequestQueue(this).add(req);
*/
        Intent i = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(i);
    }
}
