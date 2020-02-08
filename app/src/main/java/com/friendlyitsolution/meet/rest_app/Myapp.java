package com.friendlyitsolution.meet.rest_app;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Meet on 20-02-2018.
 */

public class Myapp extends Application {
    public static FirebaseDatabase db;
    public static DatabaseReference ref;
    public static SharedPreferences pref;
    public static String mynumber,myname;
    public static DatabaseReference myref;
    public static Map<String,Object> userdata;
    public static ProgressDialog pd;
    public static Context con;

    private static final Object TAG = "ss";
    public static List<String> area;
    public static String setall;

    private static Myapp sInstance;
    private RequestQueue mRequestQueue;

    static public FirebaseMessaging m;
    static public Map<String,Object> userres,menu;
    @Override
    public void onCreate() {
        super.onCreate();
        db=FirebaseDatabase.getInstance();
        db.setPersistenceEnabled(true);
        ref=db.getReference();
        con=getApplicationContext();
        sInstance=this;
        m=FirebaseMessaging.getInstance();
        addArea();
        pref=getSharedPreferences("myinfo",MODE_PRIVATE);
        mynumber=pref.getString("mynumber","");
        setall=pref.getString("setall","");
        if(!mynumber.equals(""))
        {
            getUserdata();
            m.subscribeToTopic(mynumber);

        }


    }

    void addArea()
    {
        area=new ArrayList<>();
        area.add("Manjalpur");
        area.add("Maneja");
        area.add("Makarpura");
        area.add("Karelibaug");
        area.add("Subhanpura");
        area.add("Gorwa");
        area.add("Gotri");
        area.add("O.P.Road");
        area.add("Dandiya Bazar");
    }

    public static void sendNotificationTo(String to,String nn,String mm)
    {
        String sst="/topics/"+to;
        JSONObject json = new JSONObject();
        try {
            JSONObject userData=new JSONObject();
            userData.put("title",nn);
            userData.put("body",mm);
            //  userData.put("sound","default");

            json.put("data",userData);
            json.put("to", sst);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //   Toasty.info(getApplicationContext(),"response : "+response.toString(),Toast.LENGTH_LONG).show();

                Log.i("onResponse", "" + response.toString());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error", "" + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","key=AAAAD1DQn-M:APA91bG_FxD6mYOCiKxj6HGNpQaaG0hKGPtnOgyGJ64qRLd_3aPwC2cYzTtEb2P4Vx8PRVBFl1K8YlzYXAQW_naLAa1PY6oy4GGLkgEj_DSkgky2mkG0xnQ2I_qkGYWMgikMULVBjp3a");
                params.put("Content-Type","application/json");
                return params;
            }
        };
        Myapp.getInstance().addToRequestQueue(jsonObjectRequest);

    }


    public static synchronized Myapp getInstance() {
        return sInstance;
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        VolleyLog.e("Adding request to queue: %s", req.getUrl());
        getRequestQueue().add(req);
    }
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);

        getRequestQueue().add(req);
    }
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    static void getUserdata()
    {
        myref=db.getReference("rest").child(mynumber);
        myref.keepSynced(true);
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userdata=(Map<String, Object>)dataSnapshot.getValue();
                myname=userdata.get("name")+"";

                if(userdata.containsKey("res"))
                {
                    userres=(Map<String, Object>)userdata.get("res");
                    menu=(Map<String, Object>)userres.get("menu");
                    SharedPreferences.Editor edit=pref.edit();
                    edit.putString("setall","yes");
                    edit.commit();
                }
                else
                {
                    SharedPreferences.Editor edit=pref.edit();
                    edit.putString("setall","");
                    edit.commit();
                }

                //   Toast.makeText(getApplicationContext(),"Data : "+userdata,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static void showMsg(String st)
    {
        Toast.makeText(con,st,Toast.LENGTH_LONG).show();
    }
}
