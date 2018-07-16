package com.example.lenovo.myapplication.adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.myapplication.activity.LessonsDetails;
import com.example.lenovo.myapplication.activity.MainActivity;
import com.example.lenovo.myapplication.R;
import com.example.lenovo.myapplication.fragment.LessonsFragment;
import com.example.lenovo.myapplication.models.Day;
import com.example.lenovo.myapplication.models.Header;
import com.example.lenovo.myapplication.models.Lesson;

import java.util.ArrayList;
import java.util.List;

public class RVAdapter_LESSONS extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_NTF = 0;
    private static final int TYPE_ITEM = 1;
    public  List<Day> days;
    LessonsFragment fragment;
    public RVAdapter_LESSONS(List<Day> days , LessonsFragment fragment){
        this.days = days;
        this.fragment = fragment;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("Viewgroup Type", String.valueOf(viewType));
        if(viewType == TYPE_ITEM){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson, parent, false);
            return new LessonViewHolder(v);
        } else if(viewType == TYPE_NTF){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
            return new NtfViewHolder(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if(viewHolder instanceof LessonViewHolder){
            i -=1;
            final LessonViewHolder holder = (LessonViewHolder) viewHolder;
            holder.dayName.setText(days.get(i).getName());
            final List<Lesson> lessons  = new ArrayList<Lesson>() ;
            int j=0;
            Log.d("currentLessons", days.get(i).getLessons().toString());
            for(Lesson ls : days.get(i).getLessons()){
                //Log.d("currentLesson", ls.get);
                if(lessons.size()==0){
                    lessons.add(ls);
                    j++;
                    Log.d("lessons adding", "size 0 "+ls.getTitle());
                } else{
                    int size = lessons.size();
                    for (int k = 0; k <= size; k++) {
                        if(k == lessons.size()){
                            lessons.add(ls);
                        } else
                        if(getSeconds(ls.getStartTime()) < getSeconds(lessons.get(k).getStartTime())){
                            lessons.add(k, ls);
                            break;
                        }
                        Log.d("lessons adding", "size is "+String.valueOf(lessons.size())+" "+ls.getTitle());
                    }
                }
            }
            LVAdapter lvAdapter = new LVAdapter(holder.list.getContext(), lessons);

            holder.list.setAdapter(lvAdapter);
            holder.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(holder.list.getContext(), LessonsDetails.class);
                    intent.putExtra("lesson", lessons.get(i));
                    holder.list.getContext().startActivity(intent);
                    //Toast.makeText(holder.list.getContext(), lessons.get(i).getStartTime()+" " +lessons.get(i).getTitle(), Toast.LENGTH_SHORT).show() ;
                }
            });
        } else{
            final NtfViewHolder holder = (NtfViewHolder) viewHolder;
            holder.btn.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.sendnotif();
                }
            });
        }
    }
    @Override

    public int getItemViewType(int position) {
        if(position == 0) return TYPE_NTF;
        return TYPE_ITEM;
    }
    @Override
    public int getItemCount() {
        return days.size()+1;
    }

    public int getSeconds(String timeValue){

        String[] splitByColon = timeValue.split(":");
        int hoursValue = Integer.parseInt(splitByColon[0]);
        String[] splitForMins = splitByColon[1].split(" ");
        if(splitForMins[1].equals("PM"))
        {
            if(hoursValue!=12) hoursValue = hoursValue + 12;
        }

        int minutesValue = Integer.parseInt(splitForMins[0]);

        return 3600*hoursValue + 60*minutesValue;

    }

    public static class LessonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView dayName;
        ListView list;


        LessonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            dayName = (TextView)itemView.findViewById(R.id.day_name);
            list  = (ListView) itemView.findViewById(R.id.listView);
        }
    }

    public static class NtfViewHolder extends RecyclerView.ViewHolder {
        Button btn;


        NtfViewHolder(View itemView) {
            super(itemView);
            btn  = (Button) itemView.findViewById(R.id.ntf_btn);

        }
    }




}