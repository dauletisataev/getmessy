package com.example.lenovo.myapplication.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lenovo.myapplication.R;
import com.example.lenovo.myapplication.activity.LessonsDetails;
import com.example.lenovo.myapplication.interfaces.RecyclerViewClickListener;
import com.example.lenovo.myapplication.models.Event;
import com.example.lenovo.myapplication.models.EventDetails;
import com.example.lenovo.myapplication.other.CircleTransform;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RVAdapter_EVENTS extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public  List<Event> events;
    private static RecyclerViewClickListener itemListener;
    public RVAdapter_EVENTS(List<Event> events, RecyclerViewClickListener itemListener ){
        this.events = events;
        this.itemListener = itemListener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("Viewgroup Type", String.valueOf(viewType));
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);

            return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        final EventViewHolder holder = (EventViewHolder) viewHolder;
        holder.eventName.setText(events.get(i).getName());
        holder.time.setText(events.get(i).getDetails().getStart_date());
        holder.description.setText(events.get(i).getDescription());

        Glide.with(holder.logo.getContext()).load(events.get(i).getLogo())
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(holder.logo.getContext()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.logo);

        Glide.with(holder.banner.getContext()).load(events.get(i).getBanner())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.banner);
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, Object> result = new HashMap<>();
                result.put("startTime", events.get(i).getDetails().getStart_time());
                result.put("endTime", events.get(i).getDetails().getEnd_date());

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                result.put("room", events.get(i).getLocation());
                result.put("description", events.get(i).getDescription());
                result.put("title", events.get(i).getName());
                String key = mDatabase.child("schedules").push().getKey();
                Map<String, Object> childUpdates = new HashMap<>();
                Log.d("pushToFirebase", result.toString());

                SharedPreferences sharedPref =   holder.add.getContext().getSharedPreferences("nuschedule",Context.MODE_PRIVATE);
                String id = sharedPref.getString("cUserId", "201599251");

                childUpdates.put("/schedules/"+id+"/"+getDay(events.get(i).getDetails().getStart_date())+"/"+key , result);
                mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        holder.add.setVisibility(View.GONE);
                        holder.added.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(holder.add.getContext(), "Some error occured", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    public Event getItem(int i){
        return events.get(i);
    }
    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        TextView eventName, time, description;
        ImageView logo, banner;
        Button add, added;


        EventViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            eventName = (TextView)itemView.findViewById(R.id.event_name);
            description = (TextView)itemView.findViewById(R.id.description);
            time = (TextView)itemView.findViewById(R.id.time);
            logo = (ImageView) itemView.findViewById(R.id.logo);
            banner = (ImageView) itemView.findViewById(R.id.banner);
            add = (Button) itemView.findViewById(R.id.btn_add);
            added = (Button) itemView.findViewById(R.id.btn_added);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            itemListener.recyclerViewListClicked(view, this.getAdapterPosition());
        }

    }


    String  getDay(String date){
        String[] dates = new String[] { "Sunday", "Monday", "Tuesday", //
                "Wednesday", "Thursday", "Friday", "Saturday" };

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat( "dd.MM.yyyy" );
        try{
            c.setTime(df.parse(date));
        }catch (Exception e){
            Log.e("GETDAY ERROR", "getDay: ",e   );
        }

        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        return dates[dayOfWeek];
    }



}