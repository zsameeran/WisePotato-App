package com.wisepotato.wp_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class savedPostadapter extends ArrayAdapter {

    List<savedPostsModel> postsModelList = new ArrayList<>();
    Context context;
    private boolean isDeletable =  true;
    final String POST_IMAGE_DIR = "http://3hundredsolutions.tech/Wisepotato%20App/Post_Images/";

    public savedPostadapter(@NonNull Context context, @NonNull List<savedPostsModel> objects , boolean isDeletable) {
        super(context, R.layout.saved_post_item,objects);

        this.context = context;
        this.isDeletable = isDeletable;
        this.postsModelList = objects;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.saved_post_item ,parent , false);

        ImageView postThumb = (ImageView)view.findViewById(R.id.ivPostThumbnail);
        TextView postTitle = (TextView) view.findViewById(R.id.cardPostTitle);
        TextView postAuthor = (TextView) view.findViewById(R.id.cardPostAuthor);
        TextView upvotes  =(TextView) view.findViewById(R.id.postUpvotes);

        ImageView postDelete = (ImageView) view.findViewById(R.id.postDeleteBtn);

        RequestOptions myOptions = new RequestOptions()
                .override(250, 400);

        Glide.with(context).asBitmap().apply(myOptions).load(POST_IMAGE_DIR+postsModelList.get(position).getThumbnailPath()).into(postThumb);
        postTitle.setText(postsModelList.get(position).getPostTitle());
        postAuthor.setText(postsModelList.get(position).getPostAuthor());
        upvotes.setText("Upvotes: "+postsModelList.get(position).getPostsUpvotes());

        if(isDeletable) {
            postDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    serverManager sManager = new serverManager(context);
                    sManager.deletePostFromMyAccount(postsModelList.get(position).getPostNodeId(), new serverManager.VolleyResponseListener() {
                        @Override
                        public void onError(String message) {

                        }

                        @Override
                        public void onResponse(String response) {
                            if (response.trim().equals("success")) {

                                Toast.makeText(context, "Post Deleted Successfully", Toast.LENGTH_SHORT).show();
                                postsModelList.remove(position);
                                notifyDataSetChanged();
                            }
                            else if (response.trim().equals("failure")) {

                                Toast.makeText(context, "Failed in Deleting Post", Toast.LENGTH_SHORT).show();
                            }
                            else if (response.trim().equals("noexists")) {

                                Toast.makeText(context, "Post Does Not Exists", Toast.LENGTH_SHORT).show();
                            }



                        }
                    });
                }
            });

        }
        else
        {
            postDelete.setVisibility(View.GONE);
        }
        return view;
    }
}

