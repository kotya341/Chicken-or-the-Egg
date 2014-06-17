package com.example.chicken_or_the_egg.interfaces;

import android.graphics.Bitmap;

/**
 * Created by Konstantin on 16.06.2014.
 */
public interface MainActivityCallback {
    void onBitmapCreated(Bitmap bitmap, String title);
    void onPrintFinishing();
}
