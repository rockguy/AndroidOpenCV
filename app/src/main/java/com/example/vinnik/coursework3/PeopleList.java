package com.example.vinnik.coursework3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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


        final File sd = new File(Environment.getExternalStorageDirectory() + "/frames");
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
                people);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedFromList = (listView.getItemAtPosition(i).toString());
                File f = new File(sd.getPath()+"/"+bitmapName);
                Bitmap bmp = BitmapFactory.decodeFile(f.getPath());
                saveToSDCard(bmp,selectedFromList);
                f.delete();
                Intent intent = new Intent(PeopleList.this, FaceListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void saveToSDCard(Bitmap bmp, String filename){
        FileOutputStream out = null;
        File sd = new File(Environment.getExternalStorageDirectory() + "/frames/"+filename);
        boolean success = true;
        if (!sd.exists()) {
            success = sd.mkdir();
        }
        if (success) {
            filename=filename+sd.list().length;
            File dest = new File(sd, filename+".png");
            try {
                out = new FileOutputStream(dest);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
