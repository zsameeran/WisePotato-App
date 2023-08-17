package com.wisepotato.wp_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class addPostActivity extends AppCompatActivity {
    private static final String URL_Intrests = "http://www.3hundredsolutions.tech/Wisepotato%20App/Posts/intrest.php";
    private TextView wordCounter;
    private Spinner etDomainName;
    private EditText etTitle  , etExplainationName
            ,editTag1 , editTag2 , editTag3 , editTag4  ;
    private Button submitButton;
    private static int spaceCount = 50;
    private InputFilter filter;
    public static final String TAG_NAME = "interests";
    private ArrayList<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        customLoader customLoader = new customLoader(this);
        etTitle  = findViewById(R.id.etConceptTitle);
        etDomainName = (Spinner)findViewById(R.id.etDomainSpinner);
        etExplainationName = findViewById(R.id.etExplaination);
        editTag1 = findViewById(R.id.etTag1);
        editTag2 = findViewById(R.id.etTag2);
        editTag3 = findViewById(R.id.etTag3);
        editTag4 = findViewById(R.id.etTag4);
        wordCounter = findViewById(R.id.counterNum);
        submitButton = findViewById(R.id.formBtn);
        ImageButton backButton=findViewById(R.id.backBtn);
        names =  new ArrayList<String>();
        names.add("Select Domain");

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL_Intrests, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //Looping through all the elements of json array
                for(int i = 0; i<response.length(); i++){
                    //Creating a json object of the current index
                    JSONObject obj = null;
                    try {
                        //getting json object from current index
                        obj = response.getJSONObject(i);
                        names.add(obj.getString(TAG_NAME));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(addPostActivity.this, "Something went Wrong..", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding our request to the queue
        requestQueue.add(jsonArrayRequest);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(addPostActivity.this, android.R.layout.simple_spinner_dropdown_item, names){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }

            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etDomainName.setAdapter(adapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( getApplicationContext(), Home.class ));
            }
        });
        etExplainationName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                String text = etExplainationName.getText().toString();
                if(text.isEmpty())
                {
                    wordCounter.setText("50");
                }
                else {
                    text = text.replace("\n", " ");
                    String textList[] = text.split("\\s+");
                    wordCounter.setText("" + (spaceCount - textList.length));
                    if(spaceCount - textList.length == -1)
                    {

                        filter = new InputFilter.LengthFilter(text.length()-1);
                        etExplainationName.setFilters(new InputFilter[] { filter });
                    }
                    else if(spaceCount - textList.length > 0)
                    {
                        if(filter != null)
                        {
                            etExplainationName.setFilters(new InputFilter[0]);
                            filter = null;

                        }
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customLoader.startLoaderDialog();

                if(!etTitle.getText().toString().trim().isEmpty() && !etDomainName.getSelectedItem().toString().trim().isEmpty() &&
                        !etExplainationName.getText().toString().isEmpty())
                {


                    String postTitle =  etTitle.getText().toString().trim();
                    String postDomain = etDomainName.getSelectedItem().toString().trim();
                    String postData = etExplainationName.getText().toString();
                    String postTag1 = editTag1.getText().toString().trim();
                    String postTag2 = editTag2.getText().toString().trim();
                    String postTag3 = editTag3.getText().toString().trim();
                    String postTag4 = editTag4.getText().toString().trim();

                    User  user = SharedPrefManager.getInstance(addPostActivity.this).getUser();
                    serverManager serverManager = new serverManager(addPostActivity.this);

                    serverManager.formEntryRegistration(Integer.toString(user.getId()), postTitle, postDomain, postData, postTag1, postTag2, postTag3
                            , postTag4, new serverManager.VolleyResponseListener() {
                                @Override
                                public void onError(String message) {
                                    Toast.makeText(addPostActivity.this, message, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onResponse(String response) {


                                    Log.d("ADD POST ", "onResponse: "+response);
                                    if(response.trim().equals("success"))
                                    {
                                        customLoader.dismissLoaderDialog();
                                        Toast.makeText(addPostActivity.this, "Post Content submitted", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                    else if(response.trim().equals("failure"))
                                    {
                                        Toast.makeText(addPostActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });

                }

                else
                {
                    customLoader.dismissLoaderDialog();
                    Toast.makeText(addPostActivity.this, "PLEASE ENTER ALL FIELDS", Toast.LENGTH_SHORT).show();
                }

            }
        });










    }
}