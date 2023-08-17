package com.wisepotato.wp_app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;



public class Home extends AppCompatActivity {

    private ViewPager2 viewpager2View;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager manager;
    private ArrayList<modelPost> generatedPostList;
    private ArrayList<Integer> UserInterestsList;
    boolean backPressedTimes = false;
    private ImageView addPostBtn , searchButton;
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Context context;
    private TextView userId,userEmail;
     private View mHeaderView;
    private ProgressBar progressBar;
    public JSONArray viewedPosts;
    public boolean hasAdaptorConfigured = false;


    public ArrayList<Integer> homeNodeArray = new ArrayList<>();
    public ArrayList<Integer> FinalNodeList ;
    private boolean isFirstLot = true;
    private int prevPosition = 0;
    private boolean viewedToday = false;

    GoogleSignInClient mGoogleSignInClient;
    serverManager sManager = new serverManager(Home.this);

    private FirebaseAnalytics firebaseAnalytics;
    private int userAge;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        firebaseAnalytics = FirebaseAnalytics.getInstance(Home.this);
        nav=(NavigationView)findViewById(R.id.navmenu);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer);
        searchButton =(ImageButton) findViewById(R.id.searchBtn);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        addPostBtn = findViewById(R.id.iv_addPost);
        mHeaderView=nav.getHeaderView(0);
        progressBar = findViewById(R.id.contentLoaderProgress);
        userAge = SharedPrefManager.getInstance(Home.this).getUser().getAge();
        userId = (TextView) mHeaderView.findViewById(R.id.userId);

        userId.setText(SharedPrefManager.getInstance(context).getUser().getUsername().toLowerCase());
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this , searchActivity.class));

            }
        });

        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( getApplicationContext(), ContentWithUs.class ));
            }
        });

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {

                    case R.id.menu_logout :

                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestProfile().build();
                        mGoogleSignInClient = GoogleSignIn.getClient(Home.this , gso);
                        mGoogleSignInClient.signOut()
                                .addOnCompleteListener(Home.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        SharedPrefManager.getInstance(context).logout();
                                        startActivity(new Intent(getApplicationContext() ,Login.class));
                                        drawerLayout.closeDrawer(GravityCompat.START);
                                        // ...
                                    }
                                });


                    case R.id.menu_profile :

                        drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(getApplicationContext() , myProfileAcitivity.class));
                        break;

                    case R.id.Terms_condition:
                        Uri uri = Uri.parse("https://wisepotato.in/homepage/terms-of-use/");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);

                    case R.id.privacy:
                        Uri uri1 = Uri.parse("https://wisepotato.in/privacy-policy");
                        Intent intent1 = new Intent(Intent.ACTION_VIEW, uri1);
                        startActivity(intent1);

                }

                return true;
            }
        });


        viewpager2View =findViewById(R.id.Viewpager_view);
        Log.d("VIE PGER TWO VIEWWW", "onCreate: "+viewpager2View);
        manager = new LinearLayoutManager(this);
        viewedPosts = SharedPrefManager.getInstance(Home.this).getViewedPosts();

        generatedPostList = new ArrayList<>();
        UserInterestsList = new ArrayList<>();
        Intent iin= getIntent();
        Bundle customShowSavedPosts = iin.getExtras();

        if(customShowSavedPosts!=null)
        {
            int j =(int) customShowSavedPosts.get("postId");
            initializeCustomShowPost(j);
        }
        else
        {
            startPreChecks();
        }



       // postRecommender pr = new postRecommender();
         //   testarray = pr.getComputedPostList();

    }



    private void startPreChecks() {
        //check for if user is still logged in or not.
        Log.d("HOME CHECKS", "IN FUNCTION...");
        if(!SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn())
        {
            finishAffinity();
            startActivity(new Intent(getApplicationContext() , Login.class));
        }

        //check if user have added his interetss or not. of yes show posts according to his interests otherwise randomly.

        if(SharedPrefManager.getInstance(getApplicationContext()).isInterestAdded())
        {
           preparePersonalizedPosts();

        }
        else
        {
            prepareNonPersonalizedPosts();
        }


    }




    private void endMessage(){
        setContentView(R.layout.end_reached_message);
    }


    private void preparePersonalizedPosts() {

        String usrIntrests = SharedPrefManager.getInstance(Home.this).getInterest();

       // viewedPosts = new JSONArray();
        //TAKE VIEWED POST JASON ARRAY..

        FinalNodeList = new ArrayList<>();
        Log.d("HOMEEEE", "viewddposts"+viewedPosts);

        sManager.getNodeIdsFromInterests(usrIntrests, userAge , viewedPosts, new serverManager.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(Home.this, "SOMETHING WENT WRONG", Toast.LENGTH_SHORT).show();
               // preparePersonalizedPosts();
            }

            @Override
            public void onResponse(String response) {
                Log.d("HOME AGE CHECK", "onResponse: "+response);

                try {
                    JSONObject obj = new JSONObject(response);

                    JSONArray nodeArr = obj.getJSONArray("nodes");

                    for(int i=0 ;i<nodeArr.length() ; i++)
                    {
                        FinalNodeList.add(nodeArr.getInt(i));
                    }

                    if(FinalNodeList.isEmpty())
                    {


                        if(hasAdaptorConfigured)
                        {
                            postAdaptor.endPost = true;
                        }
                        else
                        {
                            postAdaptor.endPost =true;
                            modelPost mp = new modelPost(null ,null ,null,null,null ,null ,null,null,null,null,0,0 ,0);
                            generatedPostList.add(mp);
                            postAdaptor pAdaptor = new postAdaptor(Home.this , generatedPostList , viewpager2View);
                            viewpager2View.setAdapter(pAdaptor);
                        }
                        //mAdapter = new postAdaptor(Home.this, generatedPostList, viewpager2View);
                      //  viewpager2View.setAdapter(mAdapter);
                       // Toast.makeText(Home.this, "Oops.. You have reached the end of posts.\n PLEASE COME BACK LATER TILL WE UPDATE MORE.  ", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }


                   // else {
                        PrepareModelPostList();
                   // }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }





    private void prepareNonPersonalizedPosts(){

       // postrecommender.prepareRandompostsForUser(Home.this);





    }



    public void PrepareModelPostList(){

        sManager.getNodeDetails(FinalNodeList, new serverManager.VolleyResponseListener() {

            @Override
            public void onError(String message) {

                Toast.makeText(Home.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {

                JSONArray nodeDetails = null;
                try {
                    nodeDetails = new JSONArray(response);


                    for(int i=0 ; i<nodeDetails.length() ; i++)
                    {
                        JSONObject TempObj = nodeDetails.getJSONObject(i);

                        modelPost mp = new modelPost(TempObj.getString("PostTitle") , TempObj.getString("ImageName")
                                ,TempObj.getString("ParentName") , TempObj.getString("Tag1") ,TempObj.getString("Tag2")
                                ,TempObj.getString("Tag3") ,TempObj.getString("Tag4") , TempObj.getString("Tag5") , TempObj.getString("author")
                                ,TempObj.getInt("NodeId") ,TempObj.getInt("ParentId") ,TempObj.getInt("PostUpvotes"));

                        generatedPostList.add(mp);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                    preparePostAdaptor(generatedPostList);

                Log.d("HOME ", "GENARWTEE POST LIST sIze main" + generatedPostList.size());

            }


        });


    }

    public void preparePostAdaptor(ArrayList<modelPost> modelPostArrayList)
    {


     if(isFirstLot) {
         hasAdaptorConfigured = true;
                  mAdapter = new postAdaptor(Home.this, modelPostArrayList, viewpager2View);
         viewpager2View.setAdapter(mAdapter);
         progressBar.setVisibility(View.GONE);
     }
     else
     {
         mAdapter.notifyDataSetChanged();
         progressBar.setVisibility(View.GONE);
     }

        viewpager2View.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

            }
            @Override
            public void onPageSelected(int position) {
                Log.d("HOME ", "POST position in secoond" + position);
                super.onPageSelected(position);
                if(prevPosition < position+1) {

                    viewedPosts.put(modelPostArrayList.get(position).getNodeId());
                    prevPosition = position + 1;

                    if (position + 1 == generatedPostList.size()) {

                        isFirstLot = false;
                        viewedToday =true;
                        progressBar.setVisibility(View.VISIBLE);
                        preparePersonalizedPosts();

                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

    }



    private void initializeCustomShowPost(int j) {
        ArrayList<Integer> tempCustomNode = new ArrayList<>();

        tempCustomNode.add(j) ;
        sManager.getNodeDetails(tempCustomNode, new serverManager.VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(String response) {


                JSONArray nodeDetails = null;
                try {
                    nodeDetails = new JSONArray(response);


                    for(int i=0 ; i<nodeDetails.length() ; i++)
                    {
                        JSONObject TempObj = nodeDetails.getJSONObject(i);

                        modelPost mp = new modelPost(TempObj.getString("PostTitle") , TempObj.getString("ImageName")
                                ,TempObj.getString("ParentName") , TempObj.getString("Tag1") ,TempObj.getString("Tag2")
                                ,TempObj.getString("Tag3") ,TempObj.getString("Tag4") , TempObj.getString("Tag5") ,TempObj.getString("author")
                                ,TempObj.getInt("NodeId") ,TempObj.getInt("ParentId") ,TempObj.getInt("PostUpvotes"));

                        generatedPostList.add(mp);

                    }
                    viewedPosts.put(j);

                    preparePersonalizedPosts();
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });

    }


    @Override
    protected void onStop() {
        if (viewedToday)
        {
            SharedPrefManager.getInstance(Home.this).storeViewedPosts(viewedPosts);
        }

        super.onStop();
    }

    @Override
    public void onBackPressed() {

        if(backPressedTimes)
        {
            finishAffinity();
            super.onBackPressed();
            return;
        }

        backPressedTimes = true;
        Toast.makeText(this, "Press Back Again To Exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backPressedTimes = false;
            }
        } , 2000);

    }
}

