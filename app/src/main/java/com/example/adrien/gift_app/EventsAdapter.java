package com.example.adrien.gift_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventsAdapter extends ArrayAdapter<Event> {

    private DatabaseReference mDatabase;

    public EventsAdapter(Context context, ArrayList<Event> events){
        super(context, 0, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Event event = getItem(position); //get current item
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(convertView == null){ //check that a view is always with a layout
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_events_item, parent, false);
        }
        TextView tvTitle = (TextView)convertView.findViewById(R.id.card_title);
        TextView tvPlace = (TextView)convertView.findViewById(R.id.card_loc_field);
        TextView tvDate = (TextView)convertView.findViewById(R.id.card_date_field);
        ImageView crossButton = (ImageView)convertView.findViewById(R.id.card_cross);

        // To fill field with datas
        tvTitle.setText(event.getTitle());
        tvDate.setText(event.getDate());
        tvPlace.setText(event.getPlace());

        // Manage delete events button
        crossButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener(){ //Dialog to confirm a removal
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                event.removeFromFirebase(mDatabase); //remove item from database
                                Toast.makeText(getContext(),"Idée supprimée", Toast.LENGTH_SHORT).show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Etes-vous sûr de vouloir supprimer l'évènement ?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
        return convertView;
    }
}