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

        Bundle extras = getIntent().getExtras();
        if(extras.containsKey("backLink")) {
            final String backClass = extras.getString("backLink");
            Button backButton=(Button) findViewById(R.id.backButton);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = null;
                    try {
                        intent = new Intent(PersonListActivity.this, Class.forName(backClass));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    intent.putExtra("backLink",getPackageName()+".PersonListActivity");
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
                intent = new Intent(PersonListActivity.this,MainActivity.class);
                intent.putExtra("backLink",getPackageName()+".PersonListActivity");
                startActivity(intent);
                finish();
            }
        });

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

                    Button editPersonButton = (Button)  personInfo.findViewById(R.id.editPersonButton);
                    editPersonButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(PersonListActivity.this, FaceListActivity.class);
                            intent.putExtra("folderName",f.getName());
                            intent.putExtra("backLink",getPackageName()+".PersonListActivity");
                            startActivity(intent);
                            finish();
                        }
                    });

                    TextView countOfPhoto = (TextView) personInfo.findViewById(R.id.countOfPhoto);
                    countOfPhoto.setText("Число фотографий: " + f.list().length);
                    linLayout.addView(personInfo);


                }
            }
        }

    }
}
