package com.example.vinnik.coursework3;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;

import static com.example.vinnik.coursework3.ProcessingImage.saveToSDCard;

/**
 * Created by vinnik on 06.12.2016.
 */

public class FaceListActivity extends Activity {
    LinearLayout linLayout;
    File sd;
    SQLiteDatabase db;
    String folderName;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_list);

        db = this.openOrCreateDatabase("faces.db", Context.MODE_PRIVATE,null);

        linLayout = (LinearLayout) findViewById(R.id.linLayout);

        Bundle extras = getIntent().getExtras();
        if(extras!=null && extras.containsKey("folderName")) {
            folderName = extras.getString("folderName");
        }
        else{
            folderName="tempBitmap";
        }

        if(extras.containsKey("backLink")) {
            final String backClass = extras.getString("backLink");
            Button backButton=(Button) findViewById(R.id.backButton);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = null;
                    try {
                        intent = new Intent(FaceListActivity.this, Class.forName(backClass));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    intent.putExtra("backLink",getPackageName()+".FaceListActivity");
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
                intent = new Intent(FaceListActivity.this,MainActivity.class);
                intent.putExtra("backLink",getPackageName()+".FaceListActivity");
                startActivity(intent);
                finish();
            }
        });




        sd = new File(Environment.getExternalStorageDirectory() + "/frames/"+folderName);
        LayoutInflater ltInflater = getLayoutInflater();

        if (sd.exists()) {
            for (final File f:
                 sd.listFiles()) {
                final Bitmap bitmap = BitmapFactory.decodeFile(f.getPath());
                final View faceInfo = ltInflater.inflate(R.layout.face_info, linLayout, false);
                ImageView imageView = (ImageView) faceInfo.findViewById(R.id.faceImage);
                imageView.setImageBitmap(bitmap);

                Button existedPersonButton = (Button) faceInfo.findViewById(R.id.existedPersonButton);
                existedPersonButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(FaceListActivity.this, PeopleList.class);
                        intent.putExtra("BitmapName",f.getName());
                        intent.putExtra("backLink",getPackageName()+".FaceListActivity");
                        startActivity(intent);
                        finish();
                    }
                });

                Button newPersonButton = (Button) faceInfo.findViewById(R.id.newPersonButton);
                newPersonButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Drawable icon = new BitmapDrawable(getResources(),bitmap);
                        openFileNameDialog(icon,bitmap,f.getName(),faceInfo);
                    }
                });

                Button deletePersonButton = (Button) faceInfo.findViewById(R.id.deletePersonButton);
                deletePersonButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        f.delete();
                        linLayout.removeView(faceInfo);
                    }
                });

                linLayout.addView(faceInfo);
            }
        }
    }

    private void openFileNameDialog(Drawable icon, final Bitmap bmp,final String bitmapName,final View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Введите Фамилию Имя и Отчество человека");
        builder.setIcon(icon);
//todo: Потом будет предпроверка с распознаванием
// Set up the input

        final LinearLayout view = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.fio_layout, null);
        builder.setView(view);
// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText LastName = (EditText)view.findViewById(R.id.LastName);
                EditText FirstName = (EditText)view.findViewById(R.id.FirstName);
                EditText SecondName = (EditText)view.findViewById(R.id.SecondName);
                String s = LastName.getText().toString() + FirstName.getText().toString() + SecondName.getText().toString();
                saveToSDCard(bmp,s);

//                Person person = new Person();
//                person.setLastName(LastName.getText().toString());
//                person.setFirstName(FirstName.getText().toString());
//                person.setSecondName(SecondName.getText().toString());
//
//                DataBaseHelper dbhelp = new DataBaseHelper(getBaseContext());
//
//                dbhelp.onCreate(db);
//                dbhelp.createPerson(person);
//
//                Image image = new Image(bmp);
//                image.setOwner(person.getFIO());
//                dbhelp.createImage(image);

                File f = new File(sd.getPath()+"/"+bitmapName);
                f.delete();
                linLayout.removeView(v);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //// TODO: Надо вызвать сразу, а не после сохранения файла.
        builder.create();
        builder.show();
    }
}
