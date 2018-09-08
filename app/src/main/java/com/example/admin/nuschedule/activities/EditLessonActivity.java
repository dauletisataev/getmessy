package com.example.admin.nuschedule.activities;

import android.app.Activity;
import android.app.TimePickerDialog;
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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.admin.nuschedule.R;
import com.example.admin.nuschedule.database.DBHelper;
import com.example.admin.nuschedule.models.Lesson;
import com.example.admin.nuschedule.other.Utils;
import com.example.admin.nuschedule.view.SquareImage;

import java.util.ArrayList;
import java.util.Calendar;

import petrov.kristiyan.colorpicker.ColorPicker;

public class EditLessonActivity extends AppCompatActivity implements View.OnClickListener{
    public String STUDENT_ID = "201599251";
    Toolbar toolbar;
    TextView startTime, endTime;
    SquareImage colorPickerBtn;
    EditText mTitle, mDescription, mType, mInstructor, mRoom;
    RadioGroup radioGroup;
    Lesson lesson;
    int lessonId;
    DBHelper dbHelper;
    SQLiteDatabase SQLdatabase;
    ArrayList<String> daysChecked;
    Utils utils = new Utils();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lesson);
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("New lesson");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        Log.d("mLog", "Build SDK version: "+ android.os.Build.VERSION.SDK_INT);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            findViewById(R.id.shadow_view).setVisibility(View.GONE);
        }

        lessonId = getIntent().getIntExtra("lessonId", -1);
        Log.d("mLog", "recieved ID: "+ lessonId);
        lesson = getLesson(lessonId);
        initializeViews();
        setValues(lesson);
    }

    @Override
    public void onClick(View v) {
        Calendar mcurrentTime;
        int hour, minute;
        TimePickerDialog mTimePicker;

        switch (v.getId()) {
            case R.id.startTime:
                mcurrentTime = Calendar.getInstance();
                hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                minute = mcurrentTime.get(Calendar.MINUTE);
                mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = utils.getTimeType(selectedHour, selectedMinute);
                        startTime.setText(time);
                        lesson.setStartTime(time);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                break;
            case R.id.endTime:
                mcurrentTime = Calendar.getInstance();
                hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                minute = mcurrentTime.get(Calendar.MINUTE);
                mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = utils.getTimeType(selectedHour, selectedMinute);
                        endTime.setText(time);
                        lesson.setEndTime(time);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                break;
            case R.id.color_picker_btn:
                ColorPicker colorPicker = new ColorPicker(EditLessonActivity.this);
                colorPicker.setColors(getResources().getIntArray(R.array.mainColors));
                colorPicker.show();
                colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position,int mColor) {
                        lesson.setColor( mColor);
                        changeTintToColor(mColor);
                    }

                    @Override
                    public void onCancel(){
                        // put code
                    }
                });
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_event, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }
        if (id == R.id.save_button) {
            if (isFilledCorrect()) {
                initializeDB(STUDENT_ID);
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_STARTTIME, lesson.getStartTime());
                contentValues.put(DBHelper.KEY_ENDTIME, lesson.getEndTime());
                contentValues.put(DBHelper.KEY_TITLE, mTitle.getText().toString());
                contentValues.put(DBHelper.KEY_TYPE, mType.getText().toString());
                contentValues.put(DBHelper.KEY_DESCRIPTION, mDescription.getText().toString());
                contentValues.put(DBHelper.KEY_INSTRUCTOR, mInstructor.getText().toString());
                contentValues.put(DBHelper.KEY_ROOM, mRoom.getText().toString());
                contentValues.put(DBHelper.KEY_DAY, lesson.getDay());
                contentValues.put(DBHelper.KEY_COLOR, lesson.getColor());
                SQLdatabase.update(dbHelper.TABLE_SCHEDULE_ID, contentValues, "_id="+lesson.getId(), null);
                dbHelper.close();
                Intent intent = new Intent();
                intent.putExtra("lessonId", lesson.getId());
                intent.putExtra("day", lesson.getDay());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void initializeViews(){
        startTime = (TextView) findViewById(R.id.startTime);
        endTime = (TextView) findViewById(R.id.endTime);

        mTitle = (EditText) findViewById(R.id.lesson_title);
        mDescription = (EditText) findViewById(R.id.lesson_description);
        mType = (EditText) findViewById(R.id.lesson_type);
        mInstructor = (EditText) findViewById(R.id.lesson_instructor);
        mRoom = (EditText) findViewById(R.id.lesson_room);
        radioGroup = (RadioGroup)  findViewById(R.id.radio_group);
        colorPickerBtn = (SquareImage) findViewById(R.id.color_picker_btn);
    }

    private void setValues(final Lesson lesson){
        getSupportActionBar().setTitle(lesson.getTitle());
        toolbar.setBackgroundColor(lesson.getColor());
        changeTintToColor(lesson.getColor());

        startTime.setText(lesson.getStartTime());
        startTime.setOnClickListener(this);
        endTime.setText(lesson.getEndTime());
        endTime.setOnClickListener(this);
        colorPickerBtn.setOnClickListener(this);
        mTitle.setText(lesson.getTitle());
        mDescription.setText(lesson.getDescription());
        mType.setText(lesson.getType());
        mInstructor.setText(lesson.getInstructor());
        mRoom.setText(lesson.getRoom());
        radioGroup.check((radioGroup.getChildAt(lesson.getDayIndex())).getId());
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                lesson.setDay((String) ((RadioButton) findViewById(checkedId)).getTag());
            }
        });
    }
    private Lesson getLesson(int lessonId){
        initializeDB(STUDENT_ID);
        Lesson mLesson = null;
        final Cursor cursor = SQLdatabase.rawQuery("SELECT * FROM "+ DBHelper.TABLE_SCHEDULE_ID+" WHERE _id = " + lessonId, null);
        Log.d("mLog", "count cursor: "+ cursor.getCount());
        if (cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int dayIndex = cursor.getColumnIndex(DBHelper.KEY_DAY);
            int startIndex = cursor.getColumnIndex(DBHelper.KEY_STARTTIME);
            int endIndex = cursor.getColumnIndex(DBHelper.KEY_ENDTIME);
            int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
            int typeIndex = cursor.getColumnIndex(DBHelper.KEY_TYPE);
            int descIndex = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION);
            int roomIndex = cursor.getColumnIndex(DBHelper.KEY_ROOM);
            int instructorIndex = cursor.getColumnIndex(DBHelper.KEY_INSTRUCTOR);
            int colorIndex = cursor.getColumnIndex(DBHelper.KEY_COLOR);
            do{
                Log.d("mLog", "_id= "+cursor.getInt(idIndex)+", day = " + cursor.getString(dayIndex) +
                        ", startTime = " + cursor.getString(startIndex) +
                        ", title = " + cursor.getString(titleIndex)+
                        ", endTime = " + cursor.getString(endIndex));
                mLesson = new Lesson(cursor.getString(titleIndex), cursor.getString(startIndex),cursor.getString(endIndex),cursor.getString(instructorIndex),cursor.getString(roomIndex),cursor.getString(dayIndex), cursor.getInt(colorIndex));
                mLesson.setType(cursor.getString(typeIndex));
                mLesson.setDescription(cursor.getString(descIndex));
                mLesson.setId(cursor.getInt(idIndex));
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
    private void changeTintToColor(int tintColor){
        Log.d("mLog", "changeTintToColor: " + tintColor);
        ((SquareImage) findViewById(R.id.color_picker_btn)).setColorFilter(tintColor);
        ((SquareImage) findViewById(R.id.img_info)).setColorFilter(tintColor);
        ((SquareImage) findViewById(R.id.img_person)).setColorFilter(tintColor);
        ((SquareImage) findViewById(R.id.img_place)).setColorFilter(tintColor);
        ((TextView) findViewById(R.id.choose_txt)).setTextColor(tintColor);
        toolbar.setBackgroundColor(tintColor);

    }
    private boolean isFilledCorrect(){

        if( mTitle.getText().toString().trim().length() == 0 || mRoom.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Please, fill the required fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if( utils.getMinutes(startTime.getText().toString()) < 540 || utils.getMinutes(startTime.getText().toString())>1260
                || utils.getMinutes(endTime.getText().toString()) < 540 || utils.getMinutes(endTime.getText().toString())>1260) {
            Toast.makeText(this, "The time chosen is out of allowed scope (9:00 AM to 9:00 PM)", Toast.LENGTH_SHORT).show();
            return false;
        }
        if( utils.getMinutes(startTime.getText().toString()) >= utils.getMinutes(endTime.getText().toString()) ){
            Toast.makeText(this, "The time chosen does not follow logical order", Toast.LENGTH_SHORT).show();
            return false;
        }
        if( (utils.getMinutes(endTime.getText().toString()) - utils.getMinutes(startTime.getText().toString()) ) <15){
            Toast.makeText(this, "The chosen time interval is too short", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
