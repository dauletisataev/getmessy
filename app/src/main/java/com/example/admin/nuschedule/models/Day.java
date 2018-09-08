package com.example.admin.nuschedule.models;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.admin.nuschedule.other.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Lenovo on 06.02.2018.
 */
public class Day {
    Utils utils = new Utils();
    String name;
    List<Lesson> lessons;

    public Day(){

    }
    public Day(String name, List<Lesson> lessons) {
        this.name = name;
        this.lessons = lessons;
    }
    public Day(String name) {
        this.name = name;
        this.lessons = new ArrayList<>();
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

    public void addLesson(Lesson lesson){
        this.lessons.add(lesson);
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
    public Lesson intersect(int lessonStr){
        Log.d("DAY INTERSECTION:", "got lessonStr: "+lessonStr);
        //int lessonMin = utils.getMinutes(lesson.getStartTime());
        for(Lesson ls : lessons){
            int lsStrTime =  utils.getMinutes(ls.getStartTime());
            int lsEndTime =  utils.getMinutes(ls.getEndTime());
            Log.d("DAY INTERSECTION:", "Comparing: "+lsStrTime+" <> "+lessonStr+" <> "+lsEndTime);
            if( lessonStr >= lsStrTime && lessonStr <= lsEndTime){
                return ls;
            }
        }
        return null;
    }

    public boolean deleteLesson(int lessonId){

        for (int i = 0; i < lessons.size(); i++) {
            if(lessons.get(i).getId() == lessonId){
                lessons.remove(lessons.get(i));
                return true;
            }
        }

        return false;
    }
}