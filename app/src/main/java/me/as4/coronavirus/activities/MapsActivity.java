package me.as4.coronavirus.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.UiSettings;
import com.mapbox.mapboxsdk.style.layers.Layer;

import java.util.ArrayList;
import java.util.List;

import me.as4.coronavirus.R;
import me.as4.coronavirus.fragments.MainContentActivityFG;
import me.as4.coronavirus.models.RealTimeDatabase;

import static android.content.ContentValues.TAG;

public class MapsActivity extends AppCompatActivity {

    private MapView mapView;
    private DatabaseReference mDatabase;

    // это будет именем файла настроек
    public static final String APP_PREFERENCES = "mysettings";
    private SharedPreferences mSettings;
    private String date;
    private ArrayList<RealTimeDatabase> realTimeDatabases;
    private ArrayList<String> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_maps);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final SharedPreferences  mPrefs = getPreferences(MODE_PRIVATE);
        mSettings = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        realTimeDatabases = new ArrayList<>();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        date = mSettings.getString("date","");
        arrayList = new ArrayList<>();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    RealTimeDatabase post = postSnapshot.getValue(RealTimeDatabase.class);
                    realTimeDatabases.add(post);
                    Log.d( "DATA",   String.valueOf(postSnapshot.getKey())  + ": "+ String.valueOf(post.confirmed));
                    arrayList.add(postSnapshot.getKey());
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


        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                String uniqueStyleUrl = "mapbox://styles/napsterier/ck6et7zsg19gp1inhjjlt2zpp";
                mapboxMap.setStyle(new Style.Builder().fromUri(uniqueStyleUrl), new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                        for (int i = 0; i < realTimeDatabases.size(); i++) {
                            mapboxMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(realTimeDatabases.get(i).getLat(), realTimeDatabases.get(i).getLon()))
                                    .title(arrayList.get(i) + "\n\nConfirmed: "+realTimeDatabases.get(i).getConfirmed()+"\nRecovered: " + realTimeDatabases.get(i).getRecover()+"\nDeaths: " + realTimeDatabases.get(i).getDeath()));
                        }
                        CameraPosition position = new CameraPosition.Builder()
                                .target(new LatLng(30.581974, 114.328475))
                                .zoom(3)
                                .tilt(20)
                                .build();
                        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 300);

                    }
                });
            }
        });
    }

    // Add the mapView lifecycle to the activity's lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
