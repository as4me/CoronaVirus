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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Random;

import me.as4.coronavirus.activities.MapsActivity;
import me.as4.coronavirus.R;
import me.as4.coronavirus.models.RealTimeDatabase;

import static android.content.ContentValues.TAG;
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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);
        thiscontext = getContext();

        realTimeDatabases = new ArrayList<>();
        dataGraph = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mSettings = thiscontext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        total_deaths = view.findViewById(R.id.total_deaths);
        total_recovered = view.findViewById(R.id.total_recovered);
        total_confirmed = view.findViewById(R.id.total_confirmed);

        Log.d("DATA", String.valueOf(mSettings.getInt("deaths",0)));
        /*


*/
        a = mSettings.getInt("deaths",0);
        b = mSettings.getInt("recover",0);
        c =  mSettings.getInt("confirmed",0);

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
        mDatabase.child("data/date/2020-03-06/country").addValueEventListener(postListener);

        ValueEventListener postListenerGraph = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    RealTimeDatabase post = postSnapshot.getValue(RealTimeDatabase.class);

                    Log.d( "DATAGRAPH",   String.valueOf(postSnapshot.getKey())  + ": "+ String.valueOf(post.confirmed));
                    dataGraph.add(post.confirmed);

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
      /*

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(1, 25),
                new DataPoint(2, 28),
                new DataPoint(3, 30),
                new DataPoint(4, 36),
                new DataPoint(5, 49),
                new DataPoint(6, 54),
                new DataPoint(7, 63),
                new DataPoint(8, 110),
                new DataPoint(9, 133),
                new DataPoint(10, 141),
                new DataPoint(11, 220),
                new DataPoint(12, 284),
                new DataPoint(13, 487),
                new DataPoint(14, 621),
                new DataPoint(15, 899),
                new DataPoint(16, 1100),
                new DataPoint(17, 1500),
                new DataPoint(18, 2000),
                new DataPoint(19, 2600),
                new DataPoint(20, 3200),
                new DataPoint(21, 3400),
        });
        graph.addSeries(series);
        series.setColor(Color.GREEN);

*/
        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>(generateData());
        graph.addSeries(series1);

        series1.setColor(Color.RED);
        graph.getViewport().setMaxX(dataGraph.size()+1);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Days");
    }

    private DataPoint[] generateData() {
        DataPoint[] values = new DataPoint[dataGraph.size()];
        for (int i=0; i<dataGraph.size(); i++) {
            int x = i;
            Log.d("DATAGRAPH", String.valueOf(x) + " : " + dataGraph.get(i));

            DataPoint v = new DataPoint(x, dataGraph.get(i));
            values[i] = v;
        }
        return values;
    }
}
