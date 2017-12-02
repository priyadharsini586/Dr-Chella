package com.hexaenna.drchella.Model;

import android.graphics.Bitmap;

/**
 * Created by admin on 12/1/2017.
 */

public class TestimonyDetails {

    Bitmap imageBitmap;
    public String content;
    public boolean myMessage;
    private static TestimonyDetails ourInstance = new TestimonyDetails();
    public TestimonyDetails(){}

    public TestimonyDetails(String content, boolean myMessage) {
        this.content = content;
        this.myMessage = myMessage;
    }
    public static TestimonyDetails getInstance() {
        return ourInstance;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isMyMessage() {
        return myMessage;
    }

    public void setMyMessage(boolean myMessage) {
        this.myMessage = myMessage;
    }
}
