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
        return view;
    }
}
