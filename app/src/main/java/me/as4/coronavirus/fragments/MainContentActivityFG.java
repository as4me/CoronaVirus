package me.as4.coronavirus.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Random;

import me.as4.coronavirus.activities.MainActivity;
import me.as4.coronavirus.activities.MapsActivity;
import me.as4.coronavirus.R;
import me.as4.coronavirus.models.RealTimeDatabase;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
// Defines the xml file for the fragment

public class MainContentActivityFG extends Fragment {

    private Context thiscontext;
    private ImageView imageViewmap;
    private SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";

    private TextView total_deaths;
    private TextView total_recovered;
    private TextView total_confirmed;
    private int a,b,c;
    private DatabaseReference mDatabase;
    private ArrayList<RealTimeDatabase> realTimeDatabases;
    private ArrayList<Integer> dataGraph;
    private ArrayList<Integer> dataGraphRecovered;

    private String date;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);
        thiscontext = getContext();
        final SharedPreferences  mPrefs = getActivity().getPreferences(MODE_PRIVATE);
        realTimeDatabases = new ArrayList<>();
        dataGraph = new ArrayList<>();
        dataGraphRecovered = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mSettings = thiscontext.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);

        total_deaths = view.findViewById(R.id.total_deaths);
        total_recovered = view.findViewById(R.id.total_recovered);
        total_confirmed = view.findViewById(R.id.total_confirmed);

        Log.d("DATA", String.valueOf(mSettings.getInt("deaths",0)));
        /*


*/
        a = mSettings.getInt("deaths",0);
        b = mSettings.getInt("recover",0);
        c =  mSettings.getInt("confirmed",0);
        date = mSettings.getString("date","");

        total_deaths.setText("Total deaths: " + String.valueOf(a));
        total_recovered.setText("Total recovered: " + String.valueOf(b));
        total_confirmed.setText("Total confirmed: " + String.valueOf(c));
        imageViewmap = view.findViewById(R.id.imageViewmap);
        imageViewmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(thiscontext, MapsActivity.class);
                startActivity(intent);
            }
        });

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    RealTimeDatabase post = postSnapshot.getValue(RealTimeDatabase.class);
                    realTimeDatabases.add(post);
                    Log.d( "DATA",   String.valueOf(postSnapshot.getKey())  + ": "+ String.valueOf(post.confirmed));

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };


        mDatabase.child("data/date/"+ date +"/country").addValueEventListener(postListener);

        ValueEventListener postListenerGraph = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    RealTimeDatabase post = postSnapshot.getValue(RealTimeDatabase.class);

                    Log.d( "DATAGRAPH",   String.valueOf(postSnapshot.getKey())  + ": "+ String.valueOf(post.confirmed));
                    dataGraph.add(post.confirmed);
                    dataGraphRecovered.add(post.recover);

                }

                init(view);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.child("data/date").addValueEventListener(postListenerGraph);

        return view;
    }

    private void init(View view) {
        Log.d("DATAGRAPH", String.valueOf(dataGraph.size()));




        GraphView graph = view.findViewById(R.id.graph);
        graph.setTitle("Dashbord graph");
        graph.getViewport().setScrollable(false);


        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(generateData(dataGraphRecovered));
        graph.addSeries(series);
        series.setColor(Color.GREEN);


        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>(generateData(dataGraph));
        graph.addSeries(series1);

        series1.setColor(Color.RED);
        graph.getViewport().setMaxX(dataGraph.size()+1);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Days");
    }

    private DataPoint[] generateData(ArrayList<Integer> dataGraphf) {
        DataPoint[] values = new DataPoint[dataGraphf.size()];
        for (int i = 0; i< dataGraphf.size(); i++) {
            int x = i;
            Log.d("DATAGRAPH", String.valueOf(x) + " : " + dataGraphf.get(i));

            DataPoint v = new DataPoint(x, dataGraphf.get(i));
            values[i] = v;
        }
        return values;
    }
}
