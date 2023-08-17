package com.wisepotato.wp_app;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.ProgressBar;

import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;



public class Login extends AppCompatActivity {

    EditText etName, etPassword;

    ProgressBar progressBar;
    ImageView signInButton;
    public Context context;
    boolean backPressedTimes = false;
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {
            Toast.makeText(this, "User Already " +
                    "logged in", Toast.LENGTH_SHORT).show();
            if(SharedPrefManager.getInstance(this).isInterestAdded())
            {
                startActivity(new Intent(getApplicationContext() ,Home.class));
            }
            else
                startActivity(new Intent(getApplicationContext(), multiple_selection.class));

        }

        progressBar = findViewById(R.id.progressBar);
        etName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etUserPassword);
        signInButton = findViewById(R.id.signGooglebtn);



        //calling the method userLogin() for login the user
        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin(etName.getText().toString() ,etPassword.getText().toString());
            }
        });

        //if user presses on textview not register calling RegisterActivity
        findViewById(R.id.tvRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

                startActivity(new Intent(getApplicationContext(), SignUp.class));
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestProfile().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this , gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent googleIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(googleIntent , 2 );

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data );
        if(requestCode == 2)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            progressBar.setVisibility(View.VISIBLE);
            handleSignInResults(task);

        }

    }

    private void handleSignInResults(Task<GoogleSignInAccount> Completedtask) {

        GoogleSignInAccount account = null;

        try {

            account = Completedtask.getResult(ApiException.class);
            Log.d("LOGINNN", "handleSignInResults: "+account.getDisplayName()+" "+account.getEmail());
            new serverManager(getApplicationContext()).signup(account.getDisplayName().toLowerCase().trim(), account.getEmail().trim(), account.getId(),1, new serverManager.VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onResponse(String response) {

                    Log.d("GOOGLE CHA RESULT", "onResponse: "+response);
                    if(response.trim().equals("success")) {
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(getApplicationContext(),Age_input_activity.class);
                        startActivity(intent);
                        finish();

                    }
                    if(response.trim().equals("failure")){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "can't login right now", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (ApiException e) {
            e.printStackTrace();
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Opps.. Something Went Wrong", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {

        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account !=  null && SharedPrefManager.getInstance(context).isLoggedIn() )
        {
            startActivity(new Intent(getApplicationContext() ,Home.class));
        }
        if(account != null && !SharedPrefManager.getInstance(context).isLoggedIn())
        {
            User user = new User(1 , account.getDisplayName() , account.getEmail(),0);
            SharedPrefManager.getInstance(context).userLogin(user);
        }
    }

    private void userLogin(String userName , String pass) {
        //first getting the values
        progressBar.setVisibility(View.VISIBLE);
        //validating inputs
        if (TextUtils.isEmpty(userName)) {
            etName.setError("Please enter your username");
            etName.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (TextUtils.isEmpty(pass)) {
            etPassword.setError("Please enter your password");
            etPassword.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {


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

                                if(SharedPrefManager.getInstance(context).isInterestAdded())
                                {
                                    progressBar.setVisibility(View.GONE);

                                    startActivity(new Intent(getApplicationContext() ,Home.class));

                                }
                                else {
                                 progressBar.setVisibility(View.GONE);
                                    startActivity(new Intent(getApplicationContext(), multiple_selection.class));

                                }
                                // Intent intent = new Intent(getApplicationContext(), multiple_selection.class);
                                //startActivity(intent);
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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", userName);
                params.put("password", pass);
                params.put("email",Integer.toString(0));
                params.put("signInWithGoogle",Integer.toString(1));
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    @Override
    public void onBackPressed() {
        if(backPressedTimes)
        {
            finish();
            super.onBackPressed();
            return;
        }
        backPressedTimes = true;
        Toast.makeText(this, "Press Back Again To Exit", Toast.LENGTH_SHORT).show();
         new Handler().postDelayed(new Runnable() {
             @Override
             public void run() {
                    backPressedTimes = false;
             }
         } , 2000);
    }
}