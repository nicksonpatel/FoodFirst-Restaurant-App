package com.friendlyitsolution.meet.rest_app;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class home extends AppCompatActivity {

    Button onoff;
    FusedLocationProviderClient mFusedLocationClient;
    Double lati,longi;
    TextView pen,acp;
    RecyclerView recy;
    static SubjectAdapter2 adapter;
    static List<Subject> list;
    static String type="pending";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView lgout=(ImageView)findViewById(R.id.lgbtn);
        lgout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                logOut();
            }
        });

        ImageView pln=(ImageView)findViewById(R.id.pln);
        pln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              Intent i=new Intent(getApplicationContext(),offer.class);
              startActivity(i);
            }
        });
        pen=(TextView)findViewById(R.id.pen);
        acp=(TextView)findViewById(R.id.acp);

        pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type="pending";
                getdata("pending");
            }
        });

        acp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type="accept";
                getdata("accept");
            }
        });

        onoff=(Button)findViewById(R.id.onoff);

        onoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Myapp.sendNotificationTo("8758849273","hi","hello");

                if(Myapp.userres.get("status").equals("off"))
                {
                    Myapp.ref.child("rest").child(Myapp.mynumber).child("res").child("status").setValue("on").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        Myapp.showMsg("OPEN");
                            setallONOFF("on");
                            setOnoff();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                       Myapp.showMsg("Try again");
                        }
                    });


                }
                else
                {
                    Myapp.ref.child("rest").child(Myapp.mynumber).child("res").child("status").setValue("off").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Myapp.showMsg("CLOSE");
                            setallONOFF("off");
                            setOnoff();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Myapp.showMsg("Try again");
                        }
                    });
                }
            }
        });

        setOnoff();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(getApplicationContext(),addMenu.class);
                startActivity(i);

            }
        });
        getCurrLocation();


        recy = (RecyclerView) findViewById(R.id.recy);
        list = new ArrayList<>();
        adapter = new SubjectAdapter2(list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Myapp.con);
        recy.setLayoutManager(mLayoutManager);
        recy.setItemAnimator(new DefaultItemAnimator());
        recy.setAdapter(adapter);


        getdata(type);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getdata(type);
    }

    static void getdata(String ty)
    {
        list.clear();
        adapter.notifyDataSetChanged();
        if(Myapp.userdata.containsKey("request")) {
            Map<String, Object> da=(Map<String, Object>)Myapp.userdata.get("request");
            List<String> allke=new ArrayList<>(da.keySet());
            Collections.sort(allke);
            for(int i=allke.size()-1;i>=0;i--)
            {
                Map<String,Object> dd=(Map<String, Object>) da.get(allke.get(i));
                if(dd.get("status").equals(ty)) {
                    list.add(new Subject(allke.get(i), dd));
                }
            }
            adapter.notifyDataSetChanged();
        }
        else
        {
            Myapp.showMsg("No pending Request found");
        }
    }
    void getCurrLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            try {
                                lati = location.getLatitude();
                                longi = location.getLongitude();
                                Map<String, String> lastloc = new HashMap<>();
                                lastloc.put("lati", lati + "");
                                lastloc.put("longi", longi + "");
                                Myapp.ref.child("rest").child(Myapp.mynumber).child("res").child("location").setValue(lastloc);



                                // Toast.makeText(getApplicationContext(), "geting :"+fulladd, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Error 1 :" + e.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                });
    }


    void logOut()
    {


        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialoug_logout);
        dialog.show();

        final Button btnInDialog = (Button) dialog.findViewById(R.id.btn);
        btnInDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = Myapp.pref.edit();
                editor.clear();
                editor.commit();
                finish();

                Myapp.m.unsubscribeFromTopic(Myapp.mynumber);

                Myapp.mynumber="";
                Myapp.myname="";
                Myapp.setall="";
                Intent i = new Intent(home.this,MainActivity.class);
                startActivity(i);

                finish();
            }
        });
        final ImageView btnClose = (ImageView) dialog.findViewById(R.id.canclebtn);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });




    }

    void setOnoff()
    {
        if(Myapp.userres.get("status").equals("off"))
        {
            onoff.setBackground(getResources().getDrawable(R.drawable.butnclick_off));
            onoff.setText("Restaurant Close");
        }
        else
        {
            onoff.setBackground(getResources().getDrawable(R.drawable.butnclick_on));
            onoff.setText("Restaurant Open");

        }



    }


    void setallONOFF(String sts)
    {
        String area=Myapp.userres.get("area").toString();
        String key=Myapp.userres.get("key").toString();
        List<String> allfood=new ArrayList<>(Myapp.menu.keySet());

        for(int i=0;i<allfood.size();i++)
        {
            Myapp.ref.child("res_food").child(allfood.get(i)).child(key).child("status").setValue(sts);
        }
        Myapp.ref.child("allres").child(key).child("status").setValue(sts);
        Myapp.ref.child("res_area").child(area).child(key).child("status").setValue(sts);
    }

}
