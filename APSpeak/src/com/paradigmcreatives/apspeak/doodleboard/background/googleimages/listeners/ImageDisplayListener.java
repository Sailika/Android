package com.paradigmcreatives.apspeak.doodleboard.background.googleimages.listeners;

import android.graphics.Bitmap;

public interface ImageDisplayListener {
    public void selectedImage();
    public void selectedImage(Bitmap selectedImage);
    public void displayedImage(Bitmap image);
    public Bitmap getDisplayedBitmap();
}
