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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Access a Cloud Firestore instance from your Activity
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
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
                    for (int i = 0; i < response.body().getFeatures().size(); i++) {
                        Log.d("DATA", "Сountry " + response.body().getFeatures().get(i).getAttributes().getCountryRegion() + ": " + response.body().getFeatures().get(i).getAttributes().getDeaths() );
                        death += response.body().getFeatures().get(i).getAttributes().getDeaths();
                        recover += response.body().getFeatures().get(i).getAttributes().getRecovered();
                        confirmed += response.body().getFeatures().get(i).getAttributes().getConfirmed();
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
                    final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    LocalDate localDate = LocalDate.now();


                    DocumentReference docRef = db.collection("data").document("stat");
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();

                                if (document.exists()) {
                                    Log.d("DATAT", "DocumentSnapshot data: " + document.getData().get("deaths"));


                                } else {
                                    Log.d("DATA", "No such document");
                                }
                            } else {
                                Log.d("DATA", "get failed with ", task.getException());
                            }
                        }
                    });


                    Map<String, Object> docData = new HashMap<>();
                    Map<String, Object> nestedData = new HashMap<>();
                    Map<String, Object> nestedData1 = new HashMap<>();
                    Map<String, Object> nestedData2 = new HashMap<>();

                    nestedData.put(String.valueOf(localDate), confirmed);
                    nestedData1.put(String.valueOf(localDate), death);
                    nestedData2.put(String.valueOf(localDate), recover);

                    docData.put("total", nestedData);
                    docData.put("deaths", nestedData1);
                    docData.put("recover", nestedData2);

                    db.collection("data").document("stat")
                            .set(docData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("data", "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("DATA", "Error writing document", e);
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
                        fragmentClass = MainContentActivityFG.class;
                        break;
                    case 2:
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
