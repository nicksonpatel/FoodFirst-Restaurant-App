package com.friendlyitsolution.meet.rest_app;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * Created by Meet on 16-10-2017.
 */

public class SubjectAdapter2 extends RecyclerView.Adapter<SubjectAdapter2.MyViewHolder> {

    private List<Subject> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,time,status,code,person;
        public ImageView iv;
        public Button yes,no;
        public MyViewHolder(View view) {
            super(view);
            yes=(Button)view.findViewById(R.id.yes);
            no=(Button)view.findViewById(R.id.no);
            time=(TextView)view.findViewById(R.id.time);
            title = (TextView) view.findViewById(R.id.title);
            code = (TextView) view.findViewById(R.id.code);
              iv = (ImageView) view.findViewById(R.id.img);
            status=(TextView)view.findViewById(R.id.status);
            person=(TextView)view.findViewById(R.id.person);

        }
    }


    public SubjectAdapter2(List<Subject> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Subject movie = moviesList.get(position);
        final Map<String, Object> data = movie.data;

        holder.status.setText(data.get("status")+"");
        holder.time.setText(data.get("time")+"");
        holder.person.setText("booking for "+data.get("nop")+" person");
        holder.title.setText(data.get("rest")+"");


        holder.yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Map<String,Object> dtemp=data;
                dtemp.remove("status");
                dtemp.put("status","accept");
                dtemp.put("code",getRandom());
                Myapp.ref.child("users").child(data.get("userid")+"").child("request").child(movie.key).setValue(dtemp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Myapp.ref.child("rest").child(Myapp.mynumber).child("request").child(movie.key).setValue(dtemp).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Myapp.sendNotificationTo(data.get("userid")+"","Request Accepted",data.get("rest")+"("+data.get("time")+")");
                                Myapp.showMsg("Successfully Accepted");
                                home.getdata(home.type);
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Myapp.showMsg("try again later");
                    }
                });


            }
        });

        holder.no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Map<String,Object> dtemp=data;
                dtemp.remove("status");
                dtemp.put("status","cancle");

                Myapp.ref.child("users").child(data.get("userid")+"").child("request").child(movie.key).setValue(dtemp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Myapp.ref.child("rest").child(Myapp.mynumber).child("request").child(movie.key).setValue(dtemp).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Myapp.sendNotificationTo(data.get("userid")+"","Request Cancled",data.get("rest")+"("+data.get("time")+")");

                                Myapp.showMsg("Successfully Cancled");
                                getRs(data.get("userid")+"",data.get("nop")+"");
                                home.getdata(home.type);
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Myapp.showMsg("try again later");
                    }
                });

            }
        });

        if(data.get("status").toString().equals("pending"))
        {
            holder.iv.setImageDrawable(Myapp.con.getResources().getDrawable(R.drawable.pending));

            holder.yes.setVisibility(View.VISIBLE);

            holder.no.setVisibility(View.VISIBLE);
           holder.code.setVisibility(View.GONE);
        }
        else if(data.get("status").toString().equals("accept"))
        {
            holder.iv.setImageDrawable(Myapp.con.getResources().getDrawable(R.drawable.accept));
            holder.code.setVisibility(View.VISIBLE);
            holder.code.setText(data.get("code")+"");
            holder.yes.setVisibility(View.GONE);

            holder.no.setVisibility(View.GONE);
        }



}

String getRandom()
{
    Random rr=new Random();
    String r=rr.nextInt(9)+""+rr.nextInt(9)+""+rr.nextInt(9)+""+rr.nextInt(9);
    return r;
}

void getRs(final String uid,final String per)
{

   Myapp.ref.child("users").child(uid).child("balance").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Myapp.ref.child("users").child(uid).child("balance").removeEventListener(this);

            final int oldamt=Integer.parseInt(""+dataSnapshot.getValue());
            int newamt=(Integer.parseInt(per)*10)+oldamt;
            Myapp.ref.child("users").child(uid).child("balance").setValue(""+newamt).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
               Myapp.showMsg("Refund Successfully");
               Myapp.sendNotificationTo(uid,"Refund Successfully","Amount "+(Integer.parseInt(per)*10)+" Rs /-");
                }
            });

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

}

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
