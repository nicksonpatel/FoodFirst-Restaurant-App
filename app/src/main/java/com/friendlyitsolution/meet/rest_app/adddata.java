package com.friendlyitsolution.meet.rest_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gun0912.tedbottompicker.TedBottomPicker;

public class adddata extends AppCompatActivity {
    Spinner area,capacity;
    CheckBox ck1,ck2,ck3,ck4;
    MaterialEditText price,address,name,contact;
    Uri imgurl=null;
    Button btn;
    ImageView iv;
    ProgressDialog pd;
    RadioButton per,plot;
    List<String> num=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddata);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pd=new ProgressDialog(adddata.this);
        pd.setCancelable(false);
        pd.setMessage("please wait");


        iv=(ImageView)findViewById(R.id.iv);
        ck1=(CheckBox)findViewById(R.id.ch1);
        ck2=(CheckBox)findViewById(R.id.ch2);
        ck3=(CheckBox)findViewById(R.id.ch3);
        ck4=(CheckBox)findViewById(R.id.ch4);
        name=(MaterialEditText)findViewById(R.id.etname);
        area=(Spinner)findViewById(R.id.area);
        capacity=(Spinner)findViewById(R.id.cap);
        per=(RadioButton)findViewById(R.id.radioButton2);
        plot=(RadioButton)findViewById(R.id.radioButton);
        btn=(Button)findViewById(R.id.btn);
        price=(MaterialEditText)findViewById(R.id.etamount);
        address=(MaterialEditText)findViewById(R.id.etadd);
        contact=(MaterialEditText)findViewById(R.id.etphone);
        setCapactity();
        ArrayAdapter<String> area_adpter=new ArrayAdapter<String>(adddata.this,android.R.layout.simple_spinner_dropdown_item,Myapp.area);
        area.setAdapter(area_adpter);

        ArrayAdapter<String> cap_adpter=new ArrayAdapter<String>(adddata.this,android.R.layout.simple_spinner_dropdown_item,num);
        capacity.setAdapter(cap_adpter);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(adddata.this)
                        .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                            @Override
                            public void onImageSelected(Uri uri) {

                                // Toast.makeText(getApplicationContext(),"get : "+uri,Toast.LENGTH_LONG).show();
                                if(uri!=null)
                                {
                                    imgurl=uri;
                                    iv.setImageURI(uri);
                                }
                            }
                        })
                        .create();

                tedBottomPicker.show(getSupportFragmentManager());
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(imgurl==null)
                {
    Myapp.showMsg("Please select image");
                }
                else
                {
                    pd.setMessage("please wait");
                    pd.show();
                    uploadImage(imgurl);
                }

            }
        });

    }

    void setCapactity()
    {
        for(int i=1;i<300;i++)
        {
            num.add(""+i);
        }
    }

    private void uploadImage(final Uri uri) {
        //if there is a file to upload

        if (uri != null) {
            //  bnp.setVisibility(View.VISIBLE);
            final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            StorageReference mStorageRef=   mStorageRef = FirebaseStorage.getInstance().getReference();
            StorageReference riversRef = mStorageRef.child("img/"+Myapp.mynumber+"/IMG_"+timeStamp+".jpg");

            riversRef.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            pd.setMessage("almost done...");
                            Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();

                            addData(""+downloadUri);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            pd.dismiss();
                            Toast.makeText(getApplicationContext(),"Try again",Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            // pd.setProgress(((int) progress));

                        }
                    });
        }

        else {
            Toast.makeText(getApplicationContext(),"Try again ..",Toast.LENGTH_LONG).show();

        }
    }

    void addData(String url)
    {
        pd.show();
        final Map<String,Object> data=new HashMap<>();
        data.put("url",url);
        data.put("title",name.getText().toString());
        data.put("address",address.getText().toString());
        if(per.isChecked())
        {

            data.put("type","Vegetarian");

        }
        else
        {

            data.put("type","Non Vegetarian");

        }
        data.put("area",area.getSelectedItem()+"");
        data.put("capacity",capacity.getSelectedItem()+"");

       final Map<String,Object> menu=new HashMap<>();

        if(ck1.isChecked())
        {
            menu.put("north indian","yes");
        }
        if(ck2.isChecked())
        {
            menu.put("south indian","yes");
        }
        if(ck3.isChecked())
        {
            menu.put("chinese","yes");
        }
        if(ck4.isChecked())
        {
            menu.put("Fast-Food","yes");
        }
        data.put("status","off");
        data.put("menu",menu);
        data.put("contact",contact.getText().toString());
        data.put("userid",Myapp.mynumber);
        data.put("price",price.getText().toString());
        final String key=Myapp.ref.child("allres").push().getKey();
        data.put("key",key);
        if(menu.size()>0) {

            Myapp.ref.child("rest").child(Myapp.mynumber).child("res").setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {



            Myapp.ref.child("allres").child(key).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    pd.setMessage("Almost done");

                    List<String> kk=new ArrayList<>(menu.keySet());
                    for(int i=0;i<kk.size();i++)
                    {
                        Myapp.ref.child("res_food").child(kk.get(i)).child(key).setValue(data);
                    }
                    Myapp.ref.child("res_area").child(area.getSelectedItem()+"").child(key).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            pd.dismiss();
                            Myapp.showMsg("Successfully added");
                            Intent i = new Intent(adddata.this, home.class);
                            startActivity(i);
                            finish();
                            SharedPreferences.Editor edit=Myapp.pref.edit();
                            edit.putString("setall","yes");
                            edit.commit();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Myapp.showMsg("Please try again");
                    pd.dismiss();
                }
            });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Myapp.showMsg("Please try again later");
                }
            });
        }
        else
        {
            Myapp.showMsg("Please select atleast one food type");
        }
    }

}
