package com.example.vinnik.coursework3;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
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


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_list);

        linLayout = (LinearLayout) findViewById(R.id.linLayout);

        sd = new File(Environment.getExternalStorageDirectory() + "/frames/tempBitmap");
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
        builder.setTitle("Введите Фамилию Имя и Отчество человека");
        builder.setIcon(icon);
//todo: Потом будет предпроверка с распознаванием
// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        final String[] m_Text = new String[1];
// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String filename =input.getText().toString();
                saveToSDCard(bmp,filename);
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
