package me.as4.coronavirus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;

import android.net.ConnectivityManager;
import android.os.Bundle;

import android.util.Log;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.mapbox.mapboxsdk.maps.MapView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

                    switch (index){
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

    private void changeFG(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fooFragment, fragment).commitAllowingStateLoss();
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
