package com.example.vinnik.coursework3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.vinnik.coursework3.ProcessingImage.saveToSDCard;

/**
 * Created by vinnik on 06.12.2016.
 */

public class PeopleList extends Activity {
    String bitmapName;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_list);

        if(getIntent().hasExtra("BitmapName"))
        {
            bitmapName=getIntent().getStringExtra("BitmapName");
        }

        Bundle extras = getIntent().getExtras();
        if(extras.containsKey("backLink")) {
            final String backClass = extras.getString("backLink");
            Button backButton=(Button) findViewById(R.id.backButton);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = null;
                    try {
                        intent = new Intent(PeopleList.this, Class.forName(backClass));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    intent.putExtra("backLink",getPackageName()+".PeopleList");
                    startActivity(intent);
                    finish();
                }
            });
        }

        Button homeButton=(Button) findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                intent = new Intent(PeopleList.this,MainActivity.class);
                intent.putExtra("backLink",getPackageName()+".PeopleList");
                startActivity(intent);
                finish();
            }
        });

        final File sd = new File(Environment.getExternalStorageDirectory() + "/frames");
        List<String> people = new ArrayList<>();
        for (File f : sd.listFiles()) {
            if (!f.getName().contains("temp")) {
                people.add(f.getName());
            }
        }

        final ListView listView = (ListView)findViewById(R.id.peopleList);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                people);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedFromList = (listView.getItemAtPosition(i).toString());
                File f = new File(sd.getPath()+"/tempBitmap/"+bitmapName);
                Bitmap bmp = BitmapFactory.decodeFile(f.getPath());
                saveToSDCard(bmp,selectedFromList);

//
//                Person person = Person.find(Person.class, "fio = ?", selectedFromList).get(0);
//                person.save();
//
//                Image image = new Image(bmp);
//                image.owner=person;
//                image.save();



                f.delete();
                Intent intent = new Intent(PeopleList.this, FaceListActivity.class);
                intent.putExtra("backLink",getPackageName()+".PeopleList");
                startActivity(intent);
                finish();
            }
        });
    }
}
