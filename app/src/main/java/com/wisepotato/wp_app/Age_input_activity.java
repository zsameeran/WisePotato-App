package com.wisepotato.wp_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class Age_input_activity extends AppCompatActivity {
    EditText Age;
    int key_id;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_input_activity);
        Age=findViewById(R.id.AgeInput);
        progressBar = findViewById(R.id.ageProgress);
        Log.d("kai ata", "onCreate: "+ "acitbity madhy alai");
        if (SharedPrefManager.getInstance(getApplicationContext()).isAgeEntered()) {
                startActivity(new Intent(getApplicationContext() ,multiple_selection.class));
            Log.d("kai ata", "onCreate: "+"if madhye pan alai bapooo");
        }


        key_id = SharedPrefManager.getInstance(getApplicationContext()).getUser().getId();
        findViewById(R.id.Age_Next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                final String userAge = Age.getText().toString().trim();
                if (TextUtils.isEmpty(userAge)) {
                    Age.setError("Please enter Age");
                    Age.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
               int IntAge=Integer.parseInt(userAge);
                AddAge(IntAge , key_id);



            }
        });
    }
public void AddAge(int Age ,int key_id){
    //if everything is fine
    StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_userAge,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(response);
                        if (!obj.getBoolean("error")) {

                           // Log.d(TAG, "ithe alai : ");
                            //getting the user from the response
                            JSONObject userJson = obj.getJSONObject("user");

                            //creating a new user object
                            User user = new User(
                                    userJson.getInt("id"),
                                    userJson.getString("username"),
                                    userJson.getString("email"),
                                    userJson.getInt("Age")


                            );


                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                            //starting the profile activity

                            if(SharedPrefManager.getInstance(Age_input_activity.this).isInterestAdded())
                            {
                                startActivity(new Intent(Age_input_activity.this ,Home.class));

                            }
                            else {

                                startActivity(new Intent(Age_input_activity.this, multiple_selection.class));

                            }

                            finish();

                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
        }
    }) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<>();
            params.put("Age", Integer.toString(Age));
            params.put("KEY", Integer.toString(key_id));
            return params;

        }
    };

    RequestQueue requestQueue = Volley.newRequestQueue(Age_input_activity.this);
    requestQueue.add(stringRequest);
    }



}
