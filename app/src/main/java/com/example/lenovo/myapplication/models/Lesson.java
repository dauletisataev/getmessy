package com.example.lenovo.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by Lenovo on 07.02.2018.
 */

public class Lesson implements Parcelable {
    private String title, startTime, endTime, instructor, room;
    public Lesson(){

    }
    public Lesson(HashMap<String, Object> lesson) {
        this.title = lesson.get("title").toString();
        this.startTime = lesson.get("startTime").toString();
        this.endTime = lesson.get("endTime").toString();
        this.instructor = lesson.get("instructor").toString();
        this.room = lesson.get("room").toString();

    }

    protected Lesson(Parcel in) {
        String[] data = new String[5];

        in.readStringArray(data);
        title = data[0];
        startTime = data[1];
        endTime = data[2];
        instructor = data[3];
        room = data[4];
    }

    public static final Creator<Lesson> CREATOR = new Creator<Lesson>() {
        @Override
        public Lesson createFromParcel(Parcel in) {
            return new Lesson(in);
        }

        @Override
        public Lesson[] newArray(int size) {
            return new Lesson[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {this.title,
                this.startTime,
                this.endTime, this.instructor, this.room});
    }
}
