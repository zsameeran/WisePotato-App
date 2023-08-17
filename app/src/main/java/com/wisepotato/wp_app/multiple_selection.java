package com.wisepotato.wp_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
;
import java.util.Map;

public class multiple_selection extends AppCompatActivity {
    private static final String URL_Intrests = "http://www.3hundredsolutions.tech/Wisepotato%20App/Posts/intrest.php";
    private GridView gridView;
    private ImageButton btn;
    private ArrayList<String> selectedStrings;
    private ArrayList<String> selectedIDS;
    private String finalstr;
    private String finalIds;
    private int userID;
    private ProgressDialog loading;
    boolean backmultiplebtnpressed = false;

    private ArrayList<Integer> selectedInterests  = new ArrayList<>();

    public static final String TAG_NAME = "interests";
    public static final String TAG_ID="interestsIds";
    private ArrayList<String> names ,IDS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiple_selection);

        gridView = (GridView) findViewById(R.id.datagrid);
        btn=findViewById(R.id.arrow);
        names = new ArrayList<>();
        IDS= new ArrayList<>();
        loading = ProgressDialog.show(this, "Please wait...","Fetching data...",false,false);


        selectedStrings = new ArrayList<>();
        selectedIDS = new ArrayList<>();


        // checking if we have user interests already stored on databse
        if(!SharedPrefManager.getInstance(getApplicationContext()).isInterestAdded()) {

            serverManager serverManager = new serverManager(multiple_selection.this);
            serverManager.checkForInterests(SharedPrefManager.getInstance(multiple_selection.this).getUser().getId(), new serverManager.VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    Toast.makeText(multiple_selection.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.getString("interestsExists").trim().equals("yes")) {
                            SharedPrefManager.getInstance(multiple_selection.this).addInterest(obj.getJSONObject("userInterests").getString("InterestsIds"));
                            Intent forwardHomeIntent = new Intent(getApplicationContext(), Home.class);
                            forwardHomeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(forwardHomeIntent);

                        } else if (obj.getString("interestsExists").trim().equals("no")) {

                            initializeGrid();

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
        }
        else
        {
            Intent forwardHomeIntent = new Intent(getApplicationContext(), Home.class);
            forwardHomeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(forwardHomeIntent);
        }
        
        
        

    }
    


    private void initializeGrid()
    {

        if(!SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn())
        {

            Toast.makeText(this, "PLEASE LOGIN FIRST TO AD INTERESTS!", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent backLoginIntent = new Intent(getApplicationContext() , Login.class);
                    backLoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(backLoginIntent);
                }
            } ,500);
        }
        else {
            userID = SharedPrefManager.getInstance(getApplicationContext()).getUser().getId();
        }



        //Creating a json array request to get the json from our api
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL_Intrests, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //Dismissing the progressdialog on response
               loading.dismiss();
                //Displaying our grid
                showGrid(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(multiple_selection.this, "Something went Wrong..", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding our request to the queue
        requestQueue.add(jsonArrayRequest);

    }






    private void showGrid(JSONArray jsonArray){
        //Looping through all the elements of json array
        for(int i = 0; i<jsonArray.length(); i++){
            //Creating a json object of the current index
            JSONObject obj = null;
            try {
                //getting json object from current index
                obj = jsonArray.getJSONObject(i);
                names.add(obj.getString(TAG_NAME));
                IDS.add(obj.getString(TAG_ID));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Creating GridViewAdapter Object
        final GridViewAdapter adapter = new GridViewAdapter(names, IDS, multiple_selection.this);

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                int selectedIndex = adapter.selectedPositions.indexOf(position);

                // these arrray elemenst also have to be removed whne user again click the same interests or deselect it

                if (selectedIndex > -1) {
                    adapter.selectedPositions.remove(selectedIndex);
                    ((GridItemView) v).display(false);
                    selectedStrings.remove((String) parent.getItemAtPosition(position));
                    selectedIDS.remove((String)adapter.getInterestId(position));
                   // Log.d("Intrest ID string after removal ", "see: " + selectedIDS);


                } else {
                    adapter.selectedPositions.add(position);
                    ((GridItemView) v).display(true);
                    selectedStrings.add((String) parent.getItemAtPosition(position));
                    selectedIDS.add((String)adapter.getInterestId(position));
                    //Log.d("Intrest ID string after adding ", "see: " + selectedIDS);
                }
                assert selectedStrings != null;
                assert selectedIDS!=null;
                StringBuffer resultInterest = new StringBuffer();
                StringBuffer InterestIds=new StringBuffer();
                if (selectedStrings.size() > 0) {

                            for (String s : selectedStrings) {
                                resultInterest.append(s);
                                resultInterest.append(",");
                            }
                    finalstr = resultInterest.toString();
                    finalstr = finalstr.substring(0, finalstr.length() - 1);
                }
                if (selectedIDS.size() > 0) {

                    for (String s : selectedIDS) {
                        InterestIds.append(s);
                        InterestIds.append(",");
                    }
                    finalIds = InterestIds.toString();
                    finalIds = finalIds.substring(0, finalIds.length() - 1);
                }

                btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(multiple_selection.this , "Please Wait.." ,"" ,true , false );

                if (selectedStrings.size() != 0) {
                    final String URL = "http://www.3hundredsolutions.tech/Wisepotato%20App/Intrests/save_intrest.php";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response", "onResponse: " +response);
                            if (response.contains("success")) {


                                SharedPrefManager.getInstance(getApplicationContext()).addInterest(finalIds);
                                Intent finalhomeintent = new Intent(getApplicationContext(), Home.class);

                                finalhomeintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                loading.dismiss();
                                startActivity(new Intent(getApplicationContext(), Home.class));

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("userID", Integer.toString(userID));
                            params.put("user_intrest", finalstr);
                            params.put("InterestIDS",finalIds);


                            return params;

                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(multiple_selection.this);
                    requestQueue.add(stringRequest);

                }
                else{
                    Toast.makeText(getApplicationContext(), "Please Select Interests", Toast.LENGTH_SHORT).show();
                }
            }
        });
            }
        });
            }


    @Override
    public void onBackPressed() {
        if(backmultiplebtnpressed)
        {
            finishAffinity();
            super.onBackPressed();
            return;
        }
        backmultiplebtnpressed =true;
        Toast.makeText(this, "Press Again to Exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backmultiplebtnpressed = false;
            }
        } , 2000);
    }
}











