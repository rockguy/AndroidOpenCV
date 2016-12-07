package com.example.vinnik.coursework3;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

/**
 * Created by vinnik on 07.12.2016.
 */

public class PersonListActivity extends Activity {
    LinearLayout linLayout;
    File sd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_list);

        linLayout = (LinearLayout) findViewById(R.id.linLayout2);

        sd = new File(Environment.getExternalStorageDirectory() + "/frames/");
        LayoutInflater ltInflater = getLayoutInflater();

        if (sd.exists()) {
            for (final File f:
                    sd.listFiles()) {
                if(f.isDirectory()&&(!f.getName().contains("temp"))) {
                    //// TODO: fucking hardcode
                    final Bitmap bitmap = BitmapFactory.decodeFile(f.getPath() + "/" + f.getName() + "0.png");
                    final View personInfo = ltInflater.inflate(R.layout.person_info, linLayout, false);
                    ImageView imageView = (ImageView) personInfo.findViewById(R.id.faceImage);
                    imageView.setImageBitmap(bitmap);

                    TextView fio = (TextView) personInfo.findViewById(R.id.fio);
                    fio.setText("ФИО: " + f.getName());
                    TextView countOfPhoto = (TextView) personInfo.findViewById(R.id.countOfPhoto);
                    countOfPhoto.setText("Число фотографий: " + f.list().length);
                    linLayout.addView(personInfo);
                }
            }
        }

    }
}
