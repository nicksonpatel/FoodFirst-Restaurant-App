package com.friendlyitsolution.meet.rest_app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;

public class add_offer extends AppCompatActivity {
    EditText etname,etprice,etpricee,etdes,etdis;
    Button btn;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pd=new ProgressDialog(add_offer.this);
        pd.setCancelable(false);
        pd.setMessage("please wait");

        btn=(Button)findViewById(R.id.btn);
        etdes=(EditText)findViewById(R.id.etdes);
        etname=(EditText)findViewById(R.id.etname);
        etdis=(EditText)findViewById(R.id.etdiscount);
        etprice=(EditText)findViewById(R.id.etprice);
        etpricee=(EditText)findViewById(R.id.etpricee);

        etname.setText(Myapp.userres.get("title")+"");
        etname.setEnabled(false);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                addOffer();


            }
        });


    }
    void addOffer()
    {
        pd.show();
        final Map<String,String> data=new HashMap<>();
        data.put("capacity",etdis.getText().toString());
        data.put("price",etprice.getText().toString());
        data.put("eprice",etpricee.getText().toString());
        data.put("name",etname.getText().toString());
        data.put("dec",etdes.getText().toString());
        final String key= Myapp.ref.child("system").child("plans").push().getKey();
        Myapp.ref.child("system").child("plans").child(key).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              Myapp.ref.child("rest").child(Myapp.mynumber).child("plans").child(key).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {
                      pd.dismiss();
                      Myapp.showMsg("Add Successfully");
                      offer.getData();
                      add_offer.super.onBackPressed();
                  }
              });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Myapp.showMsg("Try again");
            }
        });

    }
}
