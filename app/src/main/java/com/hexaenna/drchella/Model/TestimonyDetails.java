package com.hexaenna.drchella.Model;

import android.graphics.Bitmap;

/**
 * Created by admin on 12/1/2017.
 */

public class TestimonyDetails {

    Bitmap imageBitmap;
    private static TestimonyDetails ourInstance = new TestimonyDetails();
    private TestimonyDetails(){}

    public static TestimonyDetails getInstance() {
        return ourInstance;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }
}
