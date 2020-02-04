package me.as4.coronavirus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ListView;

import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ItemArrayAdapter itemArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        itemArrayAdapter = new ItemArrayAdapter(getApplicationContext(), R.layout.item_layout);

        Parcelable state = listView.onSaveInstanceState();
        listView.setAdapter(itemArrayAdapter);
        listView.onRestoreInstanceState(state);

        InputStream inputStream = getResources().openRawResource(R.raw.data);
        CSVFile csvFile = new CSVFile(inputStream);
        List<String[]> scoreList = csvFile.read();


        for(String[] scoreData:scoreList ) {
            itemArrayAdapter.add(scoreData);
        }
    }
}
