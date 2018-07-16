package com.example.lenovo.myapplication;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.database.sqlite.SQLiteDatabase;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.lenovo.myapplication.activity.MainActivity;
import com.example.lenovo.myapplication.models.Day;

public class  Reciever extends BroadcastReceiver {


    SQLiteDatabase db;
    SharedPreferences sPref;
    List<Day> days;
    String notifid;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("NOTIFICATION", "onReceive: Notificatoin recieved");
        String action = intent.getAction();
        //sPref = context.getSharedPreferences("nuschedule" , Context.MODE_PRIVATE);

         if(action.equals("PREVIOUS")) {
             Toast.makeText(context, "PREVIOUS", Toast.LENGTH_SHORT);
/*
             String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
             sendnotif();
             sPref = context.getSharedPreferences("mypreferences" , Context.MODE_PRIVATE);
             Editor ed = sPref.edit();
             ed.putString("notID", String.valueOf(curjj));
             ed.commit();*/

        } else if(action.equals("CLOSE")) {
             //Toast.makeText(context, "close" , Toast.LENGTH_SHORT).show();
             NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                nm.cancelAll();
        } else if(action.equals("NEXT")) {
             Toast.makeText(context, "next", Toast.LENGTH_SHORT);
             /*String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
             int nid = Integer.valueOf(notifid);
             int curjj=nid;
             if (nid>3 ) curjj=0;
             else curjj  = nid+1;

             ArrayList<String> lessons = new ArrayList<String>();
             String[] les = new String[9];
             for (int i = 0; i < 9; i++) {
                 les[i] = tmtbl.get(i)[curjj];
                 if (!les[i].contains("---"))  lessons.add(les[i]);

             }
             sendnotif(context, days[curjj],lessons);
             sPref = context.getSharedPreferences("mypreferences" , Context.MODE_PRIVATE);
             Editor ed = sPref.edit();
             ed.putString("notID", String.valueOf(curjj));
             ed.commit();*/

        }


    }
    public void sendnotif(Context context){
        Day day = days.get(0);
        String PREVIOUS = "PREVIOUS", CLOSE="CLOSE", NEXT="NEXT";
        Intent intent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

        Notification.Builder builder = new Notification.Builder(context);

        builder.setAutoCancel(false);
        builder.setTicker("Lessons for "+ day.getName());
        builder.setContentTitle("Lessons for "+day.getName());
        builder.setContentText("You have "+String.valueOf(day.getCount())+" lessons");
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);
        Intent yesReceive = new Intent();
        yesReceive.setAction(PREVIOUS);
        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(0, "Previous", pendingIntentYes);

//Maybe intent
        Intent maybeReceive = new Intent();
        maybeReceive.setAction(CLOSE);
        PendingIntent pendingIntentMaybe = PendingIntent.getBroadcast(context, 12345, maybeReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(0, "Close", pendingIntentMaybe);

//No intent
        Intent noReceive = new Intent();
        noReceive.setAction(NEXT);
        PendingIntent pendingIntentNo = PendingIntent.getBroadcast(context, 12345, noReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(0, "Next", pendingIntentNo);


        //builder.setNumber(100);
        builder.build();
        Notification.InboxStyle inboxStyle = new Notification.InboxStyle();

        inboxStyle.setBigContentTitle(day.getName());
        for (int i=0;i<day.getCount();i++){
            inboxStyle.addLine(day.getLessons().get(i).getTitle()+" "+day.getLessons().get(i).getRoom());
        }
        builder.setStyle(inboxStyle);
        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(12 , notification);

        ComponentName receiver = new ComponentName(context, Reciever.class);

        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

}