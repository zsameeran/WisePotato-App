package com.wisepotato.wp_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class myPostsFragment extends Fragment {

    ListView myPostsListView ;
    TextView NoPostMessage , addPostLink;
    Context context;
    ProgressBar contentLoader;


    List<savedPostsModel> MyPostsShowList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_posts, container, false);
        context = getActivity();
        NoPostMessage = (TextView) view.findViewById(R.id.noPostsMsg);
        addPostLink = (TextView) view.findViewById(R.id.noPostsMsg2);
        myPostsListView = (ListView)view.findViewById(R.id.myPostsListView);
        contentLoader = (ProgressBar) view.findViewById(R.id.savedPostsLoader);
        contentLoader.setVisibility(View.VISIBLE);


        addPostLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                startActivity(new Intent(getActivity() , addPostActivity.class));
            }
        });

        serverManager serverManager = new serverManager(context);

        serverManager.getMyPosts(context, new serverManager.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                contentLoader.setVisibility(View.GONE);
                myPostsListView.setVisibility(View.GONE);
                NoPostMessage.setText("Content Loading Failed");
            }

            @Override
            public void onResponse(String response) {

                try {

                    JSONArray arr = new JSONArray(response.trim());
                    Log.d("mY POSTS", "onResponse: "+response);
                    if(!arr.isNull(0)) {

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);

                            savedPostsModel savedPostsModel = new savedPostsModel(obj.getString("ImageName"), obj.getString("PostTitle")
                                    , obj.getString("Author"), obj.getInt("PostUpvotes") , obj.getInt("NodeId"));

                            MyPostsShowList.add(savedPostsModel);
                        }
                       // Log.d("FRAGMNT", "onResponse: " + MyPostsShowList.get(0).getPostAuthor());
                        savedPostadapter myPostsAdaptor = new savedPostadapter(context, MyPostsShowList , false);
                        contentLoader.setVisibility(View.GONE);
                        myPostsListView.setAdapter(myPostsAdaptor);

                    }
                    else {
                        contentLoader.setVisibility(View.GONE);
                        myPostsListView.setVisibility(View.GONE);
                        NoPostMessage.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


        return view;
    }
}