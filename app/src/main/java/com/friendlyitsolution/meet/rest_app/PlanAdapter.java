package com.friendlyitsolution.meet.rest_app;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.util.List;


/**
 * Created by Meet on 16-10-2017.
 */

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.MyViewHolder> {

private List<plan_model> moviesList;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView title, fees,capacity,dec,eprice;
    Button btn;

    public MyViewHolder(View view) {
        super(view);

        title = (TextView) view.findViewById(R.id.name);
        fees = (TextView) view.findViewById(R.id.price);
        capacity = (TextView) view.findViewById(R.id.number);
        dec = (TextView) view.findViewById(R.id.address);
        eprice = (TextView) view.findViewById(R.id.eprice);
        btn=(Button)view.findViewById(R.id.btn);
    }
}


    public PlanAdapter(List<plan_model> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plan_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final plan_model movie = moviesList.get(position);
    holder.capacity.setText("Discount "+movie.capacity+" %");
    holder.fees.setText(movie.fees+" \u20B9 /-");
    holder.dec.setText(movie.dec);
    holder.title.setText(movie.name);
    holder.eprice.setText(movie.eprice+" \u20B9 /-");

    holder.btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Myapp.ref.child("system").child("plans").child(movie.key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                 Myapp.ref.child("rest").child(Myapp.mynumber).child("plans").child(movie.key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                         Myapp.showMsg("Successfully removed");
                         offer.list.remove(movie);
                         offer.adapter.notifyDataSetChanged();

                     }
                 });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
               Myapp.showMsg("Please try again");
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
