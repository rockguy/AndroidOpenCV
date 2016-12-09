package com.example.vinnik.coursework3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;

import static android.content.ContentValues.TAG;
import static com.example.vinnik.coursework3.ProcessingImage.saveToSDCard;

public class MainActivity extends Activity{

    static {
        System.loadLibrary("native-lib");
        if(OpenCVLoader.initDebug()){
            Log.d(TAG,"Opencv successfully loaded");
        }
        else{
            Log.d(TAG,"OpenCV not loaded");
        }
    }

    Button takePictureButton;
    Button getPictureButton;
    Button processingPictureButton;
    Button listOfPersonButton;
    ImageView imageView;
    Mat currentImage;
    private CascadeClassifier cascadeClassifier;
    private int absoluteFaceSize;


    private static int RESULT_LOAD_IMAGE = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takePictureButton = (Button) findViewById(R.id.takePictureButton);
        getPictureButton = (Button) findViewById(R.id.getPictureButton);
        processingPictureButton = (Button) findViewById(R.id.processingPictureButton);
        listOfPersonButton=(Button) findViewById(R.id.listOfPersonButton);


        Bundle extras = getIntent().getExtras();
        if(extras!=null && extras.containsKey("backLink")) {
            final String backClass = extras.getString("backLink");
            Button backButton=(Button) findViewById(R.id.backButton);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = null;
                    try {
                        intent = new Intent(MainActivity.this, Class.forName(backClass));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    intent.putExtra("backLink",getPackageName()+".MainActivity");
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
                intent = new Intent(MainActivity.this,MainActivity.class);
                intent.putExtra("backLink",getPackageName()+".MainActivity");
                startActivity(intent);
                finish();
            }
        });



        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CameraLayout.class);
                intent.putExtra("backLink",getPackageName()+".MainActivity");
                startActivity(intent);
                finish();
            }
        });

        getPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        processingPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                absoluteFaceSize = (int) (currentImage.height() * 0.005);
                MatOfRect faces = new MatOfRect();
                File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                File mCascadeFile = new File(cascadeDir, "haarcascade_frontalface_default.xml");
                cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());

                if (cascadeClassifier != null) {
                    cascadeClassifier.detectMultiScale(currentImage, faces, 1.1, 2, 2,
                            new Size(absoluteFaceSize, absoluteFaceSize), new Size());
                }
                Rect[] facesArray = faces.toArray();

                Intent intent =new Intent(MainActivity.this, FaceListActivity.class);

                File sd = new File(Environment.getExternalStorageDirectory() + "/frames/tempBitmap");
                if(sd.length()>0) {
                    for (File f : sd.listFiles()
                            ) {
                        f.delete();
                    }
                }

                for (int i = 0; i < facesArray.length; i++) {
                    Mat face = new Mat(currentImage, facesArray[i]);
                    Bitmap bmp = null;
                    try {
                        bmp = Bitmap.createBitmap(face.cols(), face.rows(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(face, bmp);
                    } catch (CvException e) {
                    }
                    face.release();
                    saveToSDCard(bmp,"tempBitmap");
                }
                intent.putExtra("folderName","tempBitmap");
                intent.putExtra("backLink",getPackageName()+".MainActivity");
                startActivity(intent);
                finish();
            }
        });

        listOfPersonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PersonListActivity.class);
                intent.putExtra("folderName","tempBitmap");
                intent.putExtra("backLink",getPackageName()+".MainActivity");
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap bmp = BitmapFactory.decodeFile(picturePath);
            imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageBitmap(bmp);

            currentImage=new Mat();
            Utils.bitmapToMat(bmp,currentImage);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }
}
