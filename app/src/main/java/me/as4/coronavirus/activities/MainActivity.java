package me.as4.coronavirus.activities;

import androidx.annotation.NonNull;
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

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import me.as4.coronavirus.R;
import me.as4.coronavirus.fragments.AboutContentActivityFG;
import me.as4.coronavirus.fragments.MainContentActivityFG;
import me.as4.coronavirus.models.APIModel;
import me.as4.coronavirus.service.AppCoronaService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private AppCoronaService appCoronaService;
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
    private SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appCoronaService = AppCoronaService.retrofit.create(AppCoronaService.class);
        final Call<APIModel> call = appCoronaService.getData();

        call.enqueue(new Callback<APIModel>() {
            @Override
            public void onResponse(Call<APIModel> call, Response<APIModel> response) {
                if (response.isSuccessful()) {
                    int death = 0;
                    int recover = 0;
                    int confirmed = 0;
                    for (int i = 0; i < response.body().getFeatures().size(); i++) {
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
