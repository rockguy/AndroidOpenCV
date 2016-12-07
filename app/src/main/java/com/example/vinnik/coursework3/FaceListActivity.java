package com.example.vinnik.coursework3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;

/**
 * Created by vinnik on 06.12.2016.
 */

public class FaceListActivity extends Activity {
    LinearLayout linLayout;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_list);

        LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayout);

        File sd = new File(Environment.getExternalStorageDirectory() + "/frames/tempBitmap");
        LayoutInflater ltInflater = getLayoutInflater();

        if (sd.exists()) {
            for (File f:
                 sd.listFiles()) {
                Bitmap bitmap = BitmapFactory.decodeFile(f.getPath());
                View faceInfo = ltInflater.inflate(R.layout.face_info, linLayout, false);
                ImageView imageView = (ImageView) faceInfo.findViewById(R.id.faceImage);
                imageView.setImageBitmap(bitmap);
                Button existedPersonButton = (Button) faceInfo.findViewById(R.id.existedPersonButton);
                existedPersonButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(FaceListActivity.this, PeopleList.class);
                        startActivity(intent);
                    }
                });
                linLayout.addView(faceInfo);
            }
        }
    }

}
