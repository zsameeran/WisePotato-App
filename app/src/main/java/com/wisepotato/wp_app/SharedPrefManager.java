package com.wisepotato.wp_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "AccountStorage";
    private static final String KEY_USERNAME = "keyusername";
    private static final String Key_Age="keyAge";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_INTEREST_ADDED = "keyinterests";
    private static final String KEY_ADDING_INTERESTS = "keyToAddInterests";
    private static final String KEY_VIEWED_POSTS = "keyToViewedPosts";
    private static final String KEY_SAVING_POSTS = "keyToSavePosts";
    private static final String KEY_VIEWED_POSTS_NONATUTH_USER = "keyToViewedPostOfNonAuthorizedUser";
    private static final String KEY_ADDING_SHUFFLED_INTERESTS = "keyToAddShuffledInterests";
    private static final String KEY_ID = "keyid";
    private static SharedPrefManager mInstance;
    SharedPreferences sharedPreferences;
    private static Context ctx;

    private SharedPrefManager(Context context) {
        ctx = context;
    }
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }


    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, ctx.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putInt(Key_Age,user.getAge());
        Log.d("shared pref ", "madhla age "+user.getAge());
        editor.commit(); }


    public boolean isAgeEntered(){
        sharedPreferences=ctx.getSharedPreferences(SHARED_PREF_NAME, ctx.MODE_PRIVATE);
        return sharedPreferences.getInt(Key_Age,0)!= 0;
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, ctx.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, ctx.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getInt(Key_Age,-1)
        );
    }
    public boolean isInterestAdded(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME , ctx.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ADDING_INTERESTS, null) != null;
    }

    //this method will logout the user
    public boolean logout() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, ctx.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();



        return true;

    }


    public void addInterest(String interests)
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME , ctx.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString( KEY_ADDING_INTERESTS , interests);
        editor.commit();



    }


    public boolean isShuffledInterestArrayExists()
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME , ctx.MODE_PRIVATE );
        return sharedPreferences.getString(KEY_ADDING_SHUFFLED_INTERESTS , null) != null;
    }

    public void addShuffledinterets(String shuffledInterestsString)
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME , ctx.MODE_PRIVATE );
        if(sharedPreferences.getString(KEY_ADDING_SHUFFLED_INTERESTS , null) == null)
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString( KEY_ADDING_SHUFFLED_INTERESTS , shuffledInterestsString);
            editor.commit();
        }

    }

    public JSONArray getViewedPosts(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME , ctx.MODE_PRIVATE );
        JSONArray savedViewedPosts = new JSONArray();

        String temp = sharedPreferences.getString(KEY_VIEWED_POSTS ,null);
        if(temp != null) {
            temp = temp.substring(1, temp.indexOf(']'));
            String tempArray[];
            tempArray = temp.split(",");
            for (int i = 0; i < tempArray.length; i++) {
                savedViewedPosts.put(Integer.parseInt(tempArray[i].trim()));
            }
            return savedViewedPosts;
        }

        return savedViewedPosts;
    }

    public void storeViewedPosts(JSONArray viewedPostsArray){

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME , ctx.MODE_PRIVATE );
        JSONArray savedViewedPosts = new JSONArray();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        String temp = sharedPreferences.getString(KEY_VIEWED_POSTS ,null);


        if(temp != null) {
            temp = temp.substring(1, temp.indexOf(']'));
            String tempArray[];
            tempArray = temp.split(",");
            for (int i = 0; i < tempArray.length; i++) {
                savedViewedPosts.put(Integer.parseInt(tempArray[i].trim()));
            }

            for(int i=0 ;i<viewedPostsArray.length() ;i++){
                try {

                    savedViewedPosts.put(viewedPostsArray.getInt(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            editor.putString( KEY_VIEWED_POSTS, savedViewedPosts.toString());
            editor.commit();

        }
        else
        {
            editor.putString( KEY_VIEWED_POSTS, viewedPostsArray.toString());
            editor.commit();
        }

    }

public void clearViewedPosts(){
    SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME , ctx.MODE_PRIVATE );
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.remove(KEY_VIEWED_POSTS);
    editor.commit();
}
    public void clearSaveedPosts()
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME , ctx.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_SAVING_POSTS);
        editor.commit();
    }
    public String GetSavedPost()
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME , ctx.MODE_PRIVATE );
        return sharedPreferences.getString(KEY_SAVING_POSTS ,null);
    }

    public void SavePostLocally(int postId){

        String postIdString = Integer.toString(postId).trim();

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME , ctx.MODE_PRIVATE );

        SharedPreferences.Editor editor = sharedPreferences.edit();

        List<String> savepst = new ArrayList<>();

        String temp[];

        String Data = sharedPreferences.getString(KEY_SAVING_POSTS ,null);

        if(Data != null)
        {
            Data = Data.substring(Data.indexOf('[')+1 , Data.indexOf(']'));
            temp = Data.split(", ");

            for(int i=0 ;i<temp.length ;i++) {
            savepst.add(temp[i]);
            }

            savepst.add(postIdString);
            editor.putString(KEY_SAVING_POSTS,savepst.toString());
            editor.apply();



        }
        else
        {
            savepst.add(postIdString);
            editor.putString(KEY_SAVING_POSTS,savepst.toString());
            editor.apply();
        }



    }

    public ArrayList<Integer> getShuffledInterests(){
        ArrayList<Integer> tempArray = new ArrayList<>();
        String temp;
        String temp1[];
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME , ctx.MODE_PRIVATE );
        temp = sharedPreferences.getString(KEY_ADDING_SHUFFLED_INTERESTS ,null);

        temp1 = temp.split(",");

        for (int i =0 ; i<temp1.length ; i++)
        {
            tempArray.add(Integer.parseInt(temp1[i]));
        }

        return tempArray;
    }

    public String getInterest(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME , ctx.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();
       return sharedPreferences.getString(KEY_ADDING_INTERESTS ,null);
    }

    public void getViewedPostsIdsOfNonAuthorizedUser() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME , ctx.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();


    }
    public void StoreViewedPostsIdsOfNonAuthorizedUser(List<Integer> viewedPostList) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME , ctx.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String temp = sharedPreferences.getString(KEY_VIEWED_POSTS_NONATUTH_USER ,null);
        String temparray[];
        List<Integer> viewedPosts = new ArrayList<>();
        temp = temp.substring(temp.indexOf('[')+1 , temp.indexOf(']'));
        temparray = temp.split(", ");

        for(int i=0 ;i<temparray.length ;i++) {
            viewedPosts.add(Integer.parseInt(temparray[i].trim()));
        }
       // viewedPosts.add(viewedPostList);

        //editor.putString(KEY_SAVING_POSTS,savepst.toString());
        editor.apply();

        editor.putString(KEY_VIEWED_POSTS_NONATUTH_USER , viewedPostList.toString());
        editor.apply();


    }
}