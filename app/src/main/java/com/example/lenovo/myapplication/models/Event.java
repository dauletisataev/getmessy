package com.example.lenovo.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Lenovo on 06.02.2018.
 */

public class Event implements Parcelable {
    String  name,
            club_id,
            description,
            contacts,
            location,
            reg_date,
            logo,
            banner;
    EventDetails details;

    public Event(){

    }
    protected Event(Parcel in) {
        String[] data = new String[12];

        in.readStringArray(data);
        name = data[0];
        club_id= data[1];
        description= data[2];
        contacts= data[3];
        location= data[4];
        reg_date= data[5];
        logo= data[6];
        banner= data[7];
        details = new EventDetails(data[8],data[9],data[10],data[11]);
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {
                name,
                club_id,
                description,
                contacts,
                location,
                reg_date,
                logo,
                banner,
                details.getStart_date(),
                details.getEnd_date(),
                details.getStart_time(),
                details.getEnd_time()
        });
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public String getName() {
        return name;
    }

    public String getClub_id() {
        return club_id;
    }

    public String getDescription() {
        return description;
    }

    public String getContacts() {
        return contacts;
    }

    public String getLocation() {
        return location;
    }

    public String getReg_date() {
        return reg_date;
    }

    public String getLogo() {
        return logo;
    }

    public String getBanner() {
        return banner;
    }

    public EventDetails getDetails() {
        return details;
    }
    @Override
    public String toString() {
        String result="";
        result = name+" "+details;
        return result;
    }


}
