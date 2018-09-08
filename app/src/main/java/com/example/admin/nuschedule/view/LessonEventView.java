package com.example.admin.nuschedule.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.nuschedule.R;

import java.util.Random;

public class LessonEventView extends LinearLayout {
    LinearLayout layout = null;
    TextView nameTextView = null;
    TextView roomTextView = null;
    Context mContext = null;

    public LessonEventView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LessonEventView);
        String nameText = a.getString(R.styleable.LessonEventView_lessonName);

        String roomText = a.getString(R.styleable.LessonEventView_lessonRoom);

        nameText = nameText == null ? "" : nameText;
        roomText = roomText == null ? "" : roomText;

        String service = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(service);

        layout = (LinearLayout) li.inflate(R.layout.view_lesson_event, this, true);
        Random rnd = new Random();
        /*int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        layout.setBackgroundColor(color);*/

        nameTextView = (TextView) layout.findViewById(R.id.lesson_name);
        roomTextView = (TextView) layout.findViewById(R.id.lesson_room);

        roomTextView.setText(roomText);
        nameTextView.setText(nameText);

        a.recycle();
    }

    @SuppressWarnings("unused")
    public void setNameText(String text) {
        nameTextView.setText(text.substring(0, Math.min(text.length(), 8)));
    }
    @SuppressWarnings("unused")
    public void setRoomText(String text) {
        roomTextView.setText(text);
    }

    @SuppressWarnings("unused")
    public String getRoomText() {
        return roomTextView.getText().toString();
    }

    @SuppressWarnings("unused")
    public String getNameText() {
        return nameTextView.getText().toString();
    }

}
