package com.example.vinnik.coursework3;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.util.Date;
import java.util.Timer;

public class MainActivity extends Activity
        implements CameraBridgeViewBase.CvCameraViewListener {

    // Used to load the 'native-lib' library on application startup.

    private static final String TAG="MainActivity";

    boolean b=false;

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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        openCvCameraView = new JavaCameraView(this, -1);
        setContentView(openCvCameraView);
        openCvCameraView.setCvCameraViewListener(this);
    }

    public void takePhotoes(View view)
    {
        b=true;
    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        grayscaleImage = new Mat(height, width, CvType.CV_8UC4);


        // The faces will be a 20% of the height of the screen
        absoluteFaceSize = (int) (height * 0.2);
    }


    @Override
    public void onCameraViewStopped() {
    }


    @Override
    public Mat onCameraFrame(Mat aInputFrame) {
        // Create a grayscale image
        Imgproc.cvtColor(aInputFrame, grayscaleImage, Imgproc.COLOR_RGBA2RGB);
        MatOfRect faces = new MatOfRect();
        // Use the classifier to detect faces
        if (cascadeClassifier != null) {
            cascadeClassifier.detectMultiScale(grayscaleImage, faces, 1.1, 2, 2,
                    new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        }
        //Rect rect = new Rect(20,20,500,500);
        //Imgproc.rectangle(aInputFrame, rect.tl(), rect.br(), new Scalar(0, 255, 0, 255));
        //Imgproc.line(aInputFrame, rect.tl(), rect.br(), new Scalar(0, 255, 0, 255));
        // If there are any faces found, draw a rectangle around it
        Rect[] facesArray = faces.toArray();
        for (int i = 0; i <facesArray.length; i++)
            Imgproc.rectangle(aInputFrame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 255), 3);
        if(b)
        {
            SaveFaces(aInputFrame,facesArray);
            b=false;
        }

        return aInputFrame;
    }

    public void SaveFaces(Mat fullImage,Rect[] faces) {
        Date date = new Date();

        for (int i = 0; i < faces.length; i++) {
            Mat face = new Mat(fullImage, faces[i]);
            Bitmap bmp = null;
            try {
                bmp = Bitmap.createBitmap(face.cols(), face.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(face, bmp);
            } catch (CvException e) {
                Log.d(TAG, e.getMessage());
            }
            face.release();


            FileOutputStream out = null;

            String filename = "frame" + date.getTime() + ".png";

            File sd = new File(Environment.getExternalStorageDirectory() + "/frames");
            boolean success = true;
            if (!sd.exists()) {
                success = sd.mkdir();
            }
            if (success) {
                File dest = new File(sd, filename);
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
    }


    @Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, this, mLoaderCallback);
    }
}
