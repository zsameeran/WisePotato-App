package com.wisepotato.wp_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class searchActivity extends AppCompatActivity {

    private EditText etSearchQuery;
    private ListView queryResultListView;
    private TextView noResultsFound;
    List<queryResultModel> toShowResultList = new ArrayList<>();
    queryResultAdaptor searchAdaptor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        noResultsFound = findViewById(R.id.notFoundMsg);
        etSearchQuery = findViewById(R.id.etSearchQuery);
        queryResultListView = findViewById(R.id.resultList);
        serverManager smanager = new serverManager(searchActivity.this);

        noResultsFound.setVisibility(View.GONE);

        queryResultListView.setAdapter(searchAdaptor); ;
        etSearchQuery.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if((event.getAction() == KeyEvent.ACTION_DOWN)  && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && !etSearchQuery.getText().toString().trim().isEmpty()){

               if(!toShowResultList.isEmpty()){

                   toShowResultList.clear();
               }

                smanager.getQueryResults(etSearchQuery.getText().toString().trim(), new serverManager.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(searchActivity.this, "Search Failed..", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response.trim());

                            if(!arr.isNull(0)) {

                                for (int i = 0; i < arr.length(); i++) {

                                    JSONObject obj = arr.getJSONObject(i);

                                    queryResultModel queryResultModel = new queryResultModel(obj.getInt("nodeid") , obj.getString("image_path") , obj.getString("post_title") , obj.getString("author"));

                                    toShowResultList.add(queryResultModel);
                                }

                                noResultsFound.setVisibility(View.GONE);
                                queryResultListView.setVisibility(View.VISIBLE);
                                queryResultAdaptor searchAdaptor = new queryResultAdaptor(searchActivity.this, toShowResultList );
                               queryResultListView.setAdapter(searchAdaptor);
                            }

                            else{

                                noResultsFound.setVisibility(View.VISIBLE);
                                queryResultListView.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                    return true;
                }
                return false;
            }
        });

    queryResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent ii=new Intent(searchActivity.this, Home.class);
            ii.putExtra("postId", toShowResultList.get(position).getNodeId());
            searchActivity.this.finish();
            startActivity(ii);

        }
    });

    }

}