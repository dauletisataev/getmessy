package com.example.lenovo.myapplication.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lenovo.myapplication.R;
import com.example.lenovo.myapplication.activity.MainActivity;
import com.example.lenovo.myapplication.activity.SignInActivity;
import com.example.lenovo.myapplication.adapters.RVAdapter_LESSONS;
import com.example.lenovo.myapplication.models.Day;
import com.example.lenovo.myapplication.models.Header;
import com.example.lenovo.myapplication.models.Lesson;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LessonsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LessonsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LessonsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private List<Day> days;
    RecyclerView rv;
    ProgressDialog progress;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    public LessonsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LessonsFragment newInstance(String param1, String param2) {
        LessonsFragment fragment = new LessonsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lessons, container, false);
        rv = (RecyclerView) view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);


        SharedPreferences sharedPref = getActivity().getSharedPreferences("nuschedule",Context.MODE_PRIVATE);
        String id = sharedPref.getString("cUserId", "201599251");
        //Toast.makeText(getContext(), "got cUserId:"+ id, Toast.LENGTH_SHORT).show();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        mAuth = FirebaseAuth.getInstance();
        initializeData(id);
        return view;

    }
    public void initializeData(String id){
        showProgress();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("schedules/"+id);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                days = new ArrayList<>();
                hideProgress();
                if(dataSnapshot.getChildrenCount() == 0){
                    Toast.makeText(getActivity(), "ID does not exist", Toast.LENGTH_SHORT).show();
                    signOut();
                    return;
                }
                int id = Integer.valueOf(dataSnapshot.getKey());

                String[] dayOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
                for (int i=0; i<dayOfWeek.length; i++) {
                    DataSnapshot snapshot = dataSnapshot.child(dayOfWeek[i]);

                    List<Lesson> dayLessons =  new ArrayList<>();
                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                        Lesson lesson = postSnapshot.getValue(Lesson.class);
                        dayLessons.add(lesson);
                    }
                    Day day = new Day(snapshot.getKey(), dayLessons);
                    //Log.d("lessons in ONE DAY", dayLessons.toString());
                    days.add( day);
                }
                Log.d("firebase", "Value is: " + id);
                initializeAdapter();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("firebase", "Failed to read value.", error.toException());
            }
        });
        //persons.add(new Person("Lavery Maiss", "25 years old", R.mipmap.ic_launcher));
    }

    private void initializeAdapter(){
        RVAdapter_LESSONS adapter = new RVAdapter_LESSONS(days, LessonsFragment.this);
        rv.setAdapter(adapter);
    }
    private void showProgress(){
        progress = new ProgressDialog(getActivity());
        progress.setMessage("Its loading....");
        progress.setTitle("Please, wait!");
        progress.show();
    }
    private void hideProgress(){
        progress.dismiss();
    }


    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(),
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getContext() , SignInActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
    }

    public void sendnotif(){
        Day day = days.get(0);
        String PREVIOUS = "PREVIOUS", CLOSE="CLOSE", NEXT="NEXT";
        Intent intent = new Intent(getActivity(), MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), (int) System.currentTimeMillis(), intent, 0);

        Notification.Builder builder = new Notification.Builder(getActivity());

        builder.setAutoCancel(false);
        builder.setTicker("Lessons for "+ day.getName());
        builder.setContentTitle("Lessons for "+day.getName());
        builder.setContentText("You have "+String.valueOf(day.getCount())+" lessons");
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);
        Intent yesReceive = new Intent(getActivity(), MainActivity.class);
        yesReceive.setAction(PREVIOUS);
        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(getActivity(), (int) System.currentTimeMillis(), yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(0, "Previous", pendingIntentYes);

//Maybe intent
        Intent maybeReceive = new Intent(getActivity(), MainActivity.class);
        maybeReceive.setAction(CLOSE);
        PendingIntent pendingIntentMaybe = PendingIntent.getBroadcast(getActivity(), 12345, maybeReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(0, "Close", pendingIntentMaybe);

//No intent
        Intent noReceive = new Intent(getActivity(), MainActivity.class);
        noReceive.setAction(NEXT);
        PendingIntent pendingIntentNo = PendingIntent.getBroadcast(getActivity(), 12345, noReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(0, "Next", pendingIntentNo);


        //builder.setNumber(100);
        builder.build();
        Notification.InboxStyle inboxStyle = new Notification.InboxStyle();

        inboxStyle.setBigContentTitle(day.getName());
        for (int i=0;i<day.getCount();i++){
            inboxStyle.addLine(day.getLessons().get(i).getStartTime()+": "+day.getLessons().get(i).getTitle()+" "+day.getLessons().get(i).getRoom());
        }
        builder.setStyle(inboxStyle);
        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);

        notificationManager.notify(12 , notification);


    }
    // TODO: Rename method, update argument and hook method into UI event

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
