package com.friendlyitsolution.meet.rest_app;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Created by Meet on 16-10-2017.
 */

public class SubjectAdapter1 extends RecyclerView.Adapter<SubjectAdapter1.MyViewHolder> {

private List<Subject1> moviesList;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public EditText etname,etprice;
    public ImageView del,update,qr;
    public Bitmap bitmap;

    public MyViewHolder(View view) {
        super(view);

        etname=(EditText) view.findViewById(R.id.name);
        etprice=(EditText) view.findViewById(R.id.price);
              del=(ImageView)view.findViewById(R.id.del);
        update=(ImageView)view.findViewById(R.id.update);




    }
}


    public SubjectAdapter1(List<Subject1> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.usersubject1, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
       final  Subject1 movie = moviesList.get(position);
        holder.etname.setText(movie.name);
        holder.etprice.setText(movie.price);


        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String,String> data=new HashMap<>();
                data.put("name",holder.etname.getText().toString());
                data.put("price",holder.etprice.getText().toString());
                Myapp.ref.child("rest").child(Myapp.mynumber).child("res").child("menu").child(addMenu.seltype).child(movie.key).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Myapp.showMsg("Updated");
                    }
                });

            }
        });
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Myapp.ref.child("rest").child(Myapp.mynumber).child("res").child("menu").child(addMenu.seltype).child(movie.key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Myapp.showMsg("Removed");
                        addMenu.list.remove(movie);
                        addMenu.adapter.notifyDataSetChanged();
                    }
                });

            }
        });



}

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
