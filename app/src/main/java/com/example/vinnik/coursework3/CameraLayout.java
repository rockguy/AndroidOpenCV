package com.example.vinnik.coursework3;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CameraLayout extends Activity
        implements CameraBridgeViewBase.CvCameraViewListener2 {

    // Used to load the 'native-lib' library on application startup.

    private static final String TAG="MainActivity";
    Mat currentImage;
    Mat currentImage2;
    Rect[] facesArray;
    boolean b=false;
    SQLiteDatabase db;

    static {
        System.loadLibrary("native-lib");
        if(OpenCVLoader.initDebug()){
            Log.d(TAG,"Opencv successfully loaded");
        }
        else{
            Log.d(TAG,"OpenCV not loaded");
        }
    }


    private CameraBridgeViewBase openCvCameraView;
    private CascadeClassifier cascadeClassifier;
    private Button button;
    private Mat grayscaleImage;
    private int absoluteFaceSize;


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    initializeOpenCVDependencies();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };
    private void initializeOpenCVDependencies() {


        try {
            // Copy the resource into a temp file so OpenCV can load it
            InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, "haarcascade_frontalface_default.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);


            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();


            // Load the cascade classifier
            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
        } catch (Exception e) {
            Log.e("OpenCVActivity", "Error loading cascade", e);
        }


        // And we are ready to go
        openCvCameraView.enableView();
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Toast.makeText(getApplicationContext(), "Image saving", Toast.LENGTH_SHORT).show();
            Intent intent =new Intent(CameraLayout.this, FaceListActivity.class);

            File sd = new File(Environment.getExternalStorageDirectory() + "/frames/tempBitmap");
            for (File f:sd.listFiles()
                 ) {
                f.delete();
            }

            for (int i = 0; i < facesArray.length; i++) {
                Mat face = new Mat(currentImage2, facesArray[i]);
                Bitmap bmp = null;
                try {
                    bmp = Bitmap.createBitmap(face.cols(), face.rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(face, bmp);
                } catch (CvException e) {
                    Log.d(TAG, e.getMessage());
                }
                face.release();

//todo:Здесь будет проверка лиц

                Drawable icon;
                icon = new BitmapDrawable(getResources(),bmp);
                //openFileNameDialog(icon,bmp);

                saveToSDCard(bmp,"tempBitmap");
            }

            startActivity(intent);
            finish();
        }
    };

    private void saveToSDCard(Bitmap bmp,String filename){
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
                Log.d(TAG, e.getMessage());
            } finally {
                try {
                    if (out != null) {
                        out.close();
                        Log.d(TAG, "OK!!");
                    }
                } catch (IOException e) {
                    Log.d(TAG, e.getMessage() + "Error");
                    e.printStackTrace();
                }
            }
        }

    }

    private void openFileNameDialog(Drawable icon, final Bitmap bmp)
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
                String filename = "frame " +  input.getText().toString() + ".png";
                saveToSDCard(bmp,filename);

                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(Environment.getExternalStorageDirectory() + "/frames/"+filename);
                    saveImage(fis);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
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

    public void saveImage(FileInputStream fis)
    {
        try {
            byte[] image = new byte[fis.available()];
            fis.read(image);

            ContentValues values = new ContentValues();
            values.put("a",image);
            db.insert("tb",null,values);

            fis.close();

            Toast.makeText(this, "insert success", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //db=this.openOrCreateDatabase("faces.db",Context.MODE_PRIVATE,null);
        //db.execSQL("create table if not exists faceList (a blob)");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        openCvCameraView = new JavaCameraView(this, -1);
        openCvCameraView.setOnClickListener(onClickListener);
        setContentView(openCvCameraView);
        openCvCameraView.setCvCameraViewListener(this);
    }


    public void getImage()
    {
        Cursor c = db.rawQuery("select * from tb",null);
        if (c.moveToNext())
        {
            byte[] image = c.getBlob(0);
            Bitmap bmp = BitmapFactory.decodeByteArray(image,0,image.length);
            Toast.makeText(this, "select success", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        grayscaleImage = new Mat(height, width, CvType.CV_32FC2);


        // The faces will be a 20% of the height of the screen
        absoluteFaceSize = (int) (height * 0.01);
    }


    @Override
    public void onCameraViewStopped() {
    }


    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame aInputFrame) {
        currentImage = aInputFrame.gray();
        currentImage2= aInputFrame.gray();
        // Create a grayscale image
        //Imgproc.cvtColor(aInputFrame, grayscaleImage, Imgproc.COLOR_RGBA2RGB);
        MatOfRect faces = new MatOfRect();
        // Use the classifier to detect faces
        if (cascadeClassifier != null) {
            cascadeClassifier.detectMultiScale(currentImage, faces, 1.1, 2, 2,
                    new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        }
        //Rect rect = new Rect(20,20,500,500);
        //Imgproc.rectangle(aInputFrame, rect.tl(), rect.br(), new Scalar(0, 255, 0, 255));
        //Imgproc.line(aInputFrame, rect.tl(), rect.br(), new Scalar(0, 255, 0, 255));
        // If there are any faces found, draw a rectangle around it
        facesArray = faces.toArray();
        for (int i = 0; i <facesArray.length; i++)
            Imgproc.rectangle(currentImage, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 255), 3);

        return currentImage;
    }

    @Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, this, mLoaderCallback);    }
}
