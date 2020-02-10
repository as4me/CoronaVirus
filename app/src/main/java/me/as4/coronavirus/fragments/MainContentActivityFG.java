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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import me.as4.coronavirus.activities.MapsActivity;
import me.as4.coronavirus.R;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        thiscontext = getContext();
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

        GraphView graph = view.findViewById(R.id.graph);
        graph.setTitle("Dashbord graph");
        graph.getViewport().setScrollable(true);
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


        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(1, 278),
                new DataPoint(2, 547),
                new DataPoint(3, 639),
                new DataPoint(4, 916),
                new DataPoint(5, 2000),
                new DataPoint(6, 2700),
                new DataPoint(7, 4400),
                new DataPoint(8, 6000),
                new DataPoint(9, 7700),
                new DataPoint(10, 9700),
                new DataPoint(11, 11200),
                new DataPoint(12, 14300),
                new DataPoint(13, 17200),
                new DataPoint(14, 19700),
                new DataPoint(15, 23700),
                new DataPoint(16, 27400),
                new DataPoint(17, 34100),
                new DataPoint(18, 36800),
                new DataPoint(19, 39800),
                new DataPoint(20, 40200),
                new DataPoint(21, 40200),



        });
        graph.addSeries(series1);
        series1.setColor(Color.RED);
        return view;
    }
}
