package com.wisepotato.wp_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;


public class myProfileAcitivity extends AppCompatActivity {
    ImageButton backbtn;
    ImageView profileImage;
    TextView displayUserName , author;
    private final int PICK_IMAGE_REQUEST = 71;
    private String selectedPicture=  "";
    serverManager sManager;
    int userId =  SharedPrefManager.getInstance(myProfileAcitivity.this).getUser().getId();
    AppBarLayout topBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_acitivity);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 tabPager = findViewById(R.id.tabViewPager);
        topBarLayout = findViewById(R.id.topBackground);
        backbtn=findViewById(R.id.backBtn);
        sManager = new serverManager(myProfileAcitivity.this);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent( getApplicationContext(), Home.class ));
            }
        });
        tabPager.setAdapter(new tabLayoutPagerAdaptor(this));
        User user = SharedPrefManager.getInstance(myProfileAcitivity.this).getUser();
        String userName = user.getUsername();

        String firstChar  = userName.trim().toUpperCase().substring(0 , 1);
        Toast.makeText(this, firstChar, Toast.LENGTH_SHORT).show();
        TextDrawable textDrawable = TextDrawable.builder().buildRound(firstChar , getColor(R.color.Primary));

        profileImage = findViewById(R.id.profileImageCircle);
        profileImage.setImageDrawable(textDrawable);

        displayUserName = findViewById(R.id.userFullName);
        author = findViewById(R.id.userAuthorName);
        displayUserName.setText(userName);
        author.setText("@"+userName.toLowerCase());

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });


         sManager.FetchProfilePic(userId, new serverManager.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                //Toast.makeText(myProfileAcitivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {

                try {

                    JSONObject obj = new JSONObject(response);
                    String result = obj.getString("response");
                    if( result.equals("sucess")){
                        String fileName = obj.getString("path");
                        if(!fileName.equals("")){
                            Glide.with(myProfileAcitivity.this).asBitmap().load(URLs.URL_ProfilePicDir+fileName).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).circleCrop().into(profileImage);
                            int colorFrom = getResources().getColor(R.color.white);
                            int colorTo = getResources().getColor(R.color.primaryLight);
                            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                            colorAnimation.setDuration(450); // milliseconds
                            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                                @Override
                                public void onAnimationUpdate(ValueAnimator animator) {
                                    topBarLayout.setBackgroundResource(R.mipmap.profileback);



                                }

                            });
                            colorAnimation.start();



                        }
                    }
                    else {
                        Toast.makeText(myProfileAcitivity.this, "Something went wrong..", Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(myProfileAcitivity.this, "Something went wrong..", Toast.LENGTH_SHORT).show();
                }

            }
        });




        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, tabPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                switch (position){
                    case 0: {
                        tab.setText("SAVED POSTS");
                        break;
                    }
                    case 1:
                    {
                        tab.setText("MY POSTS");
                        break;
                    }

                }
            }
        });
        tabLayoutMediator.attach();



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Bitmap reducedBitmap = imageResizer.reduceBitmapSize(bitmap , 40000);


                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                reducedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                selectedPicture = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                sManager.updateProfilePicture(userId, selectedPicture, new serverManager.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(myProfileAcitivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(myProfileAcitivity.this, response, Toast.LENGTH_SHORT).show();
                        Glide.with(myProfileAcitivity.this).asBitmap().load(reducedBitmap).circleCrop().into(profileImage);
                    }
                });


            }catch(Exception e){
                Toast.makeText(myProfileAcitivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();

            }


        }

    }















}