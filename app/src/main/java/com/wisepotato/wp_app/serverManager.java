package com.wisepotato.wp_app;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import java.util.Map;

public class serverManager {
    private String userInterestsArray[] ;
    String InterestReesponse;
    Context ctx;
    boolean isAvailable = false;
    String viewPostPreString;
    private static final String URL_Intrests = "http://www.3hundredsolutions.tech/Wisepotato%20App/Posts/intrest.php";



    public interface VolleyResponseListener {
        void onError(String message);
        void onResponse(String response);
    }


    public serverManager(Context ctx) {
        this.ctx = ctx;

    }



    public void interestcheck(){
        JSONArray interestsIdArray = new JSONArray();
        JSONArray interestsArray = new JSONArray();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL_Intrests, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i=0 ; i<response.length() ; i++)
                    {
                        JSONObject obj = response.getJSONObject(i);
                        interestsArray.put(obj.getString("interests"));
                        interestsIdArray.put(obj.getInt("interestsIds"));

                    }

                    Log.d("INTERESTS CHECK..", "onResponse: "+interestsArray+"   "+interestsIdArray);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        //Adding our request to the queue
        requestQueue.add(jsonArrayRequest);




    }


    public void getQueryResults( String Query , VolleyResponseListener volleyResponseListener) {



        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_SEARCH_QUERY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("SEARCH QUERYYY RESULT", "onResponse 1: "+response);
                        volleyResponseListener.onResponse(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        volleyResponseListener.onError(error.getMessage());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<String, String>();
                params.put("query",Query);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String ,String> param = new HashMap<String, String>();
                param.put("content-type" , "application/x-www-form-urlencoded");
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        requestQueue.add(stringRequest);


    }


    public void getNodeIdsFromInterests(String userInterest,int userAge ,JSONArray viewedpostsByUser , VolleyResponseListener volleyResponseListener)
    {

      userInterestsArray =  userInterest.split(",");

      viewPostPreString = viewedpostsByUser.toString();
      if(!viewPostPreString.isEmpty())
      {
          viewPostPreString = viewPostPreString.substring(viewPostPreString.indexOf('[')+1 , viewPostPreString.indexOf(']'));
      }



        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_POST_FETCHING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("SERVER MANAGER", "onResponse: PARTH CHA "+response);


                        volleyResponseListener.onResponse(response);



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                      volleyResponseListener.onError("failure");
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<String, String>();
                params.put("interests",userInterest);
                params.put("userage" , Integer.toString(userAge));
                params.put("viewedPosts" ,viewPostPreString);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String ,String> param = new HashMap<String, String>();
                param.put("content-type" , "application/x-www-form-urlencoded");
                return param;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        requestQueue.add(stringRequest);


    }
    public void getMyPosts(Context context , VolleyResponseListener volleyResponseListener) {

        User user = SharedPrefManager.getInstance(context).getUser();
        Log.d("server manager", "getSavedPosts: ");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_MY_POST_FETCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Log.d("NEW SERVER MANAGER NODE", "onResponse 1: "+response);
                        volleyResponseListener.onResponse(response);



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        volleyResponseListener.onError(error.getMessage());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId",Integer.toString(user.getId()));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String ,String> param = new HashMap<String, String>();
                param.put("content-type" , "application/x-www-form-urlencoded");
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        requestQueue.add(stringRequest);


    }

    public void getSavedPosts(Context context , VolleyResponseListener volleyResponseListener){

        User user = SharedPrefManager.getInstance(context).getUser();
        Log.d("server manager", "getSavedPosts: ");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_SAVED_POST_FETCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Log.d("NEW SERVER MANAGER NODE", "onResponse 1: "+response);
                        volleyResponseListener.onResponse(response);



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        volleyResponseListener.onError(error.getMessage());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId",Integer.toString(user.getId()));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String ,String> param = new HashMap<String, String>();
                param.put("content-type" , "application/x-www-form-urlencoded");
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        requestQueue.add(stringRequest);



    }



    public void getNodeDetails(ArrayList<Integer> NodeIdsList  ,VolleyResponseListener volleyResponseListener2)
    {
        ArrayList<modelPost> generatedPostList  = new ArrayList<>();
        String temp = NodeIdsList.toString();
        String parth = temp.substring(temp.indexOf('[')+1 , temp.indexOf(']'));



        if(NodeIdsList != null) {


            StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_POST_DATA_FETCHING,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            volleyResponseListener2.onResponse(response);


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                           // Log.d("FETCH_NODES :", error.getMessage() );
                            volleyResponseListener2.onError("failure");

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("nodeidslist", parth);

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> param = new HashMap<String, String>();
                    param.put("content-type", "application/x-www-form-urlencoded");
                    return param;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
            requestQueue.add(stringRequest);



        }


    }


    public void addUpvoteToDatabase(int postId ,int Status, VolleyResponseListener volleyResponseListener)
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_POST_UPVOTE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        volleyResponseListener.onResponse(response);
                        //converting response to json object
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        volleyResponseListener.onError(error.getMessage());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<String, String>();
                params.put("PostId",Integer.toString(postId));
                params.put("Status",Integer.toString(Status));
                //params.put("username" , "wisePotatoNodesdata");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String ,String> param = new HashMap<String, String>();
                param.put("content-type" , "application/x-www-form-urlencoded");
                return param;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        requestQueue.add(stringRequest);

    }

    public void signup(String username, String email, String password,int signInwithGoogle,VolleyResponseListener volleyResponseListener){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(response);
                    //if no error in response
                    if (!obj.getBoolean("error")) {

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        //creating a new user object
                        User user = new User(
                                userJson.getInt("id"),
                                userJson.getString("username"),
                                userJson.getString("email"),
                                userJson.getInt("Age")
                        );

                        //storing the user in shared preferences
                        Log.d("SERVER MANAGER RES", "User LOGIN "+user.getAge());
                        SharedPrefManager.getInstance(ctx).userLogin(user);
                        volleyResponseListener.onResponse("success");


                    } else {
                        Toast.makeText(ctx, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ctx, error.getMessage(), Toast.LENGTH_SHORT).show();
                        volleyResponseListener.onError("failure");
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                params.put("signInWithGoogle",Integer.toString(signInwithGoogle));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void savePostOnServer( int postIdToBeSaved , VolleyResponseListener volleyResponseListener){

        User user  = SharedPrefManager.getInstance(ctx).getUser();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_POST_SAVE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("server manager","onResponse: SAVE POST RESP "+response);
                        volleyResponseListener.onResponse(response);
                        //converting response to json object
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        volleyResponseListener.onError(error.getMessage());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<String, String>();

                params.put("savedposts",Integer.toString(postIdToBeSaved));
                params.put("username" , Integer.toString(user.getId()));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String ,String> param = new HashMap<String, String>();
                param.put("content-type" , "application/x-www-form-urlencoded");
                return param;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        requestQueue.add(stringRequest);





    }

    public void deletePostFromMyAccount(int postNodeId , VolleyResponseListener volleyResponseListener) {

        User user  = SharedPrefManager.getInstance(ctx).getUser();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_POST_SAVE_DELETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("server manager","onResponse: DELETE POST  "+response);
                        volleyResponseListener.onResponse(response);
                        //converting response to json object
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        volleyResponseListener.onError(error.getMessage());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<String, String>();

                params.put("postIdDelete",Integer.toString(postNodeId));
                params.put("username" , Integer.toString(user.getId()));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String ,String> param = new HashMap<String, String>();
                param.put("content-type" , "application/x-www-form-urlencoded");
                return param;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        requestQueue.add(stringRequest);




    }


    public void updateProfilePicture(int userid , String pictureString  ,VolleyResponseListener volleyResponseListener){
        RequestQueue queue = Volley.newRequestQueue(ctx.getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_UpdateProfilePic, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.d("UPDATE PROFILE PICTURE", "onResponse: "+response);
                        volleyResponseListener.onResponse("success");


            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        volleyResponseListener.onError("failure");
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("userId", Integer.toString(userid));
                params.put("picture", pictureString);

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);


    }



    public void FetchProfilePic(int userId , VolleyResponseListener volleyResponseListener) {

        RequestQueue queue = Volley.newRequestQueue(ctx.getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_FetchProfilePic, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.d("UPDATE PROFILE PICTURE", "onResponse: "+response);
                volleyResponseListener.onResponse(response);


            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        volleyResponseListener.onError("failure");
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("userId", Integer.toString(userId));

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);



    }
    public void checkForInterests(int userId , VolleyResponseListener volleyResponseListener)
    {

        Log.d("INTERETS CHECKING......", "checkForInterests: fetcHINGGG");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_CHECK_INTEREST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        volleyResponseListener.onResponse(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        volleyResponseListener.onError(error.getMessage());

                    }
                })
        {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId" , Integer.toString(userId));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String ,String> param = new HashMap<String, String>();
                param.put("content-type" , "application/x-www-form-urlencoded");
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        requestQueue.add(stringRequest);

    }


    public void formEntryRegistration(String userid , String title , String domain , String explanation , String tag1 ,
                                      String tag2 ,String tag3 ,String tag4 , VolleyResponseListener volleyResponseListener){




        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ADD_POSTFORM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        volleyResponseListener.onResponse(response);
                        //converting response to json object
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        volleyResponseListener.onError(error.getMessage());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Title",title);
                params.put("Domain",domain);
                params.put("explaination",explanation);
                params.put("tag1",tag1);
                params.put("tag2",tag2);
                params.put("tag3",tag3);
                params.put("tag4",tag4);
                params.put("userId" , userid);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String ,String> param = new HashMap<String, String>();
                param.put("content-type" , "application/x-www-form-urlencoded");
                return param;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        requestQueue.add(stringRequest);


    }

}
