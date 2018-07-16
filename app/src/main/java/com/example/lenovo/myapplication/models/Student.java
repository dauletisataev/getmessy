package com.example.lenovo.myapplication.models;

import java.util.HashMap;

/**
 * Created by Lenovo on 06.02.2018.
 */

public class Student {
    private int id;
    private String name, surname;


    public Student(int id, String name, String surname, HashMap<String, Object> lessons) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public HashMap<String, Object>  getLessons(HashMap<String, Object> lessons){
        return lessons;
    }

}
