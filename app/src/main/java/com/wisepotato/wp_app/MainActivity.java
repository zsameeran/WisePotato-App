package com.wisepotato.wp_app;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;


import android.os.Bundle;

import android.util.Log;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startupCheck();

    }
    public void startupCheck(){

        if(SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn())

        {
            Log.d("main activity", "startupCheck: "+SharedPrefManager.getInstance(MainActivity.this).isAgeEntered());

            if(!SharedPrefManager.getInstance(getApplicationContext()).isAgeEntered())
            {
                finish();
                Log.d("main actti", "startupCheck: "+"ikadchya if madhye");
                Intent Homeint = new Intent(getApplicationContext(), Age_input_activity.class);
                Homeint.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Homeint.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Homeint);
            }
            else if (SharedPrefManager.getInstance(getApplicationContext()).isInterestAdded())
            {
                finish();
                Intent Homeintent = new Intent(getApplicationContext(), Home.class);
                Homeintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity(Homeintent);

            }
            else
            {
                finish();
                Intent addInterestintent = new Intent(getApplicationContext(), multiple_selection.class);
                addInterestintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity(addInterestintent);
            }
        }

        else
        {
            finish();
            Intent loginintent = new Intent(getApplicationContext(), Login.class);
            loginintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity(loginintent);
        }



    }





}