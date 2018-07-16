package com.example.lenovo.myapplication.models;

import java.util.List;

/**
 * Created by Lenovo on 06.02.2018.
 */

public class EventDetails {
    String start_date, end_date, start_time, end_time;

    public EventDetails(){

    }

    public EventDetails(String start_date, String end_date, String start_time, String end_time) {
        this.start_date = start_date;
        this.end_date = end_date;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    @Override
    public String toString() {
        return "--"+start_date+"--";
    }
}
