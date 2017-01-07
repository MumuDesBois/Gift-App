package com.example.adrien.gift_app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EventsFragment extends Fragment {

    private DatabaseReference mDatabase;
    private FirebaseUser user;

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        final Button create_event = (Button)view.findViewById(R.id.button_create_event);
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction  = fm.beginTransaction();
                Fragment addEventFragment = new EventsFormFragment();
                fragmentTransaction.replace(R.id.id_fragment_addEvents, addEventFragment);
                fragmentTransaction.commit();

                create_event.setVisibility(View.GONE);
            }
        });

        ArrayList<Event> arrayOfEvents = new ArrayList<Event>();
        final EventsAdapter adapter = new EventsAdapter(this.getContext(), arrayOfEvents);
        ListView listView = (ListView) view.findViewById(R.id.id_ListView_Events);
        listView.setAdapter(adapter);


        mDatabase.child("events").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("EventsFragment", "onChildAdded:"+ dataSnapshot.getValue(Event.class).getTitle());
                Event newEvent = dataSnapshot.getValue(Event.class);

                if(user.getUid().equals(newEvent.getCreatedBy())){
                    adapter.add(newEvent);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("EventsFragment", "onChildChanged:"+ dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("EventsFragment", "onChildRemoved:"+ dataSnapshot.getValue(Event.class).getTitle());
                Event newEvent = dataSnapshot.getValue(Event.class);

                if(user.getUid().equals(newEvent.getCreatedBy())){
                    adapter.remove(newEvent);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("EventsFragment", "onChildMoved:"+ dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}