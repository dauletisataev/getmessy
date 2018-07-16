package com.example.lenovo.myapplication.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.lenovo.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;
import android.view.View;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddActivity extends AppCompatActivity implements View.OnClickListener{
    TextView startTime, endTime;
    EditText title, location, description;

    ProgressDialog progress;

    Button add;
    String day = "Monday";
    int DIALOG_TIME = 1;
    Date today = Calendar.getInstance().getTime();
    String[] data = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

    HashMap<String, Object> result;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        startTime = (TextView) findViewById(R.id.startTime);
        endTime = (TextView) findViewById(R.id.endTime);

        add = (Button) findViewById(R.id.add);

        title = (EditText) findViewById(R.id.title) ;
        location = (EditText) findViewById(R.id.location) ;
        description = (EditText) findViewById(R.id.description) ;

        startTime.setOnClickListener(this);
        startTime.setText("9:00 AM");

        endTime.setOnClickListener(this);
        endTime.setText("10:00 AM");

        add.setOnClickListener(this);

        result = new HashMap<>();
        result.put("startTime", "09:00 AM");
        result.put("endTime", "10:00 AM");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // адаптер

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        // заголовок
        spinner.setPrompt("Choose a day");
        // выделяем элемент
        spinner.setSelection(0);
        // устанавливаем обработчик нажатия
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                day = data[position];

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    Calendar myCalendar = Calendar.getInstance();
    @Override
    public void onClick(View view) {
        Calendar mcurrentTime;
        int hour, minute;
        TimePickerDialog mTimePicker;

        switch (view.getId()){
            case R.id.startTime:
                mcurrentTime = Calendar.getInstance();
                hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                minute = mcurrentTime.get(Calendar.MINUTE);
                mTimePicker = new TimePickerDialog( this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time =  getTimeType(selectedHour, selectedMinute);
                        startTime.setText( time);
                        result.put("startTime", time);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                break;
            case R.id.endTime:
                  mcurrentTime = Calendar.getInstance();
                  hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                  minute = mcurrentTime.get(Calendar.MINUTE);
                  mTimePicker = new TimePickerDialog( this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time =  getTimeType(selectedHour, selectedMinute);
                        endTime.setText( time);
                        result.put("endTime", time);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                break;
            case R.id.add:
                result.put("room", location.getText().toString());
                result.put("description", description.getText().toString());
                result.put("title", title.getText().toString());
                String key = mDatabase.child("schedules").push().getKey();
                Map<String, Object> childUpdates = new HashMap<>();
                Log.d("pushToFirebase", result.toString());
                childUpdates.put("/schedules/"+getId()+"/"+day+"/"+key , result);
                showProgress();
                mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideProgress();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Some error occured", Toast.LENGTH_SHORT).show();
                        hideProgress();
                    }
                });

                break;

        }
    }
    private String getId(){
        SharedPreferences sharedPref =   getSharedPreferences("nuschedule",Context.MODE_PRIVATE);
        String id = sharedPref.getString("cUserId", "201599251");
        return id;
    }

    private void showProgress(){
        progress = new ProgressDialog(this);
        progress.setMessage("Its loading....");
        progress.setTitle("Please, wait!");
        progress.show();
    }
    private void hideProgress(){
        progress.dismiss();
    }
    private String getTimeType(int hours, int minutes){
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
            final Date dateObj = sdf.parse(String.valueOf(hours)+":"+String.valueOf(minutes));
            System.out.println(dateObj);
            return new SimpleDateFormat("hh:mm aa").format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
            return "err";
        }
    }
    private void updateLabel(int type) {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
    }
}
