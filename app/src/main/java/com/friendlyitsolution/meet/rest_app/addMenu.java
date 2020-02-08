package com.friendlyitsolution.meet.rest_app;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class addMenu extends AppCompatActivity {

    List<String> alltype=new ArrayList<>();
    Spinner spin;
   public static String seltype;

    RecyclerView recy;
    static SubjectAdapter1 adapter;
    static List<Subject1> list;
    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
            spin=(Spinner)findViewById(R.id.type);
    tv=(TextView)findViewById(R.id.nm);
        alltype.addAll(Myapp.menu.keySet());
        ArrayAdapter<String> area_adpter=new ArrayAdapter<String>(addMenu.this,android.R.layout.simple_spinner_dropdown_item,alltype);
        spin.setAdapter(area_adpter);

        recy=(RecyclerView)findViewById(R.id.recy);
        list=new ArrayList<>();
        adapter=new SubjectAdapter1(list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Myapp.con);
        recy.setLayoutManager(mLayoutManager);
        recy.setItemAnimator(new DefaultItemAnimator());
        recy.setAdapter(adapter);


        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                tv.setText(alltype.get(position));
               // Myapp.showMsg(alltype.get(position));
                seltype=alltype.get(position);
                setvalues();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
additemdiloug();
             }
        });


    }


    static void setvalues()
    {
        list.clear();
        adapter.notifyDataSetChanged();
   try {
       Map<String, Object> oo = (Map<String, Object>) Myapp.menu.get(seltype);
       List<String> keys = new ArrayList<>(oo.keySet());

       for (int i = 0; i < keys.size(); i++) {
           Map<String, String> data = (Map<String, String>) oo.get(keys.get(i));
           list.add(new Subject1(keys.get(i), data.get("name"), data.get("price")));
       }
       adapter.notifyDataSetChanged();
   }
   catch(Exception e)
   {
       Myapp.showMsg("No data found");
   }



    }



    void additemdiloug()
    {


        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialoug_additem);
        dialog.show();

        final EditText iname=(EditText)dialog.findViewById(R.id.iname);
        final EditText iprise=(EditText)dialog.findViewById(R.id.iprise);

        final Button btnInDialog = (Button) dialog.findViewById(R.id.btn);
        btnInDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(iname.getText().toString().equals("")||iprise.getText().toString().equals(""))
                {
                    Myapp.showMsg("Please add all details");
                }
                else
                {

                    Map<String,String> dd=new HashMap<>();
                    dd.put("name",iname.getText().toString());
                    dd.put("price",iprise.getText().toString());
                    Myapp.ref.child("rest").child(Myapp.mynumber).child("res").child("menu").child(seltype).push().setValue(dd).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                          Myapp.showMsg("Successfully added");
                          dialog.dismiss();
                          setvalues();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Myapp.showMsg("please try again");
                        }
                    });
                }

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

}
