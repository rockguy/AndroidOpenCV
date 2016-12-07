package com.example.vinnik.coursework3;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vinnik on 06.12.2016.
 */

public class PeopleList extends Activity {


    String[] names = { "Иван", "Марья", "Петр", "Антон", "Даша", "Борис",
            "Костя", "Игорь", "Анна", "Денис", "Андрей" ,"Иван", "Марья", "Петр", "Антон", "Даша", "Борис",
            "Костя", "Игорь", "Анна", "Денис", "Андрей"};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_list);

        File sd = new File(Environment.getExternalStorageDirectory() + "/frames");
        List<String> people = new ArrayList<String>();
        for (File f : sd.listFiles()) {
            if (!f.getName().contains("temp")) {
                people.add(f.getName());
            }
        }

        LayoutInflater ltInflater = getLayoutInflater();

        //View PeopleList = ltInflater.inflate(R.layout.people_list, null, false);

        final ListView listView = (ListView)findViewById(R.id.peopleList);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                names);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedFromList = (listView.getItemAtPosition(i).toString());
            }
        });
    }
}
