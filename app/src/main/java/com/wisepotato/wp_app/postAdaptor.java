package com.wisepotato.wp_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.drawable.Drawable;

import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;


import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class postAdaptor extends RecyclerView.Adapter<postAdaptor.MyViewHolder> {

    private MyViewHolder localHolder;
    private ViewPager2 vp2;
    private Context mContext;
    Home home = new Home();
    public void setEndCount(int endCount) {
        this.endCount = endCount;
    }

    private int endCount = 0;
    int flag=0;

    private Animation Anim_Alpha;

 

    public static boolean endPost =  false;

    private int positon = 0;
    final String POST_IMAGE_DIR = "http://3hundredsolutions.tech/Wisepotato%20App/Post_Images/";

    private List<modelPost> postList = new ArrayList<>();

    public postAdaptor(Context mContext, List<modelPost> postList , ViewPager2 vp2) {
        this.mContext = mContext;
        this.postList = postList;
        this.vp2 = vp2;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView username, location , postUpvotes , endMessage ;
        private ImageView postImage , upvoteBtn ,shareButton , menuPopup,saveBtn;

        private LinearLayout mContainer , endMsgContainer;
        private CardView postcard;

        public MyViewHolder (View view){
            super(view);

            username = view.findViewById(R.id.postUsername);
            postImage = view.findViewById(R.id.PostImage);
            location = view.findViewById(R.id.postLocation);
            postUpvotes = view.findViewById(R.id.displayUpvotes);
            upvoteBtn = view.findViewById(R.id.upVote);
            mContainer = view.findViewById(R.id.productContainer);
            shareButton = view.findViewById(R.id.shareBtn);
            saveBtn=view.findViewById(R.id.saveButton);
            endMsgContainer = view.findViewById(R.id.endMessageContainer);
            postcard  =view.findViewById(R.id.result_cv);

            Anim_Alpha = AnimationUtils.loadAnimation(mContext,R.anim.share_anim);

            upvoteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tempPosition;
                    v.startAnimation(Anim_Alpha);
                    serverManager serverManager = new serverManager(mContext);
                    positon = vp2.getCurrentItem();
                    Log.d(TAG, "onClick: " + positon);
                    tempPosition = positon;

                    serverManager.addUpvoteToDatabase(postList.get(positon).getNodeId(), flag, new serverManager.VolleyResponseListener() {
                        @Override
                        public void onError(String message) {
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        }


                        @Override
                        public void onResponse(String response) {

                            if (response.trim().equals("success")) {
                                switch (flag) {

                                    case 0: {
                                        upvoteBtn.setImageResource(R.drawable.ic_outline_upgrade_24_clicked);

                                        if (postList.get(tempPosition).getPostUpvotes() > 0) {
                                            postUpvotes.setText(Integer.toString(postList.get(tempPosition).getPostUpvotes() + 1));
                                        }
                                        postList.get(tempPosition).setPostUpvotes(postList.get(tempPosition).getPostUpvotes() + 1);
                                        flag = 1;
                                        notifyDataSetChanged();
                                        break;
                                        //upvote added succesfully
                                    }
                                    case 1: {
                                        if (postList.get(tempPosition).getPostUpvotes() > 0) {
                                            upvoteBtn.setImageResource(R.drawable.ic_outline_upgrade_24);
                                            if (postList.get(tempPosition).getPostUpvotes() > 0) {
                                                postUpvotes.setText(Integer.toString(postList.get(tempPosition).getPostUpvotes() - 1));
                                            }
                                            postList.get(tempPosition).setPostUpvotes(postList.get(tempPosition).getPostUpvotes() - 1);
                                            flag = 0;
                                            notifyDataSetChanged();
                                            break;

                                        }

                                    }
                                }
                            }
                            if (response.trim().equals("failure")) {
                                if (postList.get(positon).getPostUpvotes() > 0) {
                                    postUpvotes.setText(postList.get(positon).getPostUpvotes());
                                }
                                upvoteBtn.setImageResource(R.drawable.ic_outline_upgrade_24);
                                //upvote adding failed
                                Toast.makeText(mContext, "Upvote Failed, Try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
            });

            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // TODO Auto-generated method stub

                    v.startAnimation(Anim_Alpha);
                    Bitmap bitmap = getBitmapFromView(postImage);

                    try {
                        File file = new File(mContext.getExternalCacheDir(), "black.png");
                        FileOutputStream fOut = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                        fOut.flush();
                        fOut.close();
                        file.setReadable(true, false);
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", file));
                        intent.putExtra(Intent.EXTRA_TEXT, postList.get(vp2.getCurrentItem()).getPostTitle() + " | Wise Potato" + "\n" + "Find the app here :  https://www.wisepotato.in/app" );
                        intent.setType("image/png");
                        mContext.startActivity(Intent.createChooser(intent, "Share Post Via"));


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });


        }
    }


    
    
    private Bitmap getBitmapFromView(ImageView postImage) {

    Bitmap returnbitmap = Bitmap.createBitmap(postImage.getWidth() ,postImage.getHeight() , Bitmap.Config.ARGB_8888);

    Canvas canvas = new Canvas(returnbitmap);
    Drawable  bgDrawable = postImage.getBackground();

        if(bgDrawable != null)
        {
            bgDrawable.draw(canvas);
        }
        else{
            canvas.drawColor(Color.BLACK);

        }
        postImage.draw(canvas);
        return returnbitmap;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
            view = LayoutInflater.from(mContext).inflate(R.layout.post_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if(endPost && position + 1 ==postList.size())
        {
            holder.mContainer.setVisibility(View.GONE);
            holder.endMsgContainer.setVisibility(View.VISIBLE);
            holder.postcard.setCardElevation(0);
            holder.postcard.setRadius(0);

        }
        else
        {
            holder.endMsgContainer.setVisibility(View.GONE);
            holder.mContainer.setVisibility(View.VISIBLE);

            final modelPost post = postList.get(position);
            holder.username.setText(post.getPostTitle());
            holder.location.setText("@"+post.getAuthor());

            if(post.getPostUpvotes()>0){
                holder.postUpvotes.setText(Integer.toString(post.getPostUpvotes()));}
            Glide.with(mContext).load(POST_IMAGE_DIR+post.getImageName()).into(holder.postImage);
            holder.saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveCurrentPost(holder);
                }
            });


        }
   




    }



    private void saveCurrentPost(MyViewHolder holder) {

        serverManager serverManager = new serverManager(mContext);
        serverManager.savePostOnServer(postList.get(vp2.getCurrentItem()).getNodeId(), new serverManager.VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(String response) {
                
                if(response.trim().equals("success"))
                {
                    holder.saveBtn.setImageResource(R.drawable.ic_baseline_bookmark_24);
                    Toast.makeText(mContext, "POST SAVED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                }
                else if(response.trim().equals("failure"))
                {
                    
                    Toast.makeText(mContext, "POST SAVING FAILED", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        Log.d("POST ADAPTOR", "getItemCount: "+postList.size()+endCount);
        return postList.size();

    }
}

