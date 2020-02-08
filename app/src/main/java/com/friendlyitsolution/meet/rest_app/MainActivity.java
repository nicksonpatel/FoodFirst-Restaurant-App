package com.friendlyitsolution.meet.rest_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button btn;
    MaterialEditText etuser,etpass;
     ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pd=new ProgressDialog(MainActivity.this);
        pd.setCancelable(false);
        pd.setMessage("please wait");


        btn=(Button)findViewById(R.id.btnLogin);
        etpass=(MaterialEditText)findViewById(R.id.etpass);
        etuser=(MaterialEditText)findViewById(R.id.etUser);

        if(!Myapp.setall.equals("")) {
            Intent i = new Intent(getApplicationContext(), home.class);
            startActivity(i);
            finish();
        }
        else if(!Myapp.mynumber.equals("")) {
            Intent i = new Intent(getApplicationContext(), adddata.class);
            startActivity(i);
            finish();
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etuser.getText().toString().equals("")&&!etpass.getText().toString().equals(""))
                {
                    pd.show();
                    signin();
                 }
                if(etuser.equals(""))
                {
                    etuser.setError("please enter");
                }
                if(etpass.equals(""))
                {
                    etpass.setError("please enter");
                }
            }
        });

    }
    void signin()
    {
        pd.setMessage("Checking Information...");
        pd.show();
        DatabaseReference reff=Myapp.ref.child("rest").child(etuser.getText().toString());
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue()==null)
                {
                   pd.dismiss();
                   Myapp.showMsg("Invalid user");

                    etuser.setError("Wrong User");
                }
                else
                {
                    pd.dismiss();
                    Myapp.userdata=(Map<String, Object>) dataSnapshot.getValue();

                    if(Myapp.userdata.get("pass").toString().equals(etpass.getText().toString())) {
                        SharedPreferences.Editor edit = Myapp.pref.edit();
                        edit.putString("mynumber", etuser.getText().toString());
                        edit.commit();

                        Myapp.mynumber=etuser.getText().toString();
                        Myapp.userdata=(Map<String, Object>)dataSnapshot.getValue();


                        Myapp.myname=Myapp.userdata.get("name")+"";

                        if(Myapp.userdata.containsKey("res"))
                        {
                            Myapp.userres=(Map<String, Object>)Myapp.userdata.get("res");
                            Myapp.menu=(Map<String, Object>)Myapp.userres.get("menu");
                            SharedPreferences.Editor edit1=Myapp.pref.edit();
                            edit1.putString("setall","yes");
                            edit1.commit();
                            Intent i = new Intent(getApplicationContext(), home.class);
                            startActivity(i);
                            finish();
                        }
                        else
                        {
                            SharedPreferences.Editor edit1=Myapp.pref.edit();
                            edit1.putString("setall","");
                            edit1.commit();
                            Intent i = new Intent(getApplicationContext(), adddata.class);
                            startActivity(i);
                            finish();
                        }





                    }
                    else
                    {
                        etpass.setError("Wrong pass");
                        Myapp.showMsg("wrong password");
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
