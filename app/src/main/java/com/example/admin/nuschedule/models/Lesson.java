package com.example.admin.nuschedule.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Lenovo on 07.02.2018.
 */
public class Lesson implements Parcelable {
    private int id;
    private String startTime;
    private String title, endTime, instructor, room, type, description;
    private String day;
    private int color;

    public Lesson(){

    }
    public Lesson(HashMap<String, Object> lesson) {
        this.title = lesson.get("title").toString();
        this.startTime = lesson.get("startTime").toString();
        this.endTime = lesson.get("endTime").toString();
        this.instructor = lesson.get("instructor").toString();
        this.room = lesson.get("room").toString();

    }
    public Lesson(String tite, String startTime, String endTime,String instructor,String room,String day, int color) {
        this.title = tite;
        this.startTime = startTime;
        this.endTime = endTime;
        this.instructor = instructor;
        this.room = room;
        this.day = day;
        this.color = color;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String toString(){
        return "{"+startTime+"; "+title+"; "+room+"; "+type+"}";
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
    public int getDayIndex(){
        String[] dayOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        return Arrays.asList(dayOfWeek).indexOf(day);
    }
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Lesson)) return false;
        Lesson o = (Lesson) obj;
        return o.getId() == this.getId();
    }
}