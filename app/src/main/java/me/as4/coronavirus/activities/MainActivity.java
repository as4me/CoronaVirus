package me.as4.coronavirus.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.gigamole.navigationtabstrip.NavigationTabStrip;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.as4.coronavirus.R;
import me.as4.coronavirus.fragments.AboutContentActivityFG;
import me.as4.coronavirus.fragments.MainContentActivityFG;
import me.as4.coronavirus.models.APIModel;
import me.as4.coronavirus.models.Feature;
import me.as4.coronavirus.models.RealTimeDatabase;
import me.as4.coronavirus.service.AppCoronaService;
import me.as4.coronavirus.service.TinyDB;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private AppCoronaService appCoronaService;
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
    private SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";
    private List<Feature > listmode;
    private DatabaseReference mDatabase;
    public  LocalDate localDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Write a message to the database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        appCoronaService = AppCoronaService.retrofit.create(AppCoronaService.class);
        final Call<APIModel> call = appCoronaService.getData();
        listmode = new ArrayList<>();
        call.enqueue(new Callback<APIModel>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<APIModel> call, Response<APIModel> response) {
                if (response.isSuccessful()) {

                    int death = 0;
                    int recover = 0;
                    int confirmed = 0;
                    final ArrayList<String> region = new ArrayList();
                    final ArrayList<String> region_death = new ArrayList();
                    final ArrayList<String> region_recover = new ArrayList();
                    final ArrayList<String> region_confirmed = new ArrayList();
                    final ArrayList<String> region_lat = new ArrayList();
                    final ArrayList<String> region_lon = new ArrayList();

                    for (int i = 0; i < response.body().getFeatures().size(); i++) {
                    //    Log.d("DATA", "Сountry " + response.body().getFeatures().get(i).getAttributes().getCountryRegion() + ": " + response.body().getFeatures().get(i).getAttributes().getDeaths() );
                        death += response.body().getFeatures().get(i).getAttributes().getDeaths();
                        recover += response.body().getFeatures().get(i).getAttributes().getRecovered();
                        confirmed += response.body().getFeatures().get(i).getAttributes().getConfirmed();
                        region.add(response.body().getFeatures().get(i).getAttributes().getCountryRegion());
                        region_death.add(String.valueOf(response.body().getFeatures().get(i).getAttributes().getDeaths()));
                        region_recover.add(String.valueOf(response.body().getFeatures().get(i).getAttributes().getRecovered()));
                        region_confirmed.add(String.valueOf(response.body().getFeatures().get(i).getAttributes().getConfirmed()));
                        region_lat.add(String.valueOf(response.body().getFeatures().get(i).getAttributes().getLat()));
                        region_lon.add(String.valueOf(response.body().getFeatures().get(i).getAttributes().getLong()));
                    }
                    mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSettings.edit();




                    editor.putInt("deaths", death);
                    editor.apply();
                    editor.putInt("recover", recover);
                    editor.apply();
                    editor.putInt("confirmed", confirmed);
                    editor.apply();

                    initF();
                    // create a clock
                    ZoneId zid = ZoneId.of("Europe/Paris");

                    final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
                    localDate = LocalDate.now(zid);

                    editor.putString("date", localDate.toString());
                    editor.apply();

                    final int finalConfirmed = confirmed;
                    final int finalDeath = death;
                    final int finalRecover = recover;
                    mDatabase.child("data").child("verif").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Log.d("DATA", String.valueOf(dataSnapshot.getValue()));

                                if (!String.valueOf(dataSnapshot.getValue()).equals(localDate.toString())){
                                    initW(localDate, finalConfirmed, finalDeath, finalRecover,region,region_death,region_lat,region_lon,region_confirmed,region_recover);
                                }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                } else {
                    Log.d("DATA", "ERROR");
                }
            }

            @Override
            public void onFailure(Call<APIModel> call, Throwable t) {
                Log.d("DATA", "ERROR" + t.getMessage());
            }
        });



    }

    private void initW(LocalDate localDate, int Confirmed, int Death, int Recover,  ArrayList region, ArrayList<String> region_deaths,ArrayList<String> region_lat,ArrayList<String> region_lon,ArrayList<String> region_confirmed,ArrayList<String> region_recovered) {
        RealTimeDatabase data = new RealTimeDatabase(Confirmed, Death,Recover,null,null,null);
        mDatabase.child("data").child("date").child(localDate.toString()).setValue(data);
        mDatabase.child("data").child("verif").setValue(localDate.toString());

        for (int i = 0; i < region.size(); i++) {
            RealTimeDatabase data1 = new RealTimeDatabase(Integer.parseInt(region_confirmed.get(i)), Integer.parseInt(region_deaths.get(i)),Integer.parseInt(region_recovered.get(i)),null, Double.parseDouble(region_lat.get(i)),Double.parseDouble(region_lon.get(i)));
            Map<String, Object> postValues = data1.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/data/date/" + localDate.toString() +"/country/"+region.get(i).toString(), postValues);
            mDatabase.updateChildren(childUpdates);
        }

    }

    private void initF() {
        Fragment fragment1 = null;
        Class fragmentClass1 = MainContentActivityFG.class;

        try {
            fragment1 = (Fragment) fragmentClass1.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        changeFG(fragment1);

        final NavigationTabStrip navigationTabStrip = findViewById(R.id.nts);
        navigationTabStrip.setTabIndex(0, true);
        navigationTabStrip.setOnTabStripSelectedIndexListener(new NavigationTabStrip.OnTabStripSelectedIndexListener() {
            @Override
            public void onStartTabSelected(String title, int index) {

                Log.d("Tab_Menu", String.valueOf(index));
                Fragment fragment = null;
                Class fragmentClass = null;

                switch (index) {
                    case 0:
                        fragmentClass = MainContentActivityFG.class;
                        break;
                    case 1:
                        fragmentClass = AboutContentActivityFG.class;
                        break;

                }

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                changeFG(fragment);

            }

            @Override
            public void onEndTabSelected(String title, int index) {
            }
        });


    }

    private void changeFG(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fooFragment, fragment).commitAllowingStateLoss();
    }

}
