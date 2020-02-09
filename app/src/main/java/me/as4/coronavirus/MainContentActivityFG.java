package me.as4.coronavirus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
// Defines the xml file for the fragment

public class MainContentActivityFG extends Fragment {

    private Context thiscontext;
    private ImageView imageViewmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        thiscontext = getContext();

        imageViewmap = view.findViewById(R.id.imageViewmap);
        imageViewmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(thiscontext, MapsActivity.class);
                startActivity(intent);
            }
        });
        GraphView graph = view.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 2),
                new DataPoint(2, 4),
                new DataPoint(3, 6),
                new DataPoint(4, 8)
        });
        graph.addSeries(series);

        return view;
    }
}
