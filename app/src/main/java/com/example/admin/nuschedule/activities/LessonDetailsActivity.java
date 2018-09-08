package com.example.admin.nuschedule.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.nuschedule.R;
import com.example.admin.nuschedule.database.DBHelper;
import com.example.admin.nuschedule.models.Lesson;
import com.example.admin.nuschedule.view.LessonEventView;
import com.example.admin.nuschedule.view.SquareImage;

public class LessonDetailsActivity extends AppCompatActivity {
    public String STUDENT_ID = "201599251";
    DBHelper dbHelper;
    SQLiteDatabase SQLdatabase;
    int lessonId;
    Toolbar toolbar;
    Lesson lesson;
    TextView lesson_title, lesson_days, lesson_time, lesson_type, lesson_instructor, lesson_room;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_details);
        initializeDB(STUDENT_ID);

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            findViewById(R.id.shadow_view).setVisibility(View.GONE);
        }
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lessonId = getIntent().getIntExtra("lessonId", -1);
        Log.d("mLog", "recieved ID: "+ lessonId);
        lesson = getLesson(lessonId);
        initializeViews();
        setValues(lesson);


    }

    private Lesson getLesson(int lessonId){
        Lesson mLesson = null;
        final Cursor cursor = SQLdatabase.rawQuery("SELECT * FROM "+DBHelper.TABLE_SCHEDULE_ID+" WHERE _id = " + lessonId, null);
        Log.d("mLog", "count cursor: "+ cursor.getCount());
        if (cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int dayIndex = cursor.getColumnIndex(DBHelper.KEY_DAY);
            int startIndex = cursor.getColumnIndex(DBHelper.KEY_STARTTIME);
            int endIndex = cursor.getColumnIndex(DBHelper.KEY_ENDTIME);
            int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
            int roomIndex = cursor.getColumnIndex(DBHelper.KEY_ROOM);
            int instructorIndex = cursor.getColumnIndex(DBHelper.KEY_INSTRUCTOR);
            int colorIndex = cursor.getColumnIndex(DBHelper.KEY_COLOR);
            int typeIndex = cursor.getColumnIndex(DBHelper.KEY_TYPE);
            int descIndex = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION);
            do{
                Log.d("mLog", "_id= "+cursor.getInt(idIndex)+", day = " + cursor.getString(dayIndex) +
                        ", startTime = " + cursor.getString(startIndex) +
                        ", title = " + cursor.getString(titleIndex)+
                        ", endTime = " + cursor.getString(endIndex));
                mLesson = new Lesson(cursor.getString(titleIndex), cursor.getString(startIndex),cursor.getString(endIndex),cursor.getString(instructorIndex),cursor.getString(roomIndex),cursor.getString(dayIndex), cursor.getInt(colorIndex));
                mLesson.setId(cursor.getInt(idIndex));
                mLesson.setType(cursor.getString(typeIndex));
                mLesson.setDescription(cursor.getString(descIndex));
            }
            while(cursor.moveToNext());
        }cursor.close();
        dbHelper.close();
        return mLesson;
    }

    private void initializeDB(String id){
        dbHelper = new DBHelper(this, id);
        SQLdatabase = dbHelper.getWritableDatabase();
    }

    private void initializeViews(){
        lesson_title = (TextView) findViewById(R.id.lesson_title);
        lesson_days = (TextView) findViewById(R.id.lesson_days);
        lesson_time = (TextView) findViewById(R.id.lesson_time);
        lesson_type = (TextView) findViewById(R.id.lesson_type);
        lesson_instructor = (TextView) findViewById(R.id.lesson_instructor);
        lesson_room = (TextView) findViewById(R.id.lesson_room);
    }

    private void setValues(Lesson lesson){
       getSupportActionBar().setTitle(lesson.getTitle());
       toolbar.setBackgroundColor(lesson.getColor());
       lesson_title.setText(lesson.getTitle());
       lesson_days.setText(lesson.getDay());
       lesson_time.setText(lesson.getStartTime()+" - "+lesson.getEndTime());
       lesson_type.setText(lesson.getType());
       lesson_instructor.setText(lesson.getInstructor());
       lesson_room.setText(lesson.getRoom());
       changeTintToColor(lesson.getColor());
    }

    private void changeTintToColor(int tintColor){
        ((SquareImage) findViewById(R.id.img_title)).setColorFilter(tintColor);
        ((SquareImage) findViewById(R.id.img_day)).setColorFilter(tintColor);
        ((SquareImage) findViewById(R.id.img_time)).setColorFilter(tintColor);
        ((SquareImage) findViewById(R.id.img_info)).setColorFilter(tintColor);
        ((SquareImage) findViewById(R.id.img_person)).setColorFilter(tintColor);
        ((SquareImage) findViewById(R.id.img_place)).setColorFilter(tintColor);
        toolbar.setBackgroundColor(tintColor);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lesson_details, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }
        if (id == R.id.edit_btn) {
            Intent intent = new Intent();
            intent.putExtra("btnType", 1);
            intent.putExtra("resultId", lessonId);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
        if (id == R.id.delete_button) {
            Intent intent = new Intent();
            intent.putExtra("btnType", 0);
            intent.putExtra("resultId", lessonId);
            intent.putExtra("day", lesson.getDay());
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
