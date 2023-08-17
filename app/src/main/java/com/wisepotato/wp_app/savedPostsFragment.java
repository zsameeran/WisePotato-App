package com.wisepotato.wp_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class savedPostsFragment extends Fragment {

    ListView savedPostsListView;
    ProgressBar contentLoader;

    List<savedPostsModel> PostsShowList = new ArrayList<>();
    TextView noPostsMsg;
    final String POST_IMAGE_DIR = "http://3hundredsolutions.tech/Wisepotato%20App/Post_Images/";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_saved_posts, container, false);

        savedPostsListView = (ListView) view.findViewById(R.id.savedListView);
        noPostsMsg = (TextView) view.findViewById(R.id.noPostsMsg);
        contentLoader = (ProgressBar) view.findViewById(R.id.savedPostsLoader);

        contentLoader.setVisibility(View.VISIBLE);
        serverManager smanager = new serverManager(getActivity());



        smanager.getSavedPosts(getActivity(), new serverManager.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                    contentLoader.setVisibility(View.GONE);
                    savedPostsListView.setVisibility(View.GONE);
                    noPostsMsg.setVisibility(View.VISIBLE);
                    noPostsMsg.setText("Content Loading Failed");

            }

            @Override
            public void onResponse(String response) {



                 try {
                     JSONArray arr = new JSONArray(response.trim());

                     if(!arr.isNull(0)) {
                         Log.d("selmon boi", "onResponse: " + arr.isNull(0));
                         for (int i = 0; i < arr.length(); i++) {
                             JSONObject obj = arr.getJSONObject(i);

                             savedPostsModel savedPostsModel = new savedPostsModel(obj.getString("ImageName"), obj.getString("PostTitle")
                                     , obj.getString("Author"), obj.getInt("PostUpvotes") , obj.getInt("NodeId"));

                             PostsShowList.add(savedPostsModel);
                         }

                         savedPostadapter saveAdaptor = new savedPostadapter(getActivity(), PostsShowList , true);
                         contentLoader.setVisibility(View.GONE);
                         savedPostsListView.setAdapter(saveAdaptor);
                     }

                     else{

                         contentLoader.setVisibility(View.GONE);
                         savedPostsListView.setVisibility(View.GONE);
                         noPostsMsg.setVisibility(View.VISIBLE);

                     }

                 } catch (JSONException e) {
                     e.printStackTrace();
                 }

            }
        });

        savedPostsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent ii=new Intent(getActivity(), Home.class);
                ii.putExtra("postId", PostsShowList.get(position).getPostNodeId());
                getActivity().finish();
                startActivity(ii);

            }
        });
        return view;
    }
}