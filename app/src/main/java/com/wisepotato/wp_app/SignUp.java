package com.wisepotato.wp_app;


import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


public class SignUp extends AppCompatActivity {
    EditText  editTextUsername, editTextEmail, editTextPassword;
    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        progressBar = findViewById(R.id.progressBar);

        serverManager serverManager = new serverManager(getApplicationContext());

        //if the user is already logged in we will directly start the Home (profile) activity
        if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {

            if (SharedPrefManager.getInstance(getApplicationContext()).isInterestAdded()) {
                startActivity(new Intent(this, Home.class));
                finish();
                return;
            } else {
                finish();
                startActivity(new Intent(this, multiple_selection.class));
            }
        }



        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);


        findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on button register
                //here we will register the user to server
                final String username = editTextUsername.getText().toString().trim();
                final String email = editTextEmail.getText().toString().trim();
                final String password = editTextPassword.getText().toString().trim();


                //first we will do the validations

                if (TextUtils.isEmpty(username)) {
                    editTextUsername.setError("Please enter username");
                    editTextUsername.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    editTextEmail.setError("Please enter your email");
                    editTextEmail.requestFocus();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editTextEmail.setError("Enter a valid email");
                    editTextEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Enter a password");
                    editTextPassword.requestFocus();
                    return;
                }

                serverManager.signup(username,email,password,0,new serverManager.VolleyResponseListener(){
                    public void onResponse(String response) {
                        if(response.trim().equals("success")) {
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(getApplicationContext(), Age_input_activity.class);
                            startActivity(intent);
                            finish();
                        }
                        if(response.trim().equals("failure")){
                            Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                        } }
                    public void onError(String message) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }

                });

            }



        });
        findViewById(R.id.textViewLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on textview that already register open LoginActivity
                finish();
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });


    }


}