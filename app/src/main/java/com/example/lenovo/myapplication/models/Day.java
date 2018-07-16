package com.example.lenovo.myapplication.models;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Lenovo on 06.02.2018.
 */

public class Day {
    String name;
    List<Lesson> lessons;
    int size;

    public Day(){

    }
    public Day(String name, List<Lesson> lessons) {
        this.name = name;
        this.lessons = lessons;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public int getCount(){
        return lessons.size();
    }

    @Override
    public String toString() {
        String result="";
        result = name+" ";
        return result;
    }
}
