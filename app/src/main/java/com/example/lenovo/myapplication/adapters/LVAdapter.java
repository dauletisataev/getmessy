package com.example.lenovo.myapplication.adapters;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.example.lenovo.myapplication.R;
import com.example.lenovo.myapplication.models.Lesson;

import java.util.List;

/**
 * Created by Lenovo on 10.02.2018.
 */

public class LVAdapter extends BaseAdapter {
    Context context;
    List<Lesson> lessons;
    LayoutInflater inflter;

    public LVAdapter(Context applicationContext, List<Lesson> lessons) {
        this.context = context;
        this.lessons = lessons;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return lessons.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.list_item, null);
        TextView lesson = (TextView) view.findViewById(R.id.lesson);
        TextView time = (TextView) view.findViewById(R.id.time);
        lesson.setText(lessons.get(i).getTitle()+" "+lessons.get(i).getRoom());
        time.setText(lessons.get(i).getStartTime());
        return view;
    }
}