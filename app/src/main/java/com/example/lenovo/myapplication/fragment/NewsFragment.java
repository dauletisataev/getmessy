package com.example.lenovo.myapplication.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lenovo.myapplication.R;
import com.example.lenovo.myapplication.activity.EventDetails;
import com.example.lenovo.myapplication.adapters.RVAdapter_EVENTS;
import com.example.lenovo.myapplication.interfaces.RecyclerViewClickListener;
import com.example.lenovo.myapplication.models.Event;
import com.example.lenovo.myapplication.models.Lesson;
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
public class NewsFragment extends Fragment implements RecyclerViewClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private List<Event> events;
    RecyclerView rv;
    ProgressDialog progress;
    public NewsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        rv = (RecyclerView) view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        initializeData();
        return view;

    }
    public void initializeData(){
        showProgress();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("events");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                events = new ArrayList<>();
                hideProgress();
                if(dataSnapshot.getChildrenCount() == 0){
                    Toast.makeText(getActivity(), "No events", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (DataSnapshot event: dataSnapshot.getChildren()) {

                    List<Lesson> dayLessons =  new ArrayList<>();
                    Event newEvent = (Event) event.getValue(Event.class);
                    events.add( newEvent);
                }
                Log.d("events: ", String.valueOf(events));
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
        RVAdapter_EVENTS adapter = new RVAdapter_EVENTS(events, this);
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


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        Intent intent = new Intent(getContext(), EventDetails.class);
        intent.putExtra("event", events.get(position));
        startActivity(intent);

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
