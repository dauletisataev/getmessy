package com.example.lenovo.myapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.lenovo.myapplication.R;
import com.example.lenovo.myapplication.models.Lesson;

public class LessonsDetails extends AppCompatActivity {
    TextView title, startTime, endTime, instructor, room;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_details);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        Bundle data = getIntent().getExtras();
        Lesson lesson = (Lesson) data.getParcelable("lesson");
        title = (TextView) findViewById(R.id.title);
        startTime = (TextView) findViewById(R.id.startTime);
        endTime = (TextView) findViewById(R.id.endTime);
        instructor = (TextView) findViewById(R.id.instructor);
        room = (TextView) findViewById(R.id.room);

        title.setText(lesson.getTitle());

        if (getSupportActionBar() != null) getSupportActionBar().setTitle(lesson.getTitle());

        startTime.setText(lesson.getStartTime());
        endTime.setText(lesson.getEndTime());
        instructor.setText(lesson.getInstructor());
        room.setText(lesson.getRoom());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
              this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
