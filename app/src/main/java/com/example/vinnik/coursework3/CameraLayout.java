package com.example.vinnik.coursework3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import static com.example.vinnik.coursework3.ProcessingImage.saveToSDCard;

public class CameraLayout extends Activity
        implements CameraBridgeViewBase.CvCameraViewListener2 {

    // Used to load the 'native-lib' library on application startup.

    private static final String TAG="MainActivity";
    Mat currentImage;
    Mat currentImage2;
    Rect[] facesArray;
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

            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
        } catch (Exception e) {
            Log.e("OpenCVActivity", "Error loading cascade", e);
        }

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
                saveToSDCard(bmp,"tempBitmap");
            }
            intent.putExtra("backLink",getPackageName()+".CameraLayout");
            intent.putExtra("folderName","tempBitmap");
            startActivity(intent);
            finish();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        openCvCameraView = new JavaCameraView(this, -1);
        openCvCameraView.setOnClickListener(onClickListener);
        setContentView(openCvCameraView);
        openCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
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
