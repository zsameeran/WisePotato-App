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


public class queryResultAdaptor extends ArrayAdapter {

    List<queryResultModel> resultModelList = new ArrayList<>();
    Context context;

    final String POST_IMAGE_DIR = "http://3hundredsolutions.tech/Wisepotato%20App/Post_Images/";

    public queryResultAdaptor(@NonNull Context context, @NonNull List<queryResultModel> objects ) {
        super(context, R.layout.saved_post_item,objects);

        this.context = context;

        this.resultModelList = objects;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.query_result_item ,parent , false);

        ImageView postThumb = (ImageView)view.findViewById(R.id.resultThumb);
        TextView postTitle = (TextView) view.findViewById(R.id.resultItemTitle);
        TextView postAuthor = (TextView) view.findViewById(R.id.resultItemAuthor);


        RequestOptions myOptions = new RequestOptions()
                .override(60, 90);

        Glide.with(context).asBitmap().apply(myOptions).load(POST_IMAGE_DIR+resultModelList.get(position).getThumbnailPath()).into(postThumb);
        postTitle.setText(resultModelList.get(position).getPostTitle());
        postAuthor.setText("@"+resultModelList.get(position).getPostAuthor());


        return view;
    }
}

