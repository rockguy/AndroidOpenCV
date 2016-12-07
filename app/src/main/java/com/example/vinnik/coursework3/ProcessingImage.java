package com.example.vinnik.coursework3;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by vinnik on 12/5/2016.
 */

public class ProcessingImage {

    public static void saveToSDCard(Bitmap bmp, String filename){
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
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out);

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
