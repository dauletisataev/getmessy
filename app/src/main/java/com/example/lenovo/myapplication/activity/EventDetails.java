package com.example.lenovo.myapplication.activity;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lenovo.myapplication.R;
import com.example.lenovo.myapplication.models.Event;
import com.example.lenovo.myapplication.models.Lesson;
import com.example.lenovo.myapplication.other.CircleTransform;

public class EventDetails extends AppCompatActivity {
    TextView name, time, startTime, endTime, description, organizer, contacts, location;
    ImageView logo, banner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Bundle data = getIntent().getExtras();
        Event event = (Event) data.getParcelable("event");

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        logo = (ImageView) findViewById(R.id.logo);
        banner = (ImageView) findViewById(R.id.banner);

        name = (TextView) findViewById(R.id.event_name);
        time = (TextView) findViewById(R.id.time);
        organizer= (TextView) findViewById(R.id.organizer);
        description= (TextView) findViewById(R.id.description);
        startTime= (TextView) findViewById(R.id.startTime);
        endTime= (TextView) findViewById(R.id.endTime);
        contacts= (TextView) findViewById(R.id.contacts);
        location= (TextView) findViewById(R.id.location);

        name.setText(event.getName());

        if (getSupportActionBar() != null) getSupportActionBar().setTitle(event.getName());

        time.setText(event.getDetails().getStart_date());
        organizer.setText(event.getClub_id());
        description.setText(event.getDescription());
        startTime.setText(event.getDetails().getStart_time());
        endTime.setText(event.getDetails().getEnd_time());
        contacts.setText(event.getContacts());
        location.setText(event.getLocation());

        Glide.with(this).load(event.getLogo())
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(logo);

        Glide.with(this).load(event.getBanner())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(banner);
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
